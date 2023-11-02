
-- Testing Records
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Peter', 'Wong', '117 Omni Dr., Unit 1107, Scar, ON', '647-1147-1432', 'peter.hk.wong@yahoo.com');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Johnny', 'Johnson', '112 Sammi Aveune, Unit 407, Scar, ON', '641-5678-4431', 'johnny.ck.johnson@my.centennialcollege.ca');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Sammi', 'Smith', '850 McCowan Road, Calgary, AB, T2P 1AA', '403-7541-6387', 'sammi.x.smith@gmail.com');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'William', 'Tung', '384 Success Street, Vancouver, BC', '604-2341-1179', 'william.ca.tung@sait.com');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Tom', 'Dav', '271 Tom Rd, Markham, ON', '647-2423-2117', 'tom.aa.dav@uot.com');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Sudo', 'Blue', '2144 Hw7, Markham, ON', '647-7894-2882', 'susan.brown@yahoo.com.ca');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Joey', 'Chan', '433 Brown Street, Caifornia, USA', '530-7321-5744', 'joey.chan@gmail.com');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Karen', 'Lam', '3375 16 Street, Markham, ON', '647-7112-2324', 'karen.lam@gmail.com');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'David', 'Leo', '972 Midland St, Scarborough', '647-2354-8877', 'david.li@uot.ca');
INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
    VALUES(seq_customerID.NEXTVAL, 'Anna', 'Su', '112 Avenue NE, Calgary, AB', '403-5227-7114', 'anna.su@ucalgary.ca');


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
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, retailPrice, stock)
    VALUES(seq_drugID.NEXTVAL, 1, 'Amoxicillin', 21.00, 85);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, retailPrice, stock)
    VALUES(seq_drugID.NEXTVAL, 0, 'Vitamin D Tablet', 28.00, 90);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, retailPrice, stock)
    VALUES(seq_drugID.NEXTVAL, 1, 'Linezolid', 19.00, 100);
INSERT INTO comp231_drugs (drugID, isPrescription, drugName, retailPrice, stock)
    VALUES(seq_drugID.NEXTVAL, 1, 'Penicillin', 31.00, 130);

INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES (seq_prescriptionID.NEXTVAL, 1, 2, 'Peter Wong', TO_DATE('1993-04-01', 'YYYY-MM-DD'), 4, 3, 'Dr. Lee', '6421 XA Rd, Scar, ON', '647-342-1312');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES(seq_prescriptionID.NEXTVAL, 2, 2, 'Johnny Johnson', TO_DATE('1970-03-13', 'YYYY-MM-DD'), 2, 1, 'Dr. Fung', '2341 Midland Ave, Scarborough, ON', '647-322-4227');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES(seq_prescriptionID.NEXTVAL, 3, 5, 'Sammi Smith', TO_DATE('1946-04-23', 'YYYY-MM-DD'), 1, 0, 'Dr. Cheung', '961 Good St, Markham, ON', '647-344-6299');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES(seq_prescriptionID.NEXTVAL, 3, 6, 'Eddy Smith', TO_DATE('1953-11-21', 'YYYY-MM-DD'), 2, 1, 'Dr. Sunny', '12 Pham Ave, Surrey, ON', '647-544-2762');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES(seq_prescriptionID.NEXTVAL, 5, 7, 'Javk Davis', TO_DATE('2001-08-09', 'YYYY-MM-DD'), 0, 0, 'Dr. Wong', '403-570 Ellemere St, Scarborough, ON', '647-754-4121');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES(seq_prescriptionID.NEXTVAL, 6, 7, 'Sudo Blue', TO_DATE('1976-05-12', 'YYYY-MM-DD'), 1, 1, 'Dr. Siu', '721 HiWay 6, Markham, ON', '647-533-2123');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES(seq_prescriptionID.NEXTVAL, 6, 9, 'Samuel Brown', TO_DATE('1993-08-02', 'YYYY-MM-DD'), 0, 0, 'Dr. Sun', '611 QE Ave, Surrey, ON', '647-544-2762');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES (seq_prescriptionID.NEXTVAL, 8, 9, 'Lawerence Lam', TO_DATE('1992-06-17', 'YYYY-MM-DD'), 3, 2, 'Dr. Sunw', '587 AX Ave, Surrey, ON', '647-544-1231');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES (seq_prescriptionID.NEXTVAL, 9, 10, 'David Leo', TO_DATE('1998-07-13', 'YYYY-MM-DD'), 3, 2, 'Dr. Fung', '20 XX Road, Pickering, ON', '647-231-1231');
INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
	VALUES (seq_prescriptionID.NEXTVAL, 9, 10, 'Johnny Li', TO_DATE('2010-07-15', 'YYYY-MM-DD'), 0, 0, 'Dr. Way', '789 Gd RD N, Richmond Hill, ON', '647-774-7312');
    
