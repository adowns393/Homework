--Andrew Downs

--
--A.
--
SELECT    DISTINCT Lgproduct.prod_sku AS SKU, 
                   Lgproduct.prod_descript AS Description, 
                   Lgproduct.prod_type AS Type, 
                   Lgproduct.prod_price AS Price
FROM      Lgproduct, 
          (SELECT * FROM Lgproduct WHERE UPPER(prod_type) = 'INTERIOR') interior, 
          (SELECT * FROM Lgproduct WHERE UPPER(prod_type) = 'EXTERIOR') exterior
WHERE     Lgproduct.prod_descript = interior.prod_descript 
AND       interior.prod_descript = exterior.prod_descript
ORDER BY  Lgproduct.prod_descript;

--
--B.
--
SELECT  emp_lname || ', ' || emp_fname AS NAME, 
        emp_title AS TITLE, 
        dept_num AS DEPARTMENT
FROM    Lgemployee JOIN Lgdepartment USING(dept_num)
WHERE   dept_num IN (SELECT dept_num
                     FROM (SELECT dept_num, COUNT(dept_num)
                              FROM   Lgemployee
                                GROUP BY dept_num
                                  ORDER BY COUNT(dept_num) desc)
                                   WHERE ROWNUM < 2)
ORDER BY emp_lname;


--
--C. List the highest paid employee in each department. List the employee name, department code, and salary. List the output by the department code (lowest to highest
--
SELECT emp_lname || ', ' || emp_fname AS NAME, 
       dept_num AS DEPT, 
       MAX(sal_amount) AS SALARY
FROM Lgdepartment JOIN (SELECT emp_fname, emp_lname, sal_amount, emp_num
                          FROM Lgemployee JOIN (SELECT sal_amount, emp_num
                                                  FROM Lgsalary_history
                                                    ORDER BY sal_amount desc) USING(emp_num)) USING(emp_num)
GROUP BY dept_num, 
         emp_lname, 
         emp_fname
ORDER BY dept_num;


--
--D. List all the products purchased that are the highest priced item for a brand. List the invoice number, the SKU, the quantity, the price, and the brand. List the output by the SKU (A to Z)
--
SELECT inv_num AS Invoice, 
       prod_sku AS SKU, 
       line_qty AS QTY, 
       line_price AS Price, 
       brand_name AS Brand
FROM Lgline JOIN (SELECT prod_sku, brand_name
                    FROM Lgproduct, (SELECT MAX(prod_price) AS prod_price, brand_name, brand_id
                                        FROM (SELECT prod_sku, prod_price, brand_name, brand_id
                                                  FROM Lgbrand JOIN (SELECT prod_sku, prod_price, brand_id
                                                                        FROM (SELECT prod_sku, MAX(line_price)
                                                                                FROM Lgline
                                                                                  GROUP BY prod_sku) JOIN Lgproduct USING(prod_sku)) USING(brand_id))
                                                                                  GROUP BY brand_name, brand_id) max_brand_prod
                                                                        WHERE Lgproduct.prod_price = max_brand_prod.prod_price
                                                                        AND Lgproduct.brand_id = max_brand_prod.brand_id) USING(prod_sku)
ORDER BY prod_sku;


--
--E. List the name of all customers who do not have an invoice. List the customer code, the customer name, and customer address. List the output by customer code with the largest customer code listed first
--
SELECT Lgcustomer.cust_code, 
       cust_lname || ', ' || cust_fname AS Customer, 
       cust_street || ', ' || cust_city || ', ' || cust_zip AS Address
FROM Lgcustomer LEFT OUTER JOIN (SELECT cust_code
                                  FROM Lgcustomer JOIN Lginvoice USING(cust_code)) invoice_cust
ON Lgcustomer.cust_code = invoice_cust.cust_code
WHERE invoice_cust.cust_code IS NULL
ORDER BY cust_code desc;

--
--F. List the name and invoice date of the customer (or customers) who have the oldest invoice. There may be one or more customer with the date. List the customer's name and the invoice date. List the output by customer last name (A to Z)
--                
SELECT cust_lname AS "Last Name", 
       cust_fname AS "First Name", 
       inv_date   AS "Invoice Date"
FROM Lgcustomer JOIN (SELECT cust_code, inv_date
                        FROM Lginvoice JOIN (SELECT min(inv_date) AS inv_date
                                               FROM Lginvoice) USING(inv_date)) USING(cust_code)
ORDER BY cust_lname;

--
--G. List the average salary of employees. Make sure you average the current salaries only
--
SELECT AVG(sal_amount) AS "Avg Current Salary"
FROM Lgsalary_History
WHERE Sal_End IS NULL;



--
--H.Calculate the current salary of all employees, the average of current salaries and display the difference between the two for each employee. 
--List the employee code, the employee name, the employee's current salary, the average of all current salaries, and the difference between the 
--current salary and the average salaray. List the output by current salary with the largest salary listed first.
--
SELECT emp_num AS "Emp#", 
       emp_lname || ', ' || emp_fname AS "Name", 
       sal_amount AS "Current Salary", 
       avg_sal AS "Average Salary", 
       sal_diff AS "Difference"
FROM Lgemployee JOIN (SELECT sal_amount - avg_sal AS sal_diff, emp_num, avg_sal, sal_amount
                        FROM Lgsalary_History, (SELECT AVG(sal_amount) AS avg_sal
                                                  FROM Lgsalary_History
                                                    WHERE Sal_End IS NULL)
                           WHERE sal_end IS NULL) USING(emp_num)
ORDER BY sal_amount desc;

--
--I. Display the total quantity ordered of each product supplied by each vendor. List the output by vendor name (A to Z)
--
SELECT vend_name AS Vendor, 
       prod_sku AS SKU, 
       total_ordered AS "TOTAL ORDERED"
FROM Lgvendor JOIN (SELECT *
                      FROM Lgsupplies JOIN (SELECT  SUM(line_qty) AS total_ordered, Prod_Sku
                                              FROM Lgline
                                                GROUP BY prod_sku) USING(prod_sku)) USING(vend_id)
ORDER BY vend_name;


--
--J.  Find the largest line item on each invoice. Largest in this case means the largest total amount (quantity x price). 
--    Display the SKU number, the invoice number, and the amount. List the output in order by invoice number with the lowest 
--    invoice number first. Format the output, including column headings, as shown in the sample outputPreview the documentView in a 
--    new window. Note: It is possible to have more than one "largest" line item on an invoice. This could occur if two (or more) 
--    line items have the same total amount. For example, invoice 907 has items 7532-PY3 and 3754-MAK and both of these have a total 
--    amount of 65.97. Therefore they both will appear in the query results. It is also possible to have the same item more than 
--    once on the same  invoice. These duplicate items may (like invoice 415) or may not (like invoice 162) cause both items to appear in the results. 
--
SELECT prod_sku AS Product, Lgline.inv_num AS Invoice, MAX(Total_$) AS "Total $"
FROM Lgline, (SELECT inv_num, MAX(Line_Price * Line_Qty) AS Total_$
                FROM Lgline
                  GROUP BY inv_num) max_line_price
WHERE Lgline.Inv_Num = max_line_price.inv_num 
GROUP BY prod_sku, Lgline.inv_num
ORDER BY Lgline.Inv_Num;
