package com.autobots.automanager.controles;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

import com.autobots.automanager.adicionador.AdicionadorLinkVeiculo;
import com.autobots.automanager.atualizador.AtualizadorVeiculo;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;
import com.autobots.automanager.selecionador.SelecionadorVeiculo;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {

    @Autowired
    private RepositorioVeiculo repositorio;

    @Autowired
    private RepositorioUsuario usuarioRepositorio;
    
    @Autowired
    private RepositorioVenda vendaRepositorio;

    @Autowired
    private AtualizadorVeiculo atualizador;
    
    @Autowired
    private SelecionadorVeiculo selecionador;

    @GetMapping("/listarVeiculos")
    public ResponseEntity<List<Veiculo>> obterVeiculos() {
        List<Veiculo> veiculos = repositorio.findAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!veiculos.isEmpty()) {
            new AdicionadorLinkVeiculo().adicionarLink(veiculos);
            return new ResponseEntity<>(veiculos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/listarVeiculo/{veiculoId}")
    public ResponseEntity<?> obterVeiculo(@PathVariable long veiculoId) {
        List<Veiculo> todosVeiculos = repositorio.findAll();
        Veiculo veiculo = selecionador.selecionar(todosVeiculos, veiculoId);
        if (veiculo != null) {
        	new AdicionadorLinkVeiculo().adicionarLink(veiculo);
            return new ResponseEntity<>(veiculo, HttpStatus.OK);
        }
        return new ResponseEntity<>("Veículo não encontrado.", HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/listarVeiculoUsuario/{usuarioId}")
    public ResponseEntity<?> listarVeiculoUsuario(@PathVariable Long usuarioId) {
        Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(usuarioId);
        if (usuarioOptional.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado.", HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioOptional.get();
        List<Veiculo> veiculos = usuario.getVeiculos();
        if (!veiculos.isEmpty()) {
            new AdicionadorLinkVeiculo().adicionarLink(veiculos);
            return new ResponseEntity<>(veiculos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Nenhum veículo encontrado para este usuário.", HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/cadastrarVeiculo")
    public ResponseEntity<?> cadastrarVeiculo(@RequestBody Veiculo veiculo){
    	if (veiculo.getId() == null) {
    		repositorio.save(veiculo);
    		return new ResponseEntity<>("Veículo cadastrado com sucesso.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Conflito: O veículo já possui um ID.", HttpStatus.CONFLICT);
        }
    }
    
    @PutMapping("/atualizarVeiculo/{veiculoid}")
    public ResponseEntity<?> atualizarVeiculo(@PathVariable Long veiculoid, @RequestBody Veiculo atualizacao) {
        Optional<Veiculo> veiculoOptional = repositorio.findById(veiculoid);
        if (veiculoOptional.isPresent()) {
            Veiculo veiculo = veiculoOptional.get();
            atualizador.atualizar(veiculo, atualizacao);
            repositorio.save(veiculo);
            return new ResponseEntity<>(veiculo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Veículo não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/excluirVeiculo/{id}")
    public ResponseEntity<?> excluirVeiculo(@PathVariable Long id) {
        Optional<Veiculo> veiculoOptional = repositorio.findById(id);
        if (veiculoOptional.isPresent()) {
            Veiculo veiculo = veiculoOptional.get();       
            List<Venda> vendas = vendaRepositorio.findByVeiculo(veiculo);
            for (Venda venda : vendas) {
                venda.setVeiculo(null);
                vendaRepositorio.save(venda);
            }
            Usuario proprietario = veiculo.getProprietario();
            if (proprietario != null) {
                proprietario.getVeiculos().remove(veiculo);
                usuarioRepositorio.save(proprietario);
            }
            veiculo.setProprietario(null);
            repositorio.save(veiculo);
            repositorio.deleteById(id);
            return new ResponseEntity<>("Veículo excluído com sucesso.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Veículo não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

}
