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

public class SlusanjeBezPreduslovController implements Initializable {
	@FXML
	private Button podnesiB;
	@FXML
	private Button back;
	@FXML
	private ComboBox<String> predmetCB;
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

			String sql = "SELECT * FROM ocjena WHERE indeks=? AND akademskaGodina<2024 ";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, student.getBrIndexa());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Ocjena ocjena = new Ocjena();
				ocjena.setIndeks(rs.getInt(1));
				ocjena.setSifNastavnik(rs.getInt(2));
				ocjena.setSifPred(rs.getString(3));
				ocjena.setOcjena(rs.getInt(4));
				ocjene.add(ocjena);
			}

			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery("select * from predmet ");

			while (rs.next()) {
				Predmet predmet = new Predmet();
				predmet.setSifPred(rs.getString(1));
				predmet.setNaziv(rs.getString(2));
				predmet.setSemestar(rs.getInt(3));
				predmet.setEcts(rs.getInt(4));
				predmet.setPreduslovi(rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
				predmeti.add(predmet);
				predmetCB.getItems().add(rs.getString(1) + " " + rs.getString(2));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
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

	public void info() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Svaka cast!");
		alert.setHeaderText(null);
		alert.setContentText("Poslali ste zahtjev! Ako bude odobren, dodat ce vam biti predmet"
				+ " i moci cete vidjeti poruku od nastavnika");
		alert.showAndWait();
	}

	public void odabirPolozenog() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Ovaj predmet ste vec polozii i ne mozete ga izabrati.");
		alert.showAndWait();
	}

	public boolean provjeraPolozenog() {
		String tmp = predmetCB.getSelectionModel().getSelectedItem();
		String str[] = tmp.split(" ");
		for (int i = 0; i < ocjene.size(); ++i) {
			if (ocjene.get(i).getSifPred().equals(str[0]) && ocjene.get(i).getOcjena() > 5) {

				return false;
			}
		}
		return true;
	}

	public void podnesi() throws SQLException, IOException {

		if (provjeraPolozenog() == false) {
			odabirPolozenog();
			return;
		}

		String tmp1 = predmetCB.getSelectionModel().getSelectedItem();
		String str1[] = tmp1.split(" ");

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		String sql = ("INSERT INTO zahtjevZaSlusanjeBezPreduslova(indeks,sifPred,porukaStudenta,odobreno) "
				+ " VALUES(?,?,?,?)");
		PreparedStatement statement1 = connection.prepareStatement(sql);
		statement1.setInt(1, student.getBrIndexa());
		statement1.setString(2, str1[0]);
		statement1.setString(3, poruka.getText());
		statement1.setInt(4, 2);
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