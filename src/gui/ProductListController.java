package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import gui.listeners.DataChangeListener;
import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Product;
import model.services.ProductService;

public class ProductListController implements Initializable, DataChangeListener {

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
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Product obj = new Product();
		createDialogForm(obj, "/gui/ProductForm.fxml", parentStage);
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
	
		private void createDialogForm(Product obj, String absoluteName, Stage parentStage) {	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			ProductFormController controller = loader.getController();
			controller.setProduct(obj);
			controller.setProductService(new ProductService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Preencha os dados do produto");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
		@Override
		public void onDataChanged() {
			updateTableView();
		}
}