package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.exceptions.ValidationException;
import model.entities.Discount;
import model.services.DiscountService;

public class DiscountFormController implements Initializable {

	private Discount entity;

	private DiscountService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtValue;
	
	@FXML
	private TextField txtAmount;

	@FXML
	private Label labelErrorAmount;

	@FXML
	private Label labelErrorValue;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	public void setDiscount(Discount entity) {
		this.entity = entity;
	}

	public void setDiscountService(DiscountService service) {
		this.service = service;
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

	private Discount getFormData() {
		Discount obj = new Discount();

		ValidationException exception = new ValidationException("Validation error");
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtAmount.getText() == null || txtAmount.getText().trim().equals("")) {
			exception.addError("amount", "Campo obrigat�rio");
		}
		obj.setAmount(Utils.tryParseToInt(txtAmount.getText()));
		
		if (txtValue.getText() == null || txtValue.getText().trim().equals("")) {
			exception.addError("value", "Campo obrigat�rio");
		}
		obj.setValue(Utils.tryParseToDouble(txtValue.getText()));

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
		Constraints.setTextFieldMaxLength(txtAmount, 5);
		Constraints.setTextFieldMaxLength(txtValue, 8);
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));

		if(entity.getAmount() != null) {
			txtAmount.setText(String.valueOf(entity.getAmount()));
		}

		if(entity.getValue() != null) {
			txtValue.setText(String.valueOf(entity.getValue()));
		}
		
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("amount")) {
			labelErrorAmount.setText(errors.get("amount"));
		}
		
		if (fields.contains("value")) {
			labelErrorValue.setText(errors.get("value"));
		}
	}
}