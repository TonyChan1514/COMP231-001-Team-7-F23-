package application;

import java.sql.Date;

public class NetworkLog {
	private int logSeqID;
    private Date accessTime;
    private String accessIPAddress;

    public NetworkLog(int logSeqID, Date accessTime, String accessIPAddress) {
        this.logSeqID = logSeqID;
        this.accessTime = accessTime;
        this.accessIPAddress = accessIPAddress;
    }
    
    public int getLogSeqID() {
        return logSeqID;
    }
    
    public Date getAccessTime() {
    	return accessTime;
    }
    
    public String getAccessIPAddress() {
        return accessIPAddress;
    }
}
