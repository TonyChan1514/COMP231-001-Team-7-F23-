package application;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class LowStockGUI {
	private GridPane gridPane;
	private QueryHandler queryHandler;
	
	private static int LOW_STOCK_THRESHOLD = 100;
	
	public LowStockGUI(QueryHandler queryHandler) {
		this.gridPane = new GridPane();
		this.queryHandler = queryHandler;
		
		gridPane.setPadding(new Insets(20, 20, 5, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        // fetch drug information.
		this.showDrugRecords();
	}
	
	public GridPane getGUI() {
		return this.gridPane;
	}
	
	@SuppressWarnings("unchecked")
	private void showDrugRecords() {
		Label instruction = new Label("Drugs at low inventory level will be highlighted in red.");
		instruction.setPadding(new Insets(5, 0, 10, 0));
		this.gridPane.add(instruction, 0, 0);
		
		TableView<DrugItem> table = new TableView<DrugItem>();
		table.setPrefWidth(900);
		table.setPrefHeight(340);
		
		TableColumn<DrugItem, String> drugNameColumn = new TableColumn<>("Drug Name");
		drugNameColumn.setCellValueFactory(new PropertyValueFactory<>("drugName"));
		drugNameColumn.setStyle("-fx-alignment: CENTER;");
		drugNameColumn.setPrefWidth(300);
        
		TableColumn<DrugItem, Double> drugPriceColumn = new TableColumn<>("Price");
		drugPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
		drugPriceColumn.setStyle("-fx-alignment: CENTER;");
		drugPriceColumn.setPrefWidth(160);
        
		TableColumn<DrugItem, String> drugStockColumn = new TableColumn<>("Available Stock");
		drugStockColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		drugStockColumn.setStyle("-fx-alignment: CENTER;");
		drugStockColumn.setPrefWidth(220);
		
		table.getColumns().addAll(drugNameColumn, drugPriceColumn, drugStockColumn);
		table.setRowFactory(tv -> new TableRow<DrugItem>() {
			@Override
			protected void updateItem(DrugItem drug, boolean empty) {
				super.updateItem(drug, empty);
				if (drug != null && drug.getQuantity() < LOW_STOCK_THRESHOLD) {
					setStyle("-fx-background-color: lightcoral;");
				} else {
					setStyle("");
				}
			}
		});
		
		ResultSet results = null;
		try {
			results = this.queryHandler.getDrugRecords();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		ObservableList<DrugItem> data = FXCollections.observableArrayList();
		try {
			while (results.next()) {
				DrugItem drug = new DrugItem(
					results.getString("drugName"),
					results.getString("isPrescription"),
					results.getDouble("retailPrice"),
					results.getInt("stock")
				);
				data.add(drug);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		table.setItems(data);
		gridPane.add(table, 0, 1);
	}
}
