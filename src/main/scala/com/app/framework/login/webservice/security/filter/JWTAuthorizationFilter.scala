package com.app.framework.login.webservice.security.filter

import java.io.IOException
import java.security.SignatureException

import com.app.framework.common.constant.GlobalConstant
import com.app.framework.login.webservice.security.constant.SecurityConstant
import io.jsonwebtoken.impl.DefaultClaims
import io.jsonwebtoken.{ExpiredJwtException, Jwts}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javax.servlet.{FilterChain, ServletException}
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.{AuthenticationManager, UsernamePasswordAuthenticationToken}
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class JWTAuthorizationFilter(val m_authManager: AuthenticationManager) extends BasicAuthenticationFilter(m_authManager) {
    
    @throws[IOException]
    @throws[ServletException]
    override protected def doFilterInternal(a_req: HttpServletRequest,
                                            a_res: HttpServletResponse, a_chain: FilterChain): Unit = {
        // Handling for OPTIONS request
        if (a_req.getMethod == "OPTIONS") {
            addOPTIONSHeader(a_res)
            return
        }
        
        // Handling for Actual request
        if (!validateJWTToken(a_req, a_res))
            return
        
        a_chain.doFilter(a_req, a_res)
    }
    
    
    private def addOPTIONSHeader(a_res: HttpServletResponse): Unit = {
        
        a_res.setStatus(HttpStatus.OK.value)
        a_res.addHeader("Access-Control-Allow-Origin", "*")
        a_res.addHeader("Access-Control-Allow-Headers", "Authorization,Content-Type")
    }
    
    private def validateJWTToken(a_req: HttpServletRequest, a_res: HttpServletResponse): Boolean = {
        
        val w_header = a_req.getHeader(SecurityConstant.AUTH_HEADER_NAME)
        
        if (w_header == null || !w_header.startsWith(SecurityConstant.TOKEN_PREFIX.trim)) {
            a_res.getWriter.write("Header does not contain Bearer prefix")
            a_res.setStatus(HttpStatus.FORBIDDEN.value)
            return false
        }
        
        val w_token: String = a_req.getHeader(SecurityConstant.AUTH_HEADER_NAME)
        var w_claim: DefaultClaims = null
        try {
            
            w_claim = Jwts.parser.setSigningKey(SecurityConstant.SECRET).parseClaimsJws(w_token.replace(
                SecurityConstant.TOKEN_PREFIX.trim, "")).getBody.asInstanceOf[DefaultClaims]
        }
        catch {
            case _: ExpiredJwtException =>
                a_res.getWriter.write("JWT token expired")
                a_res.setStatus(HttpStatus.UNAUTHORIZED.value)
                return false
            
            case _: SignatureException =>
                a_res.setStatus(HttpStatus.UNAUTHORIZED.value)
                a_res.getWriter.write("Invalid Token!")
                return false
        }
        
        val w_loginID: String = w_claim.getSubject
        a_req.setAttribute(GlobalConstant.LOGGED_IN_USER, w_loginID)
        a_req.setAttribute(GlobalConstant.JWT_TOKEN, w_token)
        
        if (w_loginID != null) SecurityContextHolder.getContext.setAuthentication(
            new UsernamePasswordAuthenticationToken(w_loginID, null, new java.util.ArrayList[GrantedAuthority]))
        
        true
    }
}