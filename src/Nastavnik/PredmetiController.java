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

import java.util.ResourceBundle;

import application.Main;
import application.Nastavnik;

public class PredmetiController implements Initializable {

	@FXML
	private ListView<String> LV;
	@FXML
	private ComboBox<String> CB;
	@FXML
	private Button button;
	@FXML
	private Button back;

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
				CB.getItems().add(rs.getString(1) + " " + rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void prikaziStudente() throws SQLException {
		LV.getItems().clear();
		String tmp = CB.getSelectionModel().getSelectedItem();
		String str[] = tmp.split(" ");

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		PreparedStatement statement = connection.prepareStatement(
				"SELECT student.indeks,imeStud,prezStud,student.status,registrovaniPredmet.status FROM registrovaniPredmet INNER JOIN "
						+ "student ON student.indeks=registrovaniPredmet.indeks WHERE sifPred=? AND akademskaGodina=2024");
		statement.setString(1, str[0]);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			LV.getItems().add("Indeks: " + rs.getInt(1) + ", Ime: " + rs.getString(2) + ", Prezime: " + rs.getString(3)
					+ ", Status studenta: " + rs.getString(4) + ", Status na predmetu: " + rs.getString(5));
		}
	}

	public void back() throws IOException {
		Main m = new Main();
		m.changeScene("/Nastavnik/Nastavnik.fxml");
	}
}