package com.anteboth.agrisys.client.model.stammdaten;

import java.io.Serializable;


public class Sorte implements Serializable {
	
	private static final long serialVersionUID = 5503777243228116725L;
	private Long id;
	private String name;
	private String beschreibung;
	private Kultur kultur;
	
	public Sorte() {
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
	
	public void setKultur(Kultur kultur) {
		this.kultur = kultur;
	}
	public Kultur getKultur() {
		return kultur;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
