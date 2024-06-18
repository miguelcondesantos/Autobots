package com.comunicacao.api.modelos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Mercadoria {
	@JsonProperty("id")
	private Long id;
	@JsonProperty("validade")
	private Date validade;
	@JsonProperty("fabricao")
	private Date fabricao;
	@JsonProperty("cadastro")
	private Date cadastro;
	@JsonProperty("nome")
	private String nome;
	@JsonProperty("quantidade")
	private long quantidade;
	@JsonProperty("valor")
	private double valor;
	@JsonProperty("descrica")
	private String descricao;
}
