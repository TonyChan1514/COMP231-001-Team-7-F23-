package application;

public class DrugItem {
	private String drugName;
    private String prescriptionID;
    private double unitPrice;
    private int quantity;
    private int prescriptionLimit;
    
    private static int DEFAULT_PRESCRIPTION_LIMIT = 99;

    public DrugItem(String drugName, String prescriptionID, double unitPrice, int quantity) {
        this.drugName = drugName;
        this.prescriptionID = prescriptionID;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.prescriptionLimit = DEFAULT_PRESCRIPTION_LIMIT;
    }
    
    public DrugItem(String drugName, String prescriptionID, double unitPrice, int quantity, int prescriptionLimit) {
        this.drugName = drugName;
        this.prescriptionID = prescriptionID;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.prescriptionLimit = prescriptionLimit;
    }

    public String getDrugName() {
        return drugName;
    }

    public String getPrescriptionID() {
        return prescriptionID;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public int getPrescriptionLimit() {
    	return this.prescriptionLimit;
    }
}
