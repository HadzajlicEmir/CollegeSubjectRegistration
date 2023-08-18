package application;

public class Nastavnik{
	private int sifNastavnik;
	private String ime;
	private String prezime;
	private String zvanje;
	private boolean prodekanBool;
	public int getSifNastavnik() {
		return sifNastavnik;
	}
	public void setSifNastavnik(int sifNastavnik) {
		this.sifNastavnik = sifNastavnik;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getZvanje() {
		return zvanje;
	}
	public void setZvanje(String zvanje) {
		this.zvanje = zvanje;
	}
	public boolean isProdekanBool() {
		return prodekanBool;
	}
	public void setProdekanBool(boolean prodekanBool) {
		this.prodekanBool = prodekanBool;
	}
	
}