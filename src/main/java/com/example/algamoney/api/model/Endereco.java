package com.example.algamoney.api.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Endereco {
	
	@NotNull (message = "Logradouro não pode ser nulo")
	@Size (
			min= 3,
			max=150,
			message = "Logradouro deve ter no máximo {max} caracteres e no minimo {min} caracteres. Você digitou: '${validatedValue}'"
			)
	private String logradouro;
	
	@NotNull (message = "Número não pode ser nulo")
	private int numero;
	
	@NotNull (message = "Bairro não pode ser nulo")
	@Size (
			min= 2,
			max=100,
			message = "Bairro deve ter no máximo {max} caracteres e no minimo {min} caracteres. Você digitou: '${validatedValue}'"
			)
	private String bairro;
	
	@NotNull (message = "CEP não pode ser nula")
	@Size (
			min= 7,
			max=7,
			message = "CEP deve ter no máximo {max} caracteres e no minimo {min} caracteres. Você digitou: '${validatedValue}'"
			)
	private String cep;
	
	@NotNull (message = "Cidade não pode ser nula")
	@Size (
			min= 2,
			max=100,
			message = "Cidade deve ter no máximo {max} caracteres e no minimo {min} caracteres. Você digitou: '${validatedValue}'"
			)
	private String cidade;
	
	@NotNull (message = "Estado não pode ser nulo")
	@Size (
			min= 2,
			max=2,
			message = "Estado (UF) deve ter no máximo {max} caracteres e no minimo {min} caracteres. Você digitou: '${validatedValue}'"
			)
	private String estado;

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
}
