package com.anteboth.agrisys.client.model;

import java.io.Serializable;

import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;


/**
 * The {@link SchlagErntejahr} entry.
 * Provides the id, name, description, area and {@link Kultur} (Anbau, Vorfrucht) entries.
 * 
 * @author michael
 */
public class SchlagErntejahr  implements Serializable {
	
	private static final long serialVersionUID = 8806815243784575918L;

	private Long id;
	private String name;
	private String beschreibung;
	private double flaeche;
	private Sorte sorte;
	private Kultur vorfrucht;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public double getFlaeche() {
		return flaeche;
	}
	public void setFlaeche(double flaeche) {
		this.flaeche = flaeche;
	}
	
	public Sorte getSorte() {
		return sorte;
	}
	public void setSorte(Sorte sorte) {
		this.sorte = sorte;
	}
	public Kultur getVorfrucht() {
		return vorfrucht;
	}
	public void setVorfrucht(Kultur vorfrucht) {
		this.vorfrucht = vorfrucht;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
