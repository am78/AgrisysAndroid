package com.anteboth.agrisys.client.model.stammdaten;

import java.io.Serializable;


public class Duengerart implements Serializable {
	
	private static final long serialVersionUID = -4550441891399762030L;
	private Long id;
	private String name;
	private String beschreibung;
	
	public Duengerart() {
		this.name = "Name";
		this.beschreibung = "Beschreibung";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBeschreibung() {
		return beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
