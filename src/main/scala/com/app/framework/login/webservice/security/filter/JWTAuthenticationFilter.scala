package com.app.framework.login.webservice.security.filter

import java.io.IOException
import java.util.Date

import com.app.core.crud.user.dataobject.dto.UserAuthData
import com.app.framework.exception.AppAppException
import com.app.framework.login.webservice.security.constant.SecurityConstant
import com.app.framework.util.general.{DateUtil, JSONUtil, StringUtil, WebUtil}
import io.jsonwebtoken.{Jwts, SignatureAlgorithm}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import javax.servlet.{FilterChain, ServletException}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.{AuthenticationManager, UsernamePasswordAuthenticationToken}
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.{Authentication, AuthenticationException, GrantedAuthority}
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


class JWTAuthenticationFilter(@Autowired m_authenticationManager: AuthenticationManager)
    extends UsernamePasswordAuthenticationFilter {
    
    @throws[AuthenticationException]
    override def attemptAuthentication(a_req: HttpServletRequest, a_res: HttpServletResponse): Authentication = {
        
        val w_authType: String = getAuthMechanism
        
        w_authType match {
            
            case SecurityConstant.AUTH_FROM_DB =>
                authenticateFromDB(readAuthCredentials(a_req))
            
            case _ =>
                null
        }
    }
    
    private def getAuthMechanism: String = {
        SecurityConstant.AUTH_FROM_DB
    }
    
    /* HELPER APIS START */
    
    private def readAuthCredentials(a_req: HttpServletRequest): UserAuthData = {
        
        var w_credentials: UserAuthData = null
        try {
            w_credentials = JSONUtil.mapJSONToObject(a_req.getInputStream, classOf[UserAuthData])
                .asInstanceOf[UserAuthData]
        }
        catch {
            case _: Exception =>
                throw new AppAppException("Bad Request.")
        }
        
        if (!StringUtil.isValidString(w_credentials.enterpriseID, w_credentials.loginID, w_credentials.getPassword))
            throw new AppAppException("Invalid credentials.")
        
        w_credentials
    }
    
    private def authenticateFromDB(a_userAuthData: UserAuthData): Authentication = {
        
        try {
            
                m_authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(a_userAuthData.getUniqueKey,
                a_userAuthData.getPassword, new java.util.ArrayList[GrantedAuthority]))
        }
        catch {
            case e: Exception =>
              WebUtil.logExceptionTrace(e, null)
                throw new AppAppException(e.getMessage)
        }
    }
    
    @throws[IOException]
    @throws[ServletException]
    override protected def successfulAuthentication(a_req: HttpServletRequest, a_res: HttpServletResponse,
                                                    a_chain: FilterChain, a_auth: Authentication): Unit = {
        
        // Get User Info
        val w_userAuthData = a_auth.getPrincipal.asInstanceOf[UserAuthData]
        
        // Build Token
        val w_token = buildAndSetJWTToken(w_userAuthData)
        
        // Set Token and other required Response Headers
        setResponseHeaders(a_res, w_token)
        
        // Add required info in Response Body
        setResponseBody(a_res, w_userAuthData)
    }
    
    private def buildAndSetJWTToken(a_user: UserAuthData): String = {
        
        val w_jwtToken: String = Jwts.builder.setSubject(a_user.getUniqueKey)
            .setExpiration(new Date(getTokenExpiryTime))
            .signWith(SignatureAlgorithm.HS512, SecurityConstant.SECRET).compact
        a_user.authToken = w_jwtToken
        
        w_jwtToken
    }
    
    private def getTokenExpiryTime: Long = {
        DateUtil.getSysDate.getTime + SecurityConstant.EXPIRATION_TIME
    }
    
    private def setResponseHeaders(a_res: HttpServletResponse, a_token: String): Unit = {
        
        a_res.setHeader(SecurityConstant.AUTH_HEADER_NAME, SecurityConstant.TOKEN_PREFIX + a_token)
        
        a_res.setHeader("Access-Control-Allow-Origin", "*")
        a_res.setHeader("Access-Control-Allow-Methods", "POST,OPTIONS,GET,PUT,DELETE,OPTIONS")
        a_res.setHeader("Access-Control-Allow-Headers", "Content-Type")
        a_res.setHeader("Access-Control-Expose-Headers", "Authorization")
        
    }
    
    private def setResponseBody(a_res: HttpServletResponse, a_user: User): Unit = {
        a_res.getWriter.write("{\"message\":\"Hello, " + a_user.getUsername + "\"}")
        a_res.setHeader("Content-Type", "application/json")
        a_res.setCharacterEncoding("utf-8")
    }
    
    /* HELPER APIS END */
}