package com.autobots.automanager.atualizador;


import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.modelos.VerificadorNulo;

@Component
public class AtualizadorEmail {
	
	private VerificadorNulo verificador = new VerificadorNulo();
	
	public void atualizar(Email email, Email atualizacao) {
			if(atualizacao != null) {
				if (!verificador.verificar(atualizacao.getEndereco())) {
					email.setEndereco(atualizacao.getEndereco());
				}
			}
		}
	
	public void atualizar(List<Email> emails, List<Email> atualizacoes) {
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
