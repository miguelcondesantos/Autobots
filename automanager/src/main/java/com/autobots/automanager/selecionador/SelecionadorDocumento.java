package com.autobots.automanager.selecionador;

import java.util.List;

import com.autobots.automanager.entitades.Documento;

public class SelecionadorDocumento {
	public Documento selecionar(List<Documento> documentos, long id) {
		Documento selecionado = null;
		for (Documento documento : documentos) {
			if (documento.getId() == id) {
				selecionado = documento;
			}
		}
		return selecionado;
	}
}
