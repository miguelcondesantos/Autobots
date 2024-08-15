package com.comunicacao.api.controles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.comunicacao.api.modelos.Veiculo;

@RestController
public class ControleVeiculo {	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/veiculos")
    public ResponseEntity<?> obterVeiculos() {
        try {
            List<Veiculo> veiculos = new ArrayList<>();
            ResponseEntity<Veiculo[]> resposta = new RestTemplate()
                    .getForEntity("http://localhost:8080/veiculo/listarVeiculos", Veiculo[].class);
            Veiculo[] veiculosArray = resposta.getBody();

            if (veiculosArray == null || veiculosArray.length == 0) {
                return new ResponseEntity<>("Nenhum veículo encontrado.", HttpStatus.NOT_FOUND);
            }
            veiculos = Arrays.asList(veiculosArray);
            return new ResponseEntity<List<Veiculo>>(veiculos, HttpStatus.OK);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>("A API de veículos não foi encontrada.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao obter veículos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
