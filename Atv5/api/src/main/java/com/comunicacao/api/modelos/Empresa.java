package com.comunicacao.api.modelos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Empresa {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("razaoSocial")
    private String razaoSocial;

    @JsonProperty("nomeFantasia")
    private String nomeFantasia;

    @JsonProperty("telefones")
    private List<Telefone> telefones;

    @JsonProperty("endereco")
    private Endereco endereco;

}
