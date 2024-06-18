package com.autobots.automanager.entitades;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.hateoas.RepresentationModel;

import com.autobots.automanager.enumeracoes.PerfilUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = { "mercadorias", "vendas", "veiculos" })
@Entity
public class Usuario extends RepresentationModel<Usuario> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column
    private String nomeSocial;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<PerfilUsuario> perfis = new ArrayList<>();
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Telefone> telefones = new ArrayList<>();
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Endereco endereco;
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private List<Documento> documentos = new ArrayList<>();
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Email> emails = new ArrayList<>();
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Credencial> credenciais = new ArrayList<>();
    
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "usuario_mercadorias",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "mercadoria_id")
    )
    private List<Mercadoria> mercadorias = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    private List<Venda> vendas = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    private List<Veiculo> veiculos = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeSocial() {
		return nomeSocial;
	}

	public void setNomeSocial(String nomeSocial) {
		this.nomeSocial = nomeSocial;
	}

	public List<PerfilUsuario> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<PerfilUsuario> perfis) {
		this.perfis = perfis;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Documento> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<Documento> documentos) {
		this.documentos = documentos;
	}

	public List<Email> getEmails() {
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	public List<Credencial> getCredenciais() {
		return credenciais;
	}

	public void setCredenciais(List<Credencial> credenciais) {
		this.credenciais = credenciais;
	}

	public List<Mercadoria> getMercadorias() {
		return mercadorias;
	}

	public void setMercadorias(List<Mercadoria> mercadorias) {
		this.mercadorias = mercadorias;
	}

	public List<Venda> getVendas() {
		return vendas;
	}

	public void setVendas(List<Venda> vendas) {
		this.vendas = vendas;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}  
	
	
	
}