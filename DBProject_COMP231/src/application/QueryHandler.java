
package application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import javafx.scene.control.TextField;

public class QueryHandler {
	Connection dbConnection;
	String sqlStatement; 
	PreparedStatement preparedStatement;
	ResultSet resultSet;
	
	// To handle database queries and return the necessary result for processing
	public QueryHandler(Connection dbConnection) throws Exception {
		this.dbConnection = dbConnection;
	}
	
	public void insertNewCustomer(TextField[] customerFields) throws Exception {
        String procedureCall = "{call new_record_handler_pkg.add_customer(?, ?, ?, ?, ?)}";
        CallableStatement callableStatement = dbConnection.prepareCall(procedureCall);
        for (int i = 0; i < customerFields.length; i++) {
        	callableStatement.setString(i+1, customerFields[i].getText());
		}
        callableStatement.execute();
	}
	
	public void insertNewPrescription(int customerID, int drugID, Date patientDOB, TextField[] prescriptionFields) throws Exception {
        String procedureCall = "{call new_record_handler_pkg.add_prescription(?, ?, ?, ?, ?, ?, ?, ?)}";
        CallableStatement callableStatement = dbConnection.prepareCall(procedureCall);
        callableStatement.setInt(1, customerID);
        callableStatement.setInt(2, drugID);
        callableStatement.setString(3, prescriptionFields[2].getText());
        callableStatement.setDate(4, patientDOB);
        callableStatement.setInt(5, Integer.parseInt(prescriptionFields[4].getText()));
        callableStatement.setString(6, prescriptionFields[6].getText());
        callableStatement.setString(7, prescriptionFields[7].getText());
        callableStatement.setString(8, prescriptionFields[8].getText());
        callableStatement.execute();
	}
	
	public void insertNewDrug(int isPrescription, TextField[] drugFields) throws Exception {
        String procedureCall = "{call new_record_handler_pkg.add_drug(?, ?, ?, ?)}";
        CallableStatement callableStatement = dbConnection.prepareCall(procedureCall);
        callableStatement.setInt(1, isPrescription);
        callableStatement.setString(2, drugFields[1].getText());
        callableStatement.setDouble(3, Double.parseDouble(drugFields[2].getText()));
        callableStatement.setInt(4, Integer.parseInt(drugFields[3].getText()));
        callableStatement.execute();
	}
	
	public int getLastCustomerID() throws Exception {
		// Get the last customer id 
		sqlStatement = "SELECT MAX(customerID) as last_customer_id FROM comp231_customers";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt("last_customer_id");
	}
	
	public int getLastPrescriptionID() throws Exception {
		// Get the last prescription id 
		sqlStatement = "SELECT MAX(prescriptionID) as last_prescription_id FROM comp231_prescriptions";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt("last_prescription_id");
	}
	
	public int getLastDrugID() throws Exception {
		// Get the last prescription id 
		sqlStatement = "SELECT MAX(drugID) as last_drug_id FROM comp231_drugs";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt("last_drug_id");
	}
	
	public int getLastOrderID() throws Exception {
		// Get the last order id 
		sqlStatement = "SELECT MAX(orderID) as last_order_id FROM comp231_orders";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt("last_order_id");
	}
	
	public int getCustomerCount() throws Exception {
		sqlStatement = "SELECT COUNT(*) as record_count FROM comp231_customers";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt("record_count");
	}
	
	public int getPrescriptionCount() throws Exception {
		sqlStatement = "SELECT COUNT(*) as record_count FROM comp231_prescriptions";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt("record_count");
	}
	
