package gui;


import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Product;
import model.entities.Sale;
import model.exceptions.ValidationException;
import model.services.ProductService;
import model.services.SaleService;

public class SaleFormController implements Initializable {

	private Sale entity;

	private SaleService service;
	
	private ProductService productService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtAmount;

	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorAmount;
	
	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;
	
	@FXML
	private ComboBox<Product> comboBoxProduct;
	
	private ObservableList<Product> obsList;

	public void setSale(Sale entity) {
		this.entity = entity;
	}

	public void setServices(SaleService service, ProductService productService) {
		this.service = service;
		this.productService = productService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Sale getFormData() {
		Sale obj = new Sale();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Campo obrigatório");
		}
		obj.setName(txtName.getText());
		
		if (txtAmount.getText() == null || txtAmount.getText().trim().equals("")) {
			exception.addError("amount", "Campo obrigatório");
		}
		
		
		obj.setAmount(Utils.tryParseToInt(txtAmount.getText()));
		
		obj.setTaxes(comboBoxProduct.getValue());
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	
		initializeComboBoxProduct();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		
		if (entity.getProduct() == null) {
			comboBoxProduct.getSelectionModel().selectFirst();
		} else {
			comboBoxProduct.setValue(entity.getProduct());
		}
	}

	public void loadAssociatedObjects() {
		if (productService == null) {
			throw new IllegalStateException("ProductService was null");
		}
		List<Product> list = productService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxProduct.setItems(obsList);
	}
	
	

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorName.setText((fields.contains("name") ? errors.get("name") : ""));
		labelErrorAmount.setText((fields.contains("amount") ? errors.get("amount") : ""));
		
	}
	
	private void initializeComboBoxProduct() {
		Callback<ListView<Product>, ListCell<Product>> factory = lv -> new ListCell<Product>() {
			@Override
			protected void updateItem(Product item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxProduct.setCellFactory(factory);
		comboBoxProduct.setButtonCell(factory.call(null));
	}
}