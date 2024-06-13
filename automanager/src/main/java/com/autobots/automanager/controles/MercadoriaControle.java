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

import com.autobots.automanager.adicionador.AdicionadorLinkMercadoria;
import com.autobots.automanager.atualizador.AtualizadorMercadoria;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.selecionador.SelecionadorMercadoria;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {
	
	@Autowired
	private RepositorioMercadoria repositorio;
	
	@Autowired
	private RepositorioUsuario repositorioUsuario;
	
	@Autowired
	private RepositorioEmpresa repositorioEmpresa;
	
	@Autowired
	private AtualizadorMercadoria atualizadorMercadoria;
	
	@Autowired
    private SelecionadorMercadoria selecionador;

	
	@GetMapping("/listarMercadorias")
	public ResponseEntity<?> obterMercadorias() {
		List<Mercadoria> mercadorias = repositorio.findAll();
		if (!mercadorias.isEmpty()) {
			new AdicionadorLinkMercadoria().adicionarLink(mercadorias);
			return new ResponseEntity<>(mercadorias, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Nenhuma mercadoria encontrada.", HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/listarMercadoria/{id}")
    public ResponseEntity<?> obterMercadoria(@PathVariable long id) {
        List<Mercadoria> todasMercadorias = repositorio.findAll();
        Mercadoria mercadoria = selecionador.selecionar(todasMercadorias, id);
        if (mercadoria != null) {
            new AdicionadorLinkMercadoria().adicionarLink(mercadoria);
            return new ResponseEntity<>(mercadoria, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Mercadoria não encontrada.", HttpStatus.NOT_FOUND);
        }
    }
	
	@PostMapping("/cadastrarMercadoria/{empresaId}")
	public ResponseEntity<?> cadastrarMercadoria(@PathVariable long empresaId,@RequestBody Mercadoria mercadoria) {
		mercadoria.setCadastro(new Date());
		mercadoria.setValidade(new Date());
		mercadoria.setFabricao(new Date());
		Empresa empresa = repositorioEmpresa.getById(empresaId);
		empresa.getMercadorias().add(mercadoria);
        repositorio.save(mercadoria);
        repositorioEmpresa.save(empresa);
		new AdicionadorLinkMercadoria().adicionarLink(mercadoria);
		return new ResponseEntity<>("Mercadoria cadastrada com sucesso.", HttpStatus.CREATED);
	}
	
	@PutMapping("/atualizarMercadoria/{id}")
	public ResponseEntity<?> atualizarMercadoria(@PathVariable long id, @RequestBody Mercadoria atualizacao) {
		Optional<Mercadoria> mercadoriaOpt = repositorio.findById(id);
		if (mercadoriaOpt.isPresent()) {
			Mercadoria mercadoria = mercadoriaOpt.get();
			atualizadorMercadoria.atualizar(mercadoria, atualizacao);
			repositorio.save(mercadoria);
			new AdicionadorLinkMercadoria().adicionarLink(mercadoria);
			return new ResponseEntity<>("Mercadoria atualizada com sucesso.", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Mercadoria não encontrada.", HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/deletarMercadoria/{id}")
	public ResponseEntity<?> deletarMercadoria(@PathVariable long id) {
		Optional<Mercadoria> mercadoriaOpt = repositorio.findById(id);
		if (mercadoriaOpt.isPresent()) {
			repositorio.delete(mercadoriaOpt.get());
			return new ResponseEntity<>("Mercadoria deletada com sucesso.", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Mercadoria não encontrada.", HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/adicionarMercadoriaUsuario/{usuarioId}/{mercadoriaId}")
	public ResponseEntity<?> adicionarMercadoriaAoUsuario(@PathVariable long usuarioId, @PathVariable long mercadoriaId) {
	    Optional<Usuario> usuarioOpt = repositorioUsuario.findById(usuarioId);
	    if (!usuarioOpt.isPresent()) {
	        return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
	    }
	    Optional<Mercadoria> mercadoriaOpt = repositorio.findById(mercadoriaId);
	    if (!mercadoriaOpt.isPresent()) {
	        return new ResponseEntity<>("Mercadoria não encontrada.", HttpStatus.NOT_FOUND);
	    }
	    Usuario usuario = usuarioOpt.get();
	    Mercadoria mercadoria = mercadoriaOpt.get();
	    if (usuario.getMercadorias().contains(mercadoria)) {
	        return new ResponseEntity<>("A mercadoria já está associada ao usuário.", HttpStatus.CONFLICT);
	    }
	    usuario.getMercadorias().add(mercadoria);
	    repositorioUsuario.save(usuario);
	    new AdicionadorLinkMercadoria().adicionarLink(mercadoria);
	    return new ResponseEntity<>("Mercadoria adicionada ao usuário com sucesso.", HttpStatus.OK);
	}

	
	@GetMapping("/listarMercadoriasUsuario/{usuarioId}")
	public ResponseEntity<?> listarMercadoriasUsuario(@PathVariable long usuarioId) {
	    Optional<Usuario> usuarioOpt = repositorioUsuario.findById(usuarioId);
	    if (!usuarioOpt.isPresent()) {
	        return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
	    }
	    Usuario usuario = usuarioOpt.get();
	    List<Mercadoria> mercadorias = usuario.getMercadorias();
	    if (mercadorias.isEmpty()) {
	        return new ResponseEntity<>("Nenhuma mercadoria encontrada para este usuário.", HttpStatus.NOT_FOUND);
	    }
	    new AdicionadorLinkMercadoria().adicionarLink(mercadorias);
	    return new ResponseEntity<>(mercadorias, HttpStatus.FOUND);
	}

}
