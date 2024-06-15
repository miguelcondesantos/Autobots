package com.autobots.automanager.controles;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.adicionador.AdicionadorLinkUsuario;
import com.autobots.automanager.atualizador.AtualizadorUsuario;
import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVenda;
import com.autobots.automanager.selecionador.SelecionadorUsuario;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

    @Autowired
    private RepositorioUsuario repositorio;
    @Autowired
    private RepositorioEmpresa empresaRepositorio;
    @Autowired
    private RepositorioVenda vendaRepositorio;
    @Autowired
    private SelecionadorUsuario selecionador;
    @Autowired
    private AdicionadorLinkUsuario adicionador;
    
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE', 'VENDEDOR', 'CLIENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<?> obterUsuario(@PathVariable long id) {
        List<Usuario> usuarios = repositorio.findAll();
        Usuario usuario = selecionador.selecionar(usuarios, id);
        if (usuario == null) {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        } else {
            adicionador.adicionarLink(usuario);
            return new ResponseEntity<>(usuario, HttpStatus.FOUND);
        }
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE', 'VENDEDOR')")
    @GetMapping("/usuarios")
    public ResponseEntity<?> obterUsuarios() {
        List<Usuario> usuarios = repositorio.findAll().stream().collect(Collectors.toList());
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>("Nenhum usuário encontrado.", HttpStatus.NOT_FOUND);
        } else {
            adicionador.adicionarLink(usuarios);
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }
    }
    
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE', 'VENDEDOR')")
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        if (usuario.getId() == null) {
            for (Credencial credencial : usuario.getCredenciais()) {
                if (credencial instanceof CredencialUsuarioSenha) {
                    CredencialUsuarioSenha credencialUsuarioSenha = (CredencialUsuarioSenha) credencial;
                    if (credencialUsuarioSenha.getCriacao() == null) {
                        credencialUsuarioSenha.setCriacao(new Date());
                    }
                    if (credencialUsuarioSenha.getUltimoAcesso() == null) {
                        credencialUsuarioSenha.setUltimoAcesso(new Date());
                    }
                }
            }
            repositorio.save(usuario);
            return new ResponseEntity<>("Usuário cadastrado com sucesso.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Conflito: O usuário já possui um ID.", HttpStatus.CONFLICT);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE', 'VENDEDOR')")
    @PutMapping("/atualizar")
    public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioEncontrado = repositorio.findById(usuario.getId()).orElse(null);
        if (usuarioEncontrado != null) {
            AtualizadorUsuario atualizador = new AtualizadorUsuario();
            atualizador.atualizar(usuarioEncontrado, usuario);
            repositorio.save(usuarioEncontrado);
            return new ResponseEntity<>("Usuário atualizado com sucesso.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE', 'VENDEDOR')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable long id) {
        Optional<Usuario> usuarioOptional = repositorio.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            List<Venda> vendasCliente = vendaRepositorio.findByCliente_Id(usuario.getId());
            for (Venda venda : vendasCliente) {
                venda.setCliente(null);
                vendaRepositorio.save(venda);
            }

            List<Venda> vendasFuncionario = vendaRepositorio.findByFuncionario_Id(usuario.getId());
            for (Venda venda : vendasFuncionario) {
                venda.setFuncionario(null);
                vendaRepositorio.save(venda);
            }

            List<Empresa> empresasComUsuario = empresaRepositorio.findByUsuarios_Id(usuario.getId());
            for (Empresa empresa : empresasComUsuario) {
                empresa.getUsuarios().remove(usuario);
                empresaRepositorio.save(empresa);
            }

            repositorio.delete(usuario);
            return new ResponseEntity<>("Usuário excluído com sucesso.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }
}
