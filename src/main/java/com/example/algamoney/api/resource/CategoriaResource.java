package com.example.algamoney.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;

@RestController
@RequestMapping(path = "/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository categoriaRepositorio;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	//@CrossOrigin(maxAge = 10, origins = {"http://localhost:8000"} )
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Categoria> listar(){
		return categoriaRepositorio.findAll();
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<Categoria> listarPeloCodigo(@PathVariable Long codigo){
		
		Categoria categoria = categoriaRepositorio.findById(codigo).orElse(null);
		if (categoria == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Categoria>(categoria, HttpStatus.OK);
		
	}
	
//	@GetMapping
//	public ResponseEntity<?> listar(){
//		
//		List<Categoria> categorias = categoriaRepositorio.findAll();
//		return categorias.isEmpty() ?
//				ResponseEntity.ok(categorias) : ResponseEntity.noContent().build();
//	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> incluir(@Valid @RequestBody Categoria categoria, HttpServletResponse response){
		Categoria novaCategoria = categoriaRepositorio.save(categoria);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, novaCategoria.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long codigo){
		categoriaRepositorio.deleteById(codigo);
	}
	

}
