package Admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class DodajPredavaca implements Initializable {

	@FXML
	private ComboBox<String> predavacCB;

	@FXML
	private ComboBox<String> predmetCB;
	@FXML
	private Button buttonDodaj;
	@FXML
	private Button backButton;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

			String sql = "SELECT * FROM nastavnik";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				predavacCB.getItems().add(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
			}

			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery("select * from predmet ");

			while (rs.next()) {

				predmetCB.getItems().add(rs.getString(1) + " " + rs.getString(2));

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void dodajPredavaca() throws SQLException, IOException {
		String predavac = predavacCB.getValue();
		String predmet = predmetCB.getValue();
		if (predavac == null || predmet == null) {
			errorDialog();
		}

		else {
			String sifraPredavaca[] = predavac.split(" ");
			predavac = sifraPredavaca[0];

			String sifraPredmeta[] = predmet.split(" ");
			predmet = sifraPredmeta[0];

			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
			String sql1 = "INSERT INTO predavac (sifNastavnik, sifPred, nosilac) VALUES (?, ?, false)";
			PreparedStatement statement1 = connection.prepareStatement(sql1);

			statement1.setString(1, predavac);
			statement1.setString(2, predmet);

			int rowsInserted = statement1.executeUpdate();

			statement1.close();
			connection.close();

			if (rowsInserted > 0) {
				showDialog();
				System.out.println("uspjeh.");
				backToMainAdmin();
			} else {
				System.out.println(":((((((");
			}
		}

	}

	public void errorDialog() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Potrebno je popuniti sva polja!");
		alert.showAndWait();
	}

	public void showDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Uspjesno ste dodali predavaca!");
		alert.showAndWait();
	}

	public void backToMainAdmin() throws IOException {
		Main m = new Main();
		m.changeScene("/Admin/Admin.fxml");
	}

}