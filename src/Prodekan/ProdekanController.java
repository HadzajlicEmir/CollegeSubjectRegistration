package Prodekan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;
import application.Main;

public class ProdekanController {

	@FXML
	private Button azurirajPredmet;
	@FXML
	private Button azurirajNastavnika;
	@FXML
	private Button periodRegistracije;
	@FXML
	private Button logOut;
	@FXML
	private Button pregledZahtjeva;
	@FXML
	private Button buttonPregledStud;

	public void userLogOut(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("/application/Sample.fxml");

	}

	public void goToAzurirajPredmet(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("/Prodekan/AzurirajPredmet.fxml");

	}

	public void goToAzurirajNastavnika(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("/Prodekan/AzurirajNastavnika.fxml");
	}

	public void goToPeriodRegistracije(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("/Prodekan/PeriodRegistracije.fxml");
	}

	public void goToPregledZahtjeva(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("/Prodekan/PregledajZahtjev.fxml");
	}

	public void goToPregledStudenata(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("/Prodekan/PregledStudenata.fxml");
	}

}