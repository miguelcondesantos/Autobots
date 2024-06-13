package com.autobots.automanager.controles;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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

import com.autobots.automanager.adicionador.AdicionadorLinkDocumento;
import com.autobots.automanager.adicionador.AdicionadorLinkEndereco;
import com.autobots.automanager.atualizador.AtualizadorDocumento;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.selecionador.SelecionadorDocumento;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

    @Autowired
    private RepositorioUsuario repositorio;
    
    @Autowired
    private SelecionadorDocumento selecionador;

    @GetMapping("/listarDocumentoUsuario/{usuarioId}")
    public ResponseEntity<List<Documento>> listarDocumentosUsuario(@PathVariable long usuarioId) {
        Usuario usuario = repositorio.findById(usuarioId).orElse(null);
        if (usuario != null) {
            new AdicionadorLinkEndereco().adicionarLink(usuario.getEndereco());
            return new ResponseEntity<>(usuario.getDocumentos(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/cadastrarDocumentoUsuario/{usuarioId}")
    @Transactional
    public ResponseEntity<String> cadastrarDocumentoUsuario(@PathVariable Long usuarioId, @RequestBody Documento novoDocumento) {
        Usuario usuario = repositorio.findById(usuarioId).orElse(null);
        if (usuario != null) {
            if (usuario.getDocumentos().stream().anyMatch(doc -> doc.getNumero().equals(novoDocumento.getNumero()))) {
                return new ResponseEntity<>("Documento já associado ao usuário.", HttpStatus.CONFLICT);
            }
            usuario.getDocumentos().add(novoDocumento);
            Usuario usuarioSalvo = repositorio.save(usuario);
            if (usuarioSalvo.getDocumentos().stream().anyMatch(doc -> doc.getNumero().equals(novoDocumento.getNumero()))) {
                return new ResponseEntity<>("Documento cadastrado com sucesso.", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Erro ao associar o documento ao usuário.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/atualizarDocumentoUsuario/{usuarioId}/{documentoId}")
    @Transactional
    public ResponseEntity<String> atualizarDocumentoUsuario(@PathVariable long usuarioId, @PathVariable long documentoId, @RequestBody Documento atualizacaoDocumento) {
        Usuario usuario = repositorio.findById(usuarioId).orElse(null);       
        if (usuario != null) {
            List<Documento> documentos = usuario.getDocumentos();
            for (Documento documento : documentos) {
                if (documento.getId() != null && documento.getId().equals(documentoId)) {
                    AtualizadorDocumento atualizador = new AtualizadorDocumento();
                    atualizador.atualizar(documento, atualizacaoDocumento);
                    repositorio.save(usuario);
                    return new ResponseEntity<>("Documento atualizado com sucesso.", HttpStatus.OK);
                }
            }
            return new ResponseEntity<>("Documento não encontrado.", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deletarDocumentoUsuario/{usuarioId}/{documentoId}")
    @Transactional
    public ResponseEntity<String> deletarDocumentoUsuario(@PathVariable long usuarioId, @PathVariable long documentoId) {
        Usuario usuario = repositorio.findById(usuarioId).orElse(null);
        if (usuario != null) {
            boolean removed = usuario.getDocumentos().removeIf(documento -> documento.getId() != null && documento.getId().equals(documentoId));
            if (removed) {
                repositorio.save(usuario);
                return new ResponseEntity<>("Documento deletado com sucesso.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Documento não encontrado.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/obterDocumento/{documentoId}")
    public ResponseEntity<?> obterDocumento(@PathVariable long documentoId) {
        Documento documento = selecionador.selecionar(repositorio.findAll().stream()
                .flatMap(u -> u.getDocumentos().stream())
                .collect(Collectors.toList()), documentoId);
        if (documento != null) {
            new AdicionadorLinkDocumento().adicionarLink(documento);
            return new ResponseEntity<>(documento, HttpStatus.OK);
        }
        return new ResponseEntity<>("Documento não encontrado.", HttpStatus.NOT_FOUND);
    }


    @GetMapping("/obterDocumentos")
    public ResponseEntity<List<Documento>> obterDocumentos() {
        List<Documento> documentos = repositorio.findAll().stream()
                .flatMap(u -> u.getDocumentos().stream())
                .collect(Collectors.toList());
        if (!documentos.isEmpty()) {
            new AdicionadorLinkDocumento().adicionarLink(documentos);
            return new ResponseEntity<>(documentos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
