package br.com.poiwifidiscover.model;

import java.io.Serializable;

public class Attribute implements Serializable {

	String audio;
	int id;
	String texto;
	String video;

	public Attribute() {
	}

	public Attribute(String texto, String audio, String video) {

		this.texto = texto;
		this.audio = audio;
		this.video = video;
	}

	public String getAudio() {
		return audio;
	}

	public int getID() {
		return id;
	}

	public String getTexto() {
		return texto;
	}

	public String getVideo() {
		return video;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public void setVideo(String video) {
		this.video = video;
	}

}