package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Sale;
import model.services.SaleService;
import model.services.ProductService;

public class SaleListController implements Initializable, DataChangeListener {

	private SaleService service;

	@FXML
	private TableView<Sale> tableViewSale;

	@FXML
	private TableColumn<Sale, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Sale, Integer> tableColumnAmount;
	
	@FXML
	private TableColumn<Sale, Double> tableColumnTaxICMS;
	
	@FXML
	private TableColumn<Sale, Double> tableColumnDiscount;

	@FXML
	private TableColumn<Sale, Double> tableColumnTaxIPI;
	
	@FXML
	private TableColumn<Sale, Double> tableColumnTaxPIS;
	
	@FXML
	private TableColumn<Sale, Double> tableColumnTaxCOFINS;
	
	@FXML
	private TableColumn<Sale, Double> tableColumnTotal;
	
	@FXML
	private TableColumn<Sale, String> tableColumnName;

	@FXML
	private TableColumn<Sale, Sale> tableColumnEDIT;

	@FXML
	private TableColumn<Sale, Sale> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Sale> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Sale obj = new Sale();
		createDialogForm(obj, "/gui/SaleForm.fxml", parentStage);
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
		tableColumnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		tableColumnDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
		Utils.formatTableColumnDouble(tableColumnDiscount, 2);
		tableColumnTaxICMS.setCellValueFactory(new PropertyValueFactory<>("taxICMS"));
		Utils.formatTableColumnDouble(tableColumnTaxICMS, 2);
		tableColumnTaxIPI.setCellValueFactory(new PropertyValueFactory<>("taxIPI"));
		Utils.formatTableColumnDouble(tableColumnTaxIPI, 2);
		tableColumnTaxPIS.setCellValueFactory(new PropertyValueFactory<>("taxPIS"));
		Utils.formatTableColumnDouble(tableColumnTaxPIS, 2);
		tableColumnTaxCOFINS.setCellValueFactory(new PropertyValueFactory<>("taxCOFINS"));
		Utils.formatTableColumnDouble(tableColumnTaxCOFINS, 2);
		tableColumnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
		Utils.formatTableColumnDouble(tableColumnTotal, 2);

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
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Sale obj, String absoluteName, Stage parentStage) {
		try {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
		Pane pane = loader.load();

		SaleFormController controller = loader.getController();
		controller.setSale(obj);
		controller.setServices(new SaleService(), new ProductService());
		controller.loadAssociatedObjects();
		controller.subscribeDataChangeListener(this);
		controller.updateFormData();

		Stage dialogStage = new Stage();
		dialogStage.setTitle("Preencha os dados da venda");
		dialogStage.setScene(new Scene(pane));
		dialogStage.setResizable(false);
		dialogStage.initOwner(parentStage);
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Sale, Sale>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Sale obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SaleForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Sale, Sale>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Sale obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Sale obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que irá deletar?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}