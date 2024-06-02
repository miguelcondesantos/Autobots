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
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {
	@Autowired
	private ClienteRepositorio repositorio;
	
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
