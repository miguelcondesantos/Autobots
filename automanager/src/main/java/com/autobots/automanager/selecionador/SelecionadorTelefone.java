package com.autobots.automanager.selecionador;

import java.util.List;

import com.autobots.automanager.entitades.Telefone;

public class SelecionadorTelefone {
	public Telefone selecionar(List<Telefone> telefones, long id) {
		Telefone selecionado = null;
		for (Telefone telefone : telefones) {
			if (telefone.getId() == id) {
				selecionado = telefone;
			}
		}
		return selecionado;
	}
}
