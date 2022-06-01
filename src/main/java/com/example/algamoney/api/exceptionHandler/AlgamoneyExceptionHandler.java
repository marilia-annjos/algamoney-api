package com.example.algamoney.api.exceptionHandler;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor =  (ex.getCause() != null ? ex.getCause().toString() : ex.getMessage().toString());
		
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		return handleExceptionInternal(ex, getListaDeErros(ex.getBindingResult()), headers, status, request);
	}
	
	@ExceptionHandler(EmptyResultDataAccessException.class)
	protected ResponseEntity<Object> handleEmptyResultDataAccessException(RuntimeException pRuntimeException, WebRequest request) {	
		
		String mensagemUsuario = messageSource.getMessage("mensagem.recurso-nao-encontrado", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor =  (pRuntimeException.getCause() != null ? pRuntimeException.getCause().toString() : pRuntimeException.getMessage().toString());
		
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(pRuntimeException, erros, new HttpHeaders() , HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class})
	protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException pEx, WebRequest request) {	
		
		String mensagemUsuario = messageSource.getMessage("mensagem.operacao-nao-permitida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor =  ExceptionUtils.getRootCauseMessage(pEx);
		
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(pEx, erros, new HttpHeaders() , HttpStatus.NOT_FOUND, request);
	}
	
	private List<Erro> getListaDeErros(BindingResult pBindingResult){
		List<Erro> erros = new ArrayList<>();
		String mensagemUsuario = "";
		String mensagemDesenvolvedor = "";
		
		for (FieldError fieldError : pBindingResult.getFieldErrors()) {
			mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			mensagemDesenvolvedor = fieldError.toString();
			erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		}
	
		 return erros;
	}
	
	public static class Erro {
		
		private String mensagemUsuario;
		private String mensagemDesenvolvedor;
		
		public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}

		public String getMensagemUsuario() {
			return mensagemUsuario;
		}

		public String getMensagemDesenvolvedor() {
			return mensagemDesenvolvedor;
		}
		
	}    
}




