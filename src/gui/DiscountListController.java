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
import model.entities.Discount;
import model.services.DiscountService;

public class DiscountListController implements Initializable, DataChangeListener {

	private DiscountService service;
	
	@FXML
	private TableView<Discount> tableViewDiscount;

	@FXML
	private TableColumn<Discount, Integer> tableColumnId;

	@FXML
	private TableColumn<Discount, Integer> tableColumnAmount;
	
	@FXML
	private TableColumn<Discount, String> tableColumnValue;
	
	@FXML
	private TableColumn<Discount, Discount> tableColumnEDIT;
	
	@FXML
	private TableColumn<Discount, Discount> tableColumnREMOVE;

	@FXML
	private Button btNew;
	
	private ObservableList<Discount> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Discount obj = new Discount();
		createDialogForm(obj, "/gui/DiscountForm.fxml", parentStage);
	}

	public void setDiscountService(DiscountService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
		tableColumnValue.setCellValueFactory(new PropertyValueFactory<>("Value"));
		
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDiscount.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
	public void updateTableView() {
		
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		List<Discount> list = service.findAll();
		
		obsList = FXCollections.observableArrayList(list);
		tableViewDiscount.setItems(obsList);
	
		initEditButtons();
		initRemoveButtons();
	}
	
		private void createDialogForm(Discount obj, String absoluteName, Stage parentStage) {	try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			DiscountFormController controller = loader.getController();
			controller.setDiscount(obj);
			controller.setDiscountService(new DiscountService());
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
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
		@Override
		public void onDataChanged() {
			updateTableView();
		}
		
		private void initEditButtons() {
			tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			tableColumnEDIT.setCellFactory(param -> new TableCell<Discount, Discount>() {
				private final Button button = new Button("Editar");

				@Override
				protected void updateItem(Discount obj, boolean empty) {
					super.updateItem(obj, empty);
					if (obj == null) {
						setGraphic(null);
						return;
					}
					setGraphic(button);
					button.setOnAction(
							event -> createDialogForm(obj, "/gui/DiscountForm.fxml", Utils.currentStage(event)));
				}
			});
		}
		
		private void initRemoveButtons() {
			tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			tableColumnREMOVE.setCellFactory(param -> new TableCell<Discount, Discount>() {
				private final Button button = new Button("Remover");

				@Override
				protected void updateItem(Discount obj, boolean empty) {
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

		private void removeEntity(Discount obj) {
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