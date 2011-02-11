package com.anteboth.agrisys.client.model.stammdaten;

import java.io.Serializable;


public class PSMittel implements Serializable {
	
	private static final long serialVersionUID = -1842691253376083570L;
	private Long id;
	private String name;
	private String beschreibung;
	
	public PSMittel() {
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
