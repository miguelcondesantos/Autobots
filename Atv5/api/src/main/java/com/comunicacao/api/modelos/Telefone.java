package com.comunicacao.api.modelos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Telefone {
	@JsonProperty("id")
	private Long id;
	@JsonProperty("ddd")
	private String ddd;
	@JsonProperty("numero")
	private String numero;
}
