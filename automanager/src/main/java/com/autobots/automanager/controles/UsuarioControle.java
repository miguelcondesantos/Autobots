package com.autobots.automanager.controles;

import java.util.List;
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

import com.autobots.automanager.adicionador.AdicionadorLinkUsuario;
import com.autobots.automanager.atualizador.AtualizadorUsuario;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.selecionador.SelecionadorUsuario;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {
	
	@Autowired
	private RepositorioUsuario repositorio;
	@Autowired
	private SelecionadorUsuario selecionador;
	@Autowired
	private AdicionadorLinkUsuario adicionador;
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
	    List<Usuario> usuarios = repositorio.findAll();
	    Usuario usuario = selecionador.selecionar(usuarios, id);
	    if (usuario == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    } else {
	    	adicionador.adicionarLink(usuario);
	        return new ResponseEntity<>(usuario, HttpStatus.FOUND);
	    }
	}

	@GetMapping("/usuarios")
	public ResponseEntity<List<Usuario>> obterUsuarios() {
	    List<Usuario> usuarios = repositorio.findAll().stream().limit(10).collect(Collectors.toList());
	    if (usuarios.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    } else {
	        adicionador.adicionarLink(usuarios);
	        return new ResponseEntity<>(usuarios, HttpStatus.OK);
	    }
	}


	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
	    if (usuario.getId() == null) {
	        repositorio.save(usuario);
	        return new ResponseEntity<>(HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>(HttpStatus.CONFLICT);
	    }
	}

	@PutMapping("/atualizar")
	public ResponseEntity<Usuario> atualizarUsuario(@RequestBody Usuario usuario) {

		HttpStatus status = HttpStatus.CONFLICT;
		Usuario usuarioEncontrado = repositorio.getById(usuario.getId());
		if (usuarioEncontrado != null) {
			AtualizadorUsuario atualizador = new AtualizadorUsuario();
			atualizador.atualizar(usuarioEncontrado, usuario);
			repositorio.save(usuarioEncontrado);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<Usuario>(status);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirUsuario(@RequestBody Usuario exclusao) {
	    Usuario usuario = repositorio.findById(exclusao.getId()).orElse(null);
	    if (usuario != null) {
	        repositorio.delete(usuario);
	        return new ResponseEntity<>(HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	}


}
