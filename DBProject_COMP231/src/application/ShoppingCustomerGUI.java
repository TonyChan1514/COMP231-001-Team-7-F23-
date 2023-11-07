package application;

import java.sql.ResultSet;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;

public class ShoppingCustomerGUI {
    private GridPane gridPane;
    private QueryHandler queryHandler;
    private Tab shoppingCartTab;
    private AddShoppingCartGUI addShoppingCartGUI;

    private static final String NOT_SELECTED_OPTION = "Not Selected";

    Label customerIdSearch = new Label("Select a Customer ID:");
    ComboBox<String> customerIDComboBox = new ComboBox<>();
    Label customerNameLabel = new Label();
    String[] customerIDList;
    Button confirmCustomerButton = new Button("Check In");

    public ShoppingCustomerGUI(Tab shoppingCartTab, QueryHandler queryHandler) throws Exception {
        gridPane = new GridPane();
        this.shoppingCartTab = shoppingCartTab;
        this.queryHandler = queryHandler;

        refreshCustomerIDComboBox();
        customerIDComboBox.setPrefWidth(150);
        customerIDComboBox.setOnAction(event -> {
            try {
                selectCustomerIDComboBox();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        confirmCustomerButton.setOnAction(event -> {
            try {
                clickCheckInCustomerButton();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        generateDisplayGUI();
    }

    public GridPane getGUI() {
        return gridPane;
    }

    public AddShoppingCartGUI getAddShoppingCartGUI() {
        return addShoppingCartGUI;
    }

    public void generateDisplayGUI() throws Exception {
        int rowOffset = 0;
        gridPane.add(customerIdSearch, 0, rowOffset, 1, 1);
        rowOffset++;
        gridPane.add(customerIDComboBox, 0, rowOffset, 1, 1);
        gridPane.add(customerNameLabel, 1, rowOffset, 1, 1);
        rowOffset++;
        gridPane.add(confirmCustomerButton, 0, rowOffset, 1, 1);
        rowOffset++;

        // Set gap of gridPane
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 5, 20));
    }

    public void refreshCustomerIDComboBox() throws Exception {
        int index = 0;

        // Get the customer ID count and available IDs
        customerIDList = new String[queryHandler.getCustomerCount() + 1];
        customerIDList[index++] = NOT_SELECTED_OPTION;
        ResultSet resultSet = queryHandler.getCustomerIDList();
        while (resultSet.next()) {
            customerIDList[index++] = Integer.toString(resultSet.getInt(1));
        }

        customerIDComboBox.getItems().clear();
        customerIDComboBox.getSelectionModel().clearSelection();
        customerIDComboBox.setItems(FXCollections.observableArrayList(customerIDList));
        customerIDComboBox.getSelectionModel().selectFirst();
    }

    private void selectCustomerIDComboBox() throws Exception {
        String selectedOption = customerIDComboBox.getValue();
        if (selectedOption != null && selectedOption.equals(NOT_SELECTED_OPTION)) {
            customerNameLabel.setText("");
        } else if (selectedOption != null) {
            ResultSet resultSet = queryHandler.getCustomerRecordByID(Integer.parseInt(selectedOption));
            customerNameLabel.setText(resultSet.getString("firstName") + " " + resultSet.getString("lastName"));
        }
    }

    private void clickCheckInCustomerButton() throws Exception {
        String customerIDOption = customerIDComboBox.getValue();
        if (customerIDOption.equals(NOT_SELECTED_OPTION)) {
            JOptionPane.showMessageDialog(null, "You have to select a customer ID");
            return;
        }

        int[] checkInResult = queryHandler.customerCheckIn(Integer.parseInt(customerIDOption));
        if (checkInResult[0] == 1) {
            JOptionPane.showMessageDialog(null, "New shopping cart ID " + checkInResult[1] + " is created.");
        }

        addShoppingCartGUI = new AddShoppingCartGUI(shoppingCartTab, queryHandler, Integer.parseInt(customerIDOption), checkInResult[1]);
        shoppingCartTab.setContent(addShoppingCartGUI.getGUI());
    }
}
