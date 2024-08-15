package com.autobots.automanager.atualizador;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.modelos.VerificadorNulo;

@Component
public class AtualizadorEmpresa {
	
	private VerificadorNulo verificador = new VerificadorNulo();
	private AtualizadorTelefone atualizadorTelefone = new AtualizadorTelefone();
	private AtualizadorEndereco atualizadorEndereco = new AtualizadorEndereco();
	private AtualizadorMercadoria atualizadorMercadoria = new AtualizadorMercadoria();
	private AtualizadorUsuario atualizadorUsuario = new AtualizadorUsuario();
	private AtualizadorServico autalizadorServico = new AtualizadorServico();
	private AtualizadorVenda atualizadorVenda = new AtualizadorVenda();
	
	public void atualizarDados(Empresa empresa, Empresa atualizacao) {
	    if(atualizacao != null) {
	        if (!verificador.verificar(atualizacao.getRazaoSocial())) {
	            empresa.setRazaoSocial(atualizacao.getRazaoSocial());
	        }
	        if (!verificador.verificar(atualizacao.getNomeFantasia())) {
	            empresa.setNomeFantasia(atualizacao.getNomeFantasia());
	        }
	    }
	}

	
	public void atualizar(Empresa empresa, Empresa atualizacao) {
		atualizarDados(empresa, atualizacao);
		atualizadorTelefone.atualizar(empresa.getTelefones(), atualizacao.getTelefones());
		atualizadorEndereco.atualizar(empresa.getEndereco(), atualizacao.getEndereco());
		atualizadorMercadoria.atualizar(empresa.getMercadorias(), atualizacao.getMercadorias());
		atualizadorUsuario.atualizar(empresa.getUsuarios(), atualizacao.getUsuarios());
		autalizadorServico.atualizar(empresa.getServicos(),atualizacao.getServicos());
		atualizadorVenda.atualizar(empresa.getVendas(),atualizacao.getVendas());
		
	}

}
