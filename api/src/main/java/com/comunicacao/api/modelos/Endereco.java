package com.comunicacao.api.modelos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Endereco {
	@JsonProperty("id")
	private Long id;
	@JsonProperty("estado")
	private String estado;
	@JsonProperty("cidade")
	private String cidade;
	@JsonProperty("bairro")
	private String bairro;
	@JsonProperty("rua")
	private String rua;
	@JsonProperty("numero")
	private String numero;
	@JsonProperty("codigoPostal")
	private String codigoPostal;
	@JsonProperty("informacoesAdicionais")
	private String informacoesAdicionais;
}
