package com.autobots.automanager.controles;

import java.util.List;

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
import com.autobots.automanager.modelo.EnderecoSelecionador;
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
	
	@GetMapping("/enderecos")
	public List<Endereco> obterEnderecos() {
		return enderecoRepositorio.findAll();
	}
	
	@GetMapping("/endereco/{id}")
	public Endereco obterEndereco(@PathVariable long id) {
		List<Endereco> enderecos = enderecoRepositorio.findAll();
		return selecionador.selecionar(enderecos, id);
	}

	@PostMapping("/cadastrarEnderecoCliente/{clienteId}")
	public Endereco cadastrarEndereco(@PathVariable long clienteId, @RequestBody Endereco novoEndereco) {
	    Cliente cliente = clienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        if (cliente.getEndereco() != null) {
	            return cliente.getEndereco();
	        } else {
	            cliente.setEndereco(novoEndereco);
	            enderecoRepositorio.save(novoEndereco);
	            clienteRepositorio.save(cliente);
	            return novoEndereco;
	        }
	    }
	    return null;
	}

	@GetMapping("/listarEnderecoCliente/{clienteId}")
	public Endereco listarEnderecoCliente(@PathVariable long clienteId) {
		Cliente cliente = clienteRepositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			return cliente.getEndereco();
		}
		return null;
	}

	@PutMapping("/atualizar")
	public void atualizarEndereco(@RequestBody Endereco atualizacao) {
		Endereco endereco = enderecoRepositorio.findById(atualizacao.getId()).orElse(null);
		if (endereco != null) {
			EnderecoAtualizador atualizador = new EnderecoAtualizador();
			atualizador.atualizar(endereco, atualizacao);
			enderecoRepositorio.save(endereco);
		}
	}

	@PutMapping("/atualizarEnderecoCliente/{clienteId}")
	public void atualizarEnderecoCliente(@PathVariable long clienteId, @RequestBody Endereco atualizarEndereco) {
		Cliente cliente = clienteRepositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			Endereco enderecoExistente = cliente.getEndereco();
			if (enderecoExistente != null) {
				EnderecoAtualizador atualizador = new EnderecoAtualizador();
				atualizador.atualizar(enderecoExistente, atualizarEndereco);
				enderecoRepositorio.save(enderecoExistente);
			} else {
				cliente.setEndereco(atualizarEndereco);
				enderecoRepositorio.save(atualizarEndereco);
			}
			clienteRepositorio.save(cliente);
		}
	}

	@DeleteMapping("/deletarEnderecoCliente/{clienteId}")
	public void deletarEnderecoCliente(@PathVariable long clienteId) {
		Cliente cliente = clienteRepositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			Endereco endereco = cliente.getEndereco();
			if (endereco != null) {
				cliente.setEndereco(null);
				enderecoRepositorio.delete(endereco);
				clienteRepositorio.save(cliente);
			}
		}
	}
}
