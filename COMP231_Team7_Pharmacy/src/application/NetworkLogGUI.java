package application;

import java.sql.Date;
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

public class NetworkLogGUI {
	private GridPane gridPane;
	private QueryHandler queryHandler;
	
	public NetworkLogGUI(QueryHandler queryHandler) {
		this.gridPane = new GridPane();
		this.queryHandler = queryHandler;
		
		gridPane.setPadding(new Insets(20, 20, 5, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        // fetch drug information.
		this.showNetworkLogRecords();
	}
	
	public GridPane getGUI() {
		return this.gridPane;
	}
	
	@SuppressWarnings("unchecked")
	private void showNetworkLogRecords() {
		Label instruction = new Label("Network Log for monitoring purpose.");
		instruction.setPadding(new Insets(5, 0, 10, 0));
		this.gridPane.add(instruction, 0, 0);
		
		TableView<NetworkLog> table = new TableView<NetworkLog>();
		table.setPrefWidth(550);
		table.setPrefHeight(500);
		
		TableColumn<NetworkLog, String> logSeqIDColumn = new TableColumn<>("Sequence ID");
		logSeqIDColumn.setCellValueFactory(new PropertyValueFactory<>("logSeqID"));
		logSeqIDColumn.setStyle("-fx-alignment: CENTER;");
		logSeqIDColumn.setPrefWidth(100);
		
		TableColumn<NetworkLog, Date> accessTimeColumn = new TableColumn<>("Access Time");
		accessTimeColumn.setCellValueFactory(new PropertyValueFactory<>("accessTime"));
		//accessTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getFormattedAccessTime());
		accessTimeColumn.setStyle("-fx-alignment: CENTER;");
		accessTimeColumn.setPrefWidth(200);
        
		TableColumn<NetworkLog, String> accessIPAddressColumn = new TableColumn<>("Access IP Address");
		accessIPAddressColumn.setCellValueFactory(new PropertyValueFactory<>("accessIPAddress"));
		accessIPAddressColumn.setStyle("-fx-alignment: CENTER;");
		accessIPAddressColumn.setPrefWidth(200);
		
		table.getColumns().addAll(logSeqIDColumn, accessTimeColumn, accessIPAddressColumn);
		table.setRowFactory(tv -> new TableRow<NetworkLog>() {
			@Override
			protected void updateItem(NetworkLog networkLog, boolean empty) {
				super.updateItem(networkLog, empty);
				setStyle("");
			}
		});
		
		ResultSet results = null;
		try {
			results = this.queryHandler.getNetworkLogRecords();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		ObservableList<NetworkLog> data = FXCollections.observableArrayList();
		try {
			while (results.next()) {
				NetworkLog networkLog = new NetworkLog(
					results.getInt("logSeqID"),
					results.getTimestamp("access_time"),
					results.getString("access_ip_address")
				);
				data.add(networkLog);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		table.setItems(data);
		gridPane.add(table, 0, 1);
	}
}
