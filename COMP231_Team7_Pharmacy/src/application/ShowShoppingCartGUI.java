package application;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ShowShoppingCartGUI {
	private int customerID, cartID;
	private Tab shoppingCartTab;
	private QueryHandler queryHandler;
	private GridPane gridPane;

    public ShowShoppingCartGUI(Tab shoppingCartTab, QueryHandler queryHandler, int customerID, int cartID) {
		gridPane = new GridPane();
		this.shoppingCartTab = shoppingCartTab;
		this.queryHandler = queryHandler;
        this.customerID = customerID;
        this.cartID = cartID;
        generateDisplayGUI();
    }
    
	public GridPane getGUI() {
        return gridPane;
    }

    public void generateDisplayGUI() {
        Label labelCustomerId = new Label("Customer ID:");
        Text textCustomerId = new Text(Integer.toString(customerID));
        Button buttonCheckOut = new Button("Check Out");
        Button buttonBack = new Button("Back");
        Button buttonCancel = new Button("Cancel");

        
        // Create a TableView to display the drug list
	    TableView<DrugItem> drugTable = new TableView<>();
	    drugTable.setPrefWidth(600);

	    // Create columns for the table
	    TableColumn<DrugItem, String> drugNameCol = new TableColumn<>("Drug Name");
	    drugNameCol.setStyle("-fx-alignment: CENTER;");
	    drugNameCol.setCellValueFactory(new PropertyValueFactory<>("drugName"));
	    drugNameCol.setPrefWidth(150);

	    TableColumn<DrugItem, String> prescriptionCol = new TableColumn<>("Prescription");
	    prescriptionCol.setStyle("-fx-alignment: CENTER;");
	    prescriptionCol.setCellValueFactory(new PropertyValueFactory<>("prescription"));
	    prescriptionCol.setPrefWidth(120);

	    TableColumn<DrugItem, String> unitPriceCol = new TableColumn<>("Unit Price");
	    unitPriceCol.setStyle("-fx-alignment: CENTER;");
	    unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
	    unitPriceCol.setPrefWidth(100);
	    
	    TableColumn<DrugItem, String> quantityCol = new TableColumn<>("Quantity");
	    quantityCol.setStyle("-fx-alignment: CENTER;");
	    quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
	    //quantityCol.setCellFactory(column -> new QuantityInputCell()); // Set custom cell
	    quantityCol.setPrefWidth(100);
	    

	    // Add columns to the table
	    drugTable.getColumns().addAll(drugNameCol, prescriptionCol, unitPriceCol, quantityCol);

	    // Create and populate the drug list
	    ObservableList<DrugItem> drugs = FXCollections.observableArrayList(
	            new DrugItem("Panadol", "2", 10, 3),
	            new DrugItem("Sertraline", "3", 5, 2),
	            new DrugItem("Penicillin", "4", 25, 2)
	    );

	    drugTable.setItems(drugs);
	    
	    // Set the width of all buttons
	    double buttonWidth = 120;
	    buttonBack.setMinWidth(buttonWidth);
	    buttonCheckOut.setMinWidth(buttonWidth);
	    buttonCancel.setMinWidth(buttonWidth);

	    // Add buttons to an HBox for consistent spacing
	    HBox buttonBox = new HBox(10); // Set spacing between buttons
	    buttonBox.getChildren().addAll(buttonBack, buttonCheckOut, buttonCancel);

	    // Add the table and buttons to the gridPane
	    gridPane.add(labelCustomerId, 0, 0);
	    gridPane.add(textCustomerId, 1, 0);
	    gridPane.add(drugTable, 0, 1, 4, 1); // Span the drugTable across 4 columns
	    gridPane.add(buttonBox, 0, 2, 4, 1); // Span the buttonBox across 4 columns
	    
        buttonBack.setOnAction(event -> {
        	AddShoppingCartGUI shop;
			try {
				shop = new AddShoppingCartGUI(shoppingCartTab, queryHandler, customerID, cartID);
	        	shoppingCartTab.setContent(shop.getGUI());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
        
        buttonCheckOut.setOnAction(event -> {
        	System.out.println("HI");
        	CheckOutGUI checkOut = new CheckOutGUI(shoppingCartTab, queryHandler, customerID, cartID);
	    	shoppingCartTab.setContent(checkOut.getGUI());
		});

        buttonCancel.setOnAction(event -> {
	    	ShoppingCustomerGUI customer;
			try {
				customer = new ShoppingCustomerGUI(shoppingCartTab, queryHandler);
		    	shoppingCartTab.setContent(customer.getGUI());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

        // Set gap of grid
        gridPane.setHgap(10);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        
        gridPane.setStyle("-fx-background-color: #F5F5F5;");
    }
}

