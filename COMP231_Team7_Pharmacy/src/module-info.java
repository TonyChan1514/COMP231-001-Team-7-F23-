module COMP231_Team7_Pharmacy {
	requires javafx.controls;
	requires java.sql;
	requires java.desktop;
	requires javafx.base;
	requires javafx.graphics;
	requires org.junit.jupiter.api;
	requires org.mockito;
	requires net.bytebuddy;
	requires net.bytebuddy.agent;
	requires org.objenesis;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
}
