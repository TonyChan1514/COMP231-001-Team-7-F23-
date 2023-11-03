package application;

import javax.swing.JOptionPane;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class CheckOutGUI {
	
	private int customerID, cartID;
	private Tab shoppingCartTab;
	private QueryHandler queryHandler;
	private GridPane gridPane;
	
	public CheckOutGUI(Tab shoppingCartTab, QueryHandler queryHandler, int customerID, int cartID) {
		gridPane = new GridPane();
		this.shoppingCartTab = shoppingCartTab;
        this.customerID = customerID;
        generateDisplayGUI();
	}
	
	public GridPane getGUI() {
        return gridPane;
    }
	
	public void generateDisplayGUI() {
	    Label labelCustomerId = new Label("Customer ID:");
	    Text textCustomerId = new Text(Integer.toString(customerID));
	    Label labelTotalPrice = new Label("Total Price:");
		Text textTotalPrice = new Text("$115");
	    
		Label labelShippingAddress = new Label("Shopping Address:");
		TextArea textAreaShippingAddress = new TextArea();
		textAreaShippingAddress.setPrefHeight(100);
		textAreaShippingAddress.setPrefWidth(400);
		textAreaShippingAddress.setWrapText(true); // Allow text to wrap within the TextArea
	    textAreaShippingAddress.setStyle("-fx-border-color: #BDBDBD; -fx-border-width: 1px;"); // Add border
		
		Button buttonBack = new Button("Back to Cart");
		Button buttonPlaceOrder = new Button("Place Order");
		Button buttonCancel = new Button("Cancel");
		
		// Set the width of all buttons
        double buttonWidth = 120;
        buttonBack.setMinWidth(buttonWidth);
        buttonPlaceOrder.setMinWidth(buttonWidth);
        buttonCancel.setMinWidth(buttonWidth);

        // Add buttons to an HBox for consistent spacing
        HBox buttonBox = new HBox(10); // Set spacing between buttons
        buttonBox.getChildren().addAll(buttonBack, buttonPlaceOrder, buttonCancel);

        // Add padding and alignment to labels and controls
        labelCustomerId.setPadding(new Insets(5));
        labelTotalPrice.setPadding(new Insets(5));
        labelShippingAddress.setPadding(new Insets(5));

        // Add padding to buttons
        buttonBack.setPadding(new Insets(5));
        buttonPlaceOrder.setPadding(new Insets(5));
        buttonCancel.setPadding(new Insets(5));
	    
		gridPane.add(labelCustomerId, 0, 0, 1, 1);
	    gridPane.add(textCustomerId, 1, 0, 1, 1);
	    gridPane.add(labelTotalPrice, 0, 1, 1, 1);
	    gridPane.add(textTotalPrice, 1, 1, 1, 1);
	    gridPane.add(labelShippingAddress, 0, 2, 1, 1);
	    gridPane.add(textAreaShippingAddress, 1, 2, 3, 1); // Span the textAreaShippingAddress across 3 columns
	    gridPane.add(buttonBack, 0, 3, 1, 1);
	    gridPane.add(buttonPlaceOrder, 1, 3, 1, 1);
	    gridPane.add(buttonCancel, 2, 3, 1, 1); // Place the "Cancel" button in column 2
		
		buttonBack.setOnAction(event -> {
			ShowShoppingCartGUI shoppingCart = new ShowShoppingCartGUI(shoppingCartTab, queryHandler, customerID, cartID);
	    	shoppingCartTab.setContent(shoppingCart.getGUI());
		});
		
		buttonPlaceOrder.setOnAction(event -> {
	    	JOptionPane.showMessageDialog(null, "Order Placed. Order ID: 32513");
			ShoppingCustomerGUI customer;
			try {
				customer = new ShoppingCustomerGUI(shoppingCartTab, queryHandler);
		    	shoppingCartTab.setContent(customer.getGUI());
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	}
}
