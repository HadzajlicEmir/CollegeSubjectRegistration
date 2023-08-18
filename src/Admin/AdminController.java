package Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import application.Main;

public class AdminController {

	@FXML
	private Button logOut;
	@FXML
	private Button dodajStudenta;
	@FXML
	private Button dodajPredmet;
	@FXML
	private Button dodajNastavnika;
	@FXML
	private Button odaberiProdekana;
	@FXML
	private Button dodajPredavaca;

	public void userLogOut(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("/application/Sample.fxml");

	}

	public void dodajStudenta(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("/Admin/DodajStudenta.fxml");

	}

	public void dodajNastavnika(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("/Admin/DodajNastavnika.fxml");

	}

	public void dodajPredmet(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("/Admin/DodajPredmet.fxml");
	}

	public void odaberiProdekana(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("/Admin/OdaberiProdekana.fxml");
	}

	public void dodajPredavaca(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("/Admin/DodajPredavaca.fxml");
	}

}