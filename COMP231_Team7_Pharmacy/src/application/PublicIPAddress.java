package application;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PublicIPAddress {
	
	public static String getPublicIPAddress() {
        try {
            // Refer to external API resources
        	URL url = new URL("https://api.ipify.org?format=json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            reader.close();

            // Parse the JSON response
            String publicIP = response.split(":")[1].replaceAll("[\"}]","").trim();

            return publicIP;
        } catch (IOException e) {
            e.printStackTrace();
            return "Unable to retrieve public IP address";
        }
	}
}
