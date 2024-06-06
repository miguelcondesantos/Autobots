package com.autobots.automanager.selecionador;

import java.util.List;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;

public class SelecionadorCredencialUsuarioSenha {
	public CredencialUsuarioSenha selecionar(List<CredencialUsuarioSenha> credenciais, long id) {
		CredencialUsuarioSenha selecionado = null;
		for (CredencialUsuarioSenha credencial : credenciais) {
			if (credencial.getId() == id) {
				selecionado = credencial;
			}
		}
		return selecionado;
	}
}
