package com.anteboth.agrisys.client.model.stammdaten;

import java.io.Serializable;


public class Kultur implements Serializable {
	
	private static final long serialVersionUID = 5542776230303472329L;
	private Long id;
	private String name;
	private String beschreibung;
	
	public Kultur() {
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
	
	@Override
	public String toString() {
		return name;
	}

	public void setId(long id) {
		this.id = id;
	}
}
