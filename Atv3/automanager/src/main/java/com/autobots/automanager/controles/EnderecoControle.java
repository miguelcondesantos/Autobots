package com.autobots.automanager.controles;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

import com.autobots.automanager.adicionador.AdicionadorLinkEndereco;
import com.autobots.automanager.atualizador.AtualizadorEndereco;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

    @Autowired
    private RepositorioUsuario repositorio;
    
    @Autowired
    private RepositorioEmpresa repositorioEmpresa;
    


    @PostMapping("/cadastrarEnderecoCliente/{clienteId}")
    public ResponseEntity<?> cadastrarEndereco(@PathVariable long clienteId, @RequestBody Endereco novoEndereco) {
        Usuario cliente = repositorio.findById(clienteId).orElse(null);
        if (cliente != null) {
            if (cliente.getEndereco() != null) {
                return new ResponseEntity<>("Já existe um endereço cadastrado para este cliente",HttpStatus.CONFLICT);
            }
            cliente.setEndereco(novoEndereco);
            repositorio.save(cliente);
            return new ResponseEntity<>("Endereço cadastrado com sucesso.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Cliente não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listarEnderecoCliente/{clienteId}")
    public ResponseEntity<?> listarEnderecoCliente(@PathVariable long clienteId) {
        Usuario cliente = repositorio.findById(clienteId).orElse(null);
        if (cliente != null) {
            if (cliente.getEndereco() == null) {
                return new ResponseEntity<>("Endereço não encontrado.", HttpStatus.NOT_FOUND);
            }
            new AdicionadorLinkEndereco().adicionarLink(cliente.getEndereco());
            return new ResponseEntity<>(cliente.getEndereco(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cliente não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/atualizarEnderecoCliente/{clienteId}")
    public ResponseEntity<?> atualizarEndereco(@PathVariable long clienteId, @RequestBody Endereco atualizarEndereco) {
        Usuario cliente = repositorio.findById(clienteId).orElse(null);
        if (cliente != null) {
            Endereco enderecoExistente = cliente.getEndereco();
            if (enderecoExistente != null) {
                AtualizadorEndereco atualizador = new AtualizadorEndereco();
                atualizador.atualizar(enderecoExistente, atualizarEndereco);
            } else {
                cliente.setEndereco(atualizarEndereco);
            }
            repositorio.save(cliente);
            return new ResponseEntity<>("Endereço atualizado com sucesso.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cliente não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deletarEnderecoCliente/{clienteId}")
    public ResponseEntity<?> deletarEnderecoCliente(@PathVariable long clienteId) {
        Usuario cliente = repositorio.findById(clienteId).orElse(null);
        if (cliente != null) {
            if (cliente.getEndereco() != null) {
                cliente.setEndereco(null);
                repositorio.save(cliente);
                return new ResponseEntity<>("Endereço deletado com sucesso.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Endereço não encontrado.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Cliente não encontrado.", HttpStatus.NOT_FOUND);
        }
    }
    
    

    @PostMapping("/cadastrarEnderecoEmpresa/{empresaId}")
    public ResponseEntity<?> cadastrarEnderecoEmpresa(@PathVariable long empresaId, @RequestBody Endereco novoEndereco) {
        Empresa empresa = repositorioEmpresa.findById(empresaId).orElse(null);
        if (empresa != null) {
            if (empresa.getEndereco() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe um endereço cadastrado para esta empresa.");
            }
            empresa.setEndereco(novoEndereco);
            repositorioEmpresa.save(empresa);
            return new ResponseEntity<>("Endereço cadastrado com sucesso para a empresa.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listarEnderecoEmpresa/{empresaId}")
    public ResponseEntity<?> listarEnderecoEmpresa(@PathVariable long empresaId) {
        Empresa empresa = repositorioEmpresa.findById(empresaId).orElse(null);
        if (empresa != null) {
            if (empresa.getEndereco() == null) {
                return new ResponseEntity<>("Endereço não encontrado para esta empresa.", HttpStatus.NOT_FOUND);
            }
            new AdicionadorLinkEndereco().adicionarLink(empresa.getEndereco());
            return new ResponseEntity<>(empresa.getEndereco(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/atualizarEnderecoEmpresa/{empresaId}")
    public ResponseEntity<?> atualizarEnderecoEmpresa(@PathVariable long empresaId, @RequestBody Endereco atualizarEndereco) {
        Empresa empresa = repositorioEmpresa.findById(empresaId).orElse(null);
        if (empresa != null) {
            Endereco enderecoExistente = empresa.getEndereco();
            if (enderecoExistente != null) {
                AtualizadorEndereco atualizador = new AtualizadorEndereco();
                atualizador.atualizar(enderecoExistente, atualizarEndereco);
            } else {
                empresa.setEndereco(atualizarEndereco);
            }
            repositorioEmpresa.save(empresa);
            return new ResponseEntity<>("Endereço atualizado com sucesso para a empresa.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deletarEnderecoEmpresa/{empresaId}")
    public ResponseEntity<?> deletarEnderecoEmpresa(@PathVariable long empresaId) {
        Empresa empresa = repositorioEmpresa.findById(empresaId).orElse(null);
        if (empresa != null) {
            if (empresa.getEndereco() != null) {
                empresa.setEndereco(null);
                repositorioEmpresa.save(empresa);
                return new ResponseEntity<>("Endereço deletado com sucesso da empresa.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Endereço não encontrado para esta empresa.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }


    
    @GetMapping("/obterEnderecoEmpresa/{empresaId}")
    public ResponseEntity<?> obterEnderecoEmpresa(@PathVariable long empresaId) {
        Empresa empresa = repositorioEmpresa.findById(empresaId).orElse(null);
        if (empresa != null) {
            Endereco endereco = empresa.getEndereco();
            if (endereco != null) {
                new AdicionadorLinkEndereco().adicionarLink(endereco);
                return new ResponseEntity<>(endereco, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Endereço não encontrado para esta empresa.", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/obterEnderecosEmpresa")
    public ResponseEntity<List<Endereco>> obterEnderecosEmpresa() {
        List<Endereco> enderecos = repositorioEmpresa.findAll().stream()
                .map(Empresa::getEndereco)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!enderecos.isEmpty()) {
            new AdicionadorLinkEndereco().adicionarLink(enderecos);
            return new ResponseEntity<>(enderecos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/obterEnderecoUsuario/{enderecoId}")
    public ResponseEntity<?> obterEndereco(@PathVariable long enderecoId) {
        Usuario usuario = repositorio.findAll().stream()
                .filter(u -> u.getEndereco() != null && u.getEndereco().getId() == enderecoId)
                .findFirst()
                .orElse(null);
        if (usuario != null) {
            Endereco endereco = usuario.getEndereco();
            new AdicionadorLinkEndereco().adicionarLink(endereco);
            return new ResponseEntity<>(endereco, HttpStatus.OK);
        }
        return new ResponseEntity<>("Endereço não encontrado.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/obterEnderecosUsuario")
    public ResponseEntity<List<Endereco>> obterEnderecos() {
        List<Endereco> enderecos = repositorio.findAll().stream()
                .map(Usuario::getEndereco)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!enderecos.isEmpty()) {
            new AdicionadorLinkEndereco().adicionarLink(enderecos);
            return new ResponseEntity<>(enderecos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
