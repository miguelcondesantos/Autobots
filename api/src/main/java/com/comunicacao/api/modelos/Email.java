package com.comunicacao.api.modelos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Email {
	@JsonProperty("id")
	private Long id;
	@JsonProperty("endereco")
	private String endereco;
}
