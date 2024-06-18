package com.comunicacao.api.modelos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Servico {
	@JsonProperty("id")
	private Long id;
	@JsonProperty("nome")
	private String nome;
	@JsonProperty("valor")
	private double valor;
	@JsonProperty("descricao")
	private String descricao;
}
