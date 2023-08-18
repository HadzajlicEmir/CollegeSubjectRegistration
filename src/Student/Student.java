package Student;

import java.util.ArrayList;
import java.util.List;

public class Student {
	private int brIndexa;
	private String ime;
	private String prezime;
	private int ostvarenihECTS;
	private String status;
	private int godStudija;
	private ArrayList<String> regPred = new ArrayList<String>();
	boolean obnova;

	public int getBrIndexa() {
		return brIndexa;
	}

	public void setBrIndexa(int brIndexa) {
		this.brIndexa = brIndexa;
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

	public int getOstvarenihECTS() {
		return ostvarenihECTS;
	}

	public void setOstvarenihECTS(int ostvarenihECTS) {
		this.ostvarenihECTS = ostvarenihECTS;
	}

	public int getGodStudija() {
		return godStudija;
	}

	public void setGodStudija(int godStudija) {
		this.godStudija = godStudija;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getRegPred() {
		return regPred;
	}

	public void setRegPred(String s) {
		regPred.add(s);
	}

	public void removeElement(String s) {
		for (int i = 0; i < regPred.size(); ++i) {
			if (regPred.get(i).equals(s)) {
				regPred.remove(i);
			}
		}
	}

	public void odrediStatus() {


		if (ostvarenihECTS >= 48 && ostvarenihECTS <= 60) {
			if (obnova == false)
				status = "Prvi put";
			else {
				status = "Obnova";
				obnova = true;
			}
			godStudija = 1;
		} else if (ostvarenihECTS >= 108 && ostvarenihECTS <= 120) {
			if (obnova == false)
				status = "Prvi put";
			else {
				status = "Obnova";
				obnova = true;
			}
			godStudija = 2;
		} else if (ostvarenihECTS >= 168 && ostvarenihECTS < 180) {
			if (obnova == false)
				status = "Prvi put";
			else {
				status = "Obnova";
				obnova = true;
			}
			godStudija = 3;
		} else if (ostvarenihECTS >= 180 && ostvarenihECTS <= 240) {
			if (obnova == false)
				status = "Apsolvent";
			else {
				status = "Imartikulant";
				obnova = true;
			}
			godStudija = 4;
		} else {
			status = "Nedefinisan";
		}
	}

}