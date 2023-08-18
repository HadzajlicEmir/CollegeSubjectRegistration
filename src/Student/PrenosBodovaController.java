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

public class PrenosBodovaController implements Initializable {
	@FXML
	private Button podnesiB;
	@FXML
	private Button back;
	@FXML
	private ComboBox<String> predmetCB;
	@FXML
	private TextField indeksTF;

	public Student student = application.LogIn.student;
	
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
			String sql = ("SELECT registrovaniPredmet.sifPred,nazPred FROM registrovaniPredmet"
					+ " INNER JOIN predmet ON registrovaniPredmet.sifPred=predmet.sifPred WHERE indeks=?"
					+ " AND status='obnova na predmetu'");
			PreparedStatement statement1 = connection.prepareStatement(sql);
			statement1.setInt(1, student.getBrIndexa());
			ResultSet rs = statement1.executeQuery();
			while (rs.next()) {
				predmetCB.getItems().add(rs.getString(1) + " " + rs.getString(2));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void info() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Svaka cast!");
		alert.setHeaderText(null);
		alert.setContentText("Prenijeli ste bodove! Sretno!");
		alert.showAndWait();
	}

	public void podnesi() throws SQLException, IOException {
		String tmp = predmetCB.getSelectionModel().getSelectedItem();
		String str[] = tmp.split(" ");

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		String sql = ("INSERT INTO zahtjevZaPrenosBodova(indeks,sifPred) VALUES(?,?)");
		PreparedStatement statement1 = connection.prepareStatement(sql);
		statement1.setInt(1,student.getBrIndexa());
		statement1.setString(2, str[0]);
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