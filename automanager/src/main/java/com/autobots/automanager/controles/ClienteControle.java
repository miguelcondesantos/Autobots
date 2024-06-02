package com.autobots.automanager.controles;

import java.util.List;

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

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.modelos.AdicionadorLinkCliente;
import com.autobots.automanager.modelos.ClienteAtualizador;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/cliente")
public class ClienteControle {
	
	@Autowired
	private ClienteRepositorio repositorio;
	@Autowired
	private ClienteSelecionador selecionador;
	@Autowired
	private AdicionadorLinkCliente adicionadorLink;
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> obterCliente(@PathVariable long id) {
		List<Cliente> clientes = repositorio.findAll();
		Cliente cliente = selecionador.selecionar(clientes, id);
		if (cliente == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(cliente);
			return new ResponseEntity<>(cliente, HttpStatus.FOUND);
		}
	}

	@GetMapping("/clientes")
	public ResponseEntity<List<Cliente>> obterClientes() {
		List<Cliente> clientes = repositorio.findAll();
		if (clientes.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			adicionadorLink.adicionarLink(clientes);
			return new ResponseEntity<>(clientes, HttpStatus.FOUND);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
		if (cliente.getId() == null) {
			repositorio.save(cliente);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarCliente(@RequestBody Cliente atualizacao) {
		Cliente cliente = repositorio.findById(atualizacao.getId()).orElse(null);
		if (cliente != null) {
			ClienteAtualizador atualizador = new ClienteAtualizador();
			atualizador.atualizar(cliente, atualizacao);
			repositorio.save(cliente);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirCliente(@RequestBody Cliente exclusao) {
		Cliente cliente = repositorio.findById(exclusao.getId()).orElse(null);
		if (cliente != null) {
			repositorio.delete(cliente);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}