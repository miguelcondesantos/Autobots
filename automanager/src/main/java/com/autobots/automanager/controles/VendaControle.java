package com.autobots.automanager.controles;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.adicionador.AdicionadorLinkVenda;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;
import com.autobots.automanager.selecionador.SelecionadorVenda;

@RestController
@RequestMapping("/venda")
public class VendaControle {

    @Autowired
    private RepositorioVenda repositorio;
    
    @Autowired
    private RepositorioUsuario usuarioRepositorio;
    
    @Autowired
    private RepositorioMercadoria mercadoriaRepositorio;
    
    @Autowired
    private RepositorioServico servicoRepositorio;
    
    @Autowired
    private RepositorioVeiculo veiculoRepositorio;
    
    @Autowired
    private RepositorioEmpresa empresaRepositoiro;
    
    @Autowired
    private AdicionadorLinkVenda adicionadorLinkVenda;
    
    @Autowired
    private SelecionadorVenda selecionador;

    @PostMapping("/criarVenda/{idFuncionario}/{idMercadoria}/{idServico}/{idVeiculo}/{idEmpresa}")
    public ResponseEntity<?> criarVenda(@PathVariable Long idFuncionario, @PathVariable Long idMercadoria,
                                        @PathVariable Long idServico, @PathVariable Long idVeiculo,
                                        @PathVariable Long idEmpresa) {
        try {
            Optional<Usuario> funcionarioOptional = usuarioRepositorio.findById(idFuncionario);
            Optional<Mercadoria> mercadoriaOptional = mercadoriaRepositorio.findById(idMercadoria);
            Optional<Servico> servicoOptional = servicoRepositorio.findById(idServico);
            Optional<Veiculo> veiculoOptional = veiculoRepositorio.findById(idVeiculo);
            Optional<Empresa> empresaOptional = empresaRepositoiro.findById(idEmpresa);

            if (funcionarioOptional.isEmpty() || mercadoriaOptional.isEmpty() || servicoOptional.isEmpty() ||
                veiculoOptional.isEmpty() || empresaOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Algum dos dados necessários não foi encontrado.");
            }

            Usuario funcionario = funcionarioOptional.get();
            Mercadoria mercadoria = mercadoriaOptional.get();
            Servico servico = servicoOptional.get();
            Veiculo veiculo = veiculoOptional.get();
            Empresa empresa = empresaOptional.get();

            Venda venda = new Venda();
            venda.setCadastro(new Date());
            venda.setIdentificacao(UUID.randomUUID().toString());
            venda.setFuncionario(funcionario);
            venda.setVeiculo(veiculo);

            venda.getMercadorias().add(mercadoria);
            venda.getServicos().add(servico);

            empresa.getVendas().add(venda);

            Venda novaVenda = repositorio.save(venda);

            return new ResponseEntity<>(novaVenda, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao criar venda: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar venda: " + e.getMessage());
        }
    }


    @PostMapping("/realizarVenda/{idCliente}/{idVenda}")
    public ResponseEntity<?> realizarVenda(@PathVariable Long idCliente, @PathVariable Long idVenda) {
        try {
            Optional<Usuario> clienteOptional = usuarioRepositorio.findById(idCliente);
            Optional<Venda> vendaOptional = repositorio.findById(idVenda);

            if (clienteOptional.isEmpty() || vendaOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente ou venda não encontrados.");
            }

            Usuario cliente = clienteOptional.get();
            Venda venda = vendaOptional.get();

            venda.setCliente(cliente);

            cliente.getVendas().add(venda);
            usuarioRepositorio.save(cliente);

            adicionadorLinkVenda.adicionarLink(venda);

            return new ResponseEntity<>("Venda associada ao cliente com sucesso.", HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao associar venda ao cliente: " + e.getMessage());
        }
    }

    @DeleteMapping("/excluirVenda/{id}")
    public ResponseEntity<?> excluirVenda(@PathVariable Long id) {
        repositorio.deleteById(id);
        return new ResponseEntity<>("Venda excluída com sucesso.", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/listarVendas")
    public ResponseEntity<?> obterVendas() {
        List<Venda> vendas = repositorio.findAll();
        vendas.forEach(adicionadorLinkVenda::adicionarLink);
        return new ResponseEntity<>(vendas, HttpStatus.OK);
    }


    @GetMapping("/listarVenda/{id}")
    public ResponseEntity<?> obterVenda(@PathVariable Long id) {
        List<Venda> todasVendas = repositorio.findAll();
        Venda venda = selecionador.selecionar(todasVendas, id);
        if (venda != null) {
            adicionadorLinkVenda.adicionarLink(venda);
            return new ResponseEntity<>(venda, HttpStatus.OK);
        }
        return new ResponseEntity<>("Venda não encontrada.", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/listarVendasPorUsuario/{idUsuario}")
    public ResponseEntity<?> listarVendasPorUsuario(@PathVariable Long idUsuario) {
        return new ResponseEntity<>(repositorio.findByCliente_Id(idUsuario), HttpStatus.OK);
    }
}
