package com.autobots.automanager.selecionador;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.CredencialUsuarioSenha;

@Component
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
