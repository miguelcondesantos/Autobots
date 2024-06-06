package com.autobots.automanager.modelos;

public class VerificadorNulo {
    public boolean verificar(Object objeto) {
        return objeto == null;
    }

    public boolean verificar(String dado) {
        return dado == null || dado.isBlank();
    }
}
