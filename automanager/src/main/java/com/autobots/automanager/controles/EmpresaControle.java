package com.autobots.automanager.controles;

import java.util.Date;
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

import com.autobots.automanager.adicionador.AdicionadorLinkEmpresa;
import com.autobots.automanager.atualizador.AtualizadorEmpresa;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVenda;
import com.autobots.automanager.selecionador.SelecionadorEmpresa;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {
	
	@Autowired
	private RepositorioEmpresa repositorioEmpresa;
	
	@Autowired
	private RepositorioUsuario repositorioUsuario;

	@Autowired
	private RepositorioMercadoria repositorioMercadoria;

	@Autowired
	private RepositorioServico repositorioServico;

	@Autowired
	private RepositorioVenda repositorioVenda;
	
	@Autowired
	private AdicionadorLinkEmpresa adicionadorLink;
	
	@Autowired
	private SelecionadorEmpresa selecionador;
	
	@Autowired
	private AtualizadorEmpresa atualizadorEmpresa;
	
	@PostMapping("/cadastrarEmpresa")
	public ResponseEntity<?> cadastrarEmpresa(@RequestBody Empresa empresa) {
		empresa.setCadastro(new Date());
		repositorioEmpresa.save(empresa);
		return new ResponseEntity<>(empresa, HttpStatus.CREATED);
	}

	@PutMapping("/associarUsuarioEmpresa/{usuarioId}/{empresaId}")
	public ResponseEntity<?> associarUsuarioEmpresa(@PathVariable Long usuarioId, @PathVariable Long empresaId) {
		Optional<Empresa> empresaOptional = repositorioEmpresa.findById(empresaId);
		Optional<Usuario> usuarioOptional = repositorioUsuario.findById(usuarioId);
		
		if (empresaOptional.isPresent() && usuarioOptional.isPresent()) {
			Empresa empresa = empresaOptional.get();
			Usuario usuario = usuarioOptional.get();
			if (empresa.getUsuarios().contains(usuario)) {
	            return new ResponseEntity<>("Usuário já associado à empresa.", HttpStatus.OK);
	        }
			empresa.getUsuarios().add(usuario);
			repositorioEmpresa.save(empresa);
			return new ResponseEntity<>(empresa, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Empresa ou Usuário não encontrado.", HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/atualizarEmpresa/{empresaId}")
	public ResponseEntity<?> atualizarEmpresa(@PathVariable Long empresaId, @RequestBody Empresa atualizacao) {
		try {
			Empresa empresa = repositorioEmpresa.findById(empresaId).orElseThrow(() -> new RuntimeException("Empresa não encontrada."));
			atualizadorEmpresa.atualizar(empresa, atualizacao);
			repositorioEmpresa.save(empresa);
			return ResponseEntity.ok(empresa);       
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar empresa: " + e.getMessage());
		}
	}

	@DeleteMapping("/deletarUsuario/{empresaId}/{usuarioId}")
	public ResponseEntity<?> deletarUsuario(@PathVariable Long empresaId, @PathVariable Long usuarioId) {
		Optional<Empresa> empresaOptional = repositorioEmpresa.findById(empresaId);
		Optional<Usuario> usuarioOptional = repositorioUsuario.findById(usuarioId);
		
		if (empresaOptional.isPresent() && usuarioOptional.isPresent()) {
			Empresa empresa = empresaOptional.get();
			Usuario usuario = usuarioOptional.get();
			
			if (empresa.getUsuarios().contains(usuario)) {
				empresa.getUsuarios().remove(usuario);
				repositorioEmpresa.save(empresa);
				return new ResponseEntity<>("Usuário excluído com sucesso.", HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>("Usuário não associado à empresa.", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Empresa ou Usuário não encontrado.", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/deletarMercadoria/{empresaId}/{mercadoriaId}")
	public ResponseEntity<?> deletarMercadoria(@PathVariable Long empresaId, @PathVariable Long mercadoriaId) {
		Optional<Empresa> empresaOptional = repositorioEmpresa.findById(empresaId);
		Optional<Mercadoria> mercadoriaOptional = repositorioMercadoria.findById(mercadoriaId);
		
		if (empresaOptional.isPresent() && mercadoriaOptional.isPresent()) {
			Empresa empresa = empresaOptional.get();
			Mercadoria mercadoria = mercadoriaOptional.get();
			
			if (empresa.getMercadorias().contains(mercadoria)) {
				empresa.getMercadorias().remove(mercadoria);
				repositorioEmpresa.save(empresa);
				return new ResponseEntity<>("Mercadoria excluída com sucesso.", HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>("Mercadoria não associada à empresa.", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Empresa ou Mercadoria não encontrada.", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/deletarServico/{empresaId}/{servicoId}")
	public ResponseEntity<?> deletarServico(@PathVariable Long empresaId, @PathVariable Long servicoId) {
		Optional<Empresa> empresaOptional = repositorioEmpresa.findById(empresaId);
		Optional<Servico> servicoOptional = repositorioServico.findById(servicoId);
		
		if (empresaOptional.isPresent() && servicoOptional.isPresent()) {
			Empresa empresa = empresaOptional.get();
			Servico servico = servicoOptional.get();
			
			if (empresa.getServicos().contains(servico)) {
				empresa.getServicos().remove(servico);
				repositorioEmpresa.save(empresa);
				return new ResponseEntity<>("Serviço excluído com sucesso.", HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>("Serviço não associado à empresa.", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Empresa ou Serviço não encontrado.", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/deletarVenda/{empresaId}/{vendaId}")
	public ResponseEntity<?> deletarVenda(@PathVariable Long empresaId, @PathVariable Long vendaId) {
		Optional<Empresa> empresaOptional = repositorioEmpresa.findById(empresaId);
		Optional<Venda> vendaOptional = repositorioVenda.findById(vendaId);
		
		if (empresaOptional.isPresent() && vendaOptional.isPresent()) {
			Empresa empresa = empresaOptional.get();
			Venda venda = vendaOptional.get();
			
			if (empresa.getVendas().contains(venda)) {
				empresa.getVendas().remove(venda);
				repositorioEmpresa.save(empresa);
				return new ResponseEntity<>("Venda excluída com sucesso.", HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>("Venda não associada à empresa.", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Empresa ou Venda não encontrada.", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/obterEmpresa/{empresaId}")
	public ResponseEntity<?> obterEmpresa(@PathVariable long empresaId) {
	    Empresa empresa = selecionador.selecionar(repositorioEmpresa.findAll(), empresaId);
	    if (empresa != null) {
	        adicionadorLink.adicionarLink(empresa);
	        return new ResponseEntity<>(empresa, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
	    }
	}


    @GetMapping("/obterEmpresas")
    public ResponseEntity<?> obterEmpresas() {
        List<Empresa> empresas = repositorioEmpresa.findAll();
        if (!empresas.isEmpty()) {
        	adicionadorLink.adicionarLink(empresas);
            return new ResponseEntity<>(empresas, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Nenhuma empresa encontrada.", HttpStatus.NOT_FOUND);
        }
    }
}
