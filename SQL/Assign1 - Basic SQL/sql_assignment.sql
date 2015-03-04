--Andrew Downs

CREATE TABLE cen_customer(
  customerid  INTEGER,
  lastname    VARCHAR2(30)  NOT NULL,
  firstname   VARCHAR2(30),
  address     VARCHAR2(30),
  city        VARCHAR2(30),
  state       VARCHAR2(2),
  ZIP         VARCHAR2(10),
  CONSTRAINT  customer_pk_customerid  PRIMARY KEY(customerid),
  CONSTRAINT  customer_ck_lastname    CHECK(LENGTH(lastname) >= 1),
  CONSTRAINT  customer_ck_state       CHECK(LENGTH(state) = 2)
);
  
CREATE TABLE cen_salesperson(
  salespersonid  INTEGER,
  lastname       VARCHAR2(30)   NOT NULL,
  firstname      VARCHAR2(30),
  ssn            VARCHAR2(11)   NOT NULL,
  address        VARCHAR2(30),
  city           VARCHAR2(30),
  state          VARCHAR2(30),
  ZIP            VARCHAR2(10),
  hiredate       DATE           DEFAULT SYSDATE NOT NULL,
  CONSTRAINT     salesperson_pk_salespersonid   PRIMARY KEY(salespersonid),
  CONSTRAINT     salesperson_ck_lastname        CHECK(LENGTH(lastname) >= 1),
  CONSTRAINT     salesperson_ck_ssn             CHECK(LENGTH(ssn) = 11)
);
  
CREATE TABLE cen_vehicle(
  vin             VARCHAR2(17),
  make            VARCHAR2(30),
  model           VARCHAR2(30),
  vehicleyear     INTEGER,
  color           VARCHAR2(10),
  suggestedprice  NUMBER(8,2)    NOT NULL,
  sold            VARCHAR2(1)    DEFAULT 'N',
  sale_discount   DECIMAL(4,3)   DEFAULT '0',
  CONSTRAINT      vehicle_pk_vin            PRIMARY KEY(vin),
  CONSTRAINT      vehicle_ck_vin            CHECK(LENGTH(vin) >= 1),
  CONSTRAINT      vehicle_ck_sold           CHECK(sold = 'Y' OR sold = 'N'),
  CONSTRAINT      vehicle_ck_sale_discount  CHECK(sale_discount >= 0 AND sale_discount <= 1)
);
  
CREATE TABLE cen_sale(
  saleid          INTEGER,
  customerid      INTEGER,
  vin             VARCHAR2(17),
  salespersonid   INTEGER,
  datesold        DATE          DEFAULT SYSDATE NOT NULL,
  saleamount      NUMBER(8,2)                   NOT NULL,
  taxamount       NUMBER(8,2),
  CONSTRAINT      sale_pk_saleid        PRIMARY KEY(saleid),
  CONSTRAINT      sale_fk_customerid    FOREIGN KEY(customerid)     REFERENCES cen_customer(customerid),
  CONSTRAINT      sale_fk_vin           FOREIGN KEY(vin)            REFERENCES cen_vehicle(vin),
  CONSTRAINT      sale_fk_salespersonid FOREIGN KEY(salespersonid)  REFERENCES cen_salesperson(salespersonid)
);

CREATE TABLE cen_mechanic(
  mechanicid      INTEGER,
  lastname        VARCHAR2(30)    NOT NULL,
  firstname       VARCHAR2(30),
  ssn             VARCHAR2(11)    NOT NULL,
  address         VARCHAR2(30),
  city            VARCHAR2(30),
  state           VARCHAR2(30),
  ZIP             VARCHAR2(10),
  hiredate        DATE            DEFAULT SYSDATE   NOT NULL,
  baserate        NUMBER(8,2),
  CONSTRAINT      mechanic_pk_mechanicid  PRIMARY KEY(mechanicid),
  CONSTRAINT      mechanic_ck_lastname    CHECK(LENGTH(lastname) >= 1),
  CONSTRAINT      mechanic_uk_ssn         UNIQUE(ssn),
  CONSTRAINT      mechanic_ck_ssn         CHECK(LENGTH(ssn) = 11)
);

CREATE TABLE cen_repair(
  repairid        INTEGER,
  description     VARCHAR2(50)  NOT NULL,
  standardhours   NUMBER(5,2)   NOT NULL,
  category        VARCHAR2(25),
  CONSTRAINT      repair_pk_repairid  PRIMARY KEY(repairid)
);

