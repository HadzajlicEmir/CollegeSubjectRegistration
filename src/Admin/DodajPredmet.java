package Admin;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import java.util.ResourceBundle;

import application.Main;

public class DodajPredmet implements Initializable {

	@FXML
	private TextField nazPredTF;
	@FXML
	private TextField sifPredTF;
	@FXML
	private TextField sifNastTF;
	@FXML
	private TextField ectsTF;
	@FXML
	private Button buttonDodaj;
	@FXML
	private Button backButton;
	@FXML
	private ComboBox<String> semestarCB;
	@FXML
	private ComboBox<String> nasPredCB;
	@FXML
	private ComboBox<String> predusl1CB;
	@FXML
	private ComboBox<String> predusl2CB;
	@FXML
	private ComboBox<String> predusl3CB;
	@FXML
	private ComboBox<String> predusl4CB;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select * from nastavnik");
			ArrayList<String> nastavnici = new ArrayList<String>();
			int i = 0;

			while (rs.next()) {
				nastavnici.add(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
				nasPredCB.getItems().add(nastavnici.get(i));
				++i;
			}

			Statement stmt1 = connection.createStatement();
			ResultSet rs1 = stmt1.executeQuery("select * from predmet");
			ArrayList<String> predmeti = new ArrayList<String>();
			i = 0;

			while (rs1.next()) {
				predmeti.add(rs1.getString(1) + " " + rs1.getString(2));
				predusl1CB.getItems().add(predmeti.get(i));
				predusl2CB.getItems().add(predmeti.get(i));
				predusl3CB.getItems().add(predmeti.get(i));
				predusl4CB.getItems().add(predmeti.get(i));

				++i;
			}

			semestarCB.setItems(FXCollections.observableArrayList("Zimski", "Ljetni"));
			// comboBox.getValue();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void errorDialog() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Neispravan unos");
		alert.showAndWait();
	}

	public void dodajPredmet(String sifraP, String nazP, String semestar, int sifraN, int ECTS, String predusl1,
			String predusl2, String predusl3, String predusl4) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		String sql = "INSERT INTO predmet (sifPred,nazPred,"
				+ "semestar,ects, preduslov1,preduslov2,preduslov3,preduslov4) " + "VALUES (?,?,?,?,?,?,?,?)";

		String sql1 = "INSERT INTO predavac (sifNastavnik, sifPred, nosilac) VALUES (?, ?, true)";

		PreparedStatement statement = connection.prepareStatement(sql);

		PreparedStatement statement1 = connection.prepareStatement(sql1);

		statement1.setInt(1, sifraN);
		statement1.setString(2, sifraP);
		statement1.executeUpdate();

		statement.setString(1, sifraP);
		statement.setString(2, nazP);
		int s = 0;

		if (semestar.equals("Zimski")) {
			s = 1;
		} else {
			s = 2;
		}

		statement.setInt(3, s);
		statement.setInt(4, ECTS);
		statement.setString(5, predusl1);
		statement.setString(6, predusl2);
		statement.setString(7, predusl3);
		statement.setString(8, predusl4);

		// Izvršavanje upita
		int rowsInserted = statement.executeUpdate();

		statement.close();
		connection.close();

		if (rowsInserted > 0) {
			System.out.println("Predmet je uspješno dodan u bazu podataka.");
		} else {
			System.out.println("Dogodila se greška prilikom dodavanja nastavnika.");
		}
	}

	public void showDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Uspjesno ste dodali predmet");
		alert.showAndWait();
	}

	public void backToAdmin(ActionEvent event) throws IOException {
		String predusl1, predusl2, predusl3, predusl4;

		if (predusl1CB.getValue() == null) {
			predusl1 = null;
		} else {
			String temp1 = predusl1CB.getValue();
			String str1[] = temp1.split(" ");
			predusl1 = str1[0];
		}

		if (predusl2CB.getValue() == null) {
			predusl2 = null;
		} else {
			String temp2 = predusl2CB.getValue();
			String str2[] = temp2.split(" ");
			predusl2 = str2[0];
		}

		if (predusl3CB.getValue() == null) {
			predusl3 = null;
		} else {
			String temp3 = predusl3CB.getValue();
			String str3[] = temp3.split(" ");

			predusl3 = str3[0];
		}

		if (predusl4CB.getValue() == null) {

			predusl4 = null;
		} else {
			String temp4 = predusl4CB.getValue();
			String str4[] = temp4.split(" ");
			predusl4 = str4[0];
		}

		try {
			dodajPredmet(sifPredTF.getText(), nazPredTF.getText(), semestarCB.getValue().toString(),
					Integer.parseInt(nasPredCB.getValue().substring(0, 4)), Integer.parseInt(ectsTF.getText()),
					predusl1, predusl2, predusl3, predusl4);
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