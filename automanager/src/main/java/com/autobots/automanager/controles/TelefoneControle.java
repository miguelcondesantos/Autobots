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
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	
	@Autowired 
	private ClienteRepositorio repositorio;
	
	@GetMapping("/listarTelefoneCliente/{clienteId}")
	public ResponseEntity<List<Telefone>> listarTelefoneCliente(@PathVariable long clienteId) {
		Cliente cliente = repositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			return new ResponseEntity<>(cliente.getTelefones(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
		
	@PostMapping("/cadastrarTelefoneCliente/{clienteId}")
	public ResponseEntity<?> cadastrarTelefoneCliente(@PathVariable long clienteId, @RequestBody Telefone cadastrarTelefone) {
		Cliente cliente = repositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			cliente.getTelefones().add(cadastrarTelefone);
			repositorio.save(cliente);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/atualizarTelefoneCliente/{clienteId}/{telefoneId}")
	public ResponseEntity<?> atualizarTelefoneCliente(@PathVariable long clienteId, @PathVariable long telefoneId, @RequestBody Telefone atualizacaoTelefone) {
		Cliente cliente = repositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			Telefone telefone = cliente.getTelefones().stream()
					.filter(t -> t.getId().equals(telefoneId))
					.findFirst()
					.orElse(null);
			if (telefone != null) {
				TelefoneAtualizador atualizador = new TelefoneAtualizador();
				atualizador.atualizar(telefone, atualizacaoTelefone);
				repositorio.save(cliente);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/deletarTelefoneCliente/{clienteId}/{telefoneId}")
	public ResponseEntity<?> deletarTelefoneCliente(@PathVariable long clienteId, @PathVariable long telefoneId) {
		Cliente cliente = repositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			Telefone telefone = cliente.getTelefones().stream()
					.filter(t -> t.getId().equals(telefoneId))
					.findFirst()
					.orElse(null);
			if (telefone != null) {
				cliente.getTelefones().remove(telefone);
				repositorio.save(cliente);
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
