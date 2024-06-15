package com.autobots.automanager.selecionador;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Usuario;

@Component
public class SelecionadorUsuario {
	public Usuario selecionar(List<Usuario> usuarios, long id) {
		Usuario selecionado = null;
		for (Usuario usuario : usuarios) {
			if (usuario.getId() == id) {
				selecionado = usuario;
			}
		}
		return selecionado;
	}
}