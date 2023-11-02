
-- Testing Records
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Peter', 'Wong', '117 Omni Dr., Unit 1107, Scar, ON', '647-1147-1432', 'peter.hk.wong@yahoo.com');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Johnny', 'Johnson', '112 Sammi Aveune, Unit 407, Scar, ON', '641-5678-4431', 'johnny.ck.johnson@my.centennialcollege.ca');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Sammi', 'Smith', '850 McCowan Road, Calgary, AB, T2P 1AA', '403-7541-6387', 'sammi.x.smith@gmail.com');

INSERT INTO comp231_drugs (drugID, isPrescription, drugName, retailPrice, stock)
    VALUES(seq_drugID.NEXTVAL, 0, 'Panadol', 12.00, 400);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, retailPrice, stock)
    VALUES(seq_drugID.NEXTVAL, 1, 'Sertraline', 7.00, 100);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, retailPrice, stock)
    VALUES(seq_drugID.NEXTVAL, 0, 'Tylenol', 18.00, 500);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, retailPrice, stock)
    VALUES(seq_drugID.NEXTVAL, 0, 'Aspirin', 14.00, 450);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, retailPrice, stock)
    VALUES(seq_drugID.NEXTVAL, 1, 'Seroquel', 25.00, 250);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, retailPrice, stock)
    VALUES(seq_drugID.NEXTVAL, 0, 'Vitamin C Tablet', 31.00, 120);

INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES (seq_prescriptionID.NEXTVAL, 1, 2, 'Peter Wong', TO_DATE('1993-04-01', 'YYYY-MM-DD'), 4, 3, 'Dr. Lee', '6421 XA Rd, Scar, ON', '647-342-1312');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES(seq_prescriptionID.NEXTVAL, 2, 2, 'Johnny Johnson', TO_DATE('1970-03-13', 'YYYY-MM-DD'), 2, 1, 'Dr. Fung', '2341 Midland Ave, Scarborough, ON', '647-322-4227');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES(seq_prescriptionID.NEXTVAL, 3, 5, 'Sammi Smith', TO_DATE('1946-04-23', 'YYYY-MM-DD'), 1, 0, 'Dr. Cheung', '961 Good St, Markham, ON', '647-344-6299');

INSERT INTO comp231_orderstatus_dict (orderStatus, orderStatusDesc)
    VALUES(1, 'CREATED');
INSERT INTO comp231_orderstatus_dict (orderStatus, orderStatusDesc)
    VALUES(2, 'PACKAGED');
INSERT INTO comp231_orderstatus_dict (orderStatus, orderStatusDesc)
    VALUES(3, 'SHIPPED');
INSERT INTO comp231_orderstatus_dict (orderStatus, orderStatusDesc)
    VALUES(4, 'DELIVERED');
INSERT INTO comp231_orderstatus_dict (orderStatus, orderStatusDesc)
    VALUES(9, 'RETURNED');

COMMIT;