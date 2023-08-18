package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Student.Student;

import java.io.IOException;

import javafx.event.*;
//Controller - kontrolira scenu

//--module-path "/usr/share/java/openjfx-20.0.1_linux-x64_bin-sdk/javafx-sdk-20.0.1/lib" --add-modules javafx.controls,javafx.fxml
public class LogIn {
	static int status;
	public static Student student = new Student();
	public static Nastavnik nastavnik = new Nastavnik();

	public LogIn() {

	}

	@FXML
	private Button loginButton;
	@FXML
	private TextField emailTextField;
	@FXML
	private PasswordField passwordTextField;

	public void userLogIn(ActionEvent event) throws IOException {
		checkLogin();
	}

	private void checkLogin() throws IOException {
		Main m = new Main();
		String email1 = emailTextField.getText().toString();
		String password1 = passwordTextField.getText().toString();
		if (validateLogin(email1, password1))
			if (status == 0) {
				m.changeScene("/Admin/Admin.fxml");
			} else if (status == 1) {
				m.changeScene("/Prodekan/Prodekan.fxml");
			} else if (status == 2) {
				m.changeScene("/Nastavnik/Nastavnik.fxml");
			} else if (status == 3) {
				m.changeScene("/Student/Student.fxml");
			}
	}

	public static void showDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Pogresan email ili password");
		alert.showAndWait();
	}

	static boolean validateLogin(String email, String password) {
		try {
			// Uspostavi konekciju sa bazom podataka (MySQL)
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

			// Pripremi SQL upit
			String sql = "SELECT * FROM users WHERE email = ? AND pass = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, email);
			statement.setString(2, password);

			// Izvrši upit i dobavi rezultate
			ResultSet resultSet = statement.executeQuery();

			// Ako je rezultat pronađen, korisnik postoji
			while (resultSet.next()) {
				if (resultSet.getInt(4) == 3) {
					if (resultSet.getString(2).equals(email) && resultSet.getString(3).equals(password)) {
						student.setBrIndexa(resultSet.getInt(1));
						System.out.println("IMA USER");
						String sql2 = "SELECT * FROM student INNER JOIN users ON sifra=indeks WHERE" + " email=?";
						PreparedStatement statement2 = connection.prepareStatement(sql2);
						statement2.setString(1, email);
						ResultSet resultSet2 = statement2.executeQuery();

						while (resultSet2.next()) {
							student.setBrIndexa(resultSet2.getInt(1));
							student.setIme(resultSet2.getString(2));
							student.setPrezime(resultSet2.getString(3));
							student.setOstvarenihECTS(resultSet2.getInt(4));
							student.setStatus(resultSet2.getString(5));
						}
						status = resultSet.getInt(4);
						return true;
					}
				} else if (resultSet.getInt(4) == 2) {
					if (resultSet.getString(2).equals(email) && resultSet.getString(3).equals(password)) {
						nastavnik.setSifNastavnik(resultSet.getInt(1));
						System.out.println("IMA USER");
						String sql2 = "SELECT * FROM nastavnik INNER JOIN users ON sifra=sifNastavnik WHERE"
								+ " email=?";
						PreparedStatement statement2 = connection.prepareStatement(sql2);
						statement2.setString(1, email);
						ResultSet resultSet2 = statement2.executeQuery();
						while (resultSet2.next()) {
							nastavnik.setSifNastavnik(resultSet2.getInt(1));
							nastavnik.setIme(resultSet2.getString(2));
							nastavnik.setPrezime(resultSet2.getString(3));
							nastavnik.setZvanje(resultSet2.getString(4));
							nastavnik.setProdekanBool(resultSet2.getBoolean(5));
						}
						status = resultSet.getInt(4);
						return true;
					}
				}
				status = resultSet.getInt(4);
				return true;
			}

			// Zatvori konekciju i resurse
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("NEMA USER");
		showDialog();
		return false;
	}
}