package com.example.algamoney.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "lancamento")
public class Lancamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotNull(message = "Descrição Lançamento não pode ser nulo")
	@Size(min = 3, max = 30, message = "Descrição Lançamento deve ter no máximo {max} caracteres e no minimo {min} caracteres. Você digitou: '${validatedValue}'")
	private String descricao;

	@Column(name = "data_vencimento")
	@NotNull(message = "Data de Vencimento não pode ser nulo")
	private LocalDate dataVencimento;

	@Column(name = "data_pagamento")
	@NotNull(message = "Data de pagamento não pode ser nulo")
	private LocalDate dataPagamento;

	@NotNull(message = "Valor Lançamento não pode ser nulo")
	private BigDecimal valor;

	@Size(min = 3, max = 150, message = "Observação Lançamento deve ter no máximo {max} caracteres e no minimo {min} caracteres. Você digitou: '${validatedValue}'")
	private String observacao;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Tipo de Lançamento não pode ser nulo")
	private TipoLancamento tipo;

	@ManyToOne
	@JoinColumn(name = "codigo_categoria")
	@NotNull(message = "Categoria de Lançamento não pode ser nulo")
	private Categoria categoria;

	@ManyToOne
	@JoinColumn(name = "codigo_pessoa")
	@NotNull(message = "Pessoa não pode ser nulo")
	private Pessoa pessoa;
	
	/**
	 * Constantes que sinbolizam as variaveis presente na base
	 * OBS: Deve-se está a identificação que esta no objeto lancamento java
	 */
	public static final String ID_REQ_DESCRICAO = "descricao";
	public static final String ID_REQ_DATA_VENCIMENTO = "dataVencimento";
	public static final String ID_REQ_DATA_PAGAMENTO = "dataPagamento";
	

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public TipoLancamento getTipo() {
		return tipo;
	}

	public void setTipo(TipoLancamento tipo) {
		this.tipo = tipo;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lancamento other = (Lancamento) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
