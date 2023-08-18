package Prodekan;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import application.Main;

public class PeriodRegistracijeController {

	@FXML
	private Button dodaj;
	@FXML
	private DatePicker pocetakReg;
	@FXML
	private DatePicker krajReg;

	public void spremiDatume() throws SQLException, IOException {

		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		String sql = "UPDATE periodRegistracije SET pocetak=?,kraj=? WHERE sifra=1";

		PreparedStatement statement = connection.prepareStatement(sql);

		String strPocetak = pocetakReg.getValue().toString();
		Date pocetakDate = Date.valueOf(strPocetak);
		String strKraj = krajReg.getValue().toString();
		Date krajDate = Date.valueOf(strKraj);
		System.out.println(strKraj);

		statement.setDate(1, pocetakDate);
		statement.setDate(2, krajDate);

		statement.executeUpdate();

		sql = "SELECT * FROM periodRegistracije";
		ResultSet rs = statement.executeQuery(sql);

		while (rs.next()) {
			System.out.println(rs.getString(2) + " " + rs.getString(3));
		}

		statement.close();
		connection.close();

		backToProdekan();

	}

	public void backToProdekan() throws IOException {
		Main m = new Main();
		m.changeScene("/Prodekan/Prodekan.fxml");
	}

}