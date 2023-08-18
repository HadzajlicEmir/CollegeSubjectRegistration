package Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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

public class OdaberiProdekana implements Initializable {

	@FXML
	private ListView<String> listView;
	@FXML
	private Button buttonDodaj;

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
				listView.getItems().add(nastavnici.get(i));
				++i;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void odaberiProdekana(String sifraNastavnika) throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("UPDATE nastavnik SET prodekanBool=false WHERE prodekanBool=true");

		String sql1 = "UPDATE nastavnik SET prodekanBool=true WHERE sifNastavnik = ?";

		PreparedStatement statement1 = connection.prepareStatement(sql1);

		statement1.setInt(1, Integer.parseInt(sifraNastavnika));
		statement1.executeUpdate();
		String sql2 = "UPDATE users SET status=2  WHERE sifra IN (SELECT sifNastavnik FROM nastavnik WHERE prodekanBool=false)";
		PreparedStatement statement2 = connection.prepareStatement(sql2);

		statement2.executeUpdate();

		String sql3 = "UPDATE users SET status=1  WHERE sifra IN (SELECT sifNastavnik FROM nastavnik WHERE prodekanBool=true)";

		PreparedStatement statement3 = connection.prepareStatement(sql3);
		statement3.executeUpdate();
		connection.close();
	}

	public void showDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Uspjesno ste dodali prodekana");
		alert.showAndWait();
	}

	public void errorDialog() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Odaberite nastavnika iz liste!");
		alert.showAndWait();
	}

	public void backToAdmin(ActionEvent event) throws IOException {
		try {
			odaberiProdekana(listView.getSelectionModel().getSelectedItem().substring(0, 4));
			Main m = new Main();
			showDialog();
			m.changeScene("/Admin/Admin.fxml");
		} catch (SQLException e) {
			errorDialog();
		} catch (NullPointerException e) {
			errorDialog();
		}

	}

	public void backToMainAdmin() throws IOException {
		Main m = new Main();
		m.changeScene("/Admin/Admin.fxml");
	}

}