package Student;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
//import java.util.Date;

import application.Main;
import application.Nastavnik;
import application.Ocjena;
import application.Predmet;

public class StudentController3 implements Initializable {
	@FXML
	private Label welcomeLabel;
	@FXML
	private Label statusL;
	@FXML
	private Button logout;
	@FXML
	private Button dodajZimski;
	@FXML
	private Button dodajLjetni;
	@FXML
	private Button izbaciB;
	@FXML
	private Button zakljucajB;
	@FXML
	private ListView<String> zimskiLV;
	@FXML
	private ListView<String> ljetniLV;
	@FXML
	private ListView<String> odabraniLV;
	@FXML
	private TextField pretragaPredmetaTF;

	// varijable koristene
	public Student student = application.LogIn.student;
	ArrayList<Predmet> predmeti = new ArrayList<Predmet>();
	ArrayList<Ocjena> ocjene = new ArrayList<Ocjena>();
	ArrayList<String> regPred = new ArrayList<String>();
	static int odabraniECTSZima = 0;
	static int odabraniECTSLjeto = 0;

	boolean ZAKLJUCANO = false;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			// SETOVANJE VARIJABLI ODMAH NA NULU, LOGICKI CE SE PRORACUNATI
			odabraniECTSZima = 0;
			odabraniECTSLjeto = 0;
			proracunajStatus();
						
			welcomeLabel.setText("Dobrodosli " + student.getIme() + " " + student.getPrezime());
			statusL.setText("Godina studija i status: " + student.getGodStudija() + ", " + student.getStatus());

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

			String sql = ("SELECT * from registrovaniPredmet WHERE indeks=? AND confirm=true");
			PreparedStatement statement1 = connection.prepareStatement(sql);
			statement1.setInt(1, student.getBrIndexa());
			ResultSet tempRS1 = statement1.executeQuery();
			if (tempRS1.next()) {
				zakljucaliStePredmete();
				ZAKLJUCANO = true;
				return;
			}

			// POCETAK I KRAJ REGISTRACIJE

			Statement datum = connection.createStatement();
			ResultSet datum1 = datum.executeQuery("SELECT kraj FROM periodRegistracije");
			while (datum1.next()) {
				LocalDate trenutnoVrijeme = LocalDate.now();
				Date tempsql = datum1.getDate(1);
				LocalDate sqlVrijeme = null;
				if (tempsql != null)
					sqlVrijeme = tempsql.toLocalDate();

				if (tempsql != null) {
					if (trenutnoVrijeme.isAfter(sqlVrijeme)) {
						zakljucaj();
						zakljucaliStePredmete();
						ZAKLJUCANO = true;
						return;
					}
				}
			}

			datum = connection.createStatement();
			datum1 = datum.executeQuery("SELECT pocetak FROM periodRegistracije");
			while (datum1.next()) {
				LocalDate trenutnoVrijeme = LocalDate.now();
				Date tempsql = datum1.getDate(1);
				LocalDate sqlVrijeme = null;
				if (tempsql != null)
					sqlVrijeme = tempsql.toLocalDate();

				if (tempsql != null) {
					if (trenutnoVrijeme.isBefore(sqlVrijeme)) {
						nijePoceoPeriod();
						return;
					}
				}
			}

			Statement stmt = connection.createStatement();

			// DODAVANJE ZIMSKIH PREDMETA U LISTVIEW
			ResultSet rs = stmt
					.executeQuery("SELECT predavac.sifPred,nazPred, imeNast,prezNast,zvanje FROM predavac INNER JOIN "
							+ "nastavnik on nastavnik.sifNastavnik=predavac.sifNastavnik INNER JOIN predmet ON "
							+ "predmet.sifPred=predavac.sifPred where nosilac=true AND semestar=1");

