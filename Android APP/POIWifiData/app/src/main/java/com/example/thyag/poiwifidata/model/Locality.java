package com.example.thyag.poiwifidata.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Locality implements Serializable {

	Attribute descricao = new Attribute();
	String fazParteDe = "";
	int idLocalizacao;
	Attribute nome = new Attribute();

	public Attribute getDescricao() {
		return descricao;
	}

	public int getID() {
		return idLocalizacao;
	}

	public String getIdPai() {
		return fazParteDe;
	}

	public Attribute getNome() {
		return nome;
	}

	public void setDescricao(Attribute descricao) {
		this.descricao = descricao;
	}

	public void setID(int id) {
		this.idLocalizacao = id;
	}

	public void setIdPai(String idPai) {
		this.fazParteDe = fazParteDe;
	}

	public void setNome(Attribute nome) {
		this.nome = nome;
	}

}
