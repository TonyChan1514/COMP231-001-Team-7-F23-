CREATE OR REPLACE PACKAGE new_record_handler_pkg
AS
    g_new_customerID comp231_customers.customerID%TYPE;    -- Customer ID of the latest created customer
    g_new_drugID comp231_drugs.drugID%TYPE;    -- Drug ID of the latest created drug
    g_new_prescriptionID comp231_prescriptions.prescriptionID%TYPE;    -- Prescription ID of the latest created prescription

    PROCEDURE add_customer(
        p_firstName IN comp231_customers.firstName%TYPE,
        p_lastName IN comp231_customers.lastName%TYPE,
        p_address IN comp231_customers.address%TYPE,
        p_phone IN comp231_customers.phone%TYPE,
        p_email IN comp231_customers.email%TYPE
    );
    
    PROCEDURE add_drug(
        p_isPrescription IN comp231_drugs.isPrescription%TYPE,
        p_drugName IN comp231_drugs.drugName%TYPE,
        p_retailPrice IN comp231_drugs.retailPrice%TYPE,
        p_stock IN comp231_drugs.stock%TYPE
    );
    
    PROCEDURE add_prescription(
		p_customerID IN comp231_prescriptions.customerID%TYPE,
		p_drugID IN comp231_prescriptions.drugID%TYPE,
		p_patientName IN comp231_prescriptions.patientName%TYPE,
		p_patientDOB IN comp231_prescriptions.patientDOB%TYPE,
		p_refills IN comp231_prescriptions.refills%TYPE,
		p_doctorName IN comp231_prescriptions.doctorName%TYPE,
		p_doctorAddress IN comp231_prescriptions.doctorAddress%TYPE,
		p_doctorPhone IN comp231_prescriptions.doctorPhone%TYPE
	);
END new_record_handler_pkg;
/

CREATE OR REPLACE PACKAGE BODY new_record_handler_pkg 
AS
    PROCEDURE add_customer(
        p_firstName IN comp231_customers.firstName%TYPE,
        p_lastName IN comp231_customers.lastName%TYPE,
        p_address IN comp231_customers.address%TYPE,
        p_phone IN comp231_customers.phone%TYPE,
        p_email IN comp231_customers.email%TYPE
    )
    IS
    BEGIN
        INSERT INTO comp231_customers (customerID, firstName, lastName, address, phone, email)
        VALUES (seq_customerID.NEXTVAL, p_firstName, p_lastName, p_address, p_phone, p_email)
        RETURNING customerID INTO g_new_customerID;

        COMMIT;
        DBMS_OUTPUT.PUT_LINE('New customer ' || p_firstName || ' ' || p_lastName || ' added successfully.');
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            DBMS_OUTPUT.PUT_LINE('Customer ID: ' || g_new_customerID || ' is already existing. ' || SQLERRM);
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Error adding customer: ' || SQLERRM);
            ROLLBACK;
    END add_customer;
    
    PROCEDURE add_drug(
        p_isPrescription IN comp231_drugs.isPrescription%TYPE,
        p_drugName IN comp231_drugs.drugName%TYPE,
        p_retailPrice IN comp231_drugs.retailPrice%TYPE,
        p_stock IN comp231_drugs.stock%TYPE
    )
    IS
    BEGIN
        INSERT INTO comp231_drugs (drugID, isPrescription, drugName, retailPrice, stock)
        VALUES (seq_drugID.NEXTVAL, p_isPrescription, p_drugName, p_retailPrice, p_stock)
        RETURNING drugID INTO g_new_drugID;
    
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('New drug ' || p_drugName || ' added successfully.');
    EXCEPTION
         WHEN DUP_VAL_ON_INDEX THEN
            DBMS_OUTPUT.PUT_LINE('Drug ID: ' || g_new_drugID || ' is already existing. ' || SQLERRM);
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
            ROLLBACK;
    END add_drug;
    
    PROCEDURE add_prescription(
		p_customerID IN comp231_prescriptions.customerID%TYPE,
		p_drugID IN comp231_prescriptions.drugID%TYPE,
		p_patientName IN comp231_prescriptions.patientName%TYPE,
		p_patientDOB IN comp231_prescriptions.patientDOB%TYPE,
		p_refills IN comp231_prescriptions.refills%TYPE,
		p_doctorName IN comp231_prescriptions.doctorName%TYPE,
		p_doctorAddress IN comp231_prescriptions.doctorAddress%TYPE,
		p_doctorPhone IN comp231_prescriptions.doctorPhone%TYPE
	)
	IS
	BEGIN
		INSERT INTO comp231_prescriptions (prescriptionID, customerID, drugID, patientName, patientDOB, refills, orderCount, doctorName, doctorAddress, doctorPhone)
		VALUES (seq_prescriptionID.NEXTVAL, p_customerID, p_drugID, p_patientName, p_patientDOB, p_refills, DEFAULT, p_doctorName, p_doctorAddress, p_doctorPhone)
		RETURNING prescriptionID INTO g_new_prescriptionID;
		
		COMMIT;
		DBMS_OUTPUT.PUT_LINE('New prescription for drug ID ' || p_drugID || ' added to customer ID ' || p_customerID || ' successfully.');
	EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            DBMS_OUTPUT.PUT_LINE('Drug ID: ' || g_new_prescriptionID || ' is already existing. ' || SQLERRM);
		WHEN OTHERS THEN
			DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
			ROLLBACK;
	END add_prescription;
