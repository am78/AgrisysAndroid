package com.anteboth.agrisys.client.model;

import java.io.Serializable;
import java.util.Date;

public class Aktivitaet implements Serializable {
	
	private static final long serialVersionUID = -6308217296604194963L;
	
	public static final int BODENBEARBEITUNG_TYPE = 0;
	public static final int AUSSAAT_TYPE = 1;
	public static final int DUENGUNG_TYPE = 2;
	public static final int ERNTE_TYPE = 3;
	public static final int PFLANZENSCHUTZ_TYPE = 4;

	private Long id;
	private Date datum;
	private double flaeche;
	private String bemerkung;
	private int type;
	private Date lastModification;
	private boolean synchron;
	private long schlagErntejahrId;
	private boolean deleted = false;
	
	
	public Aktivitaet(long schlagErntejahrId) {
		this.schlagErntejahrId = schlagErntejahrId;
		this.deleted = false;
		//set synchron flag to false when creating a new aktivitaet entry
		//set lastModification to now when creating a new aktivitaet entry
		setModified();
	}
	
	/**
	 * Sets the synchron flag to false and the last modification date to now.
	 */
	private void setModified() {
		this.synchron = false;
		this.lastModification = new Date();
	}

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
		setModified();
	}
	public double getFlaeche() {
		return flaeche;
	}
	public void setFlaeche(double flaeche) {
		this.flaeche = flaeche;
		setModified();
	}
	public String getBemerkung() {
		return bemerkung;
	}
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
		setModified();
	}
	

	public Date getLastModification() {
		return lastModification;
	}

	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}

	public boolean isSynchron() {
		return synchron;
	}

	public void setSynchron(boolean synchron) {
		this.synchron = synchron;
	}

	public long getSchlagErntejahrId() {
		return schlagErntejahrId;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
