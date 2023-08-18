package Prodekan;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.Main;

public class AzurirajPredmetController implements Initializable {

	@FXML
	private Button backButton;
	@FXML
	private Button buttonOdaberi;
	@FXML
	private Button buttonAzuriraj;
	@FXML
	private ListView<String> listaPredmeta;
	@FXML
	private TextField nazivPredmetaTF;
	@FXML
	private ComboBox<String> preduslov1;
	@FXML
	private ComboBox<String> preduslov2;
	@FXML
	private ComboBox<String> preduslov3;
	@FXML
	private ComboBox<String> preduslov4;
	@FXML
	private ComboBox<String> nosiocCB;
	@FXML
	private TextField pretragaPredmetaTF;
	
	public void pretraziPredmete() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		
		String query = ("SELECT predavac.sifPred,nazPred, imeNast,prezNast,zvanje FROM predavac INNER JOIN "
				+ "nastavnik on nastavnik.sifNastavnik=predavac.sifNastavnik INNER JOIN predmet ON "
				+ "predmet.sifPred=predavac.sifPred where nosilac=true AND (nazPred LIKE '%" + pretragaPredmetaTF.getText() + "%'" + "OR imeNast LIKE '%" + pretragaPredmetaTF.getText() + "%'" + "OR prezNast LIKE '%" + pretragaPredmetaTF.getText() + "%'" + "OR predavac.sifPred LIKE '%" + pretragaPredmetaTF.getText() + "%')");
		PreparedStatement stmt = connection.prepareStatement(query);
	
