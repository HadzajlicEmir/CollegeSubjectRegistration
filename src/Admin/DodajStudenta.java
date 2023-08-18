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

public class DodajStudenta {

	@FXML
	private TextField brIndexTF;
	@FXML
	private TextField imeTF;
	@FXML
	private TextField prezimeTF;

	@FXML
	private TextField ectsTF;
	@FXML
	private TextField statusTF;
	@FXML
	private Button buttonDodaj;

	public void dodajStudenta(String ime, String prezime, int indeks, int ects, String status) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		String sql = "INSERT INTO student (indeks,imeStud,prezStud,ostvareniECTS, status) " + "VALUES (?,?,?,?,?)";

		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, indeks);
		statement.setString(2, ime);
		statement.setString(3, prezime);
		statement.setInt(4, ects);
		statement.setString(5, status);

		// Izvršavanje upita
		int rowsInserted = statement.executeUpdate();

		statement.close();
		connection.close();

		if (rowsInserted > 0) {
			System.out.println("Student je uspješno dodan u bazu podataka.");
		} else {
			System.out.println("Dogodila se greška prilikom dodavanja studenta.");
		}
	}

	public void generisiUser(String ime, String prezime, int indeks) throws SQLException {
		// Uspostavi konekciju sa bazom podataka (MySQL)
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		// Pripremi SQL upit
		String sql = "SELECT * FROM student WHERE imeStud = ? AND prezStud = ?";
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

		statement1.setInt(1, indeks);
		statement1.setString(2, email);
		statement1.setString(3, password);
		statement1.setInt(4, 3);

		// Izvršavanje upita
		int rowsInserted = statement1.executeUpdate();

		statement1.close();
		statement.close();
		connection.close();

		if (rowsInserted > 0) {
			System.out.println("Student je uspješno dodan u bazu podataka.");
		} else {
			System.out.println("Dogodila se greška prilikom dodavanja studenta.");
		}

	}

	public void showDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Uspjesno ste dodali studenta");
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
		String ime, prezime, brIndexa, status, ects;
		ime = imeTF.getText();
		prezime = prezimeTF.getText();
		brIndexa = brIndexTF.getText();
		status = statusTF.getText();
		ects = ectsTF.getText();

		try {
			if (ime == "" || prezime == "" || brIndexa == "" || status == "" || ects == "") {
				throw new NumberFormatException();
			}

			generisiUser(imeTF.getText(), prezimeTF.getText(), Integer.parseInt(brIndexTF.getText()));
			dodajStudenta(imeTF.getText(), prezimeTF.getText(), Integer.parseInt(brIndexTF.getText()),
					Integer.parseInt(ectsTF.getText()), statusTF.getText());

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