			while (rs.next()) {
				zimskiLV.getItems().add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + " "
						+ rs.getString(4) + ", " + rs.getString(5));
			}

			// DODAVANJE LJETNIH PREDMETA U LISTVIEW
			rs = stmt.executeQuery("SELECT predavac.sifPred,nazPred, imeNast,prezNast,zvanje FROM predavac INNER JOIN "
					+ "nastavnik on nastavnik.sifNastavnik=predavac.sifNastavnik INNER JOIN predmet ON "
					+ "predmet.sifPred=predavac.sifPred where nosilac=true AND semestar=2");

			while (rs.next()) {
				ljetniLV.getItems().add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + " "
						+ rs.getString(4) + ", " + rs.getString(5));
			}

			// AKO JE STUDENT PRVA GODINA IMA VEC REGISTROVANE PREDMETE
			if (student.getOstvarenihECTS() == 0) {
				odabraniLV.getItems().add("MAT1 Matemtika 1");
				odabraniLV.getItems().add("FIZ1 Fizika 1");
				odabraniLV.getItems().add("ESKE001 Osnovi elektrotehnike 1");
				odabraniLV.getItems().add("RI101 Osnovi racunarstva");
				odabraniLV.getItems().add("EEMS001 Uvod u energetske sisteme");

				odabraniLV.getItems().add("MAT2 Matematika 2");
				odabraniLV.getItems().add("FIZ2 Fizika 2");
				odabraniLV.getItems().add("ESKE002 Osnovi elektrotehnike 2");
				odabraniLV.getItems().add("RI101 Osnovi programiranja");
				odabraniLV.getItems().add("TK001 Tehnicka podrska tehnickom pisanju");

				regPred.add("MAT1");
				regPred.add("FIZ1");
				regPred.add("ESKE001");
				regPred.add("RI101");
				regPred.add("EEMS001");

				regPred.add("MAT2");
				regPred.add("FIZ2");
				regPred.add("ESKE002");
				regPred.add("RI101");
				regPred.add("TK001");
				sql = "SELECT status FROM  registrovaniPredmet WHERE indeks=? AND confirm=true";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setInt(1, student.getBrIndexa());
				ResultSet tempRS = statement.executeQuery();

				if (!tempRS.next()) {
					for (int i = 0; i < regPred.size(); ++i) {
						sql = "INSERT INTO registrovaniPredmet (sifPred,indeks,akademskaGodina,confirm,status) "
								+ "VALUES (?,?,?,?,?)";
						statement = connection.prepareStatement(sql);
						statement.setString(1, regPred.get(i));
						statement.setInt(2, student.getBrIndexa());
						statement.setInt(3, 2024);
						statement.setBoolean(4, true);
						statement.setString(5, "prvi put");
						statement.executeUpdate();
					}

					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Pozdrav!");
					alert.setHeaderText(null);
					alert.setContentText("Vi ste prva godina te su vam predmeti vec izabrani! Sretno :)");
					alert.showAndWait();
				}
				return;
			}

			// DODAVANJE SVIH PREDMETA U LISTU KLASE PREDMETI
			rs = stmt.executeQuery("SELECT * FROM predmet");
			while (rs.next()) {
				Predmet predmet = new Predmet();
				predmet.setSifPred(rs.getString(1));
				predmet.setNaziv(rs.getString(2));
				predmet.setSemestar(rs.getInt(3));
				predmet.setEcts(rs.getInt(4));
				predmet.setPreduslovi(rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
				predmeti.add(predmet);
			}

			// DODAVANJE SVIH OCJENA STUDENTA U LISTU OCJENA
			sql = "SELECT * FROM ocjena WHERE indeks=? AND akademskaGodina<2024 ";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, student.getBrIndexa());
			rs = statement.executeQuery();
			while (rs.next()) {
				Ocjena ocjena = new Ocjena();
				ocjena.setIndeks(rs.getInt(1));
				ocjena.setSifNastavnik(rs.getInt(2));
				ocjena.setSifPred(rs.getString(3));
				ocjena.setOcjena(rs.getInt(4));
				ocjene.add(ocjena);
			}

			statement = connection.prepareStatement(
					"SELECT ocjena.sifPred,nazPred FROM ocjena INNER JOIN predmet ON ocjena.sifPred=predmet.sifPred "
							+ " WHERE indeks=? AND ocjena=5 AND semestar=1 ");
			statement.setInt(1, student.getBrIndexa());
			rs = statement.executeQuery();
			while (rs.next()) {
				String temp = rs.getString(1);
				sql = "SELECT status FROM  registrovaniPredmet WHERE indeks=? and sifPred=? and status='obnova na predmetu' ";
				PreparedStatement STATEMENT = connection.prepareStatement(sql);
				STATEMENT.setInt(1, student.getBrIndexa());
				STATEMENT.setString(2, temp);
				ResultSet tempRS = STATEMENT.executeQuery();
				if (!tempRS.next()) {
					sql = "INSERT INTO registrovaniPredmet(sifPred,indeks,akademskaGodina,confirm,status) "
							+ "VALUES (?,?,?,?,?)";
					PreparedStatement pstatement = connection.prepareStatement(sql);
					pstatement.setString(1, rs.getString(1));
					pstatement.setInt(2, student.getBrIndexa());
					pstatement.setInt(3, 2024);
					pstatement.setBoolean(4, false);
					pstatement.setString(5, "obnova na predmetu");
					pstatement.executeUpdate();

				}
				odabraniLV.getItems().add(rs.getString(1) + " " + rs.getString(2));
				regPred.add(rs.getString(1));
				odabraniECTSZima += 6;
			}

			statement = connection.prepareStatement(
					"SELECT ocjena.sifPred,nazPred FROM ocjena INNER JOIN predmet ON ocjena.sifPred=predmet.sifPred "
							+ " WHERE indeks=? AND ocjena=5 AND semestar=2 ");

			statement.setInt(1, student.getBrIndexa());

			rs = statement.executeQuery();

			while (rs.next()) {
				String temp = rs.getString(1);
				sql = "SELECT status FROM  registrovaniPredmet WHERE indeks=? and sifPred=? AND status='obnova na predmetu' ";
				PreparedStatement STATEMENT = connection.prepareStatement(sql);
				STATEMENT.setInt(1, student.getBrIndexa());
				STATEMENT.setString(2, temp);
				ResultSet tempRS = STATEMENT.executeQuery();
				if (!tempRS.next()) {
					sql = "INSERT INTO registrovaniPredmet(sifPred,indeks,akademskaGodina,confirm,status)"
							+ " VALUES (?,?,?,?,?)";
					PreparedStatement pstatement = connection.prepareStatement(sql);
					pstatement.setString(1, rs.getString(1));
					pstatement.setInt(2, student.getBrIndexa());
					pstatement.setInt(3, 2024);
					pstatement.setBoolean(4, false);
					pstatement.setString(5, "obnova na predmetu");
					pstatement.executeUpdate();
				}
				odabraniLV.getItems().add(rs.getString(1) + " " + rs.getString(2));
				regPred.add(rs.getString(1));
				odabraniECTSLjeto += 6;
			}

			statement = connection.prepareStatement(
					"SELECT registrovaniPredmet.sifPred,nazPred FROM registrovaniPredmet INNER JOIN predmet "
							+ "ON registrovaniPredmet.sifPred=predmet.sifPred WHERE indeks=? AND semestar=1 AND status='prvi put' ");
			statement.setInt(1, student.getBrIndexa());
			rs = statement.executeQuery();

			while (rs.next()) {
				odabraniLV.getItems().add(rs.getString(1) + " " + rs.getString(2));
				regPred.add(rs.getString(1));
				odabraniECTSZima += 6;
			}

			statement = connection.prepareStatement(
					"SELECT registrovaniPredmet.sifPred,nazPred FROM registrovaniPredmet INNER JOIN predmet "
							+ "ON registrovaniPredmet.sifPred=predmet.sifPred WHERE indeks=? AND semestar=2 AND status='prvi put' ");
			statement.setInt(1, student.getBrIndexa());
			rs = statement.executeQuery();
			while (rs.next()) {
				odabraniLV.getItems().add(rs.getString(1) + " " + rs.getString(2));
				regPred.add(rs.getString(1));
				odabraniECTSLjeto += 6;
			}

			System.out.println("Odabrani ects zima: " + odabraniECTSZima);
			System.out.println("Odabrani ects ljeto: " + odabraniECTSLjeto);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void nijePoceoPeriod() throws IOException {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Nije jos poceo period registracije");
		alert.showAndWait();
	}

	void proracunajStatus() throws SQLException {
		if (student.getOstvarenihECTS() < 48) {
			student.setGodStudija(1);
		} else if (student.getOstvarenihECTS() >= 48 && student.getOstvarenihECTS() < 108) {
			student.setGodStudija(2);
		} else if (student.getOstvarenihECTS() >= 108 && student.getOstvarenihECTS() < 168) {
			student.setGodStudija(3);
		} else if (student.getOstvarenihECTS() >= 168) {
			student.setGodStudija(4);
		}
	}

	public void zakljucaliStePredmete() throws SQLException {

		String sql = ("SELECT registrovaniPredmet.sifPred,nazPred" + "  FROM registrovaniPredmet INNER JOIn predmet on "
				+ " predmet.sifPred=registrovaniPredmet.sifPred WHERE indeks=? AND confirm=true");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		PreparedStatement statement1 = connection.prepareStatement(sql);
		statement1.setInt(1, student.getBrIndexa());
		ResultSet tempRS1 = statement1.executeQuery();
		while (tempRS1.next()) {
			odabraniLV.getItems().add(tempRS1.getString(1) + " " + tempRS1.getString(2));
		}

		ZAKLJUCANO = true;

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Zakljucano!");
		alert.setHeaderText(null);
		alert.setContentText(
				"Zakljucali ste predmete ili je period registracije istekao te ako zelite izmjenu posaljite zahtjev prodekanu za nastavu");
		alert.showAndWait();
	}

	public void odabirPolozenog() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Ovaj predmet ste vec polozili te ga ne mozete registrovati ");
		alert.showAndWait();
	}
	
	public void pretraziPredmete() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		
		// zimski semestar
		String query = ("SELECT predavac.sifPred,nazPred, imeNast,prezNast,zvanje FROM predavac INNER JOIN "
				+ "nastavnik on nastavnik.sifNastavnik=predavac.sifNastavnik INNER JOIN predmet ON "
				+ "predmet.sifPred=predavac.sifPred where nosilac=true AND semestar=1 AND (nazPred LIKE '%" + pretragaPredmetaTF.getText() + "%'" + "OR imeNast LIKE '%" + pretragaPredmetaTF.getText() + "%'" + "OR prezNast LIKE '%" + pretragaPredmetaTF.getText() + "%'" + "OR predavac.sifPred LIKE '%" + pretragaPredmetaTF.getText() + "%')");
		PreparedStatement stmt = connection.prepareStatement(query);
	
		ResultSet rs = stmt.executeQuery();
		zimskiLV.getItems().clear();
		while (rs.next()) {
			zimskiLV.getItems().add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + " "
					+ rs.getString(4) + ", " + rs.getString(5));
		}
		
		//ljetni semestar
		String query2 = ("SELECT predavac.sifPred,nazPred, imeNast,prezNast,zvanje FROM predavac INNER JOIN "
				+ "nastavnik on nastavnik.sifNastavnik=predavac.sifNastavnik INNER JOIN predmet ON "
				+ "predmet.sifPred=predavac.sifPred where nosilac=true AND semestar=2 AND (nazPred LIKE '%" + pretragaPredmetaTF.getText() + "%'" + "OR imeNast LIKE '%" + pretragaPredmetaTF.getText() + "%'" + "OR prezNast LIKE '%" + pretragaPredmetaTF.getText() + "%'" + "OR predavac.sifPred LIKE '%" + pretragaPredmetaTF.getText() + "%')");
		PreparedStatement stmt2 = connection.prepareStatement(query2);
	
		rs = stmt2.executeQuery();
		ljetniLV.getItems().clear();
		while (rs.next()) {
			ljetniLV.getItems().add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + " "
					+ rs.getString(4) + ", " + rs.getString(5));
		}
	}

	public void odabirRegistrovanog() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Vec ste odabrali ovaj predmet.");
		alert.showAndWait();
	}

	public void odabirPreko30ECTS() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Izabrali ste vec 30 ECTS za ovaj semestar.");
		alert.showAndWait();
	}

	public void nemaPreduslov() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Zao nam je, nemate preduslov za ovaj predmet."
				+ " Medjutim, bez brige, mozete poslati zahtjev nosiocu predmeta"
				+ " da slusate predmet bez ispunjenih preduslova :)");
		alert.showAndWait();
	}

	public void brisanjeRegistrovanogOdProslegodine() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText(
				"NE mozete izbrisati predmet koji ste odslusali prethodne akademske godine, mora biti izabran.");
		alert.showAndWait();
	}

	public void brucosError() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Prva ste godina. Ne mozete birati ni brisati registrovane predmete!");
		alert.showAndWait();
	}

	public ArrayList<String> findPreduslove(String s) {
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < predmeti.size(); ++i) {
			if (predmeti.get(i).getSifPred().equals(s)) {
				temp = predmeti.get(i).getPreduslovi();
			}
		}
		return temp;
	}

	public void dodajZimski(ActionEvent event) throws SQLException {
		if (ZAKLJUCANO)
			return;
		/* POCETAK USLOVA */
		// AKO JE STUDENT PRVA GODINA, SIGURNOST DA NE MOZE NISTA RADITI
		if (student.getOstvarenihECTS() == 0) {
			brucosError();
			return;
		}

		// ODABERE SELECTOVANI PREDMET IZ LV I SPLITA GA GDJE JE ZAREZ TE SE SIFRA
		// PREDMETA NALAZI U str[0]
		String temp = zimskiLV.getSelectionModel().getSelectedItem();
		String str[] = temp.split(",");

		// PRVA PROVJERA, DA LI IMA VEC OCJENU IZ TOG PREDMETA ODNOSNO ZNACI DA GA JE
		// STUDENT POLOZIO TE GA NE MOZE REGISTROVATI
		for (int i = 0; i < ocjene.size(); ++i) {
			if (ocjene.get(i).getSifPred().equals(str[0]) && ocjene.get(i).getOcjena() > 5) {
				odabirPolozenog();
				return;
			}
		}
		// NAPRAVIMO LISTU PREDUSLOVA ZA TAJ PREDMET
		ArrayList<String> predusloviTemp = findPreduslove(str[0]);
		// PROVJERAVA IMA LI POLOZEN PREDUSLOVNI PREDMET AKO NE RETURN
		if (predusloviTemp.get(0) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(0)) && ocjene.get(i).getOcjena() < 6) {
					nemaPreduslov();
					return;
				}
			}
		}
		if (predusloviTemp.get(1) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(1)) && ocjene.get(i).getOcjena() < 6) {
					nemaPreduslov();
					return;
				}
			}
		}

		if (predusloviTemp.get(2) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(2)) && ocjene.get(i).getOcjena() < 6) {
					nemaPreduslov();
					return;
				}
			}
		}

		if (predusloviTemp.get(3) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(3)) && ocjene.get(i).getOcjena() < 6) {
					nemaPreduslov();
					return;
				}
			}
		}

		// AKO JE VEC IZABRANO 5 PREDMETA (30 ECTS) VRATI I ISPISI PORUKU
		if (odabraniECTSZima >= 30) {
			odabirPreko30ECTS();
			return;
		}
		/* KRAJ USLOVA */

		if (!regPred.isEmpty()) {
			for (int i = 0; i < regPred.size(); ++i) {
				if (regPred.get(i).equals(str[0])) {
					odabirRegistrovanog();
					return;
				}
			}
		}
		odabraniLV.getItems().add(0, str[0] + " " + str[1]);
		regPred.add(str[0]);
		dodajUBazu(str[0]);
		odabraniECTSZima += 6;
		System.out.println(regPred.toString());

	}

	public void dodajLjetni(ActionEvent event) throws SQLException {
		if (ZAKLJUCANO)
			return;
		/* POCETAK USLOVA */
		// AKO JE STUDENT PRVA GODINA, SIGURNOST DA NE MOZE NISTA RADITI
		if (student.getOstvarenihECTS() == 0) {
			brucosError();
			return;
		}

		// ODABERE SELECTOVANI PREDMET IZ LV I SPLITA GA GDJE JE ZAREZ TE SE SIFRA
		// PREDMETA NALAZI U str[0]
		String temp = ljetniLV.getSelectionModel().getSelectedItem();
		String str[] = temp.split(",");

		// PRVA PROVJERA, DA LI IMA VEC OCJENU IZ TOG PREDMETA ODNOSNO ZNACI DA GA JE
		// STUDENT POLOZIO TE GA NE MOZE REGISTROVATI
		for (int i = 0; i < ocjene.size(); ++i) {
			if (ocjene.get(i).getSifPred().equals(str[0]) && ocjene.get(i).getOcjena() > 5) {
				odabirPolozenog();
				return;
			}
		}
		// NAPRAVIMO LISTU PREDUSLOVA ZA TAJ PREDMET
		ArrayList<String> predusloviTemp = findPreduslove(str[0]);
		// PROVJERAVA IMA LI POLOZEN PREDUSLOVNI PREDMET AKO NE RETURN
		if (predusloviTemp.get(0) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(0)) && ocjene.get(i).getOcjena() < 6) {
					nemaPreduslov();
					return;
				}
			}
		}
		if (predusloviTemp.get(1) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(1)) && ocjene.get(i).getOcjena() < 6) {
					nemaPreduslov();
					return;
				}
			}
		}

		if (predusloviTemp.get(2) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(2)) && ocjene.get(i).getOcjena() < 6) {
					nemaPreduslov();
					return;
				}
			}
		}

		if (predusloviTemp.get(3) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(3)) && ocjene.get(i).getOcjena() < 6) {
					nemaPreduslov();
					return;
				}
			}
		}

		// AKO JE VEC IZABRANO 5 PREDMETA (30 ECTS) VRATI I ISPISI PORUKU
		if (odabraniECTSLjeto >= 30) {
			odabirPreko30ECTS();
			return;
		}
		/* KRAJ USLOVA */

		if (!regPred.isEmpty()) {
			for (int i = 0; i < regPred.size(); ++i) {
				if (regPred.get(i).equals(str[0])) {
					odabirRegistrovanog();
					return;
				}
			}
		}
		odabraniLV.getItems().add(str[0] + " " + str[1]);
		regPred.add(str[0]);
		dodajUBazu(str[0]);
		odabraniECTSLjeto += 6;
		System.out.println(regPred.toString());

	}

	public void izbaciIzDodanih(ActionEvent event) throws SQLException {
		if (ZAKLJUCANO)
			return;
		if (student.getOstvarenihECTS() == 0) {
			brucosError();
			return;
		}

		String temp = odabraniLV.getSelectionModel().getSelectedItem();
		String str[] = temp.split(" ");

		for (int i = 0; i < ocjene.size(); ++i) {
			if (ocjene.get(i).getSifPred().equals(str[0]) && ocjene.get(i).getOcjena() == 5) {
				brisanjeRegistrovanogOdProslegodine();
				return;
			}
		}

		int index = odabraniLV.getSelectionModel().getSelectedIndex();
		odabraniLV.getItems().remove(index);
		regPred.remove(str[0]);
		izbacIzBaze(str[0]);

		for (int i = 0; i < predmeti.size(); ++i) {
			if (predmeti.get(i).getSifPred().equals(str[0])) {
				if (predmeti.get(i).getSemestar() == 1) {
					odabraniECTSZima -= 6;
				} else {
					odabraniECTSLjeto -= 6;
				}
			}
		}
		System.out.println(odabraniECTSZima);
		System.out.println(odabraniECTSLjeto);
		System.out.println(regPred.toString());

	}

	void dodajUBazu(String s) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		String sql = "INSERT INTO registrovaniPredmet (sifPred,indeks,akademskaGodina,confirm,status) "
				+ "VALUES (?,?,?,?,?)";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, s);
		statement.setInt(2, student.getBrIndexa());
		statement.setInt(3, 2024);
		statement.setBoolean(4, false);
		statement.setString(5, "prvi put");
		statement.executeUpdate();
	}

	void izbacIzBaze(String s) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		String sql = "DELETE FROM registrovaniPredmet WHERE indeks=? AND sifPred=? ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, student.getBrIndexa());
		statement.setString(2, s);
		statement.executeUpdate();
	}

	public void showDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Zakljucali ste odabir!");
		alert.showAndWait();
	}

	public void showDialog1() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Niste odabrali nijedan predmet!");
		alert.showAndWait();
	}

	public void zakljucaj() throws SQLException, IOException {
		if (student.getOstvarenihECTS() == 0)
			return;

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		String sql1 = "SELECT * FROM registrovaniPredmet WHERE indeks=?";
		PreparedStatement statement1 = connection.prepareStatement(sql1);
		statement1.setInt(1, student.getBrIndexa());

		ResultSet rs = statement1.executeQuery();

		if (!rs.next()) {
			showDialog1();
			return;
		}

		String sql = "UPDATE registrovaniPredmet SET confirm=true where indeks=? ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, student.getBrIndexa());
		statement.executeUpdate();
		showDialog();
		// Main m = new Main();
		// m.changeScene("/application/Sample.fxml");
	}

	public void zahtjev() throws IOException {
		if (student.getOstvarenihECTS() == 0)
			return;
		Main m = new Main();
		m.changeScene("/Student/Zahtjev.fxml");
	}

	public void userLogOut(ActionEvent event) throws IOException, SQLException {
		Main m = new Main();
		m.changeScene("/application/Sample.fxml");
	}
}