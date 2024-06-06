package com.autobots.automanager.controles;

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

import com.autobots.automanager.atualizador.AtualizadorEndereco;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	
	@Autowired
	private RepositorioUsuario repositorio;
	
	public class MensagemComEndereco {
	    private String mensagem;
	    private Endereco endereco;
	    
	    public MensagemComEndereco(String mensagem, Endereco endereco) {
	        this.mensagem = mensagem;
	        this.endereco = endereco;
	    }
	    
		public String getMensagem() {
			return mensagem;
		}
		public void setMensagem(String mensagem) {
			this.mensagem = mensagem;
		}
		public Endereco getEndereco() {
			return endereco;
		}
		public void setEndereco(Endereco endereco) {
			this.endereco = endereco;
		}

	    
	}
	
	@PostMapping("/cadastrarEnderecoCliente/{clienteId}")
	public ResponseEntity<?> cadastrarEndereco(@PathVariable long clienteId, @RequestBody Endereco novoEndereco) {
	    Usuario cliente = repositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        if (cliente.getEndereco() != null) {
	            MensagemComEndereco mensagemComEndereco = new MensagemComEndereco("Já existe um endereço cadastrado para este cliente", cliente.getEndereco());
	            return ResponseEntity.status(HttpStatus.CONFLICT).body(mensagemComEndereco);
	        }
	        cliente.setEndereco(novoEndereco);
	        repositorio.save(cliente);
	        return new ResponseEntity<>(novoEndereco, HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

		 
	@GetMapping("/listarEnderecoCliente/{clienteId}")
	public ResponseEntity<Endereco> listarEnderecoCliente(@PathVariable long clienteId) {
		Usuario cliente = repositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			if (cliente.getEndereco() == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(cliente.getEndereco(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
		 
	@PutMapping("/atualizarEnderecoCliente/{clienteId}")
	public ResponseEntity<?> atualizarEndereco(@PathVariable long clienteId, @RequestBody Endereco atualizarEndereco) {
		Usuario cliente = repositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			Endereco enderecoExistente = cliente.getEndereco();
			if (enderecoExistente != null) {
				AtualizadorEndereco atualizador = new AtualizadorEndereco();
				atualizador.atualizar(enderecoExistente, atualizarEndereco);
			} else {
				cliente.setEndereco(atualizarEndereco);
			}
			repositorio.save(cliente);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/deletarEnderecoCliente/{clienteId}")
	public ResponseEntity<?> deletarEnderecoCliente(@PathVariable long clienteId) {
		Usuario cliente = repositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        if (cliente.getEndereco() != null) {
	            cliente.setEndereco(null);
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


