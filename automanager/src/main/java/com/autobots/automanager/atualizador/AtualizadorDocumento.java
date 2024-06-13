package com.autobots.automanager.atualizador;

import java.util.List;

import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.modelos.VerificadorNulo;

public class AtualizadorDocumento {
	
	private VerificadorNulo verificador = new VerificadorNulo();

	public void atualizar(Documento documento, Documento atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getTipo())) {
				documento.setTipo(atualizacao.getTipo());
			}
			if (!verificador.verificar(atualizacao.getNumero())) {
				documento.setNumero(atualizacao.getNumero());
			}
			if (!(atualizacao.getDataEmissao() == null)) {
				documento.setDataEmissao(atualizacao.getDataEmissao());
			}
		}
	}

	public void atualizar(List<Documento> documentos, List<Documento> atualizacoes) {
		for (Documento atualizacao : atualizacoes) {
			for (Documento documento : documentos) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == documento.getId()) {
						atualizar(documento, atualizacao);
					}
				}
			}
		}
	}
}
