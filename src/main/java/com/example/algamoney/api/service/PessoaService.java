package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepositorio;

	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaBase = buscarPessoaPorCodigo(codigo);

		BeanUtils.copyProperties(pessoa, pessoaBase, "codigo");
		pessoaRepositorio.save(pessoaBase);

		return pessoaBase;
	}

	public Pessoa buscarPessoaPorCodigo(Long codigo) {
		Pessoa pessoaBase = pessoaRepositorio.findById(codigo).orElse(null);
		if (pessoaBase == null)
			throw new EmptyResultDataAccessException(1);
		return pessoaBase;
	}
	
	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaBase = buscarPessoaPorCodigo(codigo);
		pessoaBase.setAtivo(ativo);
		
		pessoaRepositorio.save(pessoaBase);
	}
	
	
	

}
