package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class SalesReportGUI {
	
	public class MonthlySale {
		private String month;
		private double salesAmount;
		private int salesCount;
		private double supplyCost;
		private double revenue;
		
		public MonthlySale(String month, double salesAmount, int salesCount, double supplyCost, double revenue) {
			this.month = month;
			this.salesAmount = salesAmount;
			this.salesCount = salesCount;
			this.supplyCost = supplyCost;
			this.revenue = revenue;
		}
		
		public String getMonth() { return this.month; }
		public double getSalesAmount() { return this.salesAmount; }
		public int getSalesCount() { return this.salesCount; }
		public double getSupplyCost() { return this.supplyCost; }
		public double getRevenue() { return this.revenue; }
	}
	
	private GridPane gridPane;
	private QueryHandler queryHandler;
	private ObservableList<MonthlySale> salesData = FXCollections.observableArrayList();
	private ObservableList<String> availableMonths = FXCollections.observableArrayList();
	private ComboBox<String> startingMonthBox = new ComboBox<String>();
	private ComboBox<String> endingMonthBox = new ComboBox<String>();
	
	public SalesReportGUI(QueryHandler queryHandler) {
		this.gridPane = new GridPane();
		this.queryHandler = queryHandler;
		
		gridPane.setPadding(new Insets(20, 20, 5, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        this.fetchSalesFigure();
        this.placeGUIElements();
        
        // put combo box in GUI.
        this.placeSalesReportComboBox();
	}
	
	public GridPane getGUI() {
		return this.gridPane;
	}
	
	private void placeGUIElements() {
		Label title = new Label("Sales & Accounting Report Generation Page");
		this.gridPane.add(title, 0, 0);
	}
	
	private void placeSalesReportComboBox() {
		HBox hbox = new HBox(20);
		hbox.setPadding(new Insets(10));
		
		Label label = new Label("Sales Report");
		label.setPrefWidth(200);
		hbox.getChildren().add(label);
		hbox.getChildren().add(this.startingMonthBox);
		hbox.getChildren().add(this.endingMonthBox);
		
		Button generateButton = new Button("Generate Report");
		generateButton.setOnAction(e -> { 
			this.showSalesFigure(); 
		});
		hbox.getChildren().add(generateButton);
		
		this.refreshMonthSelector();
		this.gridPane.add(hbox, 0, 1);
		this.startingMonthBox.setOnAction(e -> {
			this.refreshMonthSelector();
		});
	}
	
	private void refreshMonthSelector() {
		
		// Temporary disable startingMonthBox setOnAction inside the method to prevent recursive call
		this.startingMonthBox.setOnAction(null);
		
		ObservableList<String> startingMonthList = FXCollections.observableArrayList();
		startingMonthList.addAll(this.availableMonths);
		this.startingMonthBox.setItems(startingMonthList);
		this.startingMonthBox.setPromptText("Start Date");
		this.endingMonthBox.setPromptText("End Date");
		
		int startingIndex = this.startingMonthBox.getSelectionModel().getSelectedIndex();
		
		// No boxes are selected.
		if (startingIndex <= 0) {
			this.endingMonthBox.setItems(startingMonthList);
		} else {
			ObservableList<String> endingMonthList = FXCollections.observableArrayList(startingMonthList.subList(startingIndex, startingMonthList.size()));
			this.endingMonthBox.setItems(endingMonthList);
		}
		
		// Resume the startingMonthBox setOnAction
		this.startingMonthBox.setOnAction(e -> {
	        this.refreshMonthSelector();
	    });
	}
	
	private void fetchSalesFigure() {
		ResultSet results = null;
		try {
			results = this.queryHandler.getMonthlySalesFigure();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		try {
			while (results.next()) {
				String month = results.getString("month");
				this.salesData.add(new MonthlySale(
					month,
					results.getDouble("sales_amount"),
					results.getInt("sales_count"),
					results.getDouble("supply_cost"),
					results.getDouble("revenue")
				));
				this.availableMonths.add(month);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void showSalesFigure() {
		String startDate = startingMonthBox.getValue();
		String endDate = endingMonthBox.getValue();
		 
		// Input Validation
		if (startDate == null || endDate == null) {
			JOptionPane.showMessageDialog(null, "You must select Start Date and End Date first!");
		    return;
		}
		
		// Filter the target month range
		ObservableList<MonthlySale> filteredData = FXCollections.observableArrayList();
		for (MonthlySale sale : salesData) {
	        if (sale.getMonth().compareTo(startDate) >= 0 && sale.getMonth().compareTo(endDate) <= 0) {
	            filteredData.add(sale);
	        }
	    }
		
		TableView<MonthlySale> table = new TableView<MonthlySale>();
		table.setPrefWidth(900);
		table.setPrefHeight(340);
		
		TableColumn<MonthlySale, String> monthColumn = new TableColumn<>("Month");
		monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
		monthColumn.setStyle("-fx-alignment: CENTER;");
		monthColumn.setPrefWidth(150);
		
		TableColumn<MonthlySale, Double> countColumn = new TableColumn<>("Sales Count");
		countColumn.setCellValueFactory(new PropertyValueFactory<>("salesCount"));
		countColumn.setStyle("-fx-alignment: CENTER;");
		countColumn.setPrefWidth(120);
		
		TableColumn<MonthlySale, String> amountColumn = new TableColumn<>("Sales Amount");
		amountColumn.setCellValueFactory(new PropertyValueFactory<>("salesAmount"));
		amountColumn.setStyle("-fx-alignment: CENTER;");
		amountColumn.setPrefWidth(200);
		
		TableColumn<MonthlySale, String> costColumn = new TableColumn<>("Drug Supply Cost");
		costColumn.setCellValueFactory(new PropertyValueFactory<>("supplyCost"));
		costColumn.setStyle("-fx-alignment: CENTER;");
		costColumn.setPrefWidth(200);
		
		TableColumn<MonthlySale, String> revenueColumn = new TableColumn<>("Revenue");
		revenueColumn.setCellValueFactory(new PropertyValueFactory<>("revenue"));
		revenueColumn.setStyle("-fx-alignment: CENTER;");
		revenueColumn.setPrefWidth(200);
		
		table.getColumns().addAll(monthColumn, countColumn, amountColumn, costColumn, revenueColumn);
		table.setItems(filteredData);
		gridPane.add(table, 0, 2);
		
		this.refreshMonthSelector();
	}
}
