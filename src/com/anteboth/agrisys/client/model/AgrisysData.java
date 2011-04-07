package com.anteboth.agrisys.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anteboth.agrisys.client.model.stammdaten.Stammdaten;

/**
 * {@link AgrisysData} stores all agrisys domain specific data which is used by this application.
 * @author michael
 */
public class AgrisysData implements Serializable {

	private static final long serialVersionUID = -7362941293910882188L;
	
	private Stammdaten stammdaten;
	private List<SchlagErntejahr> flurstueckList;
	private Map<Long, List<Aktivitaet>> aktivitaetMap;
	private boolean empty = true;
	
	public AgrisysData() {
		this.aktivitaetMap = new HashMap<Long, List<Aktivitaet>>();
		this.flurstueckList = new ArrayList<SchlagErntejahr>();
		this.stammdaten = new Stammdaten();
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
	
	/**
	 * Returns all changed (not in synchron state) {@link Aktivitaet} entries. 
	 * @return
	 */
	public List<Aktivitaet> getChanged() {
		List<Aktivitaet> changed = new ArrayList<Aktivitaet>();
		for (List<Aktivitaet> list : this.aktivitaetMap.values()) {
			if (list != null && !list.isEmpty()) {
				for (Aktivitaet a : list) {
					if (a != null && !a.isSynchron()) {
						changed.add(a);
					}
				}
			}
		}
		return changed;
	}

	/**
	 * Returns the empty flag.
	 * @return the empty flag
	 */
	public boolean isEmpty() {
		return this.empty;
	}
	
	/**
	 * Sets the empty flag.
	 * @param empty the flags value
	 */
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
}
