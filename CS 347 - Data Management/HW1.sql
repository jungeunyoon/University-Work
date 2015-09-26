-- HW1.sql -- Homework 1
--
-- Jung Eun Yoon
-- UT EID: jey283, UTCS username: jungyoon
-- C S 347, Fall 2014, Dr. P. Cannata
-- Department of Computer Science, The University of Texas at Austin
--

-- 3-01
SELECT vendor_id, vendor_name 
FROM vendors 
WHERE vendor_name LIKE 'C%' 
OR vendor_name LIKE 'N%' 
OR vendor_name LIKE 'P%' 
OR vendor_name LIKE 'S%' 
ORDER BY vendor_name; 


-- 3-02
SELECT vendor_id, vendor_name, vendor_address1, vendor_address2
FROM vendors
WHERE vendor_address2 NOT LIKE 'NULL';


-- 3-03
SELECT vendor_id, 
    vendor_city || ', ' || vendor_state AS "vendor_location", 
    vendor_contact_last_name || ', ' || vendor_contact_first_name AS "vendor_contact"
FROM vendors
WHERE vendor_state LIKE 'CA'
ORDER BY vendor_contact_last_name, vendor_contact_first_name;


-- 3-04
SELECT invoice_id, vendor_id, invoice_due_date
FROM invoices
WHERE invoice_due_date BETWEEN '16-MAY-08' AND '16-JUN-08';


-- 3-05
SELECT *
FROM invoices
WHERE invoice_total BETWEEN 100 AND 1000
ORDER BY invoice_total;


-- 3-06
SELECT invoice_id, invoice_total, 
    invoice_total * .1 AS "10%" , 
    invoice_total - (invoice_total * .1) AS "new total"
FROM invoices;


-- 3-07 
SELECT vendor_id, vendor_name, vendor_phone
FROM vendors
WHERE vendor_phone LIKE '(800)%';


-- 3-08
SELECT 15.75 AS "Hourly rate",
    8 AS "cars sold",
    1500 AS "commission",
    75 AS "hours worked",
    8 * 1500 AS "$ earned from cars sold",
    15.75 * 75 AS "$ from hourly rate",
    (8 * 1500) + (15.75 * 75) AS "total paid",
    TO_CHAR((sysdate + 15), 'DY MON DD, YYYY') AS "date paid"
FROM dual;
