package application;

import java.util.ArrayList;

public class Predmet {
	private String sifPred;
	private String naziv;
	private int semestar;
	private Integer ects;
	private ArrayList<String> preduslovi = new ArrayList<String>(4);

	public String getSifPred() {
		return sifPred;
	}

	public void setSifPred(String sifPred) {
		this.sifPred = sifPred;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	

	public int getSemestar() {
		return semestar;
	}

	public void setSemestar(int semestar) {
		this.semestar = semestar;
	}

	public Integer getEcts() {
		return ects;
	}

	public void setEcts(int ects) {
		this.ects = ects;
	}

	public ArrayList<String> getPreduslovi() {
		return preduslovi;
	}

	public void setPreduslovi(String p1,String p2,String p3,String p4) {
		preduslovi.add(p1);
		preduslovi.add(p2);
		preduslovi.add(p3);
		preduslovi.add(p4);
	}	
}