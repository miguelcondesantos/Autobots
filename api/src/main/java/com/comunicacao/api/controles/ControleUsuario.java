package com.comunicacao.api.controles;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.comunicacao.api.modelos.Empresa;

@RestController
public class ControleUsuario {
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @GetMapping("/usuarios-por-empresa/{idEmpresa}")
    public ResponseEntity<?> listarUsuariosPorEmpresa(@PathVariable Long idEmpresa) {
        try {
            ResponseEntity<Empresa[]> resposta = new RestTemplate()
                    .getForEntity("http://localhost:8080/usuario/usuariosEmpresa/{idEmpresa}", Empresa[].class, idEmpresa);
            
            Empresa[] empresasArray = resposta.getBody();

            if (empresasArray == null || empresasArray.length == 0) {
                return new ResponseEntity<>("Nenhum usuário encontrado para esta empresa.", HttpStatus.NOT_FOUND);
            }

            List<Empresa> empresas = Arrays.asList(empresasArray);
            return new ResponseEntity<>(empresas, HttpStatus.OK);
        } catch (HttpClientErrorException.NotFound e) {
            return new ResponseEntity<>("Empresa não encontrada.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao buscar usuários por empresa: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