END new_record_handler_pkg;
/

CREATE OR REPLACE PACKAGE shopping_handler_pkg
AS
    g_customerID comp231_customers.customerID%TYPE;      -- The customer who checked in
    g_open_cartID comp231_shopping_carts.cartID%TYPE;    -- The customer current open shopping cart
    CURSOR cur_shopping_carts IS SELECT * FROM comp231_shopping_carts WHERE cartStatus = 1 AND orderID IS NULL;
    
    PROCEDURE customer_checkIn(
        p_customerID IN comp231_customers.customerID%TYPE,
        p_is_new_cart_created OUT NUMBER,
        p_cartID OUT comp231_shopping_carts.cartID%TYPE
    );
    
    FUNCTION is_drug_allowable(
        p_drugID IN comp231_drugs.drugID%TYPE,
        p_prescriptionID IN comp231_prescriptions.prescriptionID%TYPE DEFAULT NULL,
        p_quantity IN comp231_cart_items.quantity%TYPE
    ) RETURN BOOLEAN;
    
    FUNCTION get_cart_total RETURN NUMBER;
    
    FUNCTION get_orderStatus_by_desc(
        p_description in comp231_orderstatus_map.description%TYPE
    ) RETURN comp231_orders.orderStatus%TYPE;
    
    PROCEDURE add_to_shopping_cart(
		p_drugID IN comp231_drugs.drugID%TYPE,
		p_prescriptionID IN comp231_prescriptions.prescriptionID%TYPE DEFAULT NULL,
		p_quantity IN comp231_cart_items.quantity%TYPE
	);
    
    PROCEDURE shopping_cart_checkout(
		p_shippingAddress IN comp231_orders.shippingAddress%TYPE
	);
END shopping_handler_pkg;
/

