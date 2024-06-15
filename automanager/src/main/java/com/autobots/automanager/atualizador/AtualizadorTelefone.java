package com.autobots.automanager.atualizador;

import java.util.List;

import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.modelos.VerificadorNulo;

public class AtualizadorTelefone {
	
	private VerificadorNulo verificador = new VerificadorNulo();
	
	public void atualizar(Telefone telefone, Telefone atualizacao) {
			if(atualizacao != null) {
				if (!verificador.verificar(atualizacao.getDdd())) {
					telefone.setDdd(atualizacao.getDdd());
				}
				if (!verificador.verificar(atualizacao.getNumero())) {
					telefone.setDdd(atualizacao.getNumero());
				}
			}
		}
	
	public void atualizar(List<Telefone> telefones, List<Telefone> atualizacoes) {
		for (Telefone atualizacao : atualizacoes) {
			for (Telefone telefone : telefones) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == telefone.getId()) {
						atualizar(telefone, atualizacao);
					}
				}
			}
		}
	}
}
