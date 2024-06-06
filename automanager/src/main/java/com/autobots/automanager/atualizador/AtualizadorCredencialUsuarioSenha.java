package com.autobots.automanager.atualizador;

import java.util.Set;

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.modelos.VerificadorNulo;

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
        }
    }

    public void atualizar(Set<Credencial> credenciais, Set<Credencial> atualizacoes) {
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
