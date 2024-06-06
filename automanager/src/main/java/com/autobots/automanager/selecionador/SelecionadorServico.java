package com.autobots.automanager.selecionador;

import java.util.List;

import com.autobots.automanager.entitades.Servico;

public class SelecionadorServico {
	public Servico selecionar(List<Servico> servicos, long id) {
		Servico selecionado = null;
		for (Servico servico : servicos) {
			if (servico.getId() == id) {
				selecionado = servico;
			}
		}
		return selecionado;
	}
}
