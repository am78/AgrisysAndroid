package com.anteboth.agrisys.client.model.stammdaten;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Stammdaten implements Serializable {

	private static final long serialVersionUID = 2578782692972021606L;
	private List<Kultur> kulturList;
	private List<Sorte> sorteList;
	private List<Duengerart> duengerartList;
	private List<PSMittel> psMittelList;
	private List<BodenbearbeitungTyp> bodenbearbeitungTypList;
	
	public Stammdaten() {
		this.kulturList = new ArrayList<Kultur>();
		this.sorteList = new ArrayList<Sorte>();
		this.bodenbearbeitungTypList = new ArrayList<BodenbearbeitungTyp>();
		this.duengerartList = new ArrayList<Duengerart>();
		this.psMittelList = new ArrayList<PSMittel>();
	}

	public List<Kultur> getKulturList() {
		return kulturList;
	}

	public void setKulturList(List<Kultur> kulturList) {
		this.kulturList = kulturList;
	}

	public List<Sorte> getSorteList() {
		return sorteList;
	}

	public void setSorteList(List<Sorte> sorteList) {
		this.sorteList = sorteList;
	}

	public List<Duengerart> getDuengerartList() {
		return duengerartList;
	}

	public void setDuengerartList(List<Duengerart> duengerartList) {
		this.duengerartList = duengerartList;
	}

	public List<PSMittel> getPsMittelList() {
		return psMittelList;
	}

	public void setPsMittelList(List<PSMittel> psMittelList) {
		this.psMittelList = psMittelList;
	}

	public List<BodenbearbeitungTyp> getBodenbearbeitungTypList() {
		return bodenbearbeitungTypList;
	}

	public void setBodenbearbeitungTypList(
			List<BodenbearbeitungTyp> bodenbearbeitungTypList) {
		this.bodenbearbeitungTypList = bodenbearbeitungTypList;
	}

	/**
	 * Get the {@link Kultur} entry with the specified id.
	 * 
	 * @param id the id of the {@link Kultur} entry.
	 * @return the found {@link Kultur} entry or null if no value for the id is present
	 */
	public Kultur getKultur(Long id) {
		if (id != null && this.kulturList != null && this.kulturList.size() > 0) {
			for (Kultur k : this.kulturList) {
				if (id.equals(k.getId())) {
					return k;
				}
			}
		}
		return null;
	}

	/**
	 * Get the {@link Sorte} item for the specified id.
	 * @param id the id 
	 * @return the found {@link Sorte} entrie for the id, is null if no value found
	 */
	public Sorte getSorte(Long id) {
		if (id != null && this.sorteList != null) {
			for (Sorte s : this.sorteList) {
				if (id.equals(s.getId())) {
					return s;
				}
			}
		}
		return null;
	}
	
}
