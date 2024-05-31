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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.ClienteAtualizador;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/cliente")
public class ClienteControle {
	@Autowired
	private ClienteRepositorio repositorio;
	@Autowired
	private ClienteSelecionador selecionador;
	
	
	// ******************* Cliente ******************* //
	@GetMapping("/cliente/{id}")
	public Cliente obterCliente(@PathVariable long id) {
		List<Cliente> clientes = repositorio.findAll();
		return selecionador.selecionar(clientes, id);
	}

	@GetMapping("/clientes")
	public List<Cliente> obterClientes() {
		List<Cliente> clientes = repositorio.findAll();
		return clientes;
	}

	@PostMapping("/cadastro")
	public void cadastrarCliente(@RequestBody Cliente cliente) {
		repositorio.save(cliente);
	}

	@PutMapping("/atualizar")
	public void atualizarCliente(@RequestBody Cliente atualizacao) {
		Cliente cliente = repositorio.getById(atualizacao.getId());
		ClienteAtualizador atualizador = new ClienteAtualizador();
		atualizador.atualizar(cliente, atualizacao);
		repositorio.save(cliente);
	}

	@DeleteMapping("/excluir")
	public void excluirCliente(@RequestBody Cliente exclusao) {
		Cliente cliente = repositorio.getById(exclusao.getId());
		repositorio.delete(cliente);
	}
	
	
	// ******************* Telefone ******************* //
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
	
	
	// ******************* Endereço ******************* //
	 @PostMapping("/cadastrarEnderecoCliente/{clienteId}")
	 public Endereco cadastrarEndereco(@PathVariable long clienteId, @RequestBody Endereco novoEndereco) {
		 Cliente cliente = repositorio.findById(clienteId).orElse(null);
		 	if (cliente.getEndereco() != null) {
	            return cliente.getEndereco();
	        }
	        cliente.setEndereco(novoEndereco);
	        repositorio.save(cliente);
	        return novoEndereco; 
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
	 
	 	 
	// ******************* Documento ******************* //
	 @GetMapping("/listarDocumentoCliente/{clienteId}")
	 public List<Documento> listarDocumentosCliente(@PathVariable long clienteId) {
	     Cliente cliente = repositorio.findById(clienteId).orElse(null);
	     if (cliente != null) {
	         return cliente.getDocumentos();
	     }
	     return null;
	 }

	 @PostMapping("/cadastrarDocumentoCliente/{clienteId}")
	 public void cadastrarDocumentoCliente(@PathVariable long clienteId, @RequestBody Documento cadastrarDocumento) {
	     Cliente cliente = repositorio.findById(clienteId).orElse(null);
	     if (cliente != null) {
	         cliente.getDocumentos().add(cadastrarDocumento);
	         repositorio.save(cliente);
	     }
	 }

	 @PutMapping("/atualizarDocumentoCliente/{clienteId}/{documentoId}")
	 public void atualizarDocumentoCliente(@PathVariable long clienteId, @PathVariable long documentoId, @RequestBody Documento atualizacaoDocumento) {
	     Cliente cliente = repositorio.findById(clienteId).orElse(null);
	     if (cliente != null) {
	         List<Documento> documentos = cliente.getDocumentos();
	         for (Documento documento : documentos) {
	             if (documento.getId() != null && documento.getId().equals(documentoId)) {
	                 DocumentoAtualizador atualizador = new DocumentoAtualizador();
	                 atualizador.atualizar(documento, atualizacaoDocumento);
	             }
	         }
	         repositorio.save(cliente);
	     }
	 }

	 @DeleteMapping("/deletarDocumentoCliente/{clienteId}/{documentoId}")
	 public void deletarDocumentoCliente(@PathVariable long clienteId, @PathVariable long documentoId) {
	     Cliente cliente = repositorio.findById(clienteId).orElse(null);
	     if (cliente != null) {
	         cliente.getDocumentos().removeIf(documento -> documento.getId() != null && documento.getId().equals(documentoId));
	         repositorio.save(cliente);
	     }
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
}
