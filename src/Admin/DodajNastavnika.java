package Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import application.Main;

public class DodajNastavnika {

	@FXML
	private TextField sifNastavnikTF;
	@FXML
	private TextField imeTF;
	@FXML
	private TextField prezimeTF;

	@FXML
	private TextField zvanjeTF;
	@FXML
	private Button buttonDodaj;
	@FXML
	private Button backButton;

	public void dodajNastavnika(String ime, String prezime, int sifNastavnik, String zvanje) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		String sql = "INSERT INTO nastavnik (sifNastavnik,imeNast,prezNast,zvanje, prodekanBool) "
				+ "VALUES (?,?,?,?,?)";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, sifNastavnik);
		statement.setString(2, ime);
		statement.setString(3, prezime);
		statement.setString(4, zvanje);
		statement.setBoolean(5, false);

		// Izvršavanje upita
		int rowsInserted = statement.executeUpdate();

		statement.close();
		connection.close();

		if (rowsInserted > 0) {
			System.out.println("Nastavnik je uspješno dodan u bazu podataka.");
		} else {
			System.out.println("Dogodila se greška prilikom dodavanja nastavnika.");
		}
	}

	public void generisiUser(String ime, String prezime, int sifNastavnik) throws SQLException {
		// Uspostavi konekciju sa bazom podataka (MySQL)
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		// Pripremi SQL upit
		String sql = "SELECT * FROM nastavnik WHERE imeNast = ? AND prezNast = ?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, ime);
		statement.setString(2, prezime);

		// Izvrši upit i dobavi rezultate
		ResultSet resultSet = statement.executeQuery();

		int brojRezultata = 0;
		while (resultSet.next()) {
			brojRezultata++;
		}

		String imePrezime = ime + "." + prezime;
		String domena = "fet.ba";

		if (brojRezultata > 0) {
			imePrezime += String.valueOf(brojRezultata);
		}

		String email = imePrezime + "@" + domena;
		email = email.toLowerCase();

		// PASSWORD GENERATE

		String raspoloziviKarakteri = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
		int minDuzina = 8;

		String password = "";

		for (int i = 0; i < minDuzina; ++i) {
			Random random = new Random();
			int randomIndex = random.nextInt(raspoloziviKarakteri.length());
			char karakter = raspoloziviKarakteri.charAt(randomIndex);
			password += karakter;
		}

		String sql1 = "INSERT INTO users (sifra,email,pass,status) " + "VALUES (?,?,?,?)";

		PreparedStatement statement1 = connection.prepareStatement(sql1);

		statement1.setInt(1, sifNastavnik);
		statement1.setString(2, email);
		statement1.setString(3, password);
		statement1.setInt(4, 2);

		// Izvršavanje upita
		int rowsInserted = statement1.executeUpdate();

		statement1.close();
		statement.close();
		connection.close();

		if (rowsInserted > 0) {
			System.out.println("Nastavnik je uspješno dodan u bazu podataka.");
		} else {
			System.out.println("Dogodila se greška prilikom dodavanja nastavnika.");
		}

	}

	public void showDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Uspjesno ste dodali nastavnika");
		alert.showAndWait();
	}

	public void errorDialog() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Neispravan unos");
		alert.showAndWait();
	}

	public void backToAdmin(ActionEvent event) throws IOException {
		try {
			generisiUser(imeTF.getText(), prezimeTF.getText(), Integer.parseInt(sifNastavnikTF.getText()));
			dodajNastavnika(imeTF.getText(), prezimeTF.getText(), Integer.parseInt(sifNastavnikTF.getText()),
					zvanjeTF.getText());
			Main m = new Main();
			showDialog();
			m.changeScene("/Admin/Admin.fxml");
		} catch (NumberFormatException e) {
			errorDialog();
		} catch (SQLException e) {
			errorDialog();
		}

	}

	public void backToMainAdmin() throws IOException {
		Main m = new Main();
		m.changeScene("/Admin/Admin.fxml");
	}

}