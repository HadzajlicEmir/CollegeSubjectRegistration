package Prodekan;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;

public class PregledStudenataController implements Initializable {

	@FXML
	private ComboBox<String> predmetCB;
	@FXML
	private ListView<String> listaStudenata;
	@FXML
	private Button back;
	@FXML
	private Button prikazi;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

			Statement stmt4 = connection.createStatement();
			ResultSet rs4 = stmt4.executeQuery("select * from predmet");

			while (rs4.next()) {

				predmetCB.getItems().add(rs4.getString(1) + " " + rs4.getString(2));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void prikazi() throws SQLException {
		listaStudenata.getItems().clear();
		String tmp = predmetCB.getSelectionModel().getSelectedItem();
		String str[] = tmp.split(" ");

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		PreparedStatement statement = connection
				.prepareStatement("SELECT student.indeks,imeStud,prezStud,registrovaniPredmet.status "
						+ " FROM registrovaniPredmet Inner join student on registrovaniPredmet.indeks=student.indeks"
						+ " WHERE sifPred=? AND akademskaGodina=2024");
		statement.setString(1, str[0]);
		ResultSet rs = statement.executeQuery();
		while (rs.next()) {
			listaStudenata.getItems().add("Indeks: " + rs.getInt(1) + ", Ime: " + rs.getString(2) + ", Prezime: "
					+ rs.getString(3) + ", status na predmetu: " + rs.getString(4));
		}
	}

	public void backToProdekan() throws IOException {
		Main m = new Main();
		m.changeScene("/Prodekan/Prodekan.fxml");
	}
}