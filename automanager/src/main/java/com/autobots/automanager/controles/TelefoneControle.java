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
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	
	@Autowired
	private ClienteRepositorio repositorio;
	
	@GetMapping("/listarTelefoneCliente/{clienteId}")
	public List<Telefone> listarTelefoneCliente(@PathVariable long clienteId) {
		Cliente cliente = repositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			return cliente.getTelefones();
		}
		return null;
	}
	
	@PostMapping("/cadastrarTelefoneCliente/{clienteId}")
	public void cadastrarTelefoneCliente(@PathVariable long clienteId, @RequestBody Telefone cadastrarTelefone) {
	    Cliente cliente = repositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        Telefone telefone = new Telefone();
	        telefone.setDdd(cadastrarTelefone.getDdd());
	        telefone.setNumero(cadastrarTelefone.getNumero());      
	        cliente.getTelefones().add(telefone);
	        repositorio.save(cliente);
	    }  
	}

	@PutMapping("/atualizarTelefoneCliente/{clienteId}/{telefoneId}")
	public void atualizarTelefoneCliente(@PathVariable long clienteId, @PathVariable long telefoneId, @RequestBody Telefone atualizacaoTelefone) {
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
			}
		}
	}
	
	@DeleteMapping("/deletarTelefoneCliente/{clienteId}/{telefoneId}")
	public void deletarTelefoneCliente(@PathVariable long clienteId,@PathVariable long telefoneId){
		Cliente cliente = repositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			Telefone telefone = cliente.getTelefones().stream()
                    .filter(t -> t.getId().equals(telefoneId))
                    .findFirst()
                    .orElse(null);
			if (telefone != null){
				cliente.getTelefones().remove(telefone);
				repositorio.save(cliente);
			}
		}
	} 

}
