package com.autobots.automanager;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@SpringBootApplication
public class AutomanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutomanagerApplication.class, args);
	}

	@Component
	public static class Runner implements ApplicationRunner {
		@Autowired
		public ClienteRepositorio repositorio;

		@Override
		public void run(ApplicationArguments args) throws Exception {
			Calendar calendario = Calendar.getInstance();
			calendario.set(2002, 05, 15);

			Cliente cliente = new Cliente();
			cliente.setNome("Pedro Alcântara de Bragança e Bourbon");
			cliente.setDataCadastro(Calendar.getInstance().getTime());
			cliente.setDataNascimento(calendario.getTime());
			cliente.setNomeSocial("Dom Pedro");
			
			Telefone telefone = new Telefone();
			telefone.setDdd("21");
			telefone.setNumero("981234576");
			cliente.getTelefones().add(telefone);
			
			Endereco endereco = new Endereco();
			endereco.setEstado("Rio de Janeiro");
			endereco.setCidade("Rio de Janeiro");
			endereco.setBairro("Copacabana");
			endereco.setRua("Avenida Atlântica");
			endereco.setNumero("1702");
			endereco.setCodigoPostal("22021001");
			endereco.setInformacoesAdicionais("Hotel Copacabana palace");
			cliente.setEndereco(endereco);
			
			Documento rg = new Documento();
			rg.setTipo("RG");
			rg.setNumero("1500");
			
			Documento cpf = new Documento();
			cpf.setTipo("RG");
			cpf.setNumero("00000000001");
			
			cliente.getDocumentos().add(rg);
			cliente.getDocumentos().add(cpf);
			
			repositorio.save(cliente);

			Cliente cliente2 = new Cliente();
			cliente2.setNome("Miguel Conde Santos");
			cliente2.setDataCadastro(Calendar.getInstance().getTime());
			cliente2.setDataNascimento(calendario.getTime());
			cliente2.setNomeSocial("Miguel Social");

			Telefone telefone2 = new Telefone();
			telefone2.setDdd("12");
			telefone2.setNumero("982573856");
			cliente2.getTelefones().add(telefone2);

			Endereco endereco2 = new Endereco();
			endereco2.setEstado("São Paulo");
			endereco2.setCidade("São José dos Campos");
			endereco2.setBairro("Sei lá");
			endereco2.setRua("Logo Ali");
			endereco2.setNumero("123");
			endereco2.setCodigoPostal("11111111");
			endereco2.setInformacoesAdicionais("Casinha do Miguel");
			cliente2.setEndereco(endereco2);

			Documento rg2 = new Documento();
			rg2.setTipo("RG");
			rg2.setNumero("9999");

			Documento cpf2 = new Documento();
			cpf2.setTipo("CPF");
			cpf2.setNumero("00000000000");

			cliente2.getDocumentos().add(rg2);
			cliente2.getDocumentos().add(cpf2);

			repositorio.save(cliente2);
			
		}
	}
}
