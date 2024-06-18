package com.comunicacao.api.modelos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Documento {
	@JsonProperty("id")
	private Long id;
	@JsonProperty("dataEmissao")
	private Date dataEmissao;
	@JsonProperty("numero")
	private String numero;
}
