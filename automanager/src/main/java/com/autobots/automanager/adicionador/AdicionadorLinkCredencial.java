package com.autobots.automanager.adicionador;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.CredencialUsuarioControle;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class AdicionadorLinkCredencial implements AdicionadorLink<CredencialUsuarioSenha> {
	
    @Override
    public void adicionarLink(List<CredencialUsuarioSenha> lista) {
        for (CredencialUsuarioSenha credencial : lista) {
            long id = credencial.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(CredencialUsuarioControle.class)
                            .obterCredencial(id))
                    .withSelfRel();
            credencial.add(linkProprio);
        }
    }

    @Override
	public void adicionarLink(CredencialUsuarioSenha objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(CredencialUsuarioControle.class)
						.obterCredenciais())
				.withSelfRel();
		objeto.add(linkProprio);
	}
}
