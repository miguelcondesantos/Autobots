package com.autobots.automanager.selecionador;

import java.util.List;

import com.autobots.automanager.entitades.Endereco;

public class SelecionadorEndereco {
	public Endereco selecionar(List<Endereco> enderecos, long id) {
		Endereco selecionado = null;
		for (Endereco endereco : enderecos) {
			if (endereco.getId() == id) {
				selecionado = endereco;
			}
		}
		return selecionado;
	}
}
