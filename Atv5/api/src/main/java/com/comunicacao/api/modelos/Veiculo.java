package com.comunicacao.api.modelos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Veiculo {
	@JsonProperty("id")
	private Long id;
	@JsonProperty("modelo")
	private String modelo;
	@JsonProperty("placa")
	private String placa;
	@JsonProperty("proprietario")
	private Usuario proprietario;
	@JsonProperty("vendas")
	private List<Venda> vendas;
}