CREATE OR REPLACE PACKAGE BODY shopping_handler_pkg
AS
    -- For customer check-in, every customer must be checked in to perform subsequence actions (e.g. Add item to cart, checkout)
    PROCEDURE customer_checkIn(
        p_customerID IN comp231_customers.customerID%TYPE,
        p_is_new_cart_created OUT NUMBER,
        p_cartID OUT comp231_shopping_carts.cartID%TYPE
    )
    IS
    BEGIN
        -- Store the checked in customer ID
        g_customerID := p_customerID;
        
        -- Look for his/her open shopping cart ID, create one if not existing
        g_open_cartID := NULL;
		FOR rec_shopping_carts IN cur_shopping_carts LOOP
			IF rec_shopping_carts.customerID = p_customerID THEN
				g_open_cartID := rec_shopping_carts.cartID;
                p_is_new_cart_created := 0;
			END IF;
		END LOOP;
		
		-- Insert a new shopping cart record is an open shopping cart is not existing
		IF (g_open_cartID IS NULL) THEN
			INSERT INTO comp231_shopping_carts (cartID, customerID, cartStatus, cartTotal)
			VALUES (seq_cartID.NEXTVAL, p_customerID, 1, 0)     --cartTotal will be calculated in below update statement
			RETURNING cartID INTO g_open_cartID;
            
            p_is_new_cart_created := 1;
			DBMS_OUTPUT.PUT_LINE('Shopping cart ID ' || g_open_cartID || ' is created for customer ID ' || p_customerID);
		END IF;
        
        DBMS_OUTPUT.PUT_LINE('Customer ID ' || p_customerID || ' has checked-in. Cart ID ' || g_open_cartID);
        p_cartID := g_open_cartID;
        
        COMMIT;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
			DBMS_OUTPUT.PUT_LINE('Invalid customer ID');
    END customer_checkIn;
    
    FUNCTION is_drug_allowable(
        p_drugID IN comp231_drugs.drugID%TYPE,
        p_prescriptionID IN comp231_prescriptions.prescriptionID%TYPE DEFAULT NULL,
        p_quantity IN comp231_cart_items.quantity%TYPE
    ) RETURN BOOLEAN
    IS
        v_allowable BOOLEAN := TRUE;
        v_rec_count NUMBER(7);
        v_prescription_required comp231_drugs.isPrescription%TYPE;
        v_refills comp231_prescriptions.refills%TYPE;
        v_previousOrderCount comp231_prescriptions.orderCount%TYPE;
        v_quantity_in_cart comp231_cart_items.quantity%TYPE;
        v_prescripted_customerID comp231_prescriptions.customerID%TYPE;
        v_prescripted_drugID comp231_prescriptions.drugID%TYPE;
        NO_PRESCRIPTION EXCEPTION;
        WRONG_PRESCRIPTION_CUSTOMER EXCEPTION;
        WRONG_PRESCRIPTION_DRUG EXCEPTION;
        OVER_ORDER EXCEPTION;
    BEGIN
        -- To know whether prescription is needed for input drug
        SELECT isPrescription INTO v_prescription_required
        FROM comp231_drugs 
        WHERE drugID = p_drugID;
    
        -- Mark not allowed if no prescriion, or the prescription / drug doesn't belong to the right customer / drug
        IF v_prescription_required = 1 THEN
            IF p_prescriptionID IS NULL THEN
                RAISE NO_PRESCRIPTION;
            END IF;
            
            -- To know who is holding the prescription
            SELECT customerID, drugID INTO v_prescripted_customerID, v_prescripted_drugID
            FROM comp231_prescriptions
            WHERE prescriptionID = p_prescriptionID;
            
            IF g_customerID <> v_prescripted_customerID THEN
                RAISE WRONG_PRESCRIPTION_CUSTOMER;
            END IF;
            IF p_drugID <> v_prescripted_drugID THEN
                RAISE WRONG_PRESCRIPTION_DRUG;
            END IF;
            IF p_prescriptionID IS NOT NULL THEN
                -- Get the number of refills and current ordered count
                SELECT refills, orderCount INTO v_refills, v_previousOrderCount
                FROM comp231_prescriptions
                WHERE prescriptionID = p_prescriptionID;
                
                SELECT COUNT(1) INTO v_rec_count
                FROM comp231_cart_items
                WHERE cartID = g_open_cartID
                AND drugID = p_drugID;
                
                IF v_rec_count > 0 THEN
                    SELECT quantity INTO v_quantity_in_cart
                    FROM comp231_cart_items
                    WHERE cartID = g_open_cartID
                    AND drugID = p_drugID;
                ELSE
                    v_quantity_in_cart := 0;
                END IF;
                
                -- Check if over the limit
                IF v_previousOrderCount + v_quantity_in_cart + p_quantity  > (v_refills + 1) THEN
                    RAISE OVER_ORDER;
                END IF;
            END IF;
        END IF;
    
        RETURN v_allowable;
    EXCEPTION
        WHEN NO_PRESCRIPTION THEN
            DBMS_OUTPUT.PUT_LINE('Customer ID ' || g_customerID || ' does not have prescription for prescripted drugs.');
            RETURN FALSE;
        WHEN WRONG_PRESCRIPTION_CUSTOMER THEN
            DBMS_OUTPUT.PUT_LINE('Customer ID ' || g_customerID || ' prescription is not for the corresponding customer');
            RETURN FALSE;
        WHEN WRONG_PRESCRIPTION_DRUG THEN
            DBMS_OUTPUT.PUT_LINE('Customer ID ' || g_customerID || ' prescription is not for the corresponding drug');
            RETURN FALSE;
        WHEN OVER_ORDER THEN
            DBMS_OUTPUT.PUT_LINE('Drug ID ' || p_drugID || ' in cart ID ' || g_open_cartID ||  ' will be over the limit mention in prescription ID ' || p_prescriptionID);
            RETURN FALSE;
    END is_drug_allowable;
    
    FUNCTION get_cart_total RETURN NUMBER
    IS
        v_cartTotal NUMBER;
        CART_NOT_FOUND EXCEPTION;
    BEGIN
        -- Calculate the total price of items in the shopping cart with the given cartID.
        SELECT SUM(unitPrice * quantity) INTO v_cartTotal
        FROM comp231_cart_items
        WHERE cartID = g_open_cartID;
        
        IF v_cartTotal IS NULL THEN
            RAISE CART_NOT_FOUND;
        END IF;
     
        RETURN v_cartTotal;
    EXCEPTION
        WHEN CART_NOT_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('Shopping cart ID ' || g_open_cartID || ' does not exist');
            RETURN 0;
        WHEN NO_DATA_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('Data not found');
            RETURN 0;
    END get_cart_total;
    
    -- Get the orderStatus by its description
    FUNCTION get_orderStatus_by_desc(
        p_description IN comp231_orderstatus_map.description%TYPE
    ) RETURN comp231_orders.orderStatus%TYPE
    IS
        r_orderStatus comp231_orders.orderStatus%TYPE;
    BEGIN
        SELECT orderStatus
        INTO r_orderStatus
        FROM comp231_orderstatus_map
        WHERE UPPER(description) = UPPER(p_description);
        
        RETURN r_orderStatus;   
    END get_orderStatus_by_desc;

    -- Put shopping item (drug) to the shopping cart
    PROCEDURE add_to_shopping_cart(
        p_drugID IN comp231_drugs.drugID%TYPE,
        p_prescriptionID IN comp231_prescriptions.prescriptionID%TYPE DEFAULT NULL,
        p_quantity IN comp231_cart_items.quantity%TYPE
    )
    IS
        v_isItemExist NUMBER(1) := 0;
        CURSOR cur_cart_items IS SELECT * FROM comp231_cart_items;
        v_allowable BOOLEAN := FALSE;
        DRUG_NOT_ALLOWED EXCEPTION;
    BEGIN      
        -- Call the is_drug_allowable function to check if this drug is allowable to buy by cross checking the input prescription
        v_allowable := is_drug_allowable(p_drugID, p_prescriptionID, p_quantity);
        IF NOT v_allowable THEN
            RAISE DRUG_NOT_ALLOWED;
        END IF;
        
        -- Check if the drug is existing in shopping cart
        FOR rec_cart_items IN cur_cart_items LOOP
            IF rec_cart_items.cartID = g_open_cartID AND rec_cart_items.drugID = p_drugID THEN
                v_isItemExist := 1;
            END IF;
        END LOOP;
        IF (v_isItemExist = 1) THEN
            -- Update the quantity if existing
            UPDATE comp231_cart_items
            SET quantity = quantity + p_quantity
            WHERE cartID = g_open_cartID
            AND drugID = p_drugID;
        ELSE
            -- Insert a new record if the drug is not existing in the shopping cart
            INSERT INTO comp231_cart_items (cartID, drugID, prescriptionID, unitPrice, quantity)
            SELECT g_open_cartID, p_drugID, p_prescriptionID, retailPrice, p_quantity
            FROM comp231_drugs
            WHERE drugID = p_drugID;
        END IF;
        
        DBMS_OUTPUT.PUT_LINE('Drug ID ' || p_drugID || ', quantity ' || p_quantity || ' is added to cart ID ' || g_open_cartID);
        
        -- Call the update_cart_total function to get the cartTotal for updating
        UPDATE comp231_shopping_carts
        SET cartTotal = get_cart_total()
        WHERE cartID = g_open_cartID;
        
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('Customer not found or does not have an open shopping cart.');
        WHEN DRUG_NOT_ALLOWED THEN
            DBMS_OUTPUT.PUT_LINE('Drug ID ' || p_drugID || ' add to shopping cart ID ' || g_open_cartID || ' failed as this drug is not allowable to buy.');
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
            ROLLBACK;
    END add_to_shopping_cart;
    
    -- Checkout Shopping Cart and create a new order
    PROCEDURE shopping_cart_checkout(
        p_shippingAddress IN comp231_orders.shippingAddress%TYPE
    )
    IS
        v_orderID comp231_shopping_carts.orderID%TYPE;
        v_shippingAddress comp231_orders.shippingAddress%TYPE;
    BEGIN    
        -- Use customer address as shipping address if not provided
        IF (p_shippingAddress IS NULL) THEN
            SELECT address
            INTO v_shippingAddress
            FROM comp231_customers
            WHERE customerID = g_customerID;
        ELSE
            v_shippingAddress := p_shippingAddress;
        END IF;
        
        -- Insert an order record with initial status (With no order status yet)
        INSERT INTO comp231_orders (orderID, orderDate, shippingDate, shippingaddress, orderStatus)
        VALUES(seq_orderID.NEXTVAL, DEFAULT, NULL, v_shippingAddress, DEFAULT)
        RETURNING orderID INTO v_orderID;
        
        -- Update shopping cart status and orderID
        UPDATE comp231_shopping_carts
        SET cartStatus = 2, orderID = v_orderID
        WHERE cartID = g_open_cartID;
        
        -- Update the order status (in order to trigger procedure order_status_change_trigger)
        UPDATE comp231_orders
        SET orderStatus = 1
        WHERE orderID = v_orderID;
        
        COMMIT;
        
        DBMS_OUTPUT.PUT_LINE('Customer ID ' || g_customerID || ' has checked out his/her shopping cart ID ' || g_open_cartID);
        DBMS_OUTPUT.PUT_LINE('Order ID ' || v_orderID || ' is therefore created and ship to ' || v_shippingAddress);
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            DBMS_OUTPUT.PUT_LINE('Customer does not exist.');
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
            ROLLBACK;
    END shopping_cart_checkout;
