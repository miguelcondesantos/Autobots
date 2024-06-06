package com.autobots.automanager.controles;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {
	
	private RepositorioEmpresa repositorio;

}
