package com.autobots.automanager.controles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.adicionador.AdicionadorLinkEmail;
import com.autobots.automanager.atualizador.AtualizadorEmail;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.selecionador.SelecionadorEmail;

@RestController
@RequestMapping("/email")
public class EmailControle {

    @Autowired
    private RepositorioUsuario repositorio;
    
    @Autowired
    private AtualizadorEmail atualizadorEmail;
    
    @Autowired
    private SelecionadorEmail selecionador;

    @GetMapping("/listarEmailUsuario/{usuarioId}")
    public ResponseEntity<List<Email>> listarEmailUsuario(@PathVariable long usuarioId) {
        Usuario usuario = repositorio.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Email> emails = usuario.getEmails();
        new AdicionadorLinkEmail().adicionarLink(emails);
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }

    @PostMapping("/cadastrarEmailUsuario/{usuarioId}")
    public ResponseEntity<String> cadastrarEmailUsuario(@PathVariable long usuarioId, @RequestBody @Validated Email cadastrarEmail) {
        Usuario usuario = repositorio.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        usuario.getEmails().add(cadastrarEmail);
        repositorio.save(usuario);
        return new ResponseEntity<>("Email cadastrado com sucesso", HttpStatus.CREATED);
    }


    @PutMapping("/atualizarEmailUsuario/{usuarioId}/{emailId}")
    public ResponseEntity<String> atualizarEmailUsuario(@PathVariable long usuarioId, @PathVariable long emailId, @RequestBody @Validated Email atualizacaoEmail) {
        Usuario usuario = repositorio.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        Email email = usuario.getEmails().stream()
                .filter(e -> e.getId() == emailId)
                .findFirst()
                .orElse(null);
        if (email == null) {
            return new ResponseEntity<>("Email não encontrado", HttpStatus.NOT_FOUND);
        }
        atualizadorEmail.atualizar(email, atualizacaoEmail);
        repositorio.save(usuario);
        return new ResponseEntity<>("Email atualizado com sucesso", HttpStatus.OK);
    }

    @DeleteMapping("/deletarEmailUsuario/{usuarioId}/{emailId}")
    public ResponseEntity<String> deletarEmailUsuario(@PathVariable long usuarioId, @PathVariable long emailId) {
        Usuario usuario = repositorio.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        Email email = usuario.getEmails().stream()
                .filter(e -> e.getId() == emailId)
                .findFirst()
                .orElse(null);
        if (email == null) {
            return new ResponseEntity<>("Email não encontrado", HttpStatus.NOT_FOUND);
        }
        usuario.getEmails().remove(email);
        repositorio.save(usuario);
        return new ResponseEntity<>("Email deletado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/obterEmail/{emailId}")
    public ResponseEntity<?> obterEmail(@PathVariable long emailId) {
        Email email = selecionador.selecionar(repositorio.findAll().stream()
                .flatMap(u -> u.getEmails().stream())
                .collect(Collectors.toList()), emailId);
        
        if (email != null) {
            new AdicionadorLinkEmail().adicionarLink(email);
            return new ResponseEntity<>(email, HttpStatus.OK);
        }
        
        return new ResponseEntity<>("Email não encontrado", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/obterEmails")
    public ResponseEntity<List<Email>> obterEmails() {
        List<Email> emails = repositorio.findAll().stream()
                .flatMap(u -> u.getEmails().stream())
                .collect(Collectors.toList());
        if (emails.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        new AdicionadorLinkEmail().adicionarLink(emails);
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }
}
