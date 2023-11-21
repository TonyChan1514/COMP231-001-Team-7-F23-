package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
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
        
        // put combo box in GUI.
        this.placeMonthComboBox();
        
        // fetch sales report information and display them in GUI.
        this.showSalesFigure();
	}
	
	public GridPane getGUI() {
		return this.gridPane;
	}
	
	private void placeMonthComboBox() {
		VBox vbox = new VBox(5);
		vbox.setPadding(new Insets(10));
		
		Label label = new Label("Starting Month");
		vbox.getChildren().add(label);
		vbox.getChildren().add(this.startingMonthBox);
		
		this.gridPane.add(vbox, 0, 0);
		
		VBox vbox2 = new VBox(5);
		vbox2.setPadding(new Insets(10));
		
		Label label2 = new Label("Ending Month");
		vbox2.getChildren().add(label2);
		vbox2.getChildren().add(this.endingMonthBox);
		
		this.gridPane.add(vbox2, 1, 0);
		this.startingMonthBox.setOnAction(e -> {
			this.refreshMonthSelector();
		});
	}
	
	private void refreshMonthSelector() {
		ObservableList<String> startingMonthList = FXCollections.observableArrayList();
		startingMonthList.add("--------");
		startingMonthList.addAll(this.availableMonths);
		this.startingMonthBox.setItems(startingMonthList);
		
		int startingIndex = this.startingMonthBox.getSelectionModel().getSelectedIndex();
		
		// No boxes are selected.
		if (startingIndex <= 0) {
			this.endingMonthBox.setItems(startingMonthList);
			this.startingMonthBox.getSelectionModel().selectFirst();
			this.endingMonthBox.getSelectionModel().selectFirst();
		} else {
			ObservableList<String> endingMonthList = FXCollections.observableArrayList(startingMonthList.subList(startingIndex, startingMonthList.size()));
			this.endingMonthBox.setItems(endingMonthList);
		}
	}
	
	private void showSalesFigure() {
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
		
		this.generateTable(this.salesData);
		this.refreshMonthSelector();
	}
	
	@SuppressWarnings("unchecked")
	private void generateTable(ObservableList<MonthlySale> data) {
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
		table.setItems(data);
		gridPane.add(table, 0, 1);
		GridPane.setColumnSpan(table, 2);
	}
}
