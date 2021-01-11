package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Sale;
import model.services.SaleService;

public class SaleListController implements Initializable {

	private SaleService service;
	
	@FXML
	private TableView<Sale> tableViewSale;

	@FXML
	private TableColumn<Sale, Integer> tableColumnId;

	@FXML
	private TableColumn<Sale, String> tableColumnName;

	@FXML
	private Button btNew;
	
	private ObservableList<Sale> obsList;

	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}

	public void setSaleService(SaleService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSale.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Sale> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewSale.setItems(obsList);
	}
}