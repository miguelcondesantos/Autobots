package com.autobots.automanager.atualizador;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.modelos.VerificadorNulo;

public class AtualizadorUsuario {
	
	private VerificadorNulo verificador = new VerificadorNulo();
	private AtualizadorTelefone atualizadorTelefone = new AtualizadorTelefone();
	private AtualizadorEndereco atualizadorEndereco = new AtualizadorEndereco();
	private AtualizadorDocumento atualizadorDocumento = new AtualizadorDocumento();
	private AtualizadorEmail atualizadorEmail = new AtualizadorEmail();
	private AtualizadorCredencialUsuarioSenha atualizadorCredencialUsuarioSenha = new AtualizadorCredencialUsuarioSenha();
	private AtualizadorMercadoria atualizadorMercadoria = new AtualizadorMercadoria();
	private AtualizadorVeiculo atualizadorVeiculo = new AtualizadorVeiculo();
	
	public void atualizarDados(Usuario usuario, Usuario atualizacao) {
			if(atualizacao != null) {
				if (!verificador.verificar(atualizacao.getNome())) {
					usuario.setNome(atualizacao.getNome());
				}
				if (!verificador.verificar(atualizacao.getNomeSocial())) {
					usuario.setNomeSocial(atualizacao.getNomeSocial());
				}
				if (!verificador.verificar(atualizacao.getPerfis())) {
					usuario.setPerfis(atualizacao.getPerfis());
				}
			}
		}
	
	public void atualizar(Usuario usuario, Usuario atualizacao) {
		atualizarDados(usuario, atualizacao);
		atualizadorTelefone.atualizar(usuario.getTelefones(), atualizacao.getTelefones());
		atualizadorEndereco.atualizar(usuario.getEndereco(), atualizacao.getEndereco());
		atualizadorDocumento.atualizar(usuario.getDocumentos(), atualizacao.getDocumentos());
		atualizadorEmail.atualizar(usuario.getEmails(), atualizacao.getEmails());
		atualizadorCredencialUsuarioSenha.atualizar(usuario.getCredenciais(), atualizacao.getCredenciais());
		atualizadorMercadoria.atualizar(usuario.getMercadorias(), atualizacao.getMercadorias());
		atualizadorVeiculo.atualizar(usuario.getVeiculos(), atualizacao.getVeiculos());
		
	}
}
