package com.example.algamoney.api.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)//prioridade de execução alta
public class RefreshTokenCookiePreProcessorFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		
		if ("/oauth/token".equalsIgnoreCase(req.getRequestURI())
				&& "reflesh_token".equals(req.getParameter("grant-type"))
				&& req.getCookies() != null) {
			
			for (Cookie cookie : req.getCookies()) {
				if (cookie.getName().equalsIgnoreCase("refleshToken")) {
					String valorReflashToken = cookie.getValue();
					req = new MyServletRequestWrapper(req, valorReflashToken);
				}
			}
		}
		
		chain.doFilter(req, response);
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		Filter.super.destroy();
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		Filter.super.init(filterConfig);
	}
	
	static class MyServletRequestWrapper extends HttpServletRequestWrapper {

		private String aRefleshToken  = "";
		public MyServletRequestWrapper(HttpServletRequest request, String pRefleshToken) {
			super(request);
			this.aRefleshToken = pRefleshToken;
			
		}
		
		@Override
		public Map<String, String[]> getParameterMap() {
			// TODO Auto-generated method stub
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refleshToken", new String[] {aRefleshToken});
			map.setLocked(true);
			return map;
		}		
	}

}
