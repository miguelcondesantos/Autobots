package com.comunicacao.api.controles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.comunicacao.api.modelos.Venda;

@RestController
public class ControleVenda {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/vendas-por-empresa/{idEmpresa}")
    public ResponseEntity<?> listarVendasPorEmpresa(@PathVariable Long idEmpresa) {
        try {
            List<Venda> vendas = new ArrayList<>();
            ResponseEntity<Venda[]> resposta = new RestTemplate()
                    .getForEntity("http://localhost:8080/venda/listarVendasPorEmpresa/{idEmpresa}", Venda[].class, idEmpresa);
            Venda[] vendasArray = resposta.getBody();

            if (vendasArray == null || vendasArray.length == 0) {
                return new ResponseEntity<>("Nenhuma venda encontrada para a empresa.", HttpStatus.NOT_FOUND);
            }

            vendas = Arrays.asList(vendasArray);
            return new ResponseEntity<List<Venda>>(vendas, HttpStatus.OK);

        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao buscar vendas por empresa: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
