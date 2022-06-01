package com.example.algamoney.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.event.RecursoCriadoEvent;

@Component
public class RescursoCriadoListener implements ApplicationListener<RecursoCriadoEvent>{

	@Override
	public void onApplicationEvent(RecursoCriadoEvent pRecursoCriadoEvento) {
		// TODO Auto-generated method stub
		HttpServletResponse response = pRecursoCriadoEvento.getResponse();
		Long codigo = pRecursoCriadoEvento.getCodigo();
		
		adicionarLocation(response, codigo);

	}

	public void adicionarLocation(HttpServletResponse response, Long codigo) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").
				buildAndExpand(codigo).toUri();
		
		response.setHeader("Location", uri.toASCIIString());
	}
}
