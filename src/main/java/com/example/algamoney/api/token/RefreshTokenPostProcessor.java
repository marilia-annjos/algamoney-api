package com.example.algamoney.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		System.out.println("returnType.getMethod().getName(): " + returnType.getMethod().getName());
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		
		HttpServletRequest req = ((ServletServerHttpRequest)request).getServletRequest();
		HttpServletResponse resp = ((ServletServerHttpResponse)response).getServletResponse();
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken)body;
		
		if (body.getRefreshToken() != null) {
			String refleshToken = body.getRefreshToken().getValue();
			adicionarRefreshTokenCookie(refleshToken, req, resp);
			removerRefleshTokenBody(token);
		} else {
			String refleshToken = body.getValue();
			adicionarRefreshTokenCookie(refleshToken, req, resp);
		}
		return null;
		// TODO Auto-generated method stub
    }

	private void removerRefleshTokenBody(DefaultOAuth2AccessToken token) {
		//token.setRefreshToken(null);
	}

	private void adicionarRefreshTokenCookie(String refleshToken, HttpServletRequest req, HttpServletResponse resp) {
		Cookie refleshTokenCookie = new Cookie("refleshToken", refleshToken);
		refleshTokenCookie.setHttpOnly(true);
		refleshTokenCookie.setSecure(false);//TODO: Mudar para true em producao
		refleshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
		refleshTokenCookie.setMaxAge(2592000);
		resp.addCookie(refleshTokenCookie);
	}

}
