	package com.autobots.automanager.entitades;
	
	import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.hateoas.RepresentationModel;

import com.autobots.automanager.enumeracoes.TipoVeiculo;

import lombok.Data;
import lombok.EqualsAndHashCode;
	
	@Data
	@EqualsAndHashCode(exclude = { "proprietario", "vendas" })
	@Entity
	public class Veiculo extends RepresentationModel<Veiculo>{
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		@Column(nullable = false)
		private TipoVeiculo tipo;
		
		@Column(nullable = false)
		private String modelo;
		
		@Column(nullable = false)
		private String placa;
		
		@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
		private Usuario proprietario;
		
		@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
		private List<Venda> vendas = new ArrayList<>();

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public TipoVeiculo getTipo() {
			return tipo;
		}

		public void setTipo(TipoVeiculo tipo) {
			this.tipo = tipo;
		}

		public String getModelo() {
			return modelo;
		}

		public void setModelo(String modelo) {
			this.modelo = modelo;
		}

		public String getPlaca() {
			return placa;
		}

		public void setPlaca(String placa) {
			this.placa = placa;
		}

		public Usuario getProprietario() {
			return proprietario;
		}

		public void setProprietario(Usuario proprietario) {
			this.proprietario = proprietario;
		}

		public List<Venda> getVendas() {
			return vendas;
		}

		public void setVendas(List<Venda> vendas) {
			this.vendas = vendas;
		}
		
	
		
	}