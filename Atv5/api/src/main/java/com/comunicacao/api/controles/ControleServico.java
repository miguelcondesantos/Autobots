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

import com.comunicacao.api.modelos.Servico;

@RestController
public class ControleServico {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/servicos-por-empresa/{idEmpresa}")
    public ResponseEntity<?> listarServicosPorEmpresa(@PathVariable Long idEmpresa) {
        try {
            List<Servico> servicos = new ArrayList<>();
            ResponseEntity<Servico[]> resposta = new RestTemplate()
                    .getForEntity("http://localhost:8080/servico/listarServicosPorEmpresa/{idEmpresa}", Servico[].class, idEmpresa);
            Servico[] servicosArray = resposta.getBody();

            if (servicosArray == null || servicosArray.length == 0) {
                return new ResponseEntity<>("Nenhum serviço encontrado para a empresa.", HttpStatus.NOT_FOUND);
            }

            servicos = Arrays.asList(servicosArray);
            return new ResponseEntity<>(servicos, HttpStatus.OK);

        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao buscar serviços por empresa: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
