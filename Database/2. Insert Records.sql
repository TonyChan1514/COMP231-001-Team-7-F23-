
-- Testing Records
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Peter', 'Wong', '117 Omni Dr., Unit 1107, Scar, ON', '647-1147-1432', 'peter.hk.wong@yahoo.com');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Johnny', 'Johnson', '112 Sammi Aveune, Unit 407, Scar, ON', '641-5678-4431', 'johnny.ck.johnson@my.centennialcollege.ca');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Sammi', 'Anson', '857 McCowan Road, Scarborough, ON', '403-7541-6387', 'sammi.x.smith@gmail.com');

INSERT INTO comp231_drugs (drugID, isPrescription, drugName, supplyCost, retailPrice, stock, prescriptionLimit)
    VALUES(seq_drugID.NEXTVAL, 0, 'Panadol', 4.00, 12.00, 400, 10);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, supplyCost, retailPrice, stock, prescriptionLimit)
    VALUES(seq_drugID.NEXTVAL, 1, 'Sertraline', 2.00, 7.00, 100, 20);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, supplyCost, retailPrice, stock, prescriptionLimit)
    VALUES(seq_drugID.NEXTVAL, 0, 'Tylenol', 5.00, 18.00, 500, 30);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, supplyCost, retailPrice, stock, prescriptionLimit)
    VALUES(seq_drugID.NEXTVAL, 0, 'Aspirin', 4.50, 14.00, 450, 40);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, supplyCost, retailPrice, stock, prescriptionLimit)
    VALUES(seq_drugID.NEXTVAL, 1, 'Seroquel', 8.00, 25.00, 250, 50);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, supplyCost, retailPrice, stock, prescriptionLimit)
    VALUES(seq_drugID.NEXTVAL, 0, 'Vitamin C Tablet', 7.00, 31.00, 120, 20);

INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES (seq_prescriptionID.NEXTVAL, 1, 2, 'Peter Wong', TO_DATE('1993-04-01', 'YYYY-MM-DD'), 4, 3, 'Dr. Lee', '6421 XA Rd, Scar, ON', '647-342-1312');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES(seq_prescriptionID.NEXTVAL, 2, 2, 'Johnny Johnson', TO_DATE('1970-03-13', 'YYYY-MM-DD'), 2, 1, 'Dr. Fung', '2341 Midland Ave, Scarborough, ON', '647-322-4227');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES(seq_prescriptionID.NEXTVAL, 3, 5, 'Sammi Anson', TO_DATE('1946-07-17', 'YYYY-MM-DD'), 1, 0, 'Dr. Cheung', '961 Good St, Markham, ON', '647-344-6299');

INSERT INTO comp231_orderstatus_map (orderStatus, description) VALUES(1, 'CREATED');
INSERT INTO comp231_orderstatus_map (orderStatus, description) VALUES(2, 'PACKAGED');
INSERT INTO comp231_orderstatus_map (orderStatus, description) VALUES(3, 'SHIPPED');
INSERT INTO comp231_orderstatus_map (orderStatus, description) VALUES(4, 'DELIVERED');
INSERT INTO comp231_orderstatus_map (orderStatus, description) VALUES(9, 'RETURNED');

COMMIT;