CREATE TABLE cen_repairappointment(
  apptid                INTEGER,
  vin                   VARCHAR2(17)  NOT NULL,
  customerid            INTEGER       NOT NULL,
  mechanicid            INTEGER       NOT NULL,
  repairid              INTEGER       NOT NULL,
  appt_date_time        DATE          NOT NULL,
  reason                VARCHAR2(30),
  date_time_completed   DATE,
  CONSTRAINT    repairappt_pk_apptid          PRIMARY KEY(apptid),
  CONSTRAINT    repairappt_fk_vin             FOREIGN KEY(vin)         REFERENCES cen_vehicle(vin),
  CONSTRAINT    repairappt_fk_customerid      FOREIGN KEY(customerid)  REFERENCES cen_customer(customerid),
  CONSTRAINT    repairappt_fk_mechanicid      FOREIGN KEY(mechanicid)  REFERENCES cen_mechanic(mechanicid),
  CONSTRAINT    repairappt_fk_repairid        FOREIGN KEY(repairid)    REFERENCES cen_repair(repairid),
  CONSTRAINT    repairappt_ck_vin             CHECK(LENGTH(vin) >=1 AND LENGTH(vin) <= 17)
);

--
--A:
--
UPDATE  Cen_Sale
SET     Taxamount = Saleamount * 0.06;


--
--B:
--
SELECT    lastname, state
FROM      Cen_Salesperson
WHERE     UPPER(state) != 'FL'
ORDER BY  state;


--
--C:
--
SELECT    make, MAX(saleamount) AS "Highest Sale $"
FROM      Cen_Sale, Cen_Vehicle
WHERE     Cen_Sale.Vin = Cen_Vehicle.Vin 
GROUP BY  make
ORDER BY  MAX(saleamount) DESC;  


--
--D:
--
SELECT    Cen_Salesperson.lastname, Cen_Sale.salespersonid, COUNT(Cen_Sale.salespersonid) AS "Vehicles Sold"
FROM      Cen_Salesperson, Cen_Sale
WHERE     Cen_Salesperson.Salespersonid = Cen_Sale.Salespersonid 
GROUP BY  Cen_Salesperson.lastname, Cen_Sale.salespersonid
HAVING    COUNT(Cen_Sale.salespersonid) > 1
ORDER BY  COUNT(Cen_Sale.salespersonid) DESC;


--
--E:
--
SELECT    make, model, vehicleyear, color, suggestedprice
FROM      cen_vehicle
WHERE     UPPER(sold) = 'N'
ORDER BY  suggestedprice DESC;


--
--F:
--
SELECT    firstname, lastname, address, city, state, ZIP
FROM      Cen_Customer
ORDER BY  state ASC, city ASC;


--
--G:
--
SELECT    AVG(saleamount) AS "Avg Sale Amount"
FROM      Cen_Sale;


--
--H:
--
SELECT    make, model, AVG(saleamount) AS "Avg Sale $", MAX(saleamount) AS "Max Sale $"
FROM      Cen_Vehicle, Cen_Sale
WHERE     Cen_Vehicle.Vin = Cen_Sale.Vin
GROUP BY  make, model
ORDER BY  make ASC, model ASC;


--
--I:
--
SELECT    Cen_Customer.lastname, SUM(standardhours * baserate) AS "Total $"
FROM      Cen_Customer, Cen_Mechanic, Cen_Repair, Cen_Repairappointment
WHERE     Cen_Customer.Customerid = Cen_Repairappointment.Customerid
AND       Cen_Mechanic.Mechanicid = Cen_Repairappointment.Mechanicid
AND       Cen_Repair.Repairid = Cen_Repairappointment.Repairid 
GROUP BY  Cen_Customer.lastname
ORDER BY  SUM(standardhours * baserate) DESC;


--
--J:
--
SELECT    vehicleyear ||' '|| make ||' '|| model AS "Vehicle", cen_customer.lastname AS "Customer", description, cen_mechanic.lastname AS "Mechanic", appt_date_time
FROM      cen_vehicle, cen_customer, Cen_Mechanic, Cen_Repair, Cen_Repairappointment
WHERE     cen_vehicle.vin = Cen_Repairappointment.Vin
AND       cen_customer.customerid = Cen_Repairappointment.Customerid
AND       Cen_Mechanic.Mechanicid = Cen_Repairappointment.Mechanicid
AND       Cen_Repair.Repairid = Cen_Repairappointment.Repairid
AND       Cen_Repairappointment.Appt_Date_Time >= To_Date('01-JAN-2012','DD-MON-YYYY')
AND       Cen_Repairappointment.Appt_Date_Time < To_Date('01-JAN-2013','DD-MON-YYYY')
ORDER BY  appt_date_time;


--
--K:
--
DROP TABLE cen_repairappointment;
DROP TABLE cen_repair;
DROP TABLE cen_mechanic;
DROP TABLE cen_sale;
DROP TABLE cen_customer;
DROP TABLE cen_salesperson;
DROP TABLE cen_vehicle;