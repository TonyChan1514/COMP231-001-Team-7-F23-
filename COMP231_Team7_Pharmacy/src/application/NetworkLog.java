package application;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class NetworkLog {
	private int logSeqID;
    private Timestamp accessTime;
    private String accessIPAddress;

    public NetworkLog(int logSeqID, Timestamp accessTime, String accessIPAddress) {
        this.logSeqID = logSeqID;
        this.accessTime = accessTime;
        this.accessIPAddress = accessIPAddress;
    }
    
    public int getLogSeqID() {
        return logSeqID;
    }
    
    public Timestamp getAccessTime() {
    	return accessTime;
    }
    
    public String getAccessIPAddress() {
        return accessIPAddress;
    }

    public String getFormattedAccessTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(accessTime);
    }

}
