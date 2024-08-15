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

import com.comunicacao.api.modelos.Mercadoria;

@RestController
public class ControleMercadoria {
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/mercadorias-por-empresa/{idEmpresa}")
    public ResponseEntity<?> listarMercadoriasPorEmpresa(@PathVariable Long idEmpresa) {
        try {
            List<Mercadoria> mercadorias = new ArrayList<>();
            ResponseEntity<Mercadoria[]> resposta = new RestTemplate()
                    .getForEntity("http://localhost:8080/mercadoria/listarMercadoriasPorEmpresa/{idEmpresa}", Mercadoria[].class, idEmpresa);
            Mercadoria[] mercadoriasArray = resposta.getBody();

            if (mercadoriasArray == null || mercadoriasArray.length == 0) {
                return new ResponseEntity<>("Nenhuma mercadoria encontrada para a empresa.", HttpStatus.NOT_FOUND);
            }

            mercadorias = Arrays.asList(mercadoriasArray);
            return new ResponseEntity<List<Mercadoria>>(mercadorias, HttpStatus.OK);

        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao buscar mercadorias por empresa: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
