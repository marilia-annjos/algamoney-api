package com.example.algamoney.api.resource;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.exceptionHandler.AlgamoneyExceptionHandler.Erro;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.service.LancamentoService;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping(path = "/lancamento")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepositorio;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MessageSource messageSource;
	
	@GetMapping
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepositorio.filtrar(lancamentoFilter, pageable);
	}
	
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Lancamento> listarPeloCodigo(@PathVariable Long codigo){
		
		Lancamento lancamento = lancamentoRepositorio.findById(codigo).orElse(null);
		if (lancamento == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Lancamento>(lancamento, HttpStatus.OK);
		
	}
	
	@PostMapping
	public ResponseEntity<Lancamento> incluir(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) throws PessoaInexistenteOuInativaException{
		Lancamento novaLancamento = lancamentoService.salvar(lancamento);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novaLancamento.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(novaLancamento);
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long codigo){
		lancamentoRepositorio.deleteById(codigo);
	}
	
	
	@ExceptionHandler({ PessoaInexistenteOuInativaException.class })
	protected ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex, WebRequest request) {	
		
		String mensagemUsuario = messageSource.getMessage("mensagem.pessoa-inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor =  ex.toString();
		
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return ResponseEntity.badRequest().body(erros);
	}

}
