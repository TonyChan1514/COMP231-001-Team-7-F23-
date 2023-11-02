
-- DELETE BEFORE CREATION
DROP TABLE comp231_cart_items;
DROP TABLE comp231_shopping_carts;
DROP TABLE comp231_orders;
DROP TABLE comp231_prescriptions;
DROP TABLE comp231_drugs;
DROP TABLE comp231_customers;

DROP SEQUENCE seq_customerID;
DROP SEQUENCE seq_drugID;
DROP SEQUENCE seq_prescriptionID;
DROP SEQUENCE seq_orderID;
DROP SEQUENCE seq_cartID;

-- CREATE APPLICATION TABLES
CREATE TABLE comp231_customers (
    customerID NUMBER(8) NOT NULL,
    firstName VARCHAR2(30) NOT NULL,
    lastName VARCHAR2(30),
    address VARCHAR2(100),
    phone VARCHAR2(20) NOT NULL,
    email VARCHAR2(50),
    CONSTRAINT customers_customerID_PK PRIMARY KEY (customerID)
);

CREATE TABLE comp231_drugs (
    drugID NUMBER(8) NOT NULL,
    isPrescription NUMBER(1) NOT NULL,
    drugName VARCHAR2(60) NOT NULL,
    retailPrice NUMBER(8,2) NOT NULL,
    stock NUMBER(5) NOT NULL,
    CONSTRAINT drugs_drugID_PK PRIMARY KEY (drugID),
    CONSTRAINT drugs_isPrescription_CK CHECK (isPrescription IN (0, 1))
);

CREATE TABLE comp231_prescriptions (
    prescriptionID NUMBER(7) NOT NULL,
    customerID NUMBER(8),
    drugID NUMBER(8),
    patientName VARCHAR2(60) NOT NULL,
    patientDOB DATE NOT NULL,
    refills NUMBER(3) NOT NULL,
    orderCount NUMBER(3) DEFAULT 0 NOT NULL,
    doctorName VARCHAR2(60) NOT NULL,
    doctorAddress VARCHAR2(200) NOT NULL,
    doctorPhone VARCHAR2(20) NOT NULL,
    CONSTRAINT prescriptions_preID_PK PRIMARY KEY (prescriptionID),
    CONSTRAINT prescriptions_customerID_FK FOREIGN KEY (customerID) REFERENCES comp231_customers (customerID),
    CONSTRAINT prescriptions_drugID_FK FOREIGN KEY (drugID) REFERENCES comp231_drugs (drugID)
);

CREATE TABLE comp231_orders (
    orderID NUMBER(8) NOT NULL,
    orderDate DATE DEFAULT SYSDATE NOT NULL,
    shippingDate DATE,
    shippingaddress VARCHAR2(200) NOT NULL,
    orderStatus NUMBER(1) DEFAULT NULL,
    CONSTRAINT orders_orderID_PK PRIMARY KEY (orderID),
    CONSTRAINT orders_orderStatus_CK CHECK (orderStatus IN (1, 2, 3, 4, 9))
);

CREATE TABLE comp231_shopping_carts (
    cartID NUMBER(7) NOT NULL,
    customerID	NUMBER(8) NOT NULL,
    cartStatus NUMBER(1) DEFAULT 0 NOT NULL,
    orderID NUMBER(8) DEFAULT NULL,
    cartTotal NUMBER(8, 2) NOT NULL,
    CONSTRAINT shopping_carts_cartID_PK PRIMARY KEY (cartID),
    CONSTRAINT shopping_carts_customerID_FK FOREIGN KEY (customerID) REFERENCES comp231_customers (customerID),
    CONSTRAINT shopping_carts_cartStatus_CK CHECK (cartStatus IN (1, 2)),
    CONSTRAINT shopping_carts_orderID_FK FOREIGN KEY (orderID) REFERENCES comp231_orders (orderID)
);

CREATE TABLE comp231_cart_items (
    cartID NUMBER(8) NOT NULL,
    drugID	NUMBER(8) NOT NULL,
    prescriptionID	NUMBER(8), 
    unitPrice NUMBER(8,2) NOT NULL,
    quantity NUMBER(3) NOT NULL,
    itemTotal AS (unitPrice * quantity) NOT NULL,
    CONSTRAINT cart_items_cartID_drugID_PK PRIMARY KEY (cartID, drugID),
    CONSTRAINT cart_items_cartID_FK FOREIGN KEY (cartID) REFERENCES comp231_shopping_carts (cartID),
    CONSTRAINT cart_items_drugID_FK FOREIGN KEY (drugID) REFERENCES comp231_drugs (drugID),
    CONSTRAINT cart_items_prescriptionID_FK FOREIGN KEY (prescriptionID) REFERENCES comp231_prescriptions (prescriptionID)
);

COMMIT;

-- BUILD INDEXES
CREATE INDEX idx_orders_orderDate ON comp231_orders (orderDate);
CREATE INDEX idx_orders_shippingDate ON comp231_orders (shippingDate);
CREATE INDEX idx_shopping_carts_customerID ON comp231_shopping_carts (customerID);
CREATE INDEX idx_shopping_carts_orderID ON comp231_shopping_carts (orderID);
CREATE INDEX idx_cart_items_drugID ON comp231_cart_items (drugID);

-- CREATE SEQUENCES
CREATE SEQUENCE seq_customerID START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_drugID START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_prescriptionID START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_orderID START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE seq_cartID START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- After order is made, update the order count 
CREATE OR REPLACE TRIGGER trg_update_ordercount
AFTER UPDATE OF cartStatus ON comp231_shopping_carts
FOR EACH ROW
DECLARE
    CURSOR cur_cart_items IS SELECT * FROM comp231_cart_items;
BEGIN
    -- For each item ordered, check for those which need update prescription order count
    FOR rec_cart_items in cur_cart_items LOOP
        IF (rec_cart_items.prescriptionID IS NOT NULL) AND (rec_cart_items.cartID = :NEW.cartID) THEN
            UPDATE comp231_prescriptions
            SET orderCount = orderCount + rec_cart_items.quantity
            WHERE prescriptionID = rec_cart_items.prescriptionID;
            DBMS_OUTPUT.PUT_LINE('[Trigger Event] Prescription ID ' || rec_cart_items.prescriptionID ||  ' added ' || rec_cart_items.quantity || ' to its order count');
        END IF;
    END LOOP;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
        RAISE_APPLICATION_ERROR(-20004, 'Trigger Error for updating prescription order count.');
END;
/