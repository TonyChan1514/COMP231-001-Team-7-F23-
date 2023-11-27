package application;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

class QueryHandlerTest {
	static Connection mockConnection;
	static PreparedStatement mockPreparedStatement;
	static CallableStatement mockCallableStatement;
	static ResultSet mockResult;
	
	@BeforeEach
	void setUpBeforeTest() throws Exception {
		mockConnection = mock(Connection.class);
		mockPreparedStatement = mock(PreparedStatement.class);
		mockCallableStatement = mock(CallableStatement.class);
		mockResult = mock(ResultSet.class);
		Mockito.when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
		Mockito.when(mockConnection.prepareCall(anyString())).thenReturn(mockCallableStatement);
		Mockito.when(mockPreparedStatement.executeQuery()).thenReturn(mockResult);
		Mockito.when(mockCallableStatement.executeQuery()).thenReturn(mockResult);
	}

	@Test
	void testNetworkLogCreatedAtInstantiation() throws Exception {
		QueryHandler handler = new QueryHandler(mockConnection);
		
		verify(mockConnection).prepareStatement(anyString());
		verify(mockPreparedStatement).executeQuery();
	}
	
	@Test
	void testGetLastCustomerID() throws Exception {
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		
		QueryHandler handler = new QueryHandler(mockConnection);
		handler.getLastCustomerID();
		
		verify(mockConnection, atLeast(2)).prepareStatement(captor.capture());
		// Ensure statement assesses correct table.
		Assertions.assertTrue(captor.getValue().contains("comp231_customers"));
		verify(mockPreparedStatement, atLeast(2)).executeQuery();
		verify(mockResult).getInt(anyString());
	}
	
	@Test
	void testGetLastDrugID() throws Exception {
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		
		QueryHandler handler = new QueryHandler(mockConnection);
		handler.getLastDrugID();
		
		verify(mockConnection, atLeast(2)).prepareStatement(captor.capture());
		// Ensure statement assesses correct table.
		Assertions.assertTrue(captor.getValue().contains("comp231_drugs"));
		verify(mockPreparedStatement, atLeast(2)).executeQuery();
		verify(mockResult).getInt(anyString());
	}
	
	@Test
	void testGetPrescriptionCountByCustomerAndDrug() throws Exception {
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		final int CUSTOMER_ID = 100;
		final int DRUG_ID = 200;
		
		QueryHandler handler = new QueryHandler(mockConnection);
		handler.getPrescriptionCountByCustomerAndDrug(CUSTOMER_ID, DRUG_ID);
		
		verify(mockConnection, atLeast(2)).prepareStatement(captor.capture());
		// Ensure statement assesses correct table.
		Assertions.assertTrue(captor.getValue().contains("comp231_prescriptions"));
		
		// Ensure the correct arguments are used when preparing statements.
		verify(mockPreparedStatement).setInt(1, CUSTOMER_ID);
		verify(mockPreparedStatement).setInt(2, DRUG_ID);
		verify(mockPreparedStatement, atLeast(2)).executeQuery();
		verify(mockResult).getInt(anyString());
	}
	
	@Test
	void testCustomerCheckIn() throws Exception {
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		final int CUSTOMER_ID = 50;
		
		QueryHandler handler = new QueryHandler(mockConnection);
		handler.customerCheckIn(CUSTOMER_ID);
		
		verify(mockConnection, atMost(1)).prepareCall(captor.capture());
		// Ensure statement calls correct package functions in Oracle DB.
		Assertions.assertTrue(captor.getValue().contains("shopping_handler_pkg.customer_checkIn"));
		
		verify(mockCallableStatement).setInt(1, CUSTOMER_ID);
		verify(mockCallableStatement, atLeast(2)).registerOutParameter(anyInt(), eq(Types.INTEGER));
		verify(mockCallableStatement).execute();
		verify(mockCallableStatement, atLeast(2)).getInt(anyInt());
	}

}
