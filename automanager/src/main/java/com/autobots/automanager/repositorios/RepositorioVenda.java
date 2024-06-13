package com.autobots.automanager.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;

public interface RepositorioVenda extends JpaRepository<Venda, Long> {
    List<Venda> findByCliente_Id(Long clienteId);
    List<Venda> findByFuncionario_Id(Long funcionarioId);
    List<Venda> findByServicosContaining(Servico servico);
    List<Venda> findByVeiculo(Veiculo veiculo);
}
