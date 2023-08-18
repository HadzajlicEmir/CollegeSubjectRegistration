package Nastavnik;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import application.Main;
import application.Nastavnik;

public class NastavnikController implements Initializable {

	@FXML
	private ListView<String> ocjeneLV;
	@FXML
	private ComboBox<String> predmetCB;
	@FXML
	private Button Studenti;
	@FXML
	private Button Predmeti;
	@FXML
	private Button prenosBodova;
	@FXML
	private Button ostaliZahtjevi;
	@FXML
	private TextArea zahtjevTF;
	@FXML
	private Button prikaziB;
	@FXML
	private Button logout;

	public Nastavnik nastavnik = application.LogIn.nastavnik;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
			PreparedStatement statement = connection
					.prepareStatement("SELECT predmet.sifPred,nazPred FROM predmet INNER JOIN predavac ON"
							+ " predmet.sifPred=predavac.sifPred WHERE sifNastavnik=?");
			statement.setInt(1, nastavnik.getSifNastavnik());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				predmetCB.getItems().add(rs.getString(1) + " " + rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void prikaziOcjene() throws SQLException {
		ocjeneLV.getItems().clear();
		String tmp = predmetCB.getSelectionModel().getSelectedItem();
		String str[] = tmp.split(" ");

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		PreparedStatement statement = connection
				.prepareStatement("SELECT ocjena.indeks,imeStud,prezStud,status,ocjena  FROM ocjena"
						+ " INNER JOIN student on ocjena.indeks=student.indeks WHERE"
						+ " sifPred=? and akademskaGodina=2023");
		statement.setString(1, str[0]);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			ocjeneLV.getItems().add("Indeks: " + rs.getInt(1) + " Ime: " + rs.getString(2) + " Prezime: "
					+ rs.getString(3) + " Status: " + rs.getString(4) + " " + " Ocjena: " + rs.getInt(5));
		}
	}

	public void studenti() throws IOException {
		Main m = new Main();
		m.changeScene("/Nastavnik/Studenti.fxml");
	}

	public void predmeti() throws IOException {
		Main m = new Main();
		m.changeScene("/Nastavnik/Predmeti.fxml");
	}

	public void ostaliZahtjevi() throws IOException {
		Main m = new Main();
		m.changeScene("/Nastavnik/PregledajZahtjev.fxml");
	}

	public void prenosBodova() throws IOException {
		Main m = new Main();
		m.changeScene("/Nastavnik/PrenosBodova.fxml");
	}

	public void logOut() throws IOException {
		Main m = new Main();
		m.changeScene("/application/Sample.fxml");
	}
}