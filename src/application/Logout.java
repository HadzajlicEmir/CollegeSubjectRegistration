package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class Logout {

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
    

    public void userLogOut(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("Sample.fxml");

    }
    
    public void dodajStudenta(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("DodajStudenta.fxml");

    }
    
    public void dodajNastavnika(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("DodajNastavnika.fxml");

    }
    public void dodajPredmet(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("DodajPredmet.fxml");
    }
    public void odaberiProdekana(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("OdaberiProdekana.fxml");
    }
}