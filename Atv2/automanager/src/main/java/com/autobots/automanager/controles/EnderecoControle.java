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
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.EnderecoAtualizador;
import com.autobots.automanager.modelos.EnderecoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@Autowired
	private EnderecoRepositorio enderecoRepositorio;
	
	@Autowired
	private EnderecoSelecionador selecionador;
	
	@Autowired
	private AdicionadorLinkEndereco adicionador;
	
	@GetMapping("/enderecos")
	public ResponseEntity<List<Endereco>> obterEnderecos() {
		List<Endereco> enderecos = enderecoRepositorio.findAll();
		adicionador.adicionarLink(enderecos);
		return new ResponseEntity<>(enderecos, HttpStatus.OK);
	}
	
	@GetMapping("/endereco/{id}")
	public ResponseEntity<Endereco> obterEndereco(@PathVariable long id) {
		List<Endereco> enderecos = enderecoRepositorio.findAll();
		Endereco endereco = selecionador.selecionar(enderecos, id);
		if (endereco != null) {
			adicionador.adicionarLink(endereco);
			return new ResponseEntity<>(endereco, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/listarEnderecoCliente/{clienteId}")
	public ResponseEntity<Endereco> listarEnderecosCliente(@PathVariable long clienteId) {
	    Optional<Cliente> clienteOpt = clienteRepositorio.findById(clienteId);
	    if (clienteOpt.isPresent()) {
	        Endereco endereco = clienteOpt.get().getEndereco();
	        return new ResponseEntity<>(endereco, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<Void> atualizarEndereco(@RequestBody Endereco atualizacao) {
		Optional<Endereco> enderecoOpt = enderecoRepositorio.findById(atualizacao.getId());
		if (enderecoOpt.isPresent()) {
			Endereco endereco = enderecoOpt.get();
			EnderecoAtualizador atualizador = new EnderecoAtualizador();
			atualizador.atualizar(endereco, atualizacao);
			enderecoRepositorio.save(endereco);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/cadastrarEnderecoCliente/{clienteId}")
	public ResponseEntity<Void> cadastrarEnderecoCliente(@PathVariable long clienteId, @RequestBody Endereco cadastrarEndereco) {
	    Optional<Cliente> clienteOpt = clienteRepositorio.findById(clienteId);
	    if (clienteOpt.isPresent()) {
	        Cliente cliente = clienteOpt.get();
	        if (cliente.getEndereco() == null) {
	            cliente.setEndereco(cadastrarEndereco);
	            clienteRepositorio.save(cliente);
	            return new ResponseEntity<>(HttpStatus.CREATED);
	        } else {
	            return new ResponseEntity<>(HttpStatus.CONFLICT);
	        }
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}


	@PutMapping("/atualizarEnderecoCliente/{clienteId}/{enderecoId}")
	public ResponseEntity<Void> atualizarEnderecoCliente(@PathVariable long clienteId, @PathVariable long enderecoId, @RequestBody Endereco atualizacaoEndereco) {
	    Optional<Cliente> clienteOpt = clienteRepositorio.findById(clienteId);
	    if (clienteOpt.isPresent()) {
	        Cliente cliente = clienteOpt.get();
	        Endereco endereco = cliente.getEndereco();
	        if (endereco != null && endereco.getId().equals(enderecoId)) {
	            EnderecoAtualizador atualizador = new EnderecoAtualizador();
	            atualizador.atualizar(endereco, atualizacaoEndereco);
	            enderecoRepositorio.save(endereco);
	            clienteRepositorio.save(cliente);
	            return new ResponseEntity<>(HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}


	@DeleteMapping("/deletarEnderecoCliente/{clienteId}/{enderecoId}")
	public ResponseEntity<Void> deletarEnderecoCliente(@PathVariable long clienteId, @PathVariable long enderecoId) {
	    Optional<Cliente> clienteOpt = clienteRepositorio.findById(clienteId);
	    if (clienteOpt.isPresent()) {
	        Cliente cliente = clienteOpt.get();
	        Endereco endereco = cliente.getEndereco();
	        if (endereco != null && endereco.getId().equals(enderecoId)) {
	            cliente.setEndereco(null);
	            enderecoRepositorio.deleteById(enderecoId);
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