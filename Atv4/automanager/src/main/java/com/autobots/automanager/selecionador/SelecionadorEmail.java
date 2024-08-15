package com.autobots.automanager.selecionador;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Email;

@Component
public class SelecionadorEmail {
	public Email selecionar(List<Email> emails, long id) {
		Email selecionado = null;
		for (Email email : emails) {
			if (email.getId() == id) {
				selecionado = email;
			}
		}
		return selecionado;
	}
}
