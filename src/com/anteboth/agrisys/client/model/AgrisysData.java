package com.anteboth.agrisys.client.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anteboth.agrisys.client.model.stammdaten.Stammdaten;

public class AgrisysData implements Serializable {

	private static final long serialVersionUID = -7362941293910882188L;
	
	private Stammdaten stammdaten;
	private List<SchlagErntejahr> flurstueckList;
	private Map<Long, List<Aktivitaet>> aktivitaetMap;
	
	public AgrisysData() {
		this.aktivitaetMap = new HashMap<Long, List<Aktivitaet>>();
	}
	
	public Stammdaten getStammdaten() {
		return stammdaten;
	}
	public void setStammdaten(Stammdaten stammdaten) {
		this.stammdaten = stammdaten;
	}
	public List<SchlagErntejahr> getFlurstueckList() {
		return flurstueckList;
	}
	public void setFlurstueckList(List<SchlagErntejahr> flurstueckList) {
		this.flurstueckList = flurstueckList;
	}

	public void putAktivitaet(Long schlagErntejahrId, List<Aktivitaet> aktivitaetList) {
		this.aktivitaetMap.put(schlagErntejahrId, aktivitaetList);
	}
	
	
	/**
	 * Get the {@link Aktivitaet} items for the specified {@link SchlagErntejahr} id.
	 * @param schlagErntejahrId the {@link SchlagErntejahr} id
	 * @return the found {@link Aktivitaet} entries
	 */
	public List<Aktivitaet> get(Long schlagErntejahrId) {
		return this.aktivitaetMap.get(schlagErntejahrId);
	}

	/**
	 * Get the {@link Aktivitaet} entry for the {@link SchlagErntejahr} id 
	 * and {@link Aktivitaet} id specified.
	 * @param schlagErntejahrId the {@link SchlagErntejahr} id
	 * @param aktivitaetId      the {@link Aktivitaet} id
	 * @return the {@link Aktivitaet} item if found, null otherwise
	 */
	public Aktivitaet getAktivitaet(Long schlagErntejahrId, Long aktivitaetId) {
		if (schlagErntejahrId != null && aktivitaetId != null) {
			List<Aktivitaet> al = get(schlagErntejahrId);
			if (al != null) {
				for (Aktivitaet a : al) {
					if (aktivitaetId.equals(a.getId())) {
						return a;
					}
				}
			}
		}
		return null;
	}
	
}
