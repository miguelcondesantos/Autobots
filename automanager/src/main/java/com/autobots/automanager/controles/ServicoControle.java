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

import com.autobots.automanager.adicionador.AdicionadorLinkServico;
import com.autobots.automanager.atualizador.AtualizadorServico;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioVenda;
import com.autobots.automanager.selecionador.SelecionadorServico;

@RestController
@RequestMapping("/servico")
public class ServicoControle {

    @Autowired
    private RepositorioServico repositorio;
    
    @Autowired
    private RepositorioEmpresa repositorioEmpresa;
    
    @Autowired
    private RepositorioVenda repositorioVenda;

    @Autowired
    private AdicionadorLinkServico adicionadorLinkServico;
    
    @Autowired
    private SelecionadorServico selecioandor;

    @GetMapping("/obterServico/{id}")
    public ResponseEntity<?> obterServico(@PathVariable Long id) {
        List<Servico> todosServicos = repositorio.findAll();
        Servico servico = selecioandor.selecionar(todosServicos, id);
        if (servico != null) {
            adicionadorLinkServico.adicionarLink(servico);
            return new ResponseEntity<>(servico, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Serviço não encontrado.", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/obterServicos")
    public ResponseEntity<?> obterServicos() {
        List<Servico> servicos = repositorio.findAll();
        if (!servicos.isEmpty()) {
            adicionadorLinkServico.adicionarLink(servicos);
            return new ResponseEntity<>(servicos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Nenhum serviço encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/cadastrarServico/{empresaId}")
    public ResponseEntity<?> adicionarServico(@PathVariable long empresaId, @RequestBody Servico servico) {
    	Empresa empresa = repositorioEmpresa.getById(empresaId);
        repositorio.save(servico);
        empresa.getServicos().add(servico);
        return new ResponseEntity<>("Serviço cadastrado com sucesso.", HttpStatus.CREATED);
    }

    @PutMapping("/atualizarServico/{id}")
    public ResponseEntity<?> atualizarServico(@PathVariable Long id, @RequestBody Servico atualizacao) {
        Optional<Servico> servicoOpt = repositorio.findById(id);
        if (servicoOpt.isPresent()) {
            Servico servico = servicoOpt.get();
            AtualizadorServico atualizador = new AtualizadorServico();
            atualizador.atualizar(servico, atualizacao);
            repositorio.save(servico);
            return new ResponseEntity<>("Serviço atualizado com sucesso.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Serviço não encontrado.", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deletarServico/{id}")
    public ResponseEntity<?> deletarServico(@PathVariable Long id) {
        Optional<Servico> servicoOpt = repositorio.findById(id);
        if (servicoOpt.isPresent()) {
            Servico servico = servicoOpt.get();
            List<Venda> vendas = repositorioVenda.findByServicosContaining(servico);
            for (Venda venda : vendas) {
                venda.getServicos().remove(servico);
                repositorioVenda.save(venda);
            }
            List<Empresa> empresas = repositorioEmpresa.findAll();
            for (Empresa empresa : empresas) {
                if (empresa.getServicos().contains(servico)) {
                    empresa.getServicos().remove(servico);
                    repositorioEmpresa.save(empresa);
                    break;
                }
            }
            repositorio.delete(servico);
            return new ResponseEntity<>("Serviço deletado com sucesso.", HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Serviço não encontrado.", HttpStatus.NOT_FOUND);
        }
    }
}
