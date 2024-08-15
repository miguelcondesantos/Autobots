package com.autobots.automanager.atualizador;

import java.util.List;

import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelos.VerificadorNulo;

public class AtualizadorVenda {
	
	private VerificadorNulo verificador = new VerificadorNulo();
	
	public void atualizar(Venda venda, Venda atualizacao) {
		if(atualizacao != null) {
			if (!verificador.verificar(atualizacao.getIdentificacao())) {
				venda.setIdentificacao(atualizacao.getIdentificacao());
			}
			if (!verificador.verificar(atualizacao.getCliente())) {
				venda.setCliente(atualizacao.getCliente());
			}
			if (!verificador.verificar(atualizacao.getFuncionario())) {
				venda.setFuncionario(atualizacao.getFuncionario());
			}
			if (!verificador.verificar(atualizacao.getMercadorias())) {
				venda.setMercadorias(atualizacao.getMercadorias());
			}
			if (!verificador.verificar(atualizacao.getServicos())) {
				venda.setServicos(atualizacao.getServicos());
			}
			if (!verificador.verificar(atualizacao.getVeiculo())) {
				venda.setVeiculo(atualizacao.getVeiculo());
			}
		}
	}

	public void atualizar(List<Venda> vendas, List<Venda> atualizacoes) {
		for (Venda atualizacao : atualizacoes) {
			for (Venda venda : vendas) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == venda.getId()) {
						atualizar(venda, atualizacao);
					}
				}
			}
		}
	}
}
