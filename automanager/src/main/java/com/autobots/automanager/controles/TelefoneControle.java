package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {
	
	@Autowired
	private RepositorioUsuario repositorio;
}
