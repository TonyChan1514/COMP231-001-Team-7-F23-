package application;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;

import java.sql.ResultSet;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class AddShoppingCartGUI {
	private final static String NOT_PRESCRIPTION_OPTION = "No";
	private final static int LOW_STOCK_THRESHOLD = 100;
	private ShoppingCustomerGUI customer;
	private int customerID, cartID, prescriptionID, quantity, drugIDToAdd, prescriptionLimit;
	private double unitPrice;
	private Tab shoppingCartTab;
	private QueryHandler queryHandler;
	private GridPane gridPane;
	private ResultSet resultSet;
	private TableView<DrugItem> drugTable;
	private ObservableList<DrugItem> drugs = FXCollections.observableArrayList();
	private String drugName, prescriptionIDString;
	private int rowOffset;
	private ComboBox<String> drugIDComboBox = new ComboBox<String>();
	private ComboBox<String> prescriptionIDComboBox = new ComboBox<String>();
	private TextField quantityTextField = new TextField();
	private String[] drugIDLists, prescriptionIDLists;
	private Label drugNameLabel = new Label();
	private Label isPrescriptionLabel = new Label();
	private Label prescriptionIDLabel = new Label("PrescriptionID: ");
	private Label unitPriceLabel = new Label();
	private Label quantityLabel = new Label("Quantity: ");
	private Button buttonAddToCart = new Button("Add to Cart");;
	private Label labelCustomerId = new Label("Customer ID:");
	private Text textCustomerId, textCartID;
	private Label labelCartId = new Label("Cart ID:");
	private Button buttonGoToCart = new Button("Go To Cart");
	private Button buttonClearCart = new Button("Clear Cart");
	private Button buttonCancel = new Button("Cancel");
	private Label deliveryAddressLabel = new Label("Delivery Address: ");
	private TextField deliveryAddressTextField = new TextField();
	private Button buttonCheckout = new Button("Check Out");
	
	public AddShoppingCartGUI(Tab shoppingCartTab, QueryHandler queryHandler, int customerID, int cartID) throws Exception {
		gridPane = new GridPane();
		this.shoppingCartTab = shoppingCartTab;
		this.queryHandler = queryHandler;
		this.customerID = customerID;
		this.cartID = cartID;
		initialize();
		generateDisplayGUI();
	}
	
	public GridPane getGUI() {
        return gridPane;
    }
	
	private void initialize() throws Exception {
		drugNameLabel.setPrefWidth(100);
		isPrescriptionLabel.setPrefWidth(150);
		quantityTextField.setPrefWidth(50);
		unitPriceLabel.setPrefWidth(80);
		deliveryAddressTextField.setPrefWidth(400);
		textCustomerId = new Text(Integer.toString(customerID));
		textCartID = new Text(Integer.toString(cartID));
		drugIDComboBox.setOnAction(event -> {
			try {
				selectDrugIDComboBox();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		buttonAddToCart.setOnAction(event -> {
			try {
				clickAddToCartButton();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		buttonCheckout.setOnAction(event -> {
			try {
				clickCheckOutButton();
				customer = new ShoppingCustomerGUI(shoppingCartTab, queryHandler);
		    	shoppingCartTab.setContent(customer.getGUI());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
	    // Create a TableView to display the drug list
	    drugTable = new TableView<>();
	    drugTable.setPrefWidth(600);
	    drugTable.setPrefHeight(300);


	    // Create columns for the table
	    TableColumn<DrugItem, String> drugNameCol = new TableColumn<>("Drug Name");
	    drugNameCol.setStyle("-fx-alignment: CENTER;");
	    drugNameCol.setCellValueFactory(new PropertyValueFactory<>("drugName"));
	    drugNameCol.setPrefWidth(150);

	    TableColumn<DrugItem, String> prescriptionCol = new TableColumn<>("Prescription ID");
	    prescriptionCol.setStyle("-fx-alignment: CENTER;");
	    prescriptionCol.setCellValueFactory(new PropertyValueFactory<>("prescriptionID"));
	    prescriptionCol.setPrefWidth(120);

	    TableColumn<DrugItem, String> unitPriceCol = new TableColumn<>("Unit Price");
	    unitPriceCol.setStyle("-fx-alignment: CENTER;");
	    unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
	    unitPriceCol.setPrefWidth(100);
	    
	    TableColumn<DrugItem, String> quantityCol = new TableColumn<>("Quantity");
	    quantityCol.setStyle("-fx-alignment: CENTER;");
	    quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
	    quantityCol.setPrefWidth(100);

	    // Add columns to the table
	    drugTable.getColumns().addAll(drugNameCol, prescriptionCol, unitPriceCol, quantityCol);
		
		refreshDrugItemTable();
		refreshDrugIDComboBox();
		refreshPrescriptionIDComboBox();
		selectDrugIDComboBox();
		loadCustomerAddress();
	}
	
	@SuppressWarnings("unchecked")
	public void generateDisplayGUI() throws Exception {
	 // Set the width of all buttons
	    double buttonWidth = 120;
	    buttonAddToCart.setMinWidth(buttonWidth);
	    buttonGoToCart.setMinWidth(buttonWidth);
	    buttonClearCart.setMinWidth(buttonWidth);
	    buttonCancel.setMinWidth(buttonWidth);

	    // Add buttons to an HBox for consistent spacing
	    HBox buttonBox = new HBox(10); // Set spacing between buttons
	    //buttonBox.getChildren().addAll(buttonCancel, buttonClearCart);
	    buttonBox.getChildren().addAll(buttonCancel, buttonClearCart);
	    
	    HBox addDrugBox = new HBox(10);
	    Label drugIDLabel = new Label("Drug ID: ");
	    addDrugBox.getChildren().addAll(
	    		drugIDLabel, drugIDComboBox, drugNameLabel, 
	    		isPrescriptionLabel, prescriptionIDLabel, prescriptionIDComboBox, 
	    		unitPriceLabel, 
	    		quantityLabel, quantityTextField);
	    
	    HBox addressBox = new HBox(10);
	    addressBox.getChildren().addAll(deliveryAddressLabel, deliveryAddressTextField, buttonCheckout);

	    // Add the table and buttons to the gridPane
	    rowOffset = 0;
	    gridPane.add(labelCustomerId, 0, rowOffset);
	    gridPane.add(textCustomerId, 1, rowOffset);
	    gridPane.add(labelCartId, 2, rowOffset);
	    gridPane.add(textCartID, 3, rowOffset);
	    rowOffset++;
	    gridPane.add(drugTable, 0, rowOffset, 4, 1); // Span the drugTable across 4 columns
	    rowOffset ++;
	    gridPane.add(new Label("-------------- Add new drug --------------"), 0, rowOffset, 4, 1);
	    rowOffset ++;
	    gridPane.add(addDrugBox, 0, rowOffset, 4, 1);
	    rowOffset++;
	    gridPane.add(buttonAddToCart, 0, rowOffset, 4, 1);
	    rowOffset++;
	    gridPane.add(new Label("-------------- Checkout --------------"), 0, rowOffset, 4, 1);
	    rowOffset ++;
	    gridPane.add(addressBox, 0, rowOffset, 4, 1);
	    rowOffset++;
	    gridPane.add(new Separator(), 0, rowOffset, 4, 1);
	    rowOffset++;
	    gridPane.add(buttonBox, 0, rowOffset, 4, 1); // Span the buttonBox across 4 columns
	    rowOffset++;
	    
	    buttonGoToCart.setOnAction(event -> {
	    	ShowShoppingCartGUI shoppingCart = new ShowShoppingCartGUI(shoppingCartTab, queryHandler, customerID, cartID);
			shoppingCartTab.setContent(shoppingCart.getGUI());
		});
	    
	    buttonClearCart.setOnAction(event -> {
	    	try {
				clickClearCartButton();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	    
	    buttonCancel.setOnAction(event -> {
			try {
				customer = new ShoppingCustomerGUI(shoppingCartTab, queryHandler);
		    	shoppingCartTab.setContent(customer.getGUI());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	   
	    // Set gap of gridPane
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setPadding(new Insets(20, 20, 5, 20));

	}
	
	private void refreshDrugItemTable() throws Exception {
		resultSet = queryHandler.getCartInformationByCartID(cartID);
		drugs.clear();
		while (resultSet.next()) {
			drugName = resultSet.getString("drugName");
			prescriptionID = resultSet.getInt("prescriptionID");
			prescriptionIDString = resultSet.wasNull() ? "" : Integer.toString(prescriptionID);
			unitPrice = resultSet.getDouble("unitPrice");
			quantity = resultSet.getInt("quantity");
			prescriptionLimit = resultSet.getInt("prescriptionLimit");
			drugs.add(new DrugItem(drugName, prescriptionIDString, unitPrice, quantity, prescriptionLimit));
		}

	    drugTable.setItems(drugs);
	}
	
	public void refreshDrugIDComboBox() throws Exception {
		int i = 0; 
		
		drugIDLists = new String[queryHandler.getDrugCount()];
		resultSet = queryHandler.getDrugIDList();
		while (resultSet.next()) {
			drugIDLists[i] = Integer.toString(resultSet.getInt(1));
			i++;
		}
		
		drugIDComboBox.getItems().clear();
		drugIDComboBox.getSelectionModel().clearSelection();
		drugIDComboBox.setItems(FXCollections.observableArrayList(drugIDLists));
		drugIDComboBox.getSelectionModel().selectFirst();
	}
	
	private void selectDrugIDComboBox() throws Exception {
		String option = drugIDComboBox.getValue();
		drugIDToAdd = Integer.parseInt(option);
		//refreshPrescriptionIDComboBox();
		
		resultSet = queryHandler.getDrugRecordByID(drugIDToAdd);
		drugNameLabel.setText(resultSet.getString("drugName"));
		if (resultSet.getInt("isPrescription") == 1){
			isPrescriptionLabel.setText("(Prescripted Drug)");
			prescriptionIDComboBox.setDisable(false);
		} else {
			isPrescriptionLabel.setText("(Non-prescripted Drug)");
			prescriptionIDComboBox.setDisable(true);
			prescriptionIDComboBox.getSelectionModel().selectFirst();
		}
		unitPriceLabel.setText("   $" + Double.toString(resultSet.getDouble("retailPrice")));
		quantityTextField.clear();
		
		refreshPrescriptionIDComboBox();
	}
	
	public void refreshPrescriptionIDComboBox() throws Exception {
		int i;
		
		prescriptionIDLists = new String[queryHandler.getPrescriptionCountByCustomerAndDrug(customerID, drugIDToAdd)+1];
		prescriptionIDLists[0] = NOT_PRESCRIPTION_OPTION;
		i = 1;
		resultSet = queryHandler.getPrescriptionListByCustomerAndDrug(customerID, drugIDToAdd);
		while (resultSet.next()) {
			prescriptionIDLists[i] = Integer.toString(resultSet.getInt("prescriptionID"));
			i++;
		}
		
		prescriptionIDComboBox.getItems().clear();
		prescriptionIDComboBox.getSelectionModel().clearSelection();
		prescriptionIDComboBox.setItems(FXCollections.observableArrayList(prescriptionIDLists));
		prescriptionIDComboBox.getSelectionModel().selectFirst();
	}
	
	private void clickAddToCartButton() throws Exception {
		boolean isAllowable;
		
		if (quantityTextField.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(null, "Please input quantity first.");
			return;
		} 
		
		if (prescriptionIDComboBox.getValue() == NOT_PRESCRIPTION_OPTION) {
			prescriptionID = -1;
		} else {
			prescriptionID = Integer.parseInt(prescriptionIDComboBox.getValue());
		}
		
		int prescriptAmount = Integer.parseInt(quantityTextField.getText());
		isAllowable = queryHandler.checkIsAllowable(drugIDToAdd, prescriptionID, prescriptAmount);
		ResultSet drugResultSet = queryHandler.getDrugRecordByID(drugIDToAdd);
		DrugItem drugInfo = new DrugItem(
			drugResultSet.getString("drugName"),
			drugResultSet.getBoolean("isPrescription") == true ? "Yes" : "No",
			drugResultSet.getDouble("retailPrice"),
			drugResultSet.getInt("stock"),
			drugResultSet.getInt("prescriptionLimit")
		);
		int drugStock = drugInfo.getQuantity();
		int remainingStock = drugStock - prescriptAmount;
		
		if (!isAllowable) {
			JOptionPane.showMessageDialog(null, "Not authorized to buy prescripted drug.");
			return;
		}
		
		if (prescriptAmount > drugInfo.getPrescriptionLimit()) {
			JOptionPane.showMessageDialog(null, "The prescribed quantity is higher than the maximum allowed limit.");
			return;
		}
		if (remainingStock < 0) {
			JOptionPane.showMessageDialog(null, "There is not enough stock for the drug.");
			return;
		}
		
		queryHandler.addToShoppingCart(drugIDToAdd, prescriptionID, prescriptAmount);
		if (remainingStock < LOW_STOCK_THRESHOLD) {
			JOptionPane.showMessageDialog(null, "This drug would reach a low stock level after checkout. A notification will be sent to inventory manager after checkout.");
		}
		
		refreshDrugItemTable();
	}
	
	private void clickClearCartButton() throws Exception {
		queryHandler.clearShoppingCart(cartID);
		refreshDrugItemTable();
	}
	
	private void loadCustomerAddress() throws Exception {
		resultSet = queryHandler.getCustomerRecordByID(customerID);
		deliveryAddressTextField.setText(resultSet.getString("address"));
	}
	
	private void clickCheckOutButton() throws Exception {
		if (deliveryAddressTextField.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(null, "Please input your delivery address.");
			return;
		}
		queryHandler.shoppingCartCheckOut(deliveryAddressTextField.getText());
		
		JOptionPane.showMessageDialog(null, "Checkout successfully. Order ID " + queryHandler.getLastOrderID() + " is created.");
	}
}



