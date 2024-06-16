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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.modelos.DocumentoSelecionador;
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
	
	@Autowired
	private AdicionadorLinkDocumento adicionador;
	
	@GetMapping("/documentos")
	public ResponseEntity<List<Documento>> obterDocumentos() {
		List<Documento> documentos = documentoRepositorio.findAll();
		adicionador.adicionarLink(documentos);
		return new ResponseEntity<>(documentos, HttpStatus.OK);
	}
	
	@GetMapping("/documento/{id}")
	public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
		List<Documento> documentos = documentoRepositorio.findAll();
		Documento documento = selecionador.selecionar(documentos, id);
		if (documento != null) {
			adicionador.adicionarLink(documento);
			return new ResponseEntity<>(documento, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/listarDocumentoCliente/{clienteId}")
	public ResponseEntity<List<Documento>> listarDocumentosCliente(@PathVariable long clienteId) {
	    Optional<Cliente> clienteOpt = clienteRepositorio.findById(clienteId);
	    if (clienteOpt.isPresent()) {
	        return new ResponseEntity<>(clienteOpt.get().getDocumentos(), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<Void> atualizarDocumento(@RequestBody Documento atualizacao) {
		Optional<Documento> documentoOpt = documentoRepositorio.findById(atualizacao.getId());
		if (documentoOpt.isPresent()) {
			Documento documento = documentoOpt.get();
			DocumentoAtualizador atualizador = new DocumentoAtualizador();
			atualizador.atualizar(documento, atualizacao);
			documentoRepositorio.save(documento);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/cadastrarDocumentoCliente/{clienteId}")
	public ResponseEntity<Void> cadastrarDocumentoCliente(@PathVariable long clienteId, @RequestBody Documento cadastrarDocumento) {
	    Optional<Cliente> clienteOpt = clienteRepositorio.findById(clienteId);
	    if (clienteOpt.isPresent()) {
	        Cliente cliente = clienteOpt.get();
	        documentoRepositorio.save(cadastrarDocumento);
	        cliente.getDocumentos().add(cadastrarDocumento);
	        clienteRepositorio.save(cliente);
	        return new ResponseEntity<>(HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@PutMapping("/atualizarDocumentoCliente/{clienteId}/{documentoId}")
	public ResponseEntity<Void> atualizarDocumentoCliente(@PathVariable long clienteId, @PathVariable long documentoId, @RequestBody Documento atualizacaoDocumento) {
	    Optional<Cliente> clienteOpt = clienteRepositorio.findById(clienteId);
	    if (clienteOpt.isPresent()) {
	        Cliente cliente = clienteOpt.get();
	        Documento documento = cliente.getDocumentos().stream()
	                .filter(d -> d.getId().equals(documentoId))
	                .findFirst()
	                .orElse(null);
	        if (documento != null) {
	            DocumentoAtualizador atualizador = new DocumentoAtualizador();
	            atualizador.atualizar(documento, atualizacaoDocumento);
	            documentoRepositorio.save(documento);
	            clienteRepositorio.save(cliente);
	            return new ResponseEntity<>(HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@DeleteMapping("/deletarDocumentoCliente/{clienteId}/{documentoId}")
	public ResponseEntity<Void> deletarDocumentoCliente(@PathVariable long clienteId, @PathVariable long documentoId) {
	    Optional<Cliente> clienteOpt = clienteRepositorio.findById(clienteId);
	    if (clienteOpt.isPresent()) {
	        Cliente cliente = clienteOpt.get();
	        boolean documentoRemovido = cliente.getDocumentos().removeIf(documento -> documento.getId().equals(documentoId));
	        if (documentoRemovido) {
	            documentoRepositorio.deleteById(documentoId);
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