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
import application.Ocjena;
import application.Predmet;

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
	private TextArea zahtjevTF;
	@FXML
	private Button back;
	@FXML
	private ListView<String> uspjehNaPreduslov;
	@FXML
	private ListView<String> ostaliPredmeti;

	public Nastavnik nastavnik = application.LogIn.nastavnik;
	public Student studentTemp = new Student();
	ArrayList<Predmet> predmeti = new ArrayList<Predmet>();
	ArrayList<Ocjena> ocjene = new ArrayList<Ocjena>();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
			PreparedStatement stmt = connection.prepareStatement(
					"SELECT zahtjevZaSlusanjeBezPreduslova.indeks, imeStud, prezStud, predavac.sifPred FROM "
							+ "zahtjevZaSlusanjeBezPreduslova " + "INNER JOIN student "
							+ "ON zahtjevZaSlusanjeBezPreduslova.indeks=student.indeks INNER JOIN predavac ON zahtjevZaSlusanjeBezPreduslova.sifPred=predavac.sifPred WHERE odobreno=2 AND sifNastavnik=?");
			stmt.setInt(1, nastavnik.getSifNastavnik());
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				listaZahtjeva.getItems()
						.add(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getString(4));
			}

			Statement stm = connection.createStatement();
			rs = stm.executeQuery("select * from predmet ");

			while (rs.next()) {
				Predmet predmet = new Predmet();
				predmet.setSifPred(rs.getString(1));
				predmet.setNaziv(rs.getString(2));
				predmet.setSemestar(rs.getInt(3));
				predmet.setEcts(rs.getInt(4));
				predmet.setPreduslovi(rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
				predmeti.add(predmet);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<String> findPreduslove(String s) {
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < predmeti.size(); ++i) {
			if (predmeti.get(i).getSifPred().equals(s)) {
				temp = predmeti.get(i).getPreduslovi();
			}
		}
		return temp;
	}

	public void prikaziZahtjev() throws SQLException {
		ocjene = new ArrayList<Ocjena>();
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		String temp = listaZahtjeva.getSelectionModel().getSelectedItem();
		String str[] = temp.split(" ");

		String predmet = "";
		int var = Integer.parseInt(str[0]);

		String sql = "SELECT porukaStudenta,sifPred FROM zahtjevZaSlusanjeBezPreduslova WHERE indeks=? ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, var);
		ResultSet rs = statement.executeQuery();

		if (rs.next()) {
			zahtjevTF.setText(rs.getString(1));
			predmet = rs.getString(2);
		}

		sql = "SELECT indeks,ocjena.sifPred,ocjena,nazPred FROM ocjena "
				+ " INNER JOIN predmet ON ocjena.sifPred=predmet.sifPred WHERE indeks=? AND akademskaGodina<2024 "
				+ "AND ocjena>5";
		statement = connection.prepareStatement(sql);
		statement.setInt(1, var);
		rs = statement.executeQuery();
		while (rs.next()) {
			ostaliPredmeti.getItems().add(rs.getString(2) + " " + rs.getString(4));
		}
		sql = "SELECT * FROM ocjena WHERE indeks=? AND akademskaGodina<2024";
		statement = connection.prepareStatement(sql);
		statement.setInt(1, var);
		rs = statement.executeQuery();
		while (rs.next()) {
			Ocjena ocjena = new Ocjena();
			ocjena.setIndeks(rs.getInt(1));
			ocjena.setSifNastavnik(rs.getInt(2));
			ocjena.setSifPred(rs.getString(3));
			ocjena.setOcjena(rs.getInt(4));
			ocjene.add(ocjena);
		}
		System.out.println(predmet);
		ArrayList<String> predusloviTemp = findPreduslove(predmet);
		if (predusloviTemp.get(0) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(0))) {
					uspjehNaPreduslov.getItems()
							.add("Predmet:" + ocjene.get(i).getSifPred() + ", Ocjena: " + ocjene.get(i).getOcjena());
				}
			}
		}

		if (predusloviTemp.get(1) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(1))) {
					uspjehNaPreduslov.getItems()
							.add("Predmet:" + ocjene.get(i).getSifPred() + ", Ocjena: " + ocjene.get(i).getOcjena());
				}
			}
		}

		if (predusloviTemp.get(2) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(2))) {

					uspjehNaPreduslov.getItems()
							.add("Predmet:" + ocjene.get(i).getSifPred() + ", Ocjena: " + ocjene.get(i).getOcjena());
				}
			}
		}

		if (predusloviTemp.get(3) != null) {
			for (int i = 0; i < ocjene.size(); ++i) {
				if (ocjene.get(i).getSifPred().equals(predusloviTemp.get(3))) {

					uspjehNaPreduslov.getItems()
							.add("Predmet:" + ocjene.get(i).getSifPred() + ", Ocjena: " + ocjene.get(i).getOcjena());
				}
			}
		}

	}

	Stage primaryStage = new Stage();

	public void showDialog(Stage primaryStage) throws SQLException {

		primaryStage.setTitle("Poruka za studenta");

		Dialog<String> dialog = new Dialog<>();
		dialog.setTitle("Želite li unijeti poruku za studenta?");

		// Postavljanje text polja za unos poruke
		TextField messageTextField = new TextField();

		Button sendButton = new Button("Pošalji");
		sendButton.setOnAction(event -> {
			String message = messageTextField.getText();

			String temp = listaZahtjeva.getSelectionModel().getSelectedItem();

			String str[] = temp.split(" ");
			int var = Integer.parseInt(str[0]);
			// System.out.println(var);

			try {
				Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root",
						"password");
				String sql = "UPDATE zahtjevZaSlusanjeBezPreduslova SET porukaNastavnika=?  WHERE indeks=?";
				PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, message);
				statement.setInt(2, var);
				statement.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Ovdje možete dodati logiku za obradu unesene poruke
			System.out.println("Unesena poruka: " + message);
			dialog.setResult(message);
			dialog.close();
		});

		Button cancelButton = new Button("Odustani");
		cancelButton.setOnAction(event -> {
			dialog.close();
		});

		// Postavljanje rasporeda elemenata
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

		showDialog(primaryStage);

		String temp = listaZahtjeva.getSelectionModel().getSelectedItem();
		String str[] = temp.split(" ");
		// indeks
		int var = Integer.parseInt(str[0]);
		System.out.println(var);

		int index = listaZahtjeva.getSelectionModel().getSelectedIndex();
		listaZahtjeva.getItems().remove(index);

		String sql = "UPDATE zahtjevZaSlusanjeBezPreduslova SET odobreno=1 WHERE indeks=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, var);
		statement.executeUpdate();

		sql = "SELECT sifPred FROM zahtjevZaSlusanjeBezPreduslova WHERE indeks=? ";
		statement = connection.prepareStatement(sql);
		statement.setInt(1, var);
		ResultSet rs = statement.executeQuery();
		String tmp = "";
		while (rs.next()) {
			tmp = rs.getString(1);
		}

		sql = "INSERT INTO registrovaniPredmet(sifPred,indeks,akademskaGodina,confirm,status)" + " VALUES(?,?,?,?,?)  ";
		statement = connection.prepareStatement(sql);
		statement.setString(1, tmp);
		statement.setInt(2, var);
		statement.setInt(3, 2024);
		statement.setBoolean(4, false);
		statement.setString(5, "prvi put");
		statement.executeUpdate();

	}

	public void odbijen() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Svaka cast!");
		alert.setHeaderText(null);
		alert.setContentText("Odbili ste zahtjev!");
		alert.showAndWait();
	}

	public void odbijZahtjev() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		String temp = listaZahtjeva.getSelectionModel().getSelectedItem();
		String str[] = temp.split(" ");
		showDialog(primaryStage);

		// indeks studenta
		int var = Integer.parseInt(str[0]);
		System.out.println(var);

		int index = listaZahtjeva.getSelectionModel().getSelectedIndex();
		listaZahtjeva.getItems().remove(index);

		String sql = "UPDATE zahtjevZaSlusanjeBezPreduslova SET odobreno=1 WHERE indeks=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setInt(1, var);
		statement.executeUpdate();

	}

	public void back() throws IOException {
		Main m = new Main();
		m.changeScene("/Nastavnik/Nastavnik.fxml");
	}
}