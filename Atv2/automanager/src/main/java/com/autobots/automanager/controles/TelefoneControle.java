package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

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
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.TelefoneAtualizador;
import com.autobots.automanager.modelos.TelefoneSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@Autowired
	private TelefoneRepositorio telefoneRepositorio;
	
	@Autowired
	private TelefoneSelecionador selecionador;
	
	@Autowired
	private AdicionadorLinkTelefone adicionador;

	@GetMapping("/telefones")
	public ResponseEntity<List<Telefone>> obterTelefones() {
		List<Telefone> telefones = telefoneRepositorio.findAll();
		adicionador.adicionarLink(telefones);
		return new ResponseEntity<>(telefones, HttpStatus.OK);
	}

	@GetMapping("/telefone/{id}")
	public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
		List<Telefone> telefones = telefoneRepositorio.findAll();
		Telefone telefone = selecionador.selecionar(telefones, id);
		if (telefone != null) {
			adicionador.adicionarLink(telefone);
			return new ResponseEntity<>(telefone, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/listarTelefoneCliente/{clienteId}")
	public ResponseEntity<List<Telefone>> listarTelefonesCliente(@PathVariable long clienteId) {
	    Optional<Cliente> clienteOpt = clienteRepositorio.findById(clienteId);
	    if (clienteOpt.isPresent()) {
	        return new ResponseEntity<>(clienteOpt.get().getTelefones(), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@PutMapping("/atualizar")
	public ResponseEntity<Void> atualizarTelefone(@RequestBody Telefone atualizacao) {
		Optional<Telefone> telefoneOpt = telefoneRepositorio.findById(atualizacao.getId());
		if (telefoneOpt.isPresent()) {
			Telefone telefone = telefoneOpt.get();
			TelefoneAtualizador atualizador = new TelefoneAtualizador();
			atualizador.atualizar(telefone, atualizacao);
			telefoneRepositorio.save(telefone);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/cadastrarTelefoneCliente/{clienteId}")
	public ResponseEntity<Void> cadastrarTelefoneCliente(@PathVariable long clienteId, @RequestBody Telefone cadastrarTelefone) {
	    Optional<Cliente> clienteOpt = clienteRepositorio.findById(clienteId);
	    if (clienteOpt.isPresent()) {
	        Cliente cliente = clienteOpt.get();
	        telefoneRepositorio.save(cadastrarTelefone);
	        cliente.getTelefones().add(cadastrarTelefone);
	        clienteRepositorio.save(cliente);
	        return new ResponseEntity<>(HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@PutMapping("/atualizarTelefoneCliente/{clienteId}/{telefoneId}")
	public ResponseEntity<Void> atualizarTelefoneCliente(@PathVariable long clienteId, @PathVariable long telefoneId, @RequestBody Telefone atualizacaoTelefone) {
	    Optional<Cliente> clienteOpt = clienteRepositorio.findById(clienteId);
	    if (clienteOpt.isPresent()) {
	        Cliente cliente = clienteOpt.get();
	        Telefone telefone = cliente.getTelefones().stream()
	                .filter(t -> t.getId().equals(telefoneId))
	                .findFirst()
	                .orElse(null);
	        if (telefone != null) {
	            TelefoneAtualizador atualizador = new TelefoneAtualizador();
	            atualizador.atualizar(telefone, atualizacaoTelefone);
	            telefoneRepositorio.save(telefone);
	            clienteRepositorio.save(cliente);
	            return new ResponseEntity<>(HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@DeleteMapping("/deletarTelefoneCliente/{clienteId}/{telefoneId}")
	public ResponseEntity<Void> deletarTelefoneCliente(@PathVariable long clienteId, @PathVariable long telefoneId) {
	    Optional<Cliente> clienteOpt = clienteRepositorio.findById(clienteId);
	    if (clienteOpt.isPresent()) {
	        Cliente cliente = clienteOpt.get();
	        boolean telefoneRemovido = cliente.getTelefones().removeIf(telefone -> telefone.getId().equals(telefoneId));
	        if (telefoneRemovido) {
	            telefoneRepositorio.deleteById(telefoneId);
	            clienteRepositorio.save(cliente);
	            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
}
