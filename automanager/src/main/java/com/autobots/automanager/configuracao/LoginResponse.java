package com.autobots.automanager.configuracao;

import java.util.List;

import com.autobots.automanager.enumeracoes.PerfilUsuario;

public class LoginResponse {
    private String token;
    private List<PerfilUsuario> perfis;

    public LoginResponse(String token, List<PerfilUsuario> perfis) {
        this.token = token;
        this.perfis = perfis;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<PerfilUsuario> getPerfis() {
        return perfis;
    }

    public void setPerfis(List<PerfilUsuario> perfis) {
        this.perfis = perfis;
    }
}


