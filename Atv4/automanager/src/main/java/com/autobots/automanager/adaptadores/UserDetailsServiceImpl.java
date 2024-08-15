package com.autobots.automanager.adaptadores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RepositorioUsuario repositorio;

    public Usuario obterPorNome(String nomeUsuario) {
        List<Usuario> usuarios = repositorio.findAll();
        Usuario selecionado = null;
        for (Usuario usuario : usuarios) {
            for (var credencial : usuario.getCredenciais()) {
                if (credencial instanceof CredencialUsuarioSenha && 
                    ((CredencialUsuarioSenha) credencial).getNomeUsuario().equals(nomeUsuario)) {
                    // Carrega as coleções aqui para evitar LazyInitializationException
                    usuario.getCredenciais().size();
                    usuario.getPerfis().size();
                    selecionado = usuario;
                    break;
                }
            }
            if (selecionado != null) {
                break;
            }
        }
        return selecionado;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario selecionado = this.obterPorNome(username);
        if (selecionado == null) {
            throw new UsernameNotFoundException(username);
        }
        UserDetailsImpl usuario = new UserDetailsImpl(selecionado);
        return usuario;
    }
}
