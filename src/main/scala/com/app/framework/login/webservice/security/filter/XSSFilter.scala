package com.app.framework.login.webservice.security.filter

import com.app.framework.common.constant.GlobalConstant
import javax.servlet.Filter
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(1)
class XSSFilter extends Filter {

  import javax.servlet.FilterChain
  import javax.servlet.FilterConfig
  import javax.servlet.ServletException
  import javax.servlet.ServletRequest
  import javax.servlet.ServletResponse
  import javax.servlet.http.HttpServletRequest
  import java.io.IOException

  @throws[ServletException]
  override def init(filterConfig: FilterConfig): Unit = {
  }

  override def destroy(): Unit = {
  }

  @throws[IOException]
  @throws[ServletException]
  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    if (!GlobalConstant.YES.equals(request.getParameter(GlobalConstant.HASRTFCONTENT)))
      chain.doFilter(new XSSRequestWrapper(request.asInstanceOf[HttpServletRequest]), response)
    else
      chain.doFilter(request, response)
  }
}
