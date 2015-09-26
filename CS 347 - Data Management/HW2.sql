-- HW1.sql -- Homework 2
--
-- Jung Eun Yoon
-- UT EID: jey283, UTCS username: jungyoon
-- C S 347, Fall 2014, Dr. P. Cannata
-- Department of Computer Science, The University of Texas at Austin
--

-- 4-01
SELECT invoice_id, vendor_name, invoice_due_date
FROM vendors 
  INNER JOIN invoices
  ON vendors.vendor_id = invoices.vendor_id
WHERE invoice_due_date BETWEEN '1-MAY-08' AND '1-JUN-08';


-- 4-02
SELECT invoice_id, vendor_name, invoice_due_date
FROM vendors 
  INNER JOIN invoices
  ON vendors.vendor_id = invoices.vendor_id
WHERE vendors.default_terms_id NOT LIKE invoices.terms_id;


-- 4-03
SELECT department_name, project_number, 
  first_name || ' ' || last_name AS "employee name"
FROM departments d 
  FULL OUTER JOIN employees e 
  ON d.department_number = e.department_number
  FULL OUTER JOIN projects p
  ON e.employee_id = p.employee_id
ORDER BY department_name;


-- 4-04
SELECT vendor_id, vendor_name, account_description
FROM vendors
JOIN general_ledger_accounts e
ON e.account_number = vendors.default_account_number
ORDER BY vendor_name DESC;


-- 4-05
SELECT departments.department_number
FROM departments
  LEFT OUTER JOIN employees
  ON departments.DEPARTMENT_NUMBER = employees.DEPARTMENT_NUMBER
WHERE employees.DEPARTMENT_NUMBER IS NULL;