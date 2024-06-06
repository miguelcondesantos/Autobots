package com.autobots.automanager.selecionador;

import java.util.List;

import com.autobots.automanager.entitades.Venda;

public class SelecionadorVenda {
	public Venda selecionar(List<Venda> vendas, long id) {
		Venda selecionado = null;
		for (Venda venda : vendas) {
			if (venda.getId() == id) {
				selecionado = venda;
			}
		}
		return selecionado;
	}
}
