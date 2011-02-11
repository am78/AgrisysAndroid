package com.anteboth.agrisys.client.model;

import java.io.Serializable;
import java.util.Date;

public class Aktivitaet implements Serializable {
	
	private static final long serialVersionUID = -6308217296604194963L;

	private Long id;
	private Date datum;
	private double flaeche;
	private String bemerkung;
	private String typ;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public double getFlaeche() {
		return flaeche;
	}
	public void setFlaeche(double flaeche) {
		this.flaeche = flaeche;
	}
	public String getBemerkung() {
		return bemerkung;
	}
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	public String getTyp() {
		return typ;
	}
	public void setTyp(String typ) {
		this.typ = typ;
	}
}
