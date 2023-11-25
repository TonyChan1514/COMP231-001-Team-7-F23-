package application;
	
import java.sql.*;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {
	private static final String hostname = "70.50.202.189";
	private static final int port = 1521;
	private static final String sid = "xe";
	private static final String dbUserName = "system";
	private static final String dbPassword = "0000abc!";
	
	Connection dbConnection;
	QueryHandler queryHandler;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Connect to the Oracle database
		Class.forName("oracle.jdbc.OracleDriver");
		dbConnection = DriverManager.getConnection("jdbc:oracle:thin:@" + hostname + ":" + port + ":" + sid, dbUserName, dbPassword);
		queryHandler = new QueryHandler(dbConnection); 
		
		InsertUpdateGUI insertUpdateGUI = new InsertUpdateGUI(queryHandler);
		OrderMgtGUI orderMgtGUI = new OrderMgtGUI(queryHandler);
		
		// Setup tab pane (with Insert/Update tab and Display tab respectively)
		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
		Tab insertUpdateTab = new Tab("Customer / Drug / Prescription Management", insertUpdateGUI.getGUI());
        tabPane.getTabs().add(insertUpdateTab);
        
        Tab shoppingCartTab = new Tab("Shopping Carts");
        ShoppingCustomerGUI customerCustomerInterface = new ShoppingCustomerGUI(shoppingCartTab, queryHandler);
        shoppingCartTab.setContent(customerCustomerInterface.getGUI());
        tabPane.getTabs().add(shoppingCartTab);
        
        Tab orderMgtTab = new Tab("Order Management", orderMgtGUI.getGUI());
        tabPane.getTabs().add(orderMgtTab);
        
        LowStockGUI lowStockInterface = new LowStockGUI(queryHandler);
        Tab drugInventoryTab = new Tab("Drug Inventory", lowStockInterface.getGUI());
        tabPane.getTabs().add(drugInventoryTab);
        
        NetworkLogGUI networkLogInterface = new NetworkLogGUI(queryHandler);
        Tab networkLogTab = new Tab("Network Log", networkLogInterface.getGUI());
        tabPane.getTabs().add(networkLogTab);

        SalesReportGUI salesReportInterface = new SalesReportGUI(queryHandler);
        Tab salesReportTab = new Tab("Sales & Accounting Report", salesReportInterface.getGUI());
        tabPane.getTabs().add(salesReportTab);
        
        tabPane.setPrefWidth(1200);
        tabPane.setPrefHeight(600);
        
        // Refresh the customer ID list for specific tabs
        shoppingCartTab.setOnSelectionChanged(event -> {
            if (shoppingCartTab.isSelected()) {
            	try {
    				customerCustomerInterface.refreshCustomerIDComboBox();
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
            }
        });
        orderMgtTab.setOnSelectionChanged(event -> {
            if (orderMgtTab.isSelected()) {
    			try {
    				orderMgtGUI.refreshOrderIDComboBox();
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
            }
        });
        salesReportTab.setOnSelectionChanged(event -> {
            if (salesReportTab.isSelected()) {
            	try {
            		salesReportInterface.fetchSalesFigure();
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
            }
        });
        drugInventoryTab.setOnSelectionChanged(event -> {
            if (drugInventoryTab.isSelected()) {
            	try {
            		lowStockInterface.showDrugRecords();
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
            } 
        });
        networkLogTab.setOnSelectionChanged(event -> {
            if (networkLogTab.isSelected()) {
            	try {
            		System.out.println("HI");
            		networkLogInterface.showNetworkLogRecords();
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
            } 
        });
        
        // Create a scene and place the tab pane
        Scene scene = new Scene(tabPane);
		primaryStage.setTitle("COMP231 Team7 - Pharmacy Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
