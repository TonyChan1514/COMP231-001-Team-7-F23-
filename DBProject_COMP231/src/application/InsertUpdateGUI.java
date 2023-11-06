
package application;

import java.sql.*;
import javax.swing.JOptionPane;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class InsertUpdateGUI {
	
	private final static String INSERT_OPTION = "Insert New"; 
	private final static String NOT_SELECTED_OPTION = "Not Selected";
	
	private QueryHandler queryHandler;
	private ResultSet resultSet;
	private GridPane gridPane;
	
	ComboBox<String> updateCustomerIDComboBox = new ComboBox<String>();
	ComboBox<String> updatePrescriptionIDComboBox = new ComboBox<String>();
	ComboBox<String> updateDrugIDComboBox = new ComboBox<String>();
	
	// Customer ID column
	Label[] customerLabels = {
			new Label("First Name:"), new Label("Last Name:"), new Label("Address:"), new Label("Phone:"), new Label("Email:")
		};
	TextField[] customerFields;
	
	// Prescription ID column
	Label[] prescriptionLabels = {
			new Label("Customer ID:"), new Label("Drug ID:"), new Label("Patient Name:"), new Label("Patient DOB:"), new Label("Refills:"),
			new Label("Order Count:"), new Label("Doctor Name:"), new Label("Doctor Address:"), new Label("Doctor Phone:")
		};
	TextField[] prescriptionFields;
	ComboBox<String> prescriptionCustomerIDComboBox = new ComboBox<String>();
	ComboBox<String> prescriptionDrugIDComboBox = new ComboBox<String>();
	DatePicker patientDOBPicker = new DatePicker();
	
	// Drug ID column
	Label[] drugLabels = {
			new Label ("Need Prescription:"), new Label ("Drug Name:"), new Label ("Retail Price:"), new Label("Stock:")
		};
	TextField[] drugFields;
	ComboBox<String> isPrescriptionComboBox = new ComboBox<String>();
	
	String[] customerIDLists, playerIDLists, drugIDLists, isPrescriptionLists;

 
	// Create display button and student information display area
	Button submitCustomerButton = new Button("Submit");
	Button submitPrescriptionButton = new Button("Submit");
	Button submitDrugButton = new Button ("Submit");
	
	int rowOffset;
	HBox fieldBox;
	
	public InsertUpdateGUI(QueryHandler queryHandler) throws Exception {
		this.queryHandler = queryHandler; 
		
		// Setup Grid Pane
		gridPane = new GridPane();
        
        // Setup combo boxes, date picker and other GUI
        initializeBoxes();
        
        // Generate Insert/Update Game GUI
        generateCustomerGUI();
        generatePrescriptionGUI();
        generateDrugGUI();
	}
	
	public GridPane getGUI() {
		return gridPane;
	}
	
	private void initializeBoxes() throws Exception {
        // Initialize gridPane
		gridPane.setPadding(new Insets(20, 20, 5, 20));
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
		
		// Initialize Width
		updateCustomerIDComboBox.setPrefWidth(180);
        updatePrescriptionIDComboBox.setPrefWidth(180);
        updateDrugIDComboBox.setPrefWidth(180);
        prescriptionCustomerIDComboBox.setPrefWidth(180);
        prescriptionDrugIDComboBox.setPrefWidth(180);
        patientDOBPicker.setPrefWidth(150);
        
		// Initialize combo boxes contents
		refreshUpdateCustomerIDComboBox();
        refreshUpdatePrescriptionIDComboBox();
        refreshPrescriptionCustomerIDComboBox();
        refreshIsPrescriptionComboBox();
		refreshUpdateDrugIDComboBox();
		refreshPrescriptionDrugIDComboBox();
		//refreshPrescriptionIDComboBox();
		
		// Initialize event handling functions
		submitCustomerButton.setOnAction(event -> {
			try {
				clickSubmitCustomerButton();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		submitPrescriptionButton.setOnAction(event -> {
			try {
				clickSubmitPrescriptionButton();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		submitDrugButton.setOnAction(event -> {
			try {
				clickSubmitDrugButton();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		updateCustomerIDComboBox.setOnAction(event -> {
			try {
				selectUpdateCustomerIDComboBox();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		updatePrescriptionIDComboBox.setOnAction(event -> {
			try {
				selectUpdatePrescriptionIDComboBox();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		updateDrugIDComboBox.setOnAction(event -> {
			try {
				selectUpdateDrugIDComboBox();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// Setup grip pane columns
	    ColumnConstraints col0 = new ColumnConstraints();
	    ColumnConstraints col1 = new ColumnConstraints();
	    ColumnConstraints col2 = new ColumnConstraints();
		ColumnConstraints col3 = new ColumnConstraints();
		ColumnConstraints col4 = new ColumnConstraints();
		ColumnConstraints col5 = new ColumnConstraints();
		ColumnConstraints col6 = new ColumnConstraints();
		ColumnConstraints col7 = new ColumnConstraints();
		ColumnConstraints col8 = new ColumnConstraints();
		col0.setPrefWidth(100);
		col1.setPrefWidth(200);
		col2.setPrefWidth(50);
		col3.setPrefWidth(100);
		col4.setPrefWidth(200);
		col5.setPrefWidth(50);
		col6.setPrefWidth(100);
		col7.setPrefWidth(200);
		col8.setPrefWidth(50);
		col2.setHalignment(HPos.CENTER);
		col5.setHalignment(HPos.CENTER);
	    gridPane.getColumnConstraints().addAll(col0, col1, col2, col3, col4, col5, col6, col7, col8);
	}
	
	private void generateCustomerGUI() {
        // For insert games (using columns 0, 1, 2), column 2 is dummy
        rowOffset = 0;
        gridPane.add(new Label("---- Customers Management ----"), 0, rowOffset, 2, 1);
        rowOffset++;
        gridPane.add(updateCustomerIDComboBox, 0, rowOffset, 2, 1);
        rowOffset++;
        gridPane.add(new Separator(), 0, rowOffset, 2, 1);
        rowOffset++;
        customerFields = new TextField[customerLabels.length];
		for (int i = 0; i < customerLabels.length; i++) {
			customerFields[i] = new TextField();
			gridPane.add(customerLabels[i], 0, i + rowOffset);
			gridPane.add(customerFields[i], 1, i + rowOffset);
		}
		gridPane.add(submitCustomerButton, 0, customerLabels.length + rowOffset, 2, 1);
		
		// Add vertical separator
		Separator gameSeparator = new Separator();
		gameSeparator.setOrientation(Orientation.VERTICAL);
		gridPane.add(gameSeparator, 2, 0, 1, 20);
	}
	
	private void generatePrescriptionGUI() {
		// For insert players (using columns 3, 4, 5), column 5 is dummy
        rowOffset = 0;
        gridPane.add(new Label("---- Prescriptions Management ----"), 3, rowOffset, 2, 1);
        rowOffset++;
        gridPane.add(updatePrescriptionIDComboBox, 3, rowOffset, 2, 1);
        rowOffset++;
        gridPane.add(new Separator(), 3, rowOffset, 2, 1);
        rowOffset++;
        prescriptionFields = new TextField[prescriptionLabels.length];
		for (int i = 0; i < prescriptionLabels.length; i++) {
			prescriptionFields[i] = new TextField();
			gridPane.add(prescriptionLabels[i], 3, i + rowOffset);	
			switch (i) {
			case 0:
				gridPane.add(prescriptionCustomerIDComboBox, 4, i + rowOffset);
				break;
			case 1:
				gridPane.add(prescriptionDrugIDComboBox, 4, i + rowOffset);
				break;
			case 3:
				gridPane.add(patientDOBPicker, 4, i + rowOffset);
				break;
			default:
				gridPane.add(prescriptionFields[i], 4, i + rowOffset);
				break;
			}
		}
		// Force set order count = 0 as initial
		prescriptionFields[5].setText("0");
		prescriptionFields[5].setDisable(true);
		gridPane.add(submitPrescriptionButton, 3, prescriptionLabels.length + rowOffset, 2, 1);
		
		// Add vertical separator
		Separator playerSeparator = new Separator();
		playerSeparator.setOrientation(Orientation.VERTICAL);
		gridPane.add(playerSeparator, 5, 0, 1, 20);
	}
	
	private void generateDrugGUI() {
		// For insert PlayerAndGame (using columns 6, 7, 8), column 8 is dummy
		rowOffset = 0;
        gridPane.add(new Label("---- Drugs Management ----"), 6, rowOffset, 2, 1);
        rowOffset++;
        gridPane.add(updateDrugIDComboBox, 6, rowOffset, 2, 1);
        rowOffset++;
		gridPane.add(new Separator(), 6, rowOffset, 2, 1);
        rowOffset++;
		drugFields = new TextField[drugLabels.length];
		for (int i = 0; i < drugLabels.length; i++) {
			gridPane.add(drugLabels[i], 6, i + rowOffset);
			drugFields[i] = new TextField();
			switch (i) {
			case 0:
				gridPane.add(isPrescriptionComboBox, 7, i + rowOffset);
				break;
			default:
				gridPane.add(drugFields[i], 7, i + rowOffset);
				break;
			}
		}
		gridPane.add(submitDrugButton, 6, drugLabels.length + rowOffset, 2, 1);
	}
	
	private void clickSubmitCustomerButton() throws Exception {
		// Perform field validation
		if (customerFields[0].getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(null, "First Name is a mandatory field.");
			return;
		}
		
		// Insert or Update Mode for Games
		if (updateCustomerIDComboBox.getValue() == INSERT_OPTION) {
			queryHandler.insertNewCustomer(customerFields);
			JOptionPane.showMessageDialog(null, "Customer " + customerFields[0].getText() + " (CustomerID: " + queryHandler.getLastCustomerID() + ") inserted Successfully.");;
		} else {
			if (queryHandler.updateCustomerByID(Integer.parseInt(updateCustomerIDComboBox.getValue()), customerFields)) {
				JOptionPane.showMessageDialog(null, "Customer ID " + updateCustomerIDComboBox.getValue() + " updated Successfully.");;
			}
		}
			
		// Reset fields
		customerFields[0].setText("");
		refreshUpdateCustomerIDComboBox();
		refreshPrescriptionCustomerIDComboBox();
	}
	
	private void clickSubmitPrescriptionButton() throws Exception{
		// Perform field validation
		if (prescriptionCustomerIDComboBox.getValue() == NOT_SELECTED_OPTION) {
			JOptionPane.showMessageDialog(null, "Customer ID is a mandatory field.");
			return;
		}
		if (prescriptionDrugIDComboBox.getValue() == NOT_SELECTED_OPTION) {
			JOptionPane.showMessageDialog(null, "Drug ID is a mandatory field.");
			return;
		}
		if (prescriptionFields[2].getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(null, "Patient Name is a mandatory field.");
			return;
		}
		
		// Insert or Update Mode for Prescriptions
		if (updatePrescriptionIDComboBox.getValue() == INSERT_OPTION) {
			queryHandler.insertNewPrescription(Integer.parseInt(prescriptionCustomerIDComboBox.getValue()), 
					Integer.parseInt(prescriptionDrugIDComboBox.getValue()), java.sql.Date.valueOf(patientDOBPicker.getValue()), prescriptionFields);
			JOptionPane.showMessageDialog(null, "Prescription ID " + queryHandler.getLastPrescriptionID() + " inserted Successfully.");
		}
		else {
			if (queryHandler.updatePrescriptionByID(Integer.parseInt(updatePrescriptionIDComboBox.getValue()), Integer.parseInt(prescriptionCustomerIDComboBox.getValue()), 
					Integer.parseInt(prescriptionDrugIDComboBox.getValue()), java.sql.Date.valueOf(patientDOBPicker.getValue()), prescriptionFields)) {
				JOptionPane.showMessageDialog(null, "Prescription ID " + updatePrescriptionIDComboBox.getValue() + " updated Successfully.");
			}
		}
			
		// Reset fields
		for (int i = 0; i < prescriptionLabels.length; i++) {
			prescriptionFields[i].setText("");
		}
		refreshUpdatePrescriptionIDComboBox();
	}
	
	private void clickSubmitDrugButton() throws Exception {
		int isPresctiption;
		
		// Perform field validation
		if (isPrescriptionComboBox.getValue() == NOT_SELECTED_OPTION) {
			JOptionPane.showMessageDialog(null, "Is prescription is a mandatory field.");
			return;
		} else if (drugFields[1].getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(null, "Drug name is a mandatory field");
			return;
		} else if (drugFields[2].getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(null, "Retail price is a mandatory field.");
			return;
		} else if (drugFields[3].getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(null, "Stock is a mandatory field.");
			return;
		}
		
		// Insert or Update Mode for Drugs
		isPresctiption = (isPrescriptionComboBox.getValue() == "Yes") ? 1 : 0;
		if (updateDrugIDComboBox.getValue() == INSERT_OPTION) {
			queryHandler.insertNewDrug(isPresctiption, drugFields);
			JOptionPane.showMessageDialog(null, "Drug ID " + queryHandler.getLastDrugID() + " (" + drugFields[1].getText() + ") inserted successfully.");
		} else {
			if (queryHandler.updateDrugByID(Integer.parseInt(updateDrugIDComboBox.getValue()), isPresctiption, drugFields)) {
				JOptionPane.showMessageDialog(null, "Drug ID " + updateDrugIDComboBox.getValue() + " updated successfully.");
			}
		}
		
		// Reset fields
		isPrescriptionComboBox.getSelectionModel().selectFirst();
		for (int i = 0; i < drugLabels.length; i++) {
			drugFields[i].setText("");
		}
		refreshUpdateDrugIDComboBox();
		refreshPrescriptionDrugIDComboBox();
	}
	
	private void refreshPrescriptionCustomerIDComboBox() throws Exception {
		int i; 
		
		// Get the customer id count and available customer ids 
		customerIDLists = new String[queryHandler.getCustomerCount()+1];
			
		customerIDLists[0] = NOT_SELECTED_OPTION;
		i = 1;
		resultSet = queryHandler.getCustomerIDList();
		while (resultSet.next()) {
			customerIDLists[i] = Integer.toString(resultSet.getInt(1));
			i++;
		}
			
		prescriptionCustomerIDComboBox.getItems().clear();
		prescriptionCustomerIDComboBox.getSelectionModel().clearSelection();
		prescriptionCustomerIDComboBox.setItems(FXCollections.observableArrayList(customerIDLists));
		prescriptionCustomerIDComboBox.getSelectionModel().selectFirst();
	}
	
	private void refreshPrescriptionDrugIDComboBox() throws Exception {	
		int i; 
			
		// Get the customer id count and available customer ids 
		drugIDLists = new String[queryHandler.getDrugCount()+1];
			
		drugIDLists[0] = NOT_SELECTED_OPTION;
		i = 1;
		resultSet = queryHandler.getDrugIDList();
		while (resultSet.next()) {
			drugIDLists[i] = Integer.toString(resultSet.getInt(1));
			i++;
		}
			
		prescriptionDrugIDComboBox.getItems().clear();
		prescriptionDrugIDComboBox.getSelectionModel().clearSelection();
		prescriptionDrugIDComboBox.setItems(FXCollections.observableArrayList(drugIDLists));
		prescriptionDrugIDComboBox.getSelectionModel().selectFirst();	
	}
	
	private void refreshIsPrescriptionComboBox() throws Exception {
		isPrescriptionLists = new String[3];
		isPrescriptionLists[0] = NOT_SELECTED_OPTION;
		isPrescriptionLists[1] = "Yes";
		isPrescriptionLists[2] = "No";
			
		isPrescriptionComboBox.getItems().clear();
		isPrescriptionComboBox.getSelectionModel().clearSelection();
		isPrescriptionComboBox.setItems(FXCollections.observableArrayList(isPrescriptionLists));
		isPrescriptionComboBox.getSelectionModel().selectFirst();
	}
	
	private void refreshUpdateCustomerIDComboBox() throws Exception {
		int i; 
			
		// Get the game id count and available game ids 
		customerIDLists = new String[queryHandler.getCustomerCount()+1];
		customerIDLists[0] = INSERT_OPTION;
		i = 1;
		resultSet = queryHandler.getCustomerIDList();
		while (resultSet.next()) {
			customerIDLists[i] = Integer.toString(resultSet.getInt(1));
			i++;
		}
		
		updateCustomerIDComboBox.getItems().clear();
		updateCustomerIDComboBox.getSelectionModel().clearSelection();
		updateCustomerIDComboBox.setItems(FXCollections.observableArrayList(customerIDLists));
		updateCustomerIDComboBox.getSelectionModel().selectFirst();
	}
	
	private void refreshUpdatePrescriptionIDComboBox() throws Exception {
		int i; 
			
		// Get the prescription ID count and available prescription ids 
		playerIDLists = new String[queryHandler.getPrescriptionCount()+1];
		playerIDLists[0] = INSERT_OPTION;
		i = 1;
		resultSet = queryHandler.getPrescriptionIDList();
		while (resultSet.next()) {
			playerIDLists[i] = Integer.toString(resultSet.getInt(1));
			i++;
		}
		
		updatePrescriptionIDComboBox.getItems().clear();
		updatePrescriptionIDComboBox.getSelectionModel().clearSelection();
		updatePrescriptionIDComboBox.setItems(FXCollections.observableArrayList(playerIDLists));
		updatePrescriptionIDComboBox.getSelectionModel().selectFirst();
	}
	
	private void refreshUpdateDrugIDComboBox() throws Exception {
		int i; 
			
		// Get the player game id count and available player game ids
		drugIDLists = new String[queryHandler.getDrugCount()+1];
		drugIDLists[0] = INSERT_OPTION;
		i = 1;
		resultSet = queryHandler.getDrugIDList();
		while (resultSet.next()) {
			drugIDLists[i] = Integer.toString(resultSet.getInt(1));
			i++;
		}
		
		updateDrugIDComboBox.getItems().clear();
		updateDrugIDComboBox.getSelectionModel().clearSelection();
		updateDrugIDComboBox.setItems(FXCollections.observableArrayList(drugIDLists));
		updateDrugIDComboBox.getSelectionModel().selectFirst();
	}
	
	private void selectUpdateCustomerIDComboBox() throws Exception {
		String option = updateCustomerIDComboBox.getValue();
		if (option == INSERT_OPTION) {
			for (int i = 0; i < customerFields.length; i++) {
				customerFields[i].setText("");
			}
		} else if (option != null) {
			resultSet = queryHandler.getCustomerRecordByID(Integer.parseInt(option));
			for (int i = 0; i < customerFields.length; i++) {
				customerFields[i].setText(resultSet.getString(i+1));
			}
		}
	}
	
	private void selectUpdatePrescriptionIDComboBox() throws Exception {
		String option = updatePrescriptionIDComboBox.getValue();
		if (option == INSERT_OPTION) {
			for (int i = 0; i < prescriptionFields.length; i++) {
				prescriptionFields[i].setText("");
			}
			prescriptionCustomerIDComboBox.getSelectionModel().selectFirst();
			prescriptionDrugIDComboBox.getSelectionModel().selectFirst();
			patientDOBPicker.setValue(null);
			// Force set order count = 0 as initial
			prescriptionFields[5].setText("0");
			prescriptionFields[5].setDisable(true);
		} else if (option != null) {
			resultSet = queryHandler.getPrescriptionRecordByID(Integer.parseInt(option));
			prescriptionCustomerIDComboBox.getSelectionModel().select(Integer.toString(resultSet.getInt(1)));
			prescriptionDrugIDComboBox.getSelectionModel().select(Integer.toString(resultSet.getInt(2)));
			prescriptionFields[2].setText(resultSet.getString(3));
			patientDOBPicker.setValue(resultSet.getDate(4).toLocalDate());
			prescriptionFields[4].setText(resultSet.getString(5));
			prescriptionFields[5].setText(resultSet.getString(6));
			prescriptionFields[6].setText(resultSet.getString(7));
			prescriptionFields[7].setText(resultSet.getString(8));
			prescriptionFields[8].setText(resultSet.getString(9));
			prescriptionFields[5].setDisable(false);
		}
	}
	
	private void selectUpdateDrugIDComboBox() throws Exception {
		String option = updateDrugIDComboBox.getValue();
		if (option == INSERT_OPTION) {
			isPrescriptionComboBox.getSelectionModel().selectFirst();
			for (int i = 0; i < drugLabels.length; i++) {
				drugFields[i].setText("");
			}
		} else if (option != null) {
			resultSet = queryHandler.getDrugRecordByID(Integer.parseInt(option));
			isPrescriptionComboBox.getSelectionModel().select((resultSet.getInt(1) == 1 ? "Yes" : "No"));
			drugFields[1].setText(resultSet.getString(2));
			drugFields[2].setText(resultSet.getString(3));
			drugFields[3].setText(resultSet.getString(4));
		}
	}
}

//Implement UI components for prescription management