END shopping_handler_pkg;
/

CREATE OR REPLACE PACKAGE order_handler_pkg
AS
    g_new_orderStatus comp231_orders.orderStatus%TYPE;     -- New order status of the latest status update
    
    PROCEDURE update_order_status(
		p_orderID IN comp231_orders.orderID%TYPE,
		p_newOrderStatus IN comp231_orders.orderStatus%TYPE
	);
END order_handler_pkg;
/

CREATE OR REPLACE PACKAGE BODY order_handler_pkg
AS
    -- Update Order Status (Packaged, Shipped, Delivered, Returned)
	PROCEDURE update_order_status(
		p_orderID IN comp231_orders.orderID%TYPE,
		p_newOrderStatus IN comp231_orders.orderStatus%TYPE
	)
	IS
		v_description comp231_orderstatus_map.description%TYPE;
		CURSOR cur_statusDescription IS SELECT * FROM comp231_orderstatus_map;
        INVALID_ORDER_STATUS EXCEPTION;
        ORDER_ID_NOT_EXIST EXCEPTION;
	BEGIN
	
		-- Get new order status description
		FOR rec_description in cur_statusDescription LOOP
			IF rec_description.orderStatus = p_newOrderStatus THEN
				v_description := rec_description.description;        
			END IF;
		END LOOP;
		
		IF v_description IS NULL THEN
            RAISE INVALID_ORDER_STATUS;
		END IF;
		
		-- Update order status
		IF p_newOrderStatus = 3 THEN
			-- SET shipping date is Today if new order stautus = 3 (Shipped)
			UPDATE comp231_orders
			SET orderStatus = p_newOrderStatus, shippingDate = SYSDATE
			WHERE orderID = p_orderID;
		ELSE
			UPDATE comp231_orders
			SET orderStatus = p_newOrderStatus
			WHERE orderID = p_orderID;
		END IF;
		
		IF SQL%ROWCOUNT > 0 THEN
			DBMS_OUTPUT.PUT_LINE('Order ID ' || p_orderID || ' latest status is ' || v_description);
		ELSE
            RAISE ORDER_ID_NOT_EXIST;
		END IF;
	
		COMMIT;
        
        -- Set global variable
        g_new_orderStatus := p_newOrderStatus;
		
	EXCEPTION
        WHEN INVALID_ORDER_STATUS THEN
            DBMS_OUTPUT.PUT_LINE('Invalid order status ' || p_newOrderStatus);
            g_new_orderStatus := NULL;
        WHEN ORDER_ID_NOT_EXIST THEN
            DBMS_OUTPUT.PUT_LINE('Order ID ' || p_orderID || ' does not exist.');
            g_new_orderStatus := NULL;
		WHEN OTHERS THEN
            g_new_orderStatus := NULL;
			DBMS_OUTPUT.PUT_LINE('Error Message: ' || SQLERRM);
			ROLLBACK;
	END update_order_status;
END order_handler_pkg;
/