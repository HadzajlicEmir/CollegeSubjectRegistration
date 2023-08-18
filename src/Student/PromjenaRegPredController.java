package Student;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import application.Main;
import application.Nastavnik;
import application.Ocjena;
import application.Predmet;

public class PromjenaRegPredController implements Initializable {
	@FXML
	private Button podnesiB;
	@FXML
	private Button back;
	@FXML
	private ComboBox<String> predmet1CB;
	@FXML
	private ComboBox<String> predmet2CB;
	@FXML
	private TextField indeksTF;
	@FXML
	private TextArea poruka;

	// VARIJABLE ZA KORISTENJE
	public Student student = application.LogIn.student;
	ArrayList<Predmet> predmeti = new ArrayList<Predmet>();

	ArrayList<Ocjena> ocjene = new ArrayList<Ocjena>();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
			String sql = ("SELECT registrovaniPredmet.sifPred,nazPred FROM registrovaniPredmet"
					+ " INNER JOIN predmet ON registrovaniPredmet.sifPred=predmet.sifPred WHERE indeks=?"
					+ " AND status='prvi put'");
			PreparedStatement statement1 = connection.prepareStatement(sql);
			statement1.setInt(1, student.getBrIndexa());
			ResultSet rs = statement1.executeQuery();
			while (rs.next()) {
				predmet1CB.getItems().add(rs.getString(1) + " " + rs.getString(2));
			}

			rs = statement1.executeQuery("SELECT * FROM predmet");
			while (rs.next()) {
				Predmet predmet = new Predmet();
				predmet.setSifPred(rs.getString(1));
				predmet.setNaziv(rs.getString(2));
				predmet.setSemestar(rs.getInt(3));
				predmet.setEcts(rs.getInt(4));
				predmet.setPreduslovi(rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
				predmeti.add(predmet);
				predmet2CB.getItems().add(rs.getString(1) + " " + rs.getString(2));
			}

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

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void info() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Svaka cast!");
		alert.setHeaderText(null);
		alert.setContentText("Poslali ste zahtjev! Ako bude odobren, zamijenit ce vam se automatski"
				+ " predmet i dobit cete poruku od prodekana!");
		alert.showAndWait();
	}

	public void nemaPreduslov() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Zao nam je, nemate preduslov za ovaj predmet.");
		alert.showAndWait();
	}

	public void odabirPolozenog() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Ovaj predmet ste vec polozii i ne mozete ga izabrati.");
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

	public boolean provjeraPolozenog() {
		String tmp = predmet2CB.getSelectionModel().getSelectedItem();
		String str[] = tmp.split(" ");
		for (int i = 0; i < ocjene.size(); ++i) {
			if (ocjene.get(i).getSifPred().equals(str[0]) && ocjene.get(i).getOcjena() > 5) {

				return false;
			}
		}
		return true;
	}

	public boolean provjeraPreduslova() {
		String tmp = predmet2CB.getSelectionModel().getSelectedItem();
		String str[] = tmp.split(" ");

		// NAPRAVIMO LISTU PREDUSLOVA ZA TAJ PREDMET
		ArrayList<String> predusloviTemp = findPreduslove(str[0]);
		// PROVJERAVA IMA LI POLOZEN PREDUSLOVNI PREDMET AKO NE RETURN
		if (predusloviTemp.get(0) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(0)) && ocjene.get(i).getOcjena() < 6) {

					return false;
				}
			}
		}
		if (predusloviTemp.get(1) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(1)) && ocjene.get(i).getOcjena() < 6) {

					return false;
				}
			}
		}

		if (predusloviTemp.get(2) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(2)) && ocjene.get(i).getOcjena() < 6) {

					return false;
				}
			}
		}

		if (predusloviTemp.get(3) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(3)) && ocjene.get(i).getOcjena() < 6) {

					return false;
				}
			}
		}
		return true;
	}

	public void showDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Odaberite predmete koji su isti semestar!");
		alert.showAndWait();
	}

	public void podnesi() throws SQLException, IOException {
		if (provjeraPreduslova() == false) {
			nemaPreduslov();
			return;
		}
		if (provjeraPolozenog() == false) {
			odabirPolozenog();
			return;
		}

		String tmp1 = predmet1CB.getSelectionModel().getSelectedItem();
		String str1[] = tmp1.split(" ");

		String tmp2 = predmet2CB.getSelectionModel().getSelectedItem();
		String str2[] = tmp2.split(" ");

		Predmet p1 = new Predmet();
		Predmet p2 = new Predmet();

		p1.setSifPred(str1[0]);
		p2.setSifPred(str2[0]);
		System.out.println(str1[0]);

		System.out.println(str2[0]);

		for (int i = 0; i < predmeti.size(); ++i) {
			if (predmeti.get(i).getSifPred().equals(str1[0])) {
				p1 = predmeti.get(i);
				System.out.println(predmeti.get(i).getSemestar());
			}
			if (predmeti.get(i).getSifPred().equals(str2[0])) {
				p2 = predmeti.get(i);
				System.out.println(predmeti.get(i).getSemestar());

			}

		}

		if (p1.getSemestar() != p2.getSemestar()) {
			showDialog();
			return;
		}

		System.out.println(p1.getSemestar());
		System.out.println(p2.getSemestar());

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		String sql = ("INSERT INTO zahtjevZaPromjenuRegPred(indeks,sifPredBris,sifPredDod,porukaStudenta,odobreno) "
				+ " VALUES(?,?,?,?,?)");
		PreparedStatement statement1 = connection.prepareStatement(sql);
		statement1.setInt(1, student.getBrIndexa());
		statement1.setString(2, str1[0]);
		statement1.setString(3, str2[0]);
		statement1.setString(4, poruka.getText());
		statement1.setInt(5, 2);
		statement1.executeUpdate();

		info();
		Main m = new Main();
		m.changeScene("/Student/Zahtjev.fxml");
	}

	public void back() throws IOException {
		Main m = new Main();
		m.changeScene("/Student/Zahtjev.fxml");
	}
}