package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	
	@Autowired
	private ClienteRepositorio repositorio;
	
	@GetMapping("/listarDocumentoCliente/{clienteId}")
	public ResponseEntity<List<Documento>> listarDocumentosCliente(@PathVariable long clienteId) {
		Cliente cliente = repositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			return new ResponseEntity<>(cliente.getDocumentos(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	 @PostMapping("/cadastrarDocumentoCliente/{clienteId}")
	    public ResponseEntity<?> cadastrarDocumentoCliente(@PathVariable long clienteId, @RequestBody Documento cadastrarDocumento) {
	        try {
	            Cliente cliente = repositorio.findById(clienteId).orElse(null);
	            if (cliente != null) {
	                cliente.getDocumentos().add(cadastrarDocumento);
	                repositorio.save(cliente);
	                return new ResponseEntity<>(HttpStatus.CREATED);
	            } else {
	                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	            }
	        } catch (DataIntegrityViolationException e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Já existe um documento com o mesmo número para este cliente");
	        }
	    }

	@PutMapping("/atualizarDocumentoCliente/{clienteId}/{documentoId}")
	public ResponseEntity<?> atualizarDocumentoCliente(@PathVariable long clienteId, @PathVariable long documentoId, @RequestBody Documento atualizacaoDocumento) {
		Cliente cliente = repositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			List<Documento> documentos = cliente.getDocumentos();
			for (Documento documento : documentos) {
				if (documento.getId() != null && documento.getId().equals(documentoId)) {
					DocumentoAtualizador atualizador = new DocumentoAtualizador();
					atualizador.atualizar(documento, atualizacaoDocumento);
					repositorio.save(cliente);
					return new ResponseEntity<>(HttpStatus.OK);
				}
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/deletarDocumentoCliente/{clienteId}/{documentoId}")
	public ResponseEntity<?> deletarDocumentoCliente(@PathVariable long clienteId, @PathVariable long documentoId) {
		Cliente cliente = repositorio.findById(clienteId).orElse(null);
		if (cliente != null) {
			cliente.getDocumentos().removeIf(documento -> documento.getId() != null && documento.getId().equals(documentoId));
			repositorio.save(cliente);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