		ResultSet rs = stmt.executeQuery();
		listaPredmeta.getItems().clear();
		while (rs.next()) {
			listaPredmeta.getItems().add(rs.getString(1) + ", " + rs.getString(2) + ", " + rs.getString(3) + " "
					+ rs.getString(4) + ", " + rs.getString(5));
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

			// nosioc

			Statement stmt4 = connection.createStatement();
			ResultSet rs4 = stmt4.executeQuery("select * from nastavnik");

			while (rs4.next()) {

				ArrayList<String> nastavnici = new ArrayList<String>();
				int i = 0;

				while (rs4.next()) {
					nastavnici.add(rs4.getString(1) + " " + rs4.getString(2) + " " + rs4.getString(3));
					nosiocCB.getItems().add(nastavnici.get(i));

					++i;
				}
			}

			// preduslovi
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM predmet");

			while (rs.next()) {
				listaPredmeta.getItems().add(rs.getString(1) + " " + rs.getString(2));

				// combo boxes - preduslovi
				Statement stmt1 = connection.createStatement();
				ResultSet rs1 = stmt1.executeQuery("select * from predmet");
				ArrayList<String> predmeti = new ArrayList<String>();
				int i = 0;

				while (rs1.next()) {
					predmeti.add(rs1.getString(1) + " " + rs1.getString(2));
					preduslov1.getItems().add(predmeti.get(i));
					preduslov2.getItems().add(predmeti.get(i));
					preduslov3.getItems().add(predmeti.get(i));
					preduslov4.getItems().add(predmeti.get(i));

					++i;
				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void prikaziPredmet() throws SQLException {
		System.out.println("1((((((((((((((((");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");

		String temp = listaPredmeta.getSelectionModel().getSelectedItem();
		String str[] = temp.split(" ");

		String sql = "SELECT sifPred, nazPred, preduslov1, preduslov2, preduslov3, preduslov4 FROM predmet WHERE sifPred=? ";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, str[0]);
		ResultSet rs = statement.executeQuery();
		List<String> listaPredmetaStringovi = listaPredmeta.getItems();
		

		while (rs.next()) {
			nazivPredmetaTF.setText(rs.getString(2));
			
			// prvi preduslov
			if (rs.getString(3) != null) {
				List<String> searchResults = listaPredmetaStringovi.stream()
				        .filter(predmet -> {
							try {
								return predmet.contains(rs.getString((3)));
							} catch (SQLException e) {
								e.printStackTrace();
								return false;
							}
						})
				        .collect(Collectors.toList());
				if(searchResults != null) {
					String item = searchResults.get(0);
					preduslov1.setValue(item);
				}
				}else {
					preduslov1.setValue("");
				}
		
			//
			
				if (rs.getString(4) != null) {
			List<String> searchResults = listaPredmetaStringovi.stream()
			        .filter(predmet -> {
						try {
							return predmet.contains(rs.getString((4)));
						} catch (SQLException e) {
							e.printStackTrace();
							return false;
						}
					})
			        .collect(Collectors.toList());
			if(searchResults != null) {
				String item = searchResults.get(0);
				preduslov2.setValue(item);
			}
			}else {
				preduslov2.setValue("");
			}
	
				if (rs.getString(5) != null) {
					List<String> searchResults = listaPredmetaStringovi.stream()
					        .filter(predmet -> {
								try {
									return predmet.contains(rs.getString((5)));
								} catch (SQLException e) {
									e.printStackTrace();
									return false;
								}
							})
					        .collect(Collectors.toList());
					if(searchResults != null) {
						String item = searchResults.get(0);
						preduslov3.setValue(item);
					}
					}else {
						preduslov3.setValue("");
					}
			
				if (rs.getString(6) != null) {
					List<String> searchResults = listaPredmetaStringovi.stream()
					        .filter(predmet -> {
								try {
									return predmet.contains(rs.getString((6)));
								} catch (SQLException e) {
									e.printStackTrace();
									return false;
								}
							})
					        .collect(Collectors.toList());
					if(searchResults != null) {
						String item = searchResults.get(0);
						preduslov4.setValue(item);
					}
					} 
					else {
						preduslov4.setValue("");
					}
			
			
		}

	}

	public void azurirajPredmet(String predusl1, String predusl2, String predusl3, String predusl4, String nosioc)
			throws SQLException {
		String temp = listaPredmeta.getSelectionModel().getSelectedItem();
		String str[] = temp.split(" ");
		String var = str[0];
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza", "root", "password");
		String sql1 = "UPDATE predmet SET preduslov1=? , preduslov2=?, preduslov3=?, preduslov4=? WHERE sifPred=?";
		PreparedStatement statement1 = connection.prepareStatement(sql1);

		statement1.setString(1, predusl1);
		statement1.setString(2, predusl2);
		statement1.setString(3, predusl3);
		statement1.setString(4, predusl4);
		statement1.setString(5, var);
		statement1.executeUpdate();

		String sql = "UPDATE predavac SET nosilac=false WHERE nosilac=true and sifPred=?";
		PreparedStatement statement = connection.prepareStatement(sql);
		statement.setString(1, var);
		statement.executeUpdate();

		String sql2 = "INSERT INTO predavac (sifNastavnik, sifPred, nosilac) VALUES (?, ?, true)";
		PreparedStatement statement2 = connection.prepareStatement(sql2);
		statement2.setInt(1, Integer.parseInt(nosioc));

		statement2.setString(2, var);

		statement2.executeUpdate();

		statement1.close();
		statement.close();
		statement2.close();
		connection.close();
	}

	public void backToProdekanAfterUpdate(ActionEvent event) throws IOException {
		String predusl1, predusl2, predusl3, predusl4, nosioc;

		if (preduslov1.getValue() == null) {
			predusl1 = null;
		} else {
			String temp1 = preduslov1.getValue();
			String str1[] = temp1.split(" ");
			predusl1 = str1[0];
		}

		if (preduslov2.getValue() == null) {
			predusl2 = null;
		} else {
			String temp2 = preduslov2.getValue();
			String str2[] = temp2.split(" ");
			predusl2 = str2[0];
		}

		if (preduslov3.getValue() == null) {
			predusl3 = null;
		} else {
			String temp3 = preduslov3.getValue();
			String str3[] = temp3.split(" ");

			predusl3 = str3[0];
		}

		if (preduslov4.getValue() == null) {

			predusl4 = null;
		} else {
			String temp4 = preduslov4.getValue();
			String str4[] = temp4.split(" ");
			predusl4 = str4[0];
		}

		if (nosiocCB.getValue() == null) {

			nosioc = null;
		} else {
			String temp5 = nosiocCB.getValue();
			String str5[] = temp5.split(" ");
			nosioc = str5[0];
		}

		try {
			if (nosioc == null) {
				throw new NullPointerException();
			}
			azurirajPredmet(predusl1, predusl2, predusl3, predusl4, nosioc);
			showDialog();
			Main m = new Main();
			m.changeScene("/Prodekan/Prodekan.fxml");
		} catch (SQLException e) {
			errorDialog();
		} catch (NullPointerException e) {
			errorDialog();
		}

	}

	public void errorDialog() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Greska!");
		alert.setHeaderText(null);
		alert.setContentText("Neispravan unos");
		alert.showAndWait();
	}

	public void showDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Uspjesno ste azurirali predmet!");
		alert.showAndWait();
	}

	public void backToProdekan() throws IOException {
		Main m = new Main();
		m.changeScene("/Prodekan/Prodekan.fxml");
	}

}