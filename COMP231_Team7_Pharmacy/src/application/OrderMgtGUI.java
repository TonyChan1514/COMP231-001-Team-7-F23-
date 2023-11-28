
package application;

import java.sql.*;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class OrderMgtGUI {

	private final static String ALL_CUSTOMERS_OPTION = "All Customers";
	private final static String ALL_ORDERS_OPTION = "All Orders";
	private final static String NOT_SELECTED_OPTION = "Not Selected";
	
	QueryHandler queryHandler;
	ResultSet resultSet;
	GridPane gridPane;
	
	int rowOffset, orderID;
    Date orderDate, shippingDate;
    String customerName, phone, email, shippingAddress, orderStatusDesc;

	ComboBox<String> customerIDComboBox = new ComboBox<String>();
	ComboBox<String> orderIDComboBox = new ComboBox<String>();
	ComboBox<String> orderStatusComboBox = new ComboBox<String>();
	String[] customerIDLists, playerIDLists, orderStatusLists;
	
	TableView<OrderInfo> table = new TableView<OrderInfo>();
	ObservableList<OrderInfo> data = FXCollections.observableArrayList();
	
	Label manageOrderStatusLabel = new Label("");
	Button submitOrderStatusButton = new Button("Submit");
	
	public OrderMgtGUI(QueryHandler queryHandler) throws Exception {
		this.queryHandler = queryHandler; 
		
		// Setup Grid Pane
		gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 5, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
		ColumnConstraints col1 = new ColumnConstraints();
		ColumnConstraints col2 = new ColumnConstraints();
		ColumnConstraints col3 = new ColumnConstraints();
        gridPane.getColumnConstraints().addAll(col1, col2, col3);
        
        initializeBoxes();
        generateDisplayGUI();
	}
	
	public GridPane getGUI() {
		return gridPane;
	}
	
	@SuppressWarnings("unchecked")
	private void initializeBoxes() throws Exception {
		customerIDComboBox.setPrefWidth(180);
		orderIDComboBox.setPrefWidth(180);
		orderStatusComboBox.setPrefWidth(180);
		table.setPrefWidth(1150);
		table.setPrefHeight(340);
		refreshCustomerIDComboBox();
		refreshOrderIDComboBox();
		refreshOrderStatusComboBox();
		customerIDComboBox.setOnAction(event -> {
			try {
				selectCustomerIDComboBox();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		orderIDComboBox.setOnAction(event -> {
			try {
				selectOrderIDComboBox();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		submitOrderStatusButton.setOnAction(event -> {
			try {
				clickSubmitOrderStatusButton();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
	    // Create TableColumns and define the results
        TableColumn<OrderInfo, String> orderIDColumn = new TableColumn<>("Order ID");
        orderIDColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        orderIDColumn.setStyle("-fx-alignment: CENTER;");
        orderIDColumn.setPrefWidth(80);

        TableColumn<OrderInfo, String> customerNameColumn = new TableColumn<>("Customer Name");
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerNameColumn.setStyle("-fx-alignment: CENTER;");
        customerNameColumn.setPrefWidth(120);

        TableColumn<OrderInfo, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.setStyle("-fx-alignment: CENTER;");
        phoneColumn.setPrefWidth(100);

        TableColumn<OrderInfo, Date> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setStyle("-fx-alignment: CENTER;");
        emailColumn.setPrefWidth(250);

        TableColumn<OrderInfo, String> orderDateColumn = new TableColumn<>("Order Date");
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        orderDateColumn.setStyle("-fx-alignment: CENTER;");
        orderDateColumn.setPrefWidth(90);

        TableColumn<OrderInfo, String> shippingDateColumn = new TableColumn<>("Shipping Date");
        shippingDateColumn.setCellValueFactory(new PropertyValueFactory<>("shippingDate"));
        shippingDateColumn.setStyle("-fx-alignment: CENTER;");
        shippingDateColumn.setPrefWidth(90);

        TableColumn<OrderInfo, String> shippingAddressColumn = new TableColumn<>("Shipping Address");
        shippingAddressColumn.setCellValueFactory(new PropertyValueFactory<>("shippingAddress"));
        shippingAddressColumn.setStyle("-fx-alignment: CENTER;");
        shippingAddressColumn.setPrefWidth(300);

        TableColumn<OrderInfo, String> orderStatusDescColumn = new TableColumn<>("Order Status");
        orderStatusDescColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatusDesc"));
        orderStatusDescColumn.setStyle("-fx-alignment: CENTER;");
        orderStatusDescColumn.setPrefWidth(100);
        
        table.getColumns().addAll(orderIDColumn, customerNameColumn, phoneColumn, emailColumn,
        		orderDateColumn, shippingDateColumn, shippingAddressColumn, orderStatusDescColumn);
        
        selectOrderIDComboBox();
	}
	
	private void generateDisplayGUI() {
		rowOffset = 0;
        gridPane.add(new Label("------ Select Customer ID ------"), 0, rowOffset);
        gridPane.add(new Label("------ Select Order ID ------"), 1, rowOffset);
        rowOffset++;
        gridPane.add(customerIDComboBox, 0, rowOffset);
        gridPane.add(orderIDComboBox, 1, rowOffset);
        rowOffset++;
        gridPane.add(new Separator(), 0, rowOffset, 2, 1);
        rowOffset++;
        gridPane.add(table, 0, rowOffset, 2, 1);
        rowOffset = rowOffset +2;
        manageOrderStatusLabel.setText("Manage Order Status (Disabled when multi-records in the table)");
        gridPane.add(manageOrderStatusLabel, 0, rowOffset, 2, 1);
        rowOffset++;
        gridPane.add(orderStatusComboBox, 0, rowOffset);
        rowOffset++;
        gridPane.add(submitOrderStatusButton,  0, rowOffset);
        rowOffset++;
	}
	
	public void refreshCustomerIDComboBox() throws Exception {
		int i; 
		
		// Temporary disable set on action
		customerIDComboBox.setOnAction(null);
			
		// Insert "All Customers" option and the current available player ID list to combo box
		customerIDLists = new String[queryHandler.getCustomerCount()+1];
		customerIDLists[0] = ALL_CUSTOMERS_OPTION;
		i = 1;
		resultSet = queryHandler.getCustomerIDList();
		while (resultSet.next()) {
			customerIDLists[i] = Integer.toString(resultSet.getInt(1));
			i++;
		}
		
		customerIDComboBox.getItems().clear();
		customerIDComboBox.getSelectionModel().clearSelection();
		customerIDComboBox.setItems(FXCollections.observableArrayList(customerIDLists));
		customerIDComboBox.getSelectionModel().selectFirst();
		
		// Resume set on action
		customerIDComboBox.setOnAction(event -> {
			try {
				selectCustomerIDComboBox();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public void refreshOrderIDComboBox() throws Exception {
		int i;
		String option = customerIDComboBox.getValue();
			
		// Insert "All Orders" option and the current available player ID list to combo box
		playerIDLists = (option == ALL_CUSTOMERS_OPTION) ? new String[queryHandler.getOrderCount()+1] : 
			new String[queryHandler.getOrderCountByCustomerID(Integer.parseInt(option))+1];
		playerIDLists[0] = ALL_ORDERS_OPTION;
		i = 1;
		resultSet = (option == ALL_CUSTOMERS_OPTION) ? queryHandler.getOrderIDList() : queryHandler.getOrderIDListByCustomerID(Integer.parseInt(option));
		while (resultSet.next()) {
			playerIDLists[i] = Integer.toString(resultSet.getInt(1));
			i++;
		}
		
		orderIDComboBox.getItems().clear();
		orderIDComboBox.getSelectionModel().clearSelection();
		orderIDComboBox.setItems(FXCollections.observableArrayList(playerIDLists));
		orderIDComboBox.getSelectionModel().selectFirst();
	}
	
	public void refreshOrderStatusComboBox() throws Exception {
		int i = 0;
		String orderIDOption = orderIDComboBox.getValue();
				
		orderStatusComboBox.getItems().clear();
		orderStatusComboBox.getSelectionModel().clearSelection();

		if (orderIDOption == ALL_ORDERS_OPTION) {
			orderStatusLists = new String[1];
			orderStatusLists[0] = NOT_SELECTED_OPTION;
			
			orderStatusComboBox.setItems(FXCollections.observableArrayList(orderStatusLists));
			manageOrderStatusLabel.setText("Manage Order Status (Disabled when multi-records in the table)");
			orderStatusComboBox.getSelectionModel().selectFirst();
			orderStatusComboBox.setDisable(true);
			submitOrderStatusButton.setDisable(true);
		} else {
			orderStatusLists = new String[queryHandler.getOrderStatusCount()];
			resultSet = queryHandler.getOrderStatusList();
			while (resultSet.next()) {
				orderStatusLists[i] = resultSet.getString(1);
				i++;
			}
			orderStatusComboBox.setItems(FXCollections.observableArrayList(orderStatusLists));
			manageOrderStatusLabel.setText("Manage Order Status (Order ID " + orderID + ")");
			orderStatusComboBox.getSelectionModel().select(orderStatusDesc);
			orderStatusComboBox.setDisable(false);
			submitOrderStatusButton.setDisable(false);
		}
	}
	
	private void selectCustomerIDComboBox() throws Exception {
		String option = customerIDComboBox.getValue();
		refreshOrderIDComboBox();
		
		if (option == ALL_CUSTOMERS_OPTION) {
			resultSet = queryHandler.getOrderInformationAll();
		} else if (option != null) {
			resultSet = queryHandler.getOrderInformationByCustomerID(Integer.parseInt(option));
		}
		
		data.clear();
		while (resultSet.next()) {
		    orderID = resultSet.getInt("orderID");
		    customerName = resultSet.getString("customerName");
		    phone = resultSet.getString("phone");
		    email = resultSet.getString("email");
		    orderDate = resultSet.getDate("orderDate");
		    shippingDate = resultSet.getDate("shippingDate");
		    shippingAddress = resultSet.getString("shippingAddress");
		    orderStatusDesc = resultSet.getString("description");
            data.add(new OrderInfo(orderID, customerName, phone, email, orderDate, shippingDate, shippingAddress, orderStatusDesc));
        }
		
		table.setItems(data);
	}
	
	private void selectOrderIDComboBox() throws Exception {
		String orderIDOption = orderIDComboBox.getValue();
		String customerIDOption = customerIDComboBox.getValue();
		
		if (orderIDOption == ALL_ORDERS_OPTION) {
			if (customerIDOption == ALL_CUSTOMERS_OPTION) {
				resultSet = queryHandler.getOrderInformationAll();
			} else {
				resultSet = queryHandler.getOrderInformationByCustomerID(Integer.parseInt(customerIDOption));
			}
		} else if (orderIDOption != null) {
			resultSet = queryHandler.getOrderInformationByOrderID(Integer.parseInt(orderIDOption));
		}
		
		data.clear();
		while (resultSet.next()) {
		    orderID = resultSet.getInt("orderID");
		    customerName = resultSet.getString("customerName");
		    phone = resultSet.getString("phone");
		    email = resultSet.getString("email");
		    orderDate = resultSet.getDate("orderDate");
		    shippingDate = resultSet.getDate("shippingDate");
		    shippingAddress = resultSet.getString("shippingAddress");
		    orderStatusDesc = resultSet.getString("description");
            data.add(new OrderInfo(orderID, customerName, phone, email, orderDate, shippingDate, shippingAddress, orderStatusDesc));
        }
		
		table.setItems(data);
		refreshOrderStatusComboBox();
	}
	
	private void clickSubmitOrderStatusButton() throws Exception {
		// Perform field validation
		String orderStatusOption = orderStatusComboBox.getValue();
		if (orderStatusOption.equals(orderStatusDesc)) {
			JOptionPane.showMessageDialog(null, "You have to choose a different status to submit");
			return;
		}
		
		orderStatusDesc = orderStatusOption;
		queryHandler.updateOrderStatusByID(orderID, orderStatusDesc);
		JOptionPane.showMessageDialog(null, "Order ID " + orderID + " is now " + orderStatusDesc);
		
		// Refresh the order table after update
		selectOrderIDComboBox();
	}
}
