package com.example.thyag.poiwifidata.model;

import java.io.Serializable;

public class Thing implements Serializable {

	Attribute alerta = new Attribute();
	Attribute classe = new Attribute();
	Attribute descricao = new Attribute();
	Attribute display = new Attribute();
	int idObjeto;
	Attribute localizacao = new Attribute();
	Attribute mensagem = new Attribute();
	Attribute nome = new Attribute();
	Attribute situacao = new Attribute();
	Attribute vizinhanca = new Attribute();

	public Attribute getAlerta() {
		return alerta;
	}

	public Attribute getClasse() {
		return classe;
	}

	public Attribute getDescricao() {
		return descricao;
	}

	public Attribute getDisplay() {
		return display;
	}

	public int getID() {
		return idObjeto;
	}

	public Attribute getLocalizacao() {
		return localizacao;
	}

	public Attribute getMensagem() {
		return mensagem;
	}

	public Attribute getNome() {
		return nome;
	}

	public Attribute getSituacao() {
		return situacao;
	}

	public Attribute getVizinhanca() {
		return vizinhanca;
	}

	public void setAlerta(Attribute alerta) {
		this.alerta = alerta;
	}

	public void setClasse(Attribute classe) {
		this.classe = classe;
	}

	public void setDescricao(Attribute descricao) {
		this.descricao = descricao;
	}

	public void setDisplay(Attribute display) {
		this.display = display;
	}

	public void setID(int id) {
		this.idObjeto = id;
	}

	public void setLocalizacao(Attribute localizacao) {
		this.localizacao = localizacao;
	}

	public void setMensagem(Attribute mensagem) {
		this.mensagem = mensagem;
	}

	public void setNome(Attribute nome) {
		this.nome = nome;
	}

	public void setSituacao(Attribute situacao) {
		this.situacao = situacao;
	}

	public void setVizinhanca(Attribute vizinhanca) {
		this.vizinhanca = vizinhanca;
	}

}
