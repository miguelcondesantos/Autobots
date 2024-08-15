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
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.DocumentoSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	@Autowired
	private ClienteRepositorio clienteRepositorio;
	
	@Autowired
	private DocumentoRepositorio documentoRepositorio;
	
	@Autowired
	private DocumentoSelecionador selecionador;
	
	@GetMapping("/documentos")
	public List<Documento> obterDocumentos() {
		return documentoRepositorio.findAll();
	}
	
	@GetMapping("/documento/{id}")
	public Documento obterDocumento(@PathVariable long id) {
		List<Documento> documentos = documentoRepositorio.findAll();
		return selecionador.selecionar(documentos, id);
	}
	
	@GetMapping("/listarDocumentoCliente/{clienteId}")
	public List<Documento> listarDocumentosCliente(@PathVariable long clienteId) {
	    Cliente cliente = clienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        return cliente.getDocumentos();
	    }
	    return null;
	}
	
	@PutMapping("/atualizar")
	public void atualizarDocumento(@RequestBody Documento atualizacao) {
		Documento documento = documentoRepositorio.findById(atualizacao.getId()).orElse(null);
		if (documento != null) {
			DocumentoAtualizador atualizador = new DocumentoAtualizador();
			atualizador.atualizar(documento, atualizacao);
			documentoRepositorio.save(documento);
		}
	}

	@PostMapping("/cadastrarDocumentoCliente/{clienteId}")
	public void cadastrarDocumentoCliente(@PathVariable long clienteId, @RequestBody Documento cadastrarDocumento) {
	    Cliente cliente = clienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        documentoRepositorio.save(cadastrarDocumento);
	        cliente.getDocumentos().add(cadastrarDocumento);
	        clienteRepositorio.save(cliente);
	    }
	}

	@PutMapping("/atualizarDocumentoCliente/{clienteId}/{documentoId}")
	public void atualizarDocumentoCliente(@PathVariable long clienteId, @PathVariable long documentoId, @RequestBody Documento atualizacaoDocumento) {
	    Cliente cliente = clienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        Documento documento = cliente.getDocumentos().stream()
	                .filter(d -> d.getId().equals(documentoId))
	                .findFirst()
	                .orElse(null);
	        if (documento != null) {
	            DocumentoAtualizador atualizador = new DocumentoAtualizador();
	            atualizador.atualizar(documento, atualizacaoDocumento);
	            documentoRepositorio.save(documento);
	            clienteRepositorio.save(cliente);
	        }
	    }
	}

	@DeleteMapping("/deletarDocumentoCliente/{clienteId}/{documentoId}")
	public void deletarDocumentoCliente(@PathVariable long clienteId, @PathVariable long documentoId) {
	    Cliente cliente = clienteRepositorio.findById(clienteId).orElse(null);
	    if (cliente != null) {
	        cliente.getDocumentos().removeIf(documento -> documento.getId().equals(documentoId));
	        documentoRepositorio.deleteById(documentoId);
	        clienteRepositorio.save(cliente);
	    }
	}
}
