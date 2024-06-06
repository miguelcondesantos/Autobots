package com.autobots.automanager.atualizador;


import java.util.Set;

import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.modelos.VerificadorNulo;

public class AtualizadorEmail {
	
	private VerificadorNulo verificador = new VerificadorNulo();
	
	public void atualizar(Email email, Email atualizacao) {
			if(atualizacao != null) {
				if (!verificador.verificar(atualizacao.getEndereco())) {
					email.setEndereco(atualizacao.getEndereco());
				}
			}
		}
	
	public void atualizar(Set<Email> emails, Set<Email> atualizacoes) {
		for (Email atualizacao : atualizacoes) {
			for (Email email : emails) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == email.getId()) {
						atualizar(email, atualizacao);
					}
				}
			}
		}
	}

}