	public int getPrescriptionCountByCustomerAndDrug(int customerID, int drugID) throws Exception {
		sqlStatement = "SELECT COUNT(*) as record_count FROM comp231_prescriptions WHERE customerID = ? AND drugID = ?";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, customerID);
		preparedStatement.setInt(2, drugID);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt("record_count");
	}
	
	public int getDrugCount() throws Exception {
		sqlStatement = "SELECT COUNT(*) as record_count FROM comp231_drugs";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt("record_count");
	}
	
	public int getOrderCount() throws Exception {
		sqlStatement = "SELECT COUNT(*) as record_count FROM comp231_orders";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt("record_count");
	}
	
	public int getOrderCountByCustomerID(int customerID) throws Exception {
		sqlStatement = "SELECT COUNT(*) as record_count FROM comp231_orders a INNER JOIN comp231_shopping_carts b ON a.orderID = b.orderID WHERE customerID = ?";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, customerID);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt("record_count");
	}
	
	public int getOrderStatusCount() throws Exception {
		sqlStatement = "SELECT COUNT(*) as record_count FROM comp231_orderstatus_dict";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet.getInt("record_count");
	}
	
	public ResultSet getCustomerIDList() throws Exception {
		sqlStatement = "SELECT customerID FROM comp231_customers";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		return preparedStatement.executeQuery();
	}
	
	public ResultSet getPrescriptionIDList() throws Exception {
		sqlStatement = "SELECT prescriptionID FROM comp231_prescriptions ORDER BY prescriptionID";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		return preparedStatement.executeQuery();
	}
	
	public ResultSet getDrugIDList() throws Exception {
		sqlStatement = "SELECT drugID FROM comp231_drugs ORDER BY drugID";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		return preparedStatement.executeQuery();
	}
	
	public ResultSet getOrderIDList() throws Exception {
		sqlStatement = "SELECT orderID FROM comp231_orders ORDER BY orderID";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		return preparedStatement.executeQuery();
	}
	
	public ResultSet getOrderStatusList() throws Exception {
		sqlStatement = "SELECT INITCAP(orderStatusDesc) FROM comp231_orderstatus_dict ORDER BY orderStatus";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		return preparedStatement.executeQuery();
	}
	
	public ResultSet getOrderIDListByCustomerID(int customerID) throws Exception {
		sqlStatement = "SELECT a.orderID FROM comp231_orders a INNER JOIN comp231_shopping_carts b ON a.orderID = b.orderID WHERE customerID = ?";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, customerID);
		return preparedStatement.executeQuery();
	}
	
	public ResultSet getPrescriptionListByCustomerAndDrug(int customerID, int drugID) throws Exception {
		// Get the record of player ID selected
		sqlStatement = "SELECT prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone "
				+ "FROM comp231_prescriptions WHERE customerID = ? AND drugID = ?";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, customerID);
		preparedStatement.setInt(2, drugID);
		return preparedStatement.executeQuery();
	}
	
	public ResultSet getCustomerRecordByID(int customerID) throws Exception {
		// Get the record of game ID selected
		sqlStatement = "SELECT firstName, lastName, address, phone, email FROM comp231_customers WHERE customerID = ?";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, customerID);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet;
	}
	
	public ResultSet getPrescriptionRecordByID(int prescriptionID) throws Exception {
		// Get the record of player ID selected
		sqlStatement = "SELECT customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone "
				+ "FROM comp231_prescriptions WHERE prescriptionID = ?";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, prescriptionID);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet;
	}
	
	public ResultSet getDrugRecordByID(int drugID) throws Exception {
		// Get the record of player game ID selected
		sqlStatement = "SELECT isPrescription, drugName, retailPrice, stock FROM comp231_drugs WHERE drugID = ?";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, drugID);
		resultSet = preparedStatement.executeQuery();
		resultSet.next();
		return resultSet;
	}
	
	public ResultSet getOrderInformationAll() throws Exception {
		sqlStatement = "SELECT a.orderID, d.firstName || ' ' || d.lastName as customerName, d.phone, d.email, orderDate, shippingDate, shippingAddress, INITCAP(orderStatusDesc) as orderStatusDesc "
				+ "FROM comp231_orders a INNER JOIN comp231_orderstatus_dict b ON a.orderStatus = b.orderStatus "
				+ "INNER JOIN comp231_shopping_carts c ON a.orderID = c.orderID INNER JOIN comp231_customers d ON c.customerID = d.customerID ORDER BY a.orderID";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		resultSet = preparedStatement.executeQuery();
		return resultSet;
	}
	
	public ResultSet getOrderInformationByOrderID(int orderID) throws Exception {
		sqlStatement = "SELECT a.orderID, d.firstName || ' ' || d.lastName as customerName, d.phone, d.email, orderDate, shippingDate, shippingAddress, INITCAP(orderStatusDesc) as orderStatusDesc "
				+ "FROM comp231_orders a INNER JOIN comp231_orderstatus_dict b ON a.orderStatus = b.orderStatus "
				+ "INNER JOIN comp231_shopping_carts c ON a.orderID = c.orderID INNER JOIN comp231_customers d ON c.customerID = d.customerID "
				+ "WHERE a.orderID = ?";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, orderID);
		resultSet = preparedStatement.executeQuery();
		return resultSet;
	}
	
	public ResultSet getOrderInformationByCustomerID(int orderID) throws Exception {
		sqlStatement = "SELECT a.orderID, d.firstName || ' ' || d.lastName as customerName, d.phone, d.email, orderDate, shippingDate, shippingAddress, INITCAP(orderStatusDesc) as orderStatusDesc "
				+ "FROM comp231_orders a INNER JOIN comp231_orderstatus_dict b ON a.orderStatus = b.orderStatus "
				+ "INNER JOIN comp231_shopping_carts c ON a.orderID = c.orderID INNER JOIN comp231_customers d ON c.customerID = d.customerID "
				+ "WHERE d.customerID = ? ORDER BY a.orderID";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, orderID);
		resultSet = preparedStatement.executeQuery();
		return resultSet;
	}	
	
	public ResultSet getCartInformationByCartID(int cartID) throws Exception {
		sqlStatement = "SELECT b.drugName, a.prescriptionID, a.unitPrice, a.quantity FROM comp231_cart_items a "
				+ "INNER JOIN comp231_drugs b ON a.drugID = b.drugID WHERE a.cartID = ? ORDER BY b.drugName";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, cartID);
		resultSet = preparedStatement.executeQuery();
		return resultSet;
	}	
	
	public boolean updateCustomerByID(int customerID, TextField[] customerFields) throws Exception {
		// Update customer record by ID
		int i;
		sqlStatement = "UPDATE comp231_customers SET firstName = ?, lastName = ?, address = ?, phone = ?,"
				+ " email = ? WHERE customerID = ?";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		for (i = 0; i < customerFields.length; i++) {
			preparedStatement.setString(i+1, customerFields[i].getText());
		}
		preparedStatement.setInt(i+1, customerID);
		return preparedStatement.executeUpdate() > 0;
	}
	
	public boolean updatePrescriptionByID(int prescriptionID, int customerID, int drugID, Date patientDOB, TextField[] prescriptionFields) throws Exception {
		// Update prescription record by ID
		sqlStatement = "UPDATE comp231_prescriptions SET customerID = ?, drugID = ?, patientName = ?, patientDOB = ?, refills = ?, orderCount = ?, doctorName = ?, "
				+ "doctorAddress = ?, doctorPhone = ? WHERE prescriptionID = ?";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, customerID);
		preparedStatement.setInt(2, drugID);
		preparedStatement.setString(3, prescriptionFields[2].getText());
		preparedStatement.setDate(4, patientDOB);
		preparedStatement.setInt(5, Integer.parseInt(prescriptionFields[4].getText()));
		preparedStatement.setInt(6, Integer.parseInt(prescriptionFields[5].getText()));
		preparedStatement.setString(7, prescriptionFields[6].getText());
		preparedStatement.setString(8, prescriptionFields[7].getText());
		preparedStatement.setString(9, prescriptionFields[8].getText());
		preparedStatement.setInt(10, prescriptionID);
		return preparedStatement.executeUpdate() > 0;
	}
	
	public boolean updateDrugByID(int drugID, int isPrescription, TextField[] drugFields) throws Exception {
		// Update drug record by ID
		sqlStatement = "UPDATE comp231_drugs SET isPrescription = ?, drugName = ?, retailPrice = ?, stock = ? WHERE drugID = ?";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, isPrescription);
		preparedStatement.setString(2, drugFields[1].getText());
		preparedStatement.setDouble(3, Double.parseDouble(drugFields[2].getText()));
		preparedStatement.setInt(4, Integer.parseInt(drugFields[3].getText()));
		preparedStatement.setInt(5, drugID);
		return preparedStatement.executeUpdate() > 0;
	}
	
	public void updateOrderStatusByID(int orderID, String orderStatusDesc) throws Exception {
		// Update order status by order ID
		String procedureCall = "{call order_handler_pkg.update_order_status(?, shopping_handler_pkg.get_orderStatus_by_desc(?))}";
        CallableStatement callableStatement = dbConnection.prepareCall(procedureCall);
        callableStatement.setInt(1, orderID);
        callableStatement.setString(2, orderStatusDesc);
        callableStatement.execute();
	}
	
	// return value int[0] is whether a new shopping cart is created after checkin (0: No, 1: Yes)
	// return value int[1] is the shopping cart ID assigned (customer's open shopping cart, no matter new or existng)
	public int[] customerCheckIn(int customerID) throws Exception {
		int[] outputs = new int[2];
		
		// Perform customer Check In
		String procedureCall = "{call shopping_handler_pkg.customer_checkIn(?, ?, ?)}";
        CallableStatement callableStatement = dbConnection.prepareCall(procedureCall);
        callableStatement.setInt(1, customerID);
        callableStatement.registerOutParameter(2, Types.INTEGER);
        callableStatement.registerOutParameter(3, Types.INTEGER);
        callableStatement.execute();
        
        outputs[0] = callableStatement.getInt(2);
        outputs[1] = callableStatement.getInt(3); 
        		
        // Return whether new shopping cart is created
        return outputs;
	}
	
	public boolean checkIsAllowable(int drugID, int prescriptionID, int quantity) throws Exception {
		String procedureCall = "{? = call shopping_handler_pkg.is_drug_allowable(?, ?, ?)}";
        CallableStatement callableStatement = dbConnection.prepareCall(procedureCall);
        callableStatement.registerOutParameter(1, Types.BOOLEAN);
        callableStatement.setInt(2, drugID);
        if (prescriptionID == -1) {
        	callableStatement.setNull(3, Types.INTEGER);
        } else {
        	callableStatement.setInt(3, prescriptionID);
        }
        callableStatement.setInt(4, quantity);
        callableStatement.execute();
        return callableStatement.getBoolean(1);
	}
	
	public void addToShoppingCart(int drugID, int prescriptionID, int quantity) throws Exception {
		String procedureCall = "{call shopping_handler_pkg.add_to_shopping_cart(?, ?, ?)}";
        CallableStatement callableStatement = dbConnection.prepareCall(procedureCall);
        callableStatement.setInt(1, drugID);
        if (prescriptionID == -1) {
        	callableStatement.setNull(2, Types.INTEGER);
        } else {
        	callableStatement.setInt(2, prescriptionID);
        }
        callableStatement.setInt(3, quantity);
        callableStatement.execute();
	}
	
	public void clearShoppingCart(int cartID) throws Exception {
		sqlStatement = "DELETE comp231_cart_items WHERE cartID = ?";
		preparedStatement = dbConnection.prepareStatement(sqlStatement);
		preparedStatement.setInt(1, cartID);
		preparedStatement.executeQuery();
	}
	
	public void shoppingCartCheckOut(String shippingAddress) throws Exception {
		String procedureCall = "{call shopping_handler_pkg.shopping_cart_checkout(?)}";
        CallableStatement callableStatement = dbConnection.prepareCall(procedureCall);
        callableStatement.setString(1, shippingAddress);
        callableStatement.execute();
	}
}
