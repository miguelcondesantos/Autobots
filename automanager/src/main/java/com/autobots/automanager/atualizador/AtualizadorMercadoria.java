package com.autobots.automanager.atualizador;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.modelos.VerificadorNulo;

@Component
public class AtualizadorMercadoria {
    private VerificadorNulo verificador = new VerificadorNulo();

    public void atualizar(Mercadoria mercadoria, Mercadoria atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getValidade())) {
                mercadoria.setValidade(atualizacao.getValidade());
            }
            if (!verificador.verificar(atualizacao.getFabricao())) {
                mercadoria.setFabricao(atualizacao.getFabricao());
            }
            if (!verificador.verificar(atualizacao.getNome())) {
                mercadoria.setNome(atualizacao.getNome());
            }
            if (!verificador.verificar(atualizacao.getQuantidade())) {
                long novaQuantidade = atualizacao.getQuantidade();
                if (novaQuantidade > 0) {
                    mercadoria.setQuantidade(novaQuantidade);
                }
            }
            if (!verificador.verificar(atualizacao.getValor())) {
                double novoValor = atualizacao.getValor();
                if (novoValor > 0) {
                    mercadoria.setValor(novoValor);
                }
            }
            if (!verificador.verificar(atualizacao.getDescricao())) {
                mercadoria.setDescricao(atualizacao.getDescricao());
            }
            if (!(atualizacao.getCadastro() == null)) {
                mercadoria.setCadastro(atualizacao.getCadastro());
            }
        }
    }

    public void atualizar(List<Mercadoria> mercadorias, List<Mercadoria> atualizacoes) {
        for (Mercadoria atualizacao : atualizacoes) {
            for (Mercadoria mercadoria : mercadorias) {
                if (atualizacao.getId() != null) {
                    if (atualizacao.getId() == mercadoria.getId()) {
                        atualizar(mercadoria, atualizacao);
                    }
                }
            }
        }
    }
}
