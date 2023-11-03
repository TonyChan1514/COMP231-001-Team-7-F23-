package application;

public class DrugItem {
	private String drugName;
    private String prescriptionID;
    private double unitPrice;
    private int quantity;

    public DrugItem(String drugName, String prescriptionID, double unitPrice, int quantity) {
        this.drugName = drugName;
        this.prescriptionID = prescriptionID;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
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
}
