package com.autobots.automanager.modelos;

import java.util.List;

public interface AdicionadorLink<T> {
    default void adicionarLink(List<T> lista) {
        for (T objeto : lista) {
            adicionarLink(objeto);
        }
    }
    
    void adicionarLink(T objeto);
}
