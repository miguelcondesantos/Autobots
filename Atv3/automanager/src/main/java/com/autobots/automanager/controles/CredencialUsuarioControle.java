package com.autobots.automanager.controles;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.adicionador.AdicionadorLinkCredencial;
import com.autobots.automanager.atualizador.AtualizadorCredencialUsuarioSenha;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.selecionador.SelecionadorCredencialUsuarioSenha;

@RestController
@RequestMapping("/credencial")
public class CredencialUsuarioControle {

    @Autowired
    private RepositorioUsuario repositorio;
    
    @Autowired
    private SelecionadorCredencialUsuarioSenha selecionador;
    
    @Autowired
    private AtualizadorCredencialUsuarioSenha atualizadorCredencialUsuarioSenha;

    @GetMapping("/listarCredencial/{usuarioId}")
    public ResponseEntity<?> listarCredenciais(@PathVariable long usuarioId) {
        Optional<Usuario> usuarioOpt = repositorio.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            List<CredencialUsuarioSenha> credenciais = usuario.getCredenciais()
                    .stream()
                    .filter(c -> c instanceof CredencialUsuarioSenha)
                    .map(c -> (CredencialUsuarioSenha) c)
                    .collect(Collectors.toList());
            new AdicionadorLinkCredencial().adicionarLink(credenciais);
            return new ResponseEntity<>(credenciais, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/cadastrarCredencial/{usuarioId}")
    public ResponseEntity<?> cadastrarCredencial(@PathVariable long usuarioId, @RequestBody CredencialUsuarioSenha credencial) {
        Optional<Usuario> usuarioOpt = repositorio.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();         
            boolean nomeUsuarioExistente = usuario.getCredenciais().stream()
                    .anyMatch(c -> c instanceof CredencialUsuarioSenha && ((CredencialUsuarioSenha) c).getNomeUsuario().equals(credencial.getNomeUsuario()));
            if (nomeUsuarioExistente) {
                return new ResponseEntity<>("Nome de usuário já existe.", HttpStatus.CONFLICT);
            }            
            boolean senhaExistente = usuario.getCredenciais().stream()
                    .anyMatch(c -> c instanceof CredencialUsuarioSenha && ((CredencialUsuarioSenha) c).getSenha().equals(credencial.getSenha()));
            if (senhaExistente) {
                return new ResponseEntity<>("Senha já existe.", HttpStatus.CONFLICT);
            }       
            credencial.setCriacao(new Date());
            credencial.setUltimoAcesso(new Date());
            usuario.getCredenciais().add(credencial);
            repositorio.save(usuario);
            return new ResponseEntity<>("Credencial cadastrada com sucesso.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/atualizarCredencial/{usuarioId}/{credencialId}")
    public ResponseEntity<?> atualizarCredencial(@PathVariable Long usuarioId, @PathVariable Long credencialId, @RequestBody CredencialUsuarioSenha credencial) {
        Optional<Usuario> usuarioOpt = repositorio.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Optional<CredencialUsuarioSenha> credencialOpt = usuario.getCredenciais().stream()
                    .filter(c -> c.getId().equals(credencialId) && c instanceof CredencialUsuarioSenha)
                    .map(c -> (CredencialUsuarioSenha) c)
                    .findFirst();

            if (credencialOpt.isPresent()) {
                CredencialUsuarioSenha credencialExistente = credencialOpt.get();
                atualizadorCredencialUsuarioSenha.atualizar(credencialExistente, credencial);
                repositorio.save(usuario);
                return new ResponseEntity<>("Credencial atualizada com sucesso.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Credencial não encontrada para atualização.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/deletarCredencial/{usuarioId}/{credencialId}")
    public ResponseEntity<?> deletarCredencial(@PathVariable long usuarioId, @PathVariable long credencialId) {
        Optional<Usuario> usuarioOpt = repositorio.findById(usuarioId);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Optional<CredencialUsuarioSenha> credencialOpt = usuario.getCredenciais().stream()
                    .filter(c -> c.getId().equals(credencialId) && c instanceof CredencialUsuarioSenha)
                    .map(c -> (CredencialUsuarioSenha) c)
                    .findFirst();

            if (credencialOpt.isPresent()) {
                CredencialUsuarioSenha credencial = credencialOpt.get();
                usuario.getCredenciais().remove(credencial);
                repositorio.save(usuario);
                return new ResponseEntity<>("Credencial deletada com sucesso.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Credencial não encontrada.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/obterCredencial/{credencialId}")
    public ResponseEntity<?> obterCredencial(@PathVariable long credencialId) {
        List<CredencialUsuarioSenha> credenciais = repositorio.findAll().stream()
                .flatMap(u -> u.getCredenciais().stream())
                .filter(c -> c instanceof CredencialUsuarioSenha && c.getId() == credencialId)
                .map(c -> (CredencialUsuarioSenha) c)
                .collect(Collectors.toList());       
        CredencialUsuarioSenha credencial = selecionador.selecionar(credenciais, credencialId); 
        if (credencial != null) {
            new AdicionadorLinkCredencial().adicionarLink(credencial);
            return new ResponseEntity<>(credencial, HttpStatus.OK);
        }
        return new ResponseEntity<>("Credencial não encontrada.", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/obterCredenciais")
    public ResponseEntity<List<CredencialUsuarioSenha>> obterCredenciais() {
        List<CredencialUsuarioSenha> credenciais = repositorio.findAll().stream()
                .flatMap(u -> u.getCredenciais().stream())
                .filter(c -> c instanceof CredencialUsuarioSenha)
                .map(c -> (CredencialUsuarioSenha) c)
                .collect(Collectors.toList());
        if (!credenciais.isEmpty()) {
        	new AdicionadorLinkCredencial().adicionarLink(credenciais);
            return new ResponseEntity<>(credenciais, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