INSERT INTO comp231_orders (orderID, orderDate, shippingDate, shippingaddress, orderStatus)
    VALUES (seq_orderID.NEXTVAL, TO_DATE('2023-07-20', 'yyyy-mm-dd'), NULL, '2108-118 Omni Drive, Scarborough, ON' , 1);
INSERT INTO comp231_orders (orderID, orderDate, shippingDate, shippingaddress, orderStatus)
    VALUES(seq_orderID.NEXTVAL, TO_DATE('2023-07-28', 'yyyy-mm-dd'), TO_DATE('2023-07-30', 'yyyy-mm-dd'), '2407-118 Omni Drive, Scarborough, ON', 3);
INSERT INTO comp231_orders (orderID, orderDate, shippingDate, shippingaddress, orderStatus)
    VALUES (seq_orderID.NEXTVAL, TO_DATE('2023-07-25', 'yyyy-mm-dd'), NULL, '800 Macleod Trail, Calgary, AB, T2P 1AA', 2);
INSERT INTO comp231_orders (orderID, orderDate, shippingDate, shippingaddress, orderStatus)
    VALUES(seq_orderID.NEXTVAL, TO_DATE('2023-06-25', 'yyyy-mm-dd'), NULL, '384 Success Street, Vancouver, BC', 2);
INSERT INTO comp231_orders (orderID, orderDate, shippingDate, shippingaddress, orderStatus)
    VALUES(seq_orderID.NEXTVAL, TO_DATE('2023-07-21', 'yyyy-mm-dd'), TO_DATE('2023-07-28', 'yyyy-mm-dd'), '647 Ellemere Road, Scarborough, ON', 4);
INSERT INTO comp231_orders (orderID, orderDate, shippingDate, shippingaddress, orderStatus)
    VALUES(seq_orderID.NEXTVAL, TO_DATE('2023-07-20', 'yyyy-mm-dd'), TO_DATE('2023-07-25', 'yyyy-mm-dd'), '520 Ellemere Street, Scarborough, ON', 3);
INSERT INTO comp231_orders (orderID, orderDate, shippingDate, shippingaddress, orderStatus)
    VALUES(seq_orderID.NEXTVAL, TO_DATE('2023-07-19', 'yyyy-mm-dd'), NULL, '642 Oak Ave., Toronto, ON', 1);
INSERT INTO comp231_orders (orderID, orderDate, shippingDate, shippingaddress, orderStatus)
    VALUES(seq_orderID.NEXTVAL, TO_DATE('2023-06-29', 'yyyy-mm-dd'), TO_DATE('2023-07-05', 'yyyy-mm-dd'), '3375 16 Street, Markham, ON', 9);
INSERT INTO comp231_orders (orderID, orderDate, shippingDate, shippingaddress, orderStatus)
    VALUES(seq_orderID.NEXTVAL, TO_DATE('2023-07-11', 'yyyy-mm-dd'), NULL, '972 Midland St, Scarborough', 1);
INSERT INTO comp231_orders (orderID, orderDate, shippingDate, shippingaddress, orderStatus)
    VALUES(seq_orderID.NEXTVAL, TO_DATE('2023-07-20', 'yyyy-mm-dd'), NULL, '650 Wong Ave., Markham, ON', 1);

INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 1, 2, 1, 26.00);
INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 2, 2, 2, 5.00);
INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 3, 2, 3, 70.00);
INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 4, 2, 4, 56.00);
INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 5, 2, 5, 30.00);
INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 6, 2, 6, 48.00);
INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 7, 2, 7, 366.00);
INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 8, 2, 8, 470.00);
INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 9, 2, 9, 152.00);
INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 10, 2, 10, 105.00);
INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 1, 1, null, 29.00);
INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 2, 1, null, 32.00);
INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, orderID, cartTotal)
    VALUES(seq_cartID.NEXTVAL, 3, 1, null, 10.00);

INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(1,2,1,5,2);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(1,4,NULL,8,2);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(2,2,2,5,1);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(3,5,3,20,2);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(3,6,4,30,1);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(4,4,NULL,8,7);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(5,7,5,15,2);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(6,3,NULL,12,4);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(7,8,NULL,35,10);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(7,4,NULL,8,2);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(8,9,8,20,1);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(8,6,NULL,30,15);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(9,3,NULL,12,6);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(9,4,NULL,8,10);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(10,8,NULL,35,3);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(11,2,1,5,1);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(11,3,NULL,12,2);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(12,4,NULL,8,4);
INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
    VALUES(13,1,NULL,10,1);

COMMIT;