package com.comunicacao.api.modelos;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Venda {
	@JsonProperty("id")
	private Long id;
	@JsonProperty("cadastro")
	private Date cadastro;
	@JsonProperty("identificacao")
	private String identificacao;
	@JsonProperty("cliente")
	private Usuario cliente;
	@JsonProperty("funcionario")
	private Usuario funcionario;
	@JsonProperty("mercadorias")
	private List<Mercadoria> mercadorias;
	@JsonProperty("servicos")
	private List<Servico> servicos;
	@JsonProperty("veiculo")
	private Veiculo veiculo;
}
