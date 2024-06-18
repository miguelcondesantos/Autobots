package com.comunicacao.api.modelos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Usuario {
	@JsonProperty("id")
	private Long id;
	@JsonProperty("nome")
    private String nome;
	@JsonProperty("nomeSocial")
    private String nomeSocial;
	@JsonProperty("telefones")
    private List<Telefone> telefones;
	@JsonProperty("endereco")
    private Endereco endereco;
	@JsonProperty("documentos")
    private List<Documento> documentos;
	@JsonProperty("emails")
    private List<Email> emails;
	@JsonProperty("mercadorias")
    private List<Mercadoria> mercadorias;
	@JsonProperty("vendas")
    private List<Venda> vendas;
	@JsonProperty("veiculos")
    private List<Veiculo> veiculos;

}