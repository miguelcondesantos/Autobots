package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {
	
	@Autowired
	private ClienteRepositorio repositorio;

	@PostMapping("/cadastrarEnderecoCliente/{clienteId}")
	public Endereco cadastrarEndereco(@PathVariable long clienteId, @RequestBody Endereco novoEndereco) {
	    Cliente cliente = repositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        if (cliente.getEndereco() != null) {
	            return cliente.getEndereco();
	        } else {
	            cliente.setEndereco(novoEndereco);
	            repositorio.save(cliente);
	            return novoEndereco;
	        }
	    } else {
	        return null;
	    }
	}

	 
	 @GetMapping("/listarEnderecoCliente/{clienteId}")
	 public Endereco listarEnderecoCliente(@PathVariable long clienteId) {
	     Cliente cliente = repositorio.findById(clienteId).orElse(null);
	     if (cliente != null) {
	         return cliente.getEndereco();
	     }
	     return null;
	 }
	 
	 @PutMapping("/atualizarEnderecoCliente/{clienteId}")
	 public void atualizarEndereco(@PathVariable long clienteId, @RequestBody Endereco atualizarEndereco ) {
	     Cliente cliente = repositorio.findById(clienteId).orElse(null);
	     if (cliente != null) {
	         Endereco enderecoExistente = cliente.getEndereco();
	         if (enderecoExistente != null) {
	             EnderecoAtualizador atualizador = new EnderecoAtualizador();
	             atualizador.atualizar(enderecoExistente, atualizarEndereco);
	         } else {
	             cliente.setEndereco(atualizarEndereco);
	         }
	         repositorio.save(cliente);
	     }
	 }

	 @DeleteMapping("/deletarEnderecoCliente/{clienteId}")
	 public void deletarEnderecoCliente(@PathVariable long clienteId) {
		 Cliente cliente = repositorio.findById(clienteId).orElse(null);
		 if(cliente != null) {
			 cliente.setEndereco(null);
			 repositorio.save(cliente); 
		 }
	 }
}
