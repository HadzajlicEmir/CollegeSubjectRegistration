package Student;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import application.Main;
import application.Nastavnik;
import application.Ocjena;
import application.Predmet;

public class ZahtjevController implements Initializable {
	@FXML
	private Button noPreduslov;
	@FXML
	private Button prenosBodova;
	@FXML
	private Button promjena;
	@FXML
	private Button prikazi1;
	@FXML
	private Button prikazi2;
	@FXML
	private Button back;
	@FXML
	private ListView<String> bezPreduslovLV;
	@FXML
	private ListView<String> promjenaRegLV;
	@FXML
	private TextArea textarea;

	public Student student = application.LogIn.student;

	public String temp;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
			String sql = ("SELECT sifPredBris,sifPredDod FROM zahtjevZaPromjenuRegPred WHERE indeks=? AND odobreno=1");
			PreparedStatement statement1 = connection.prepareStatement(sql);
			statement1.setInt(1, student.getBrIndexa());
			ResultSet rs = statement1.executeQuery();
			while (rs.next()) {
				promjenaRegLV.getItems().add("Trenutni:" + rs.getString(1) + " Zamjena:" + rs.getString(2));
				temp = rs.getString(1);
			}
			sql = ("SELECT predmet.sifPred,nazPred FROM zahtjevZaSlusanjeBezPreduslova "
					+ " INNER JOIN predmet ON zahtjevZaSlusanjeBezPreduslova.sifPred=predmet.sifPred"
					+ " WHERE indeks=? AND odobreno=1");
			statement1 = connection.prepareStatement(sql);
			statement1.setInt(1, student.getBrIndexa());
			rs = statement1.executeQuery();
			while (rs.next()) {
				bezPreduslovLV.getItems().add(rs.getString(1) + " " + rs.getString(2));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void prikazi1() throws SQLException {
		String tmp = bezPreduslovLV.getSelectionModel().getSelectedItem();
		String str[] = tmp.split(" ");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		String sql = ("SELECT porukaNastavnika FROM zahtjevZaSlusanjeBezPreduslova WHERE indeks=? AND " + "sifPred=?");
		PreparedStatement statement1 = connection.prepareStatement(sql);
		statement1.setInt(1, student.getBrIndexa());
		statement1.setString(2, str[0]);
		ResultSet rs = statement1.executeQuery();
		while (rs.next()) {
			textarea.setText(rs.getString(1));
		}
		sql = "DELETE FROM zahtjevZaSlusanjeBezPreduslova WHERE indeks=? AND sifPred=?";
		statement1 = connection.prepareStatement(sql);
		statement1.setInt(1, student.getBrIndexa());
		statement1.setString(2, str[0]);
		statement1.executeUpdate();
	}

	public void prikazi2() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		String sql = ("SELECT porukaProdekana FROM zahtjevZaPromjenuRegPred WHERE indeks=? AND sifPredBris=?");
		PreparedStatement statement1 = connection.prepareStatement(sql);
		statement1.setInt(1, student.getBrIndexa());
		statement1.setString(2, temp);
		ResultSet rs = statement1.executeQuery();
		while (rs.next()) {
			textarea.setText(rs.getString(1));
		}
		sql = "DELETE FROM zahtjevZaPromjenuRegPred WHERE indeks=? AND sifPredBris=?";
		statement1 = connection.prepareStatement(sql);
		statement1.setInt(1, student.getBrIndexa());
		statement1.setString(2, temp);
		statement1.executeUpdate();
	}

	public void prenosBodova() throws IOException {
		Main m = new Main();
		m.changeScene("/Student/PrenosBodova.fxml");
	}

	public void promjenaRegPred() throws IOException {
		Main m = new Main();
		m.changeScene("/Student/PromjenaRegPred.fxml");
	}

	public void slusanjeBezPreduslov() throws IOException {
		Main m = new Main();
		m.changeScene("/Student/SlusanjeBezPreduslov.fxml");
	}
	
	public void back() throws IOException {
		Main m = new Main();
		m.changeScene("/Student/Student.fxml");
	}

}