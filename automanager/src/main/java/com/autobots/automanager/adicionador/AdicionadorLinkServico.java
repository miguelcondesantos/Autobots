package com.autobots.automanager.adicionador;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.modelos.AdicionadorLink;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<Servico>{
	
	@Override
	public void adicionarLink(List<Servico> lista) {
		for (Servico servico : lista) {
			long id = servico.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ServicoControle.class)
							.obterServico(id))
					.withSelfRel();
			servico.add(linkProprio);
		}
	}
	
	@Override
	public void adicionarLink(Servico objeto) {
		long id = objeto.getId();
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ServicoControle.class)
						.obterServicos())
				.withSelfRel();
		objeto.add(linkProprio);
	}
}
