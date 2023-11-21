package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
		
		public MonthlySale(String month, double salesAmount, int salesCount) {
			this.month = month;
			this.salesAmount = salesAmount;
			this.salesCount = salesCount;
		}
		
		public String getMonth() { return this.month; }
		public double getSalesAmount() { return this.salesAmount; }
		public int getSalesCount() { return this.salesCount; }
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
        this.placeAccountingReportComboBox();
	}
	
	public GridPane getGUI() {
		return this.gridPane;
	}
	
	private void placeGUIElements() {
		Label title = new Label("Report Generation Page");
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
		generateButton.setOnAction(e -> { this.showSalesFigure(); });
		hbox.getChildren().add(generateButton);
		
		this.refreshMonthSelector();
		this.gridPane.add(hbox, 0, 1);
		this.startingMonthBox.setOnAction(e -> {
			this.refreshMonthSelector();
		});
	}
	
	private void placeAccountingReportComboBox() {
		HBox hbox = new HBox(20);
		hbox.setPadding(new Insets(10));
		
		Label label = new Label("Sales Report");
		label.setPrefWidth(200);
		hbox.getChildren().add(label);
		hbox.getChildren().add(this.startingMonthBox);
		hbox.getChildren().add(this.endingMonthBox);
		
		Button generateButton = new Button("Generate Report");
		generateButton.setOnAction(e -> { this.showSalesFigure(); });
		hbox.getChildren().add(generateButton);
		
		this.refreshMonthSelector();
		this.gridPane.add(hbox, 0, 1);
		this.startingMonthBox.setOnAction(e -> {
			this.refreshMonthSelector();
		});
	}
	
	private void refreshMonthSelector() {
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
					results.getInt("sales_count")
				));
				this.availableMonths.add(month);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void showSalesFigure() {
		ObservableList<MonthlySale> salesData = this.salesData;
		
		TableView<MonthlySale> table = new TableView<MonthlySale>();
		table.setPrefWidth(900);
		table.setPrefHeight(340);
		
		TableColumn<MonthlySale, String> monthColumn = new TableColumn<>("Month");
		monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
		monthColumn.setStyle("-fx-alignment: CENTER;");
		monthColumn.setPrefWidth(250);
		
		TableColumn<MonthlySale, String> amountColumn = new TableColumn<>("Amount");
		amountColumn.setCellValueFactory(new PropertyValueFactory<>("salesAmount"));
		amountColumn.setStyle("-fx-alignment: CENTER;");
		amountColumn.setPrefWidth(250);
        
		TableColumn<MonthlySale, Double> countColumn = new TableColumn<>("Sales Count");
		countColumn.setCellValueFactory(new PropertyValueFactory<>("salesCount"));
		countColumn.setStyle("-fx-alignment: CENTER;");
		countColumn.setPrefWidth(250);
		
		table.getColumns().addAll(monthColumn, amountColumn, countColumn);
		table.setItems(salesData);
		gridPane.add(table, 0, 2);
		
		this.refreshMonthSelector();
	}
}
