package com.autobots.automanager.atualizador;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.modelos.VerificadorNulo;

@Component 
public class AtualizadorCredencialUsuarioSenha {
    private VerificadorNulo verificador = new VerificadorNulo();

    public void atualizar(CredencialUsuarioSenha credencial, CredencialUsuarioSenha atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(atualizacao.getNomeUsuario())) {
                credencial.setNomeUsuario(atualizacao.getNomeUsuario());
            }
            if (!verificador.verificar(atualizacao.getSenha())) {
                credencial.setSenha(atualizacao.getSenha());
            }
            if (!verificador.verificar(atualizacao.getCriacao())) {
                credencial.setCriacao(atualizacao.getCriacao());
            }
            if (!verificador.verificar(atualizacao.getUltimoAcesso())) {
                credencial.setUltimoAcesso(atualizacao.getUltimoAcesso());
            }
        }
    }

    public void atualizar(List<Credencial> credenciais, List<? extends Credencial> atualizacoes) {
        for (Credencial atualizacao : atualizacoes) {
            for (Credencial credencial : credenciais) {
                if (atualizacao.getId() != null && atualizacao.getId().equals(credencial.getId())) {
                    if (credencial instanceof CredencialUsuarioSenha && atualizacao instanceof CredencialUsuarioSenha) {
                        atualizar((CredencialUsuarioSenha) credencial, (CredencialUsuarioSenha) atualizacao);
                    }
                }
            }
        }
    }
}
