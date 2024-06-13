package com.autobots.automanager.atualizador;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.modelos.VerificadorNulo;

@Component
public class AtualizadorVeiculo {
	
	private VerificadorNulo verificador = new VerificadorNulo();
	
	public void atualizar(Veiculo veiculo, Veiculo atualizacao) {
			if(atualizacao != null) {
				if (!verificador.verificar(atualizacao.getTipo())) {
					veiculo.setTipo(atualizacao.getTipo());
				}
				if (!verificador.verificar(atualizacao.getModelo())) {
					veiculo.setModelo(atualizacao.getModelo());
				}
				if (!verificador.verificar(atualizacao.getPlaca())) {
					veiculo.setPlaca(atualizacao.getPlaca());
				}
				if (!verificador.verificar(atualizacao.getProprietario())) {
					veiculo.setProprietario(atualizacao.getProprietario());
				}
			}
		}
	

	
	public void atualizar(List<Veiculo> veiculos, List<Veiculo> atualizacoes) {
		for (Veiculo atualizacao : atualizacoes) {
			for (Veiculo email : veiculos) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == email.getId()) {
						atualizar(email, atualizacao);
					}
				}
			}
		}
	}
}
