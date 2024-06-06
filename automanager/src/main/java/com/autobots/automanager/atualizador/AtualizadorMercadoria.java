package com.autobots.automanager.atualizador;

import java.util.Set;

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.modelos.VerificadorNulo;

public class AtualizadorMercadoria {
private VerificadorNulo verificador = new VerificadorNulo();
	
	public void atualizar(Mercadoria mercadoria, Mercadoria atualizacao) {
			if(atualizacao != null) {
				if (!verificador.verificar(atualizacao.getValidade())) {
					mercadoria.setValidade(atualizacao.getValidade());
				}
				if (!verificador.verificar(atualizacao.getFabricao())) {
					mercadoria.setFabricao(atualizacao.getFabricao());
				}
				if (!verificador.verificar(atualizacao.getNome())) {
					mercadoria.setNome(atualizacao.getNome());
				}
				if (!verificador.verificar(atualizacao.getQuantidade())) {
					mercadoria.setQuantidade(atualizacao.getQuantidade());
				}
				if (!verificador.verificar(atualizacao.getValor())) {
					mercadoria.setValor(atualizacao.getValor());
				}
				if (!verificador.verificar(atualizacao.getDescricao())) {
					mercadoria.setDescricao(atualizacao.getDescricao());
				}
				if (!(atualizacao.getCadastro() == null)) {
					mercadoria.setCadastro(atualizacao.getCadastro());
				}

			}
		}
	
	public void atualizar(Set<Mercadoria> mercadorias, Set<Mercadoria> atualizacoes) {
		for (Mercadoria atualizacao : atualizacoes) {
			for (Mercadoria mercadoria : mercadorias) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == mercadoria.getId()) {
						atualizar(mercadoria, atualizacao);
					}
				}
			}
		}
	}
}
