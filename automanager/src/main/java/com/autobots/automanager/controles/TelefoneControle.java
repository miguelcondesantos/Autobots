package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.adicionador.AdicionadorLinkTelefone;
import com.autobots.automanager.atualizador.AtualizadorTelefone;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

    @Autowired
    private RepositorioUsuario repositorio;
    
    @Autowired
	private RepositorioEmpresa repositorioEmpresa;
    
    @GetMapping("/listarTelefoneUsuario/{usuarioId}")
    public ResponseEntity<?> listarTelefoneUsuario(@PathVariable long usuarioId) {
        Usuario usuario = repositorio.findById(usuarioId).orElse(null);
        if (usuario != null) {
            List<Telefone> telefones = usuario.getTelefones();
            new AdicionadorLinkTelefone().adicionarLink(telefones);
            return new ResponseEntity<>(telefones, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/cadastrarTelefoneUsuario/{usuarioId}")
    public ResponseEntity<?> cadastrarTelefoneUsuario(@PathVariable long usuarioId, @RequestBody Telefone cadastrarTelefone) {
        Usuario usuario = repositorio.findById(usuarioId).orElse(null);
        if (usuario != null) {
            usuario.getTelefones().add(cadastrarTelefone);
            repositorio.save(usuario);
            return new ResponseEntity<>("Telefone cadastrado com sucesso.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/atualizarTelefoneUsuario/{usuarioId}/{telefoneId}")
    public ResponseEntity<?> atualizarTelefoneUsuario(@PathVariable long usuarioId, @PathVariable long telefoneId, @RequestBody Telefone atualizacaoTelefone) {
        Usuario usuario = repositorio.findById(usuarioId).orElse(null);
        if (usuario != null) {
            Telefone telefone = usuario.getTelefones().stream()
                    .filter(t -> t.getId().equals(telefoneId))
                    .findFirst()
                    .orElse(null);
            if (telefone != null) {
                AtualizadorTelefone atualizador = new AtualizadorTelefone();
                atualizador.atualizar(telefone, atualizacaoTelefone);
                repositorio.save(usuario);
                return new ResponseEntity<>("Telefone atualizado com sucesso.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Telefone não encontrado.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }
  

    @DeleteMapping("/deletarTelefoneUsuario/{usuarioId}/{telefoneId}")
    public ResponseEntity<?> deletarTelefoneUsuario(@PathVariable long usuarioId, @PathVariable long telefoneId) {
        Usuario usuario = repositorio.findById(usuarioId).orElse(null);
        if (usuario != null) {
            Telefone telefone = usuario.getTelefones().stream()
                    .filter(t -> t.getId().equals(telefoneId))
                    .findFirst()
                    .orElse(null);
            if (telefone != null) {
                usuario.getTelefones().remove(telefone);
                repositorio.save(usuario);
                return new ResponseEntity<>("Telefone deletado com sucesso.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Telefone não encontrado.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
    }
    
    
    
    @GetMapping("/listarTelefoneEmpresa/{empresaId}")
    public ResponseEntity<?> listarTelefoneEmpresa(@PathVariable Long empresaId) {
        Optional<Empresa> empresaOptional = repositorioEmpresa.findById(empresaId);
        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            List<Telefone> telefones = empresa.getTelefones();
            new AdicionadorLinkTelefone().adicionarLink(telefones);
            return new ResponseEntity<>(telefones, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/atualizarTelefoneEmpresa/{empresaId}/{telefoneId}")
    public ResponseEntity<?> atualizarTelefoneEmpresa(@PathVariable Long empresaId, @PathVariable Long telefoneId, @RequestBody Telefone novoTelefone) {
        Optional<Empresa> empresaOptional = repositorioEmpresa.findById(empresaId);
        
        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            Optional<Telefone> telefoneOptional = empresa.getTelefones().stream()
                .filter(t -> t.getId().equals(telefoneId))
                .findFirst();
            
            if (telefoneOptional.isPresent()) {
                Telefone telefone = telefoneOptional.get();
                AtualizadorTelefone atualizador = new AtualizadorTelefone();
                atualizador.atualizar(telefone, novoTelefone);
                repositorioEmpresa.save(empresa);
                return new ResponseEntity<>(telefone, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Telefone não associado à empresa.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/deletarTelefone/{empresaId}/{telefoneId}")
	public ResponseEntity<?> deletarTelefone(@PathVariable Long empresaId, @PathVariable Long telefoneId) {
		Optional<Empresa> empresaOptional = repositorioEmpresa.findById(empresaId);
		
		if (empresaOptional.isPresent()) {
			Empresa empresa = empresaOptional.get();
			Optional<Telefone> telefoneOptional = empresa.getTelefones().stream()
				.filter(t -> t.getId().equals(telefoneId))
				.findFirst();
			
			if (telefoneOptional.isPresent()) {
				Telefone telefone = telefoneOptional.get();
				empresa.getTelefones().remove(telefone);
				repositorioEmpresa.save(empresa);
				return new ResponseEntity<>("Telefone excluído com sucesso.", HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>("Telefone não associado à empresa.", HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<>("Empresa ou Telefone não encontrado.", HttpStatus.NOT_FOUND);
		}
	}
    
    @PostMapping("/cadastrarTelefoneEmpresa/{empresaId}")
    public ResponseEntity<?> cadastrarTelefoneEmpresa(@PathVariable Long empresaId, @RequestBody Telefone cadastrarTelefone) {
        Optional<Empresa> empresaOptional = repositorioEmpresa.findById(empresaId);
        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            empresa.getTelefones().add(cadastrarTelefone);
            repositorioEmpresa.save(empresa);
            return new ResponseEntity<>("Telefone cadastrado para a empresa com sucesso.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }
    
      

    @GetMapping("/obterTelefone/{telefoneId}")
    public ResponseEntity<?> obterTelefone(@PathVariable long telefoneId) {
        Usuario usuario = repositorio.findAll().stream()
                .filter(u -> u.getTelefones().stream().anyMatch(t -> t.getId() == telefoneId))
                .findFirst()
                .orElse(null);
        if (usuario != null) {
            Telefone telefone = usuario.getTelefones().stream()
                    .filter(t -> t.getId() == telefoneId)
                    .findFirst()
                    .orElse(null);
            if (telefone != null) {
                new AdicionadorLinkTelefone().adicionarLink(telefone);
                return new ResponseEntity<>(telefone, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Telefone não encontrado.", HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/obterTelefones")
    public ResponseEntity<?> obterTelefones() {
        List<Telefone> telefones = repositorio.findAll().stream()
                .flatMap(u -> u.getTelefones().stream())
                .collect(Collectors.toList());
        if (!telefones.isEmpty()) {
            new AdicionadorLinkTelefone().adicionarLink(telefones);
            return new ResponseEntity<>(telefones, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Nenhum telefone encontrado.", HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/obterTelefoneEmpresa/{empresaId}/{telefoneId}")
    public ResponseEntity<?> obterTelefoneEmpresa(@PathVariable Long empresaId, @PathVariable Long telefoneId) {
        Optional<Empresa> empresaOptional = repositorioEmpresa.findById(empresaId);
        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            Telefone telefone = empresa.getTelefones().stream()
                    .filter(t -> t.getId().equals(telefoneId))
                    .findFirst()
                    .orElse(null);
            if (telefone != null) {
                new AdicionadorLinkTelefone().adicionarLink(telefone);
                return new ResponseEntity<>(telefone, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Telefone não encontrado para esta empresa.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/obterTelefonesEmpresa/{empresaId}")
    public ResponseEntity<?> obterTelefonesEmpresa(@PathVariable Long empresaId) {
        Optional<Empresa> empresaOptional = repositorioEmpresa.findById(empresaId);
        if (empresaOptional.isPresent()) {
            Empresa empresa = empresaOptional.get();
            List<Telefone> telefones = empresa.getTelefones();
            if (!telefones.isEmpty()) {
                new AdicionadorLinkTelefone().adicionarLink(telefones);
                return new ResponseEntity<>(telefones, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nenhum telefone encontrado para esta empresa.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }
}
