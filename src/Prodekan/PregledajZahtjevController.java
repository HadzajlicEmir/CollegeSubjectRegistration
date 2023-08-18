package Prodekan;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
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
import java.util.ResourceBundle;

import application.Main;

public class PregledajZahtjevController implements Initializable {

	@FXML
	private ListView<String> listaZahtjeva;
	@FXML
	private Button buttonOdobri;
	@FXML
	private Button buttonPrikazi;
	@FXML
	private Button buttonOdbij;
	@FXML
	private Button backButton;
	@FXML
	private TextArea zahtjevTF;

	String predmetBrisanje = "";
	String predmetDodavanje = "";

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT zahtjevZaPromjenuRegPred.indeks, imeStud, prezStud, sifPredBris,sifPredDod FROM zahtjevZaPromjenuRegPred "
							+ "INNER JOIN student ON zahtjevZaPromjenuRegPred.indeks=student.indeks WHERE odobreno=2");

			while (rs.next()) {
				listaZahtjeva.getItems().add(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " "
						+ rs.getString(4) + " " + rs.getString(5));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void prikaziZahtjev() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		String temp = listaZahtjeva.getSelectionModel().getSelectedItem();
		String str[] = temp.split(" ");

		int var = Integer.parseInt(str[0]);
		String sifPredBris = str[3];
		String sifPredDod = str[4];

		String sql = "SELECT porukaStudenta FROM zahtjevZaPromjenuRegPred WHERE indeks=? "
				+ "AND sifPredBris=? AND sifPredDod=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, var);
		statement.setString(2, sifPredBris);
		statement.setString(3, sifPredDod);
		ResultSet rs = statement.executeQuery();

		while (rs.next()) {
			zahtjevTF.setText(rs.getString(1));
		}

	}

	Stage primaryStage = new Stage();

	public void showDialog(Stage primaryStage) throws SQLException {

		primaryStage.setTitle("Poruka za studenta");
		// showDialog(primaryStage);

		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Želite li unijeti poruku za studenta?");

		TextField messageTextField = new TextField();

		Button sendButton = new Button("Pošalji");
		sendButton.setOnAction(event -> {
			String message = messageTextField.getText();

			String temp = listaZahtjeva.getSelectionModel().getSelectedItem();
			String str[] = temp.split(" ");
			int var = Integer.parseInt(str[0]);

			try {
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root",
						"password");
				String sql = "UPDATE zahtjevZaPromjenuRegPred SET porukaProdekana=?  WHERE indeks=?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, message);
				statement.setInt(2, var);
				statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Unesena poruka: " + message);
			dialog.setResult(message);
			dialog.close();
		});

		Button cancelButton = new Button("Odustani");
		cancelButton.setOnAction(event -> {
			dialog.close();
		});

		GridPane gridPane = new GridPane();
		gridPane.setHgap(40);
		gridPane.setVgap(40);
		gridPane.setPadding(new Insets(60));

		gridPane.add(messageTextField, 0, 0, 4, 2);
		gridPane.add(sendButton, 0, 2);
		gridPane.add(cancelButton, 2, 2);

		dialog.getDialogPane().setContent(gridPane);

		dialog.showAndWait();
	}

	public void odobriZahtjev() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		String temp = listaZahtjeva.getSelectionModel().getSelectedItem();
		String str[] = temp.split(" ");

		int var = Integer.parseInt(str[0]);
		String sifPredBris = str[3];
		String sifPredDod = str[4];
		showDialog(primaryStage);

		int index = listaZahtjeva.getSelectionModel().getSelectedIndex();
		listaZahtjeva.getItems().remove(index);

		String sql = "UPDATE zahtjevZaPromjenuRegPred SET odobreno=1 WHERE indeks=? AND sifPredBris=?"
				+ " AND sifPredDod=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, var);
		statement.setString(2, sifPredBris);
		statement.setString(3, sifPredDod);
		statement.executeUpdate();

		sql = "UPDATE registrovaniPredmet SET sifPred=? WHERE indeks=? AND sifPred=?   ";
		statement = connection.prepareStatement(sql);
		statement.setString(1, sifPredDod);
		statement.setInt(2, var);
		statement.setString(3, sifPredBris);
		statement.executeUpdate();

	}

	public void backToProdekan() throws IOException {
		Main m = new Main();
		m.changeScene("/Prodekan/Prodekan.fxml");
	}

	public void odbijZahtjev() throws SQLException {

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		String temp = listaZahtjeva.getSelectionModel().getSelectedItem();
		String str[] = temp.split(" ");

		int var = Integer.parseInt(str[0]);
		String sifPredBris = str[3];
		String sifPredDod = str[4];
		showDialog(primaryStage);

		int index = listaZahtjeva.getSelectionModel().getSelectedIndex();
		listaZahtjeva.getItems().remove(index);

		String sql = "UPDATE zahtjevZaPromjenuRegPred SET odobreno=1 WHERE indeks=? AND sifPredBris=?"
				+ " AND sifPredDod=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, var);
		statement.setString(2, sifPredBris);
		statement.setString(3, sifPredDod);
		statement.executeUpdate();
	}

}