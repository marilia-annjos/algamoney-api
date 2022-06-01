package com.example.algamoney.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.service.PessoaService;

@RestController
@RequestMapping(path = "/pessoas")
public class PessoaResource {
	
	@Autowired
	private PessoaRepository pessoaRepositorio;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private PessoaService pessoaServico;
	
	@GetMapping
	public List<Pessoa> listar(){
		return pessoaRepositorio.findAll();
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> listarPeloCodigo(@PathVariable Long codigo){
		
		Pessoa pessoa = pessoaRepositorio.findById(codigo).orElse(null);
		if (pessoa == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Pessoa>(pessoa, HttpStatus.OK);
		
	}
	
	@PostMapping
	public ResponseEntity<Pessoa> incluir(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response){
		Pessoa novaPessoa = pessoaRepositorio.save(pessoa);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novaPessoa.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(novaPessoa);
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long codigo){
		pessoaRepositorio.deleteById(codigo);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> atualizar (@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa){
		
		Pessoa pessoaBase = pessoaServico.atualizar(codigo, pessoa);
		return ResponseEntity.ok(pessoaBase);
	}
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.OK)
	public void atualizarPropriedadeAtivo (@PathVariable Long codigo, @Valid @RequestBody Boolean ativo){
		pessoaServico.atualizarPropriedadeAtivo(codigo, ativo);
	}

}
