package Prodekan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.Main;

public class AzurirajNastavnikaController implements Initializable {

	@FXML
	private Button backButton;
	@FXML
	private Button buttonOdaberi;
	@FXML
	private Button buttonAzuriraj;
	@FXML
	private ListView<String> listaNastavnika;
	@FXML
	private TextField imeTF;
	@FXML
	private TextField prezimeTF;
	@FXML
	private TextField zvanjeTF;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM nastavnik");

			while (rs.next()) {
				listaNastavnika.getItems().add(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void prikaziNastavnika() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		String temp = listaNastavnika.getSelectionModel().getSelectedItem();
		String str[] = temp.split(" ");

		String sql = "SELECT imeNast, prezNast FROM nastavnik WHERE sifNastavnik=? ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, str[0]);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {

			imeTF.setText(rs.getString(1));
			prezimeTF.setText(rs.getString(2));

		}

	}

	public void azurirajNastavnik(String zvanje) throws SQLException {
		String temp = listaNastavnika.getSelectionModel().getSelectedItem();
		String str[] = temp.split(" ");
		String var = str[0];
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		String sql1 = "UPDATE nastavnik SET zvanje=? WHERE sifNastavnik=?";

		PreparedStatement statement1 = connection.prepareStatement(sql1);

		statement1.setString(1, zvanje);
		statement1.setString(2, var);

		// Izvršavanje upita
		statement1.executeUpdate();

		statement1.close();
		connection.close();
	}

	public void errorDialog() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Neispravan unos");
		alert.showAndWait();
	}

	public void backToProdekanAfterUpdate(ActionEvent event) throws IOException {

		String zvanje = zvanjeTF.getText().toString();
		try {

			if ((imeTF.getText() == "") || (prezimeTF.getText() == "") || (zvanjeTF.getText() == "")) {
				throw new NullPointerException();
			}

			azurirajNastavnik(zvanje);
			showDialog();
			backToProdekan();
		} catch (SQLException e) {
			errorDialog();
		} catch (NullPointerException e) {
			errorDialog();
		}

	}

	public void showDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Uspjesno ste azurirali nastavnika");
		alert.showAndWait();
	}

	public void backToProdekan() throws IOException {
		Main m = new Main();
		m.changeScene("/Prodekan/Prodekan.fxml");
	}

}