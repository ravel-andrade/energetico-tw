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
import model.entities.Product;
import model.services.ProductService;

public class ProductListController implements Initializable {

	private ProductService service;
	
	@FXML
	private TableView<Product> tableViewProduct;

	@FXML
	private TableColumn<Product, Integer> tableColumnId;

	@FXML
	private TableColumn<Product, String> tableColumnName;
	
	@FXML
	private TableColumn<Product, String> tableColumnValue;

	@FXML
	private Button btNew;
	
	private ObservableList<Product> obsList;

	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewActioon");
	}

	public void setProductService(ProductService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		tableColumnValue.setCellValueFactory(new PropertyValueFactory<>("Value"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewProduct.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		List<Product> list = service.findAll();
		
		obsList = FXCollections.observableArrayList(list);
		tableViewProduct.setItems(obsList);
	}
}