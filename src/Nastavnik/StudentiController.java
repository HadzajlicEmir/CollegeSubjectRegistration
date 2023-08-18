package Nastavnik;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import Student.Student;
import application.Main;
import application.Nastavnik;

public class StudentiController implements Initializable {

	@FXML
	private ComboBox<String> predmetCB;
	@FXML
	private TextField studentCB;
	@FXML
	private TextField ocjenaCB;
	@FXML
	private Button unesiB;
	@FXML
	private Button back;

	public Nastavnik nastavnik = application.LogIn.nastavnik;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
			PreparedStatement statement = connection
					.prepareStatement("SELECT predavac.sifPred,nazPred FROM predavac INNER JOIN nastavnik ON  "
							+ "predavac.sifNastavnik=nastavnik.sifNastavnik INNER JOIN predmet on "
							+ "predavac.sifPred=predmet.sifPred WHERE predavac.sifNastavnik=? ");
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

	public void info() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Svaka cast!");
		alert.setHeaderText(null);
		alert.setContentText("Dodali ste ocjenu!");
		alert.showAndWait();
	}

	public void unesi() throws SQLException, IOException {
		String tmp = predmetCB.getSelectionModel().getSelectedItem();
		String str[] = tmp.split(" ");

		int indeks = Integer.parseInt(studentCB.getText());
		int ocjena = Integer.parseInt(ocjenaCB.getText());
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		PreparedStatement statement = connection
				.prepareStatement("UPDATE ocjena SET ocjena=? WHERE indeks=? AND sifPred=? AND akademskaGodina=2023");
		statement.setInt(1, ocjena);
		statement.setInt(2, indeks);
		statement.setString(3, str[0]);
		statement.executeUpdate();

		info();

		Main m = new Main();
		m.changeScene("/Nastavnik/Studenti.fxml");
	}

	public void back() throws IOException {
		Main m = new Main();
		m.changeScene("/Nastavnik/Nastavnik.fxml");
	}
}