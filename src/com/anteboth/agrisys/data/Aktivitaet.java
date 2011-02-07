package com.anteboth.agrisys.data;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Aktivitaet implements Serializable {
	
	private Date datum;
	private double flaeche;
	private String bemerkung;
	private String typ;
	
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
