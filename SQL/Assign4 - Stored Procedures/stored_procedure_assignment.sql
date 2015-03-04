--Andrew Downs

--
--A: Create a sequence named rent_num_seq to start with 1100 and increment by 1.  Do not cache any values.
--
CREATE SEQUENCE rent_num_seq START WITH 1100 NOCACHE;

--
--B: Alter the PRICE table to include an attribute named PRICE_RENTDAYS to store integers of up to two digits.  
--   The attribute should not accept null values, and it should have a default value of 3.
--
ALTER TABLE PRICE
ADD (PRICE_RENTDAYS NUMBER(2, 0) DEFAULT 3 NOT NULL);

--
--C: Update the PRICE table to place the following values into the PRICE_RENTDAYS attribute:  
--   for PRICE_CODE = 1 set PRICE_RENTDAYS = 5, 
--   for PRICE_CODE = 2 set PRICE_RENTDAYS = 3, 
--   for PRICE_CODE = 3 set PRICE_RENTDAYS = 5, 
--   and for PRICE_CODE = 4 set PRICE_RENTDAYS = 7. 
--   Use multiple SQL statements to do this.
--
UPDATE PRICE
SET PRICE_RENTDAYS = 5
WHERE Price_Code = 1;

UPDATE PRICE
SET PRICE_RENTDAYS = 3
WHERE Price_Code = 2;

UPDATE PRICE
SET PRICE_RENTDAYS = 5
WHERE Price_Code = 3;

UPDATE PRICE
SET PRICE_RENTDAYS = 7
WHERE Price_Code = 4;

--
--D: Create a stored procedure named prc_new_rental to insert new rows into the RENTAL table.  The procedure should satisfy the following conditions:
--    The membership number will be provided as a parameter. This will be an IN parameter.
--    Use a Count() function to verify the membership number exists in the MEMBERSHIP table. 
--      You will likely want to use a SELECT ... INTO query.  If it does not exist, a message should be displayed 
--      that the membership does not exist and no data should be written to the database. 
--    If the membership does exist, retrieve the membership balance and display a message that the balance amount is the previous balance.  
--      (For example, if the membership has a balance of $5.00, then display “Previous balance: $5.00”.)  You will likely want to use a SELECT ... INTO 
--       query when retrieving the membership balance.
--    Insert a new row in the rental table using the sequence created in step a. above to generate the value for RENT_NUM, the current 
--      system date for the RENT_DATE value, and the membership number retrieved in step iii as the value for MEM_NUM.
--
create or replace PROCEDURE PRC_NEW_RENTAL 
(
  L_MEM_NUM IN MEMBERSHIP.MEM_NUM%TYPE 
) AS 

l_mem_count NUMBER;
l_mem_bal   MEMBERSHIP.MEM_BALANCE%TYPE;

BEGIN
  SELECT COUNT(mem_num)
  INTO l_mem_count
  FROM MEMBERSHIP
  WHERE mem_num = l_mem_num;
  
  IF l_mem_count = 1 THEN
    SELECT mem_balance
    INTO l_mem_bal
    FROM MEMBERSHIP
    WHERE mem_num = L_MEM_NUM;
    
    DBMS_OUTPUT.PUT_LINE('Previous balance: $' || l_mem_bal);
    
    INSERT INTO RENTAL
    (RENT_NUM, RENT_DATE, MEM_NUM)
    VALUES
    (rent_num_seq.NEXTVAL, SYSDATE, L_MEM_NUM);
  ELSE
    DBMS_OUTPUT.PUT_LINE('Member not found. Be sure you entered the member number in correctly.');
  END IF;
END PRC_NEW_RENTAL;

--
--E: Create a stored procedure named prc_new_detail to insert new rows into the DETAILRENTAL table.  The procedure should satisfy the following requirements:
--    The video number will be provided as a parameter. This will be an IN parameter.
--    Verify that the video number exists in the VIDEO table. Do this in the same way as was done in the procedure above. 
--      If it does not exist, display a message that the video does not exist and do not write any data into the database. 

--    If the video number does exist, verify that the VID_STATUS for the video is “IN”. You will likely want to use a SELECT ... INTO query.
--      If the status is not “IN”, display a message that the video’s return must be entered before it can be rented again and do 
--      not write any data into the database.

--    If the status is “IN” then retrieve the values of the video’s PRICE_RENTFEE, PRICE_DAILYLATEFEE, and PRICE_RENTDAYS from the 
--      PRICE table. You will likely want to use a SELECT ... INTO query.

--    Calculate the due date for the video rental by adding the number of days in PRICE_RENTDAYS to 11:59:59PM (hours:minutes:seconds) 
--      in the current system date.

--    Insert a new row in the DETAILRENTAL table using the previous value returned by RENT_NUM_SEQ as the RENT_NUM, the video number 
--      provided in the parameter as VID_NUM, the PRICE_RENTFEE as the value for DETAIL_FEE, the due date calculated in (v) for the 
--      DETAIL_DUEDATE, PRICE_DAILYLATEFEE as the value for DETAIL_DAILYLATEFEE, and null for the DETAIL_RETURNDATE.
--
create or replace PROCEDURE PRC_NEW_DETAIL 
(
  L_VID_NUM IN VIDEO.VID_NUM%TYPE 
) AS 

l_vid_count         NUMBER;
l_vid_status        Video.Vid_Status%TYPE;
l_vid_rentfee       Price.Price_Rentfee%TYPE;
l_vid_dailylatefee  Price.Price_Dailylatefee%TYPE;
l_vid_rentdays      Price.Price_Rentdays%TYPE;
l_vid_duedate       DATE;

BEGIN
  SELECT COUNT(vid_num)
  INTO l_vid_count
  FROM VIDEO
  WHERE vid_num = l_vid_num;
  
  IF l_vid_count = 1 THEN
    SELECT vid_status
    INTO l_vid_status
    FROM VIDEO
    WHERE vid_num = l_VID_NUM;
    
    IF UPPER(l_vid_status) = 'IN' THEN
      SELECT price_rentfee, price_dailylatefee, price_rentdays
      INTO l_vid_rentfee, l_vid_dailylatefee, l_vid_rentdays
      FROM PRICE JOIN (SELECT price_code 
                        FROM MOVIE JOIN (SELECT movie_num 
                                          FROM VIDEO 
                                            WHERE vid_num = L_VID_NUM) USING(movie_num)) USING(price_code);
                                            
      l_vid_duedate := trunc(sysdate) + 86399/86400 + l_vid_rentdays;
      
      INSERT INTO DETAILRENTAL
      (rent_num, vid_num, detail_fee, detail_duedate, detail_returndate, detail_dailylatefee)
      VALUES
      (rent_num_seq.CURRVAL, l_vid_num, l_vid_rentfee, l_vid_duedate, NULL, l_vid_dailylatefee);
    ELSE
      DBMS_OUTPUT.PUT_LINE('The video must be returned before it can be rented out again.');
    END IF;
  ELSE
    DBMS_OUTPUT.PUT_LINE('Video not found. Be sure you entered the correct video number.');
  END IF;
END PRC_NEW_DETAIL;


--
--F:Create a stored procedure named prc_return_video to enter data about the return of videos that have been rented.  The procedure should satisfy the following requirements:
--
--  The video number will be provided as a parameter. This will be an IN parameter.
--
--  Verify that the video number exists in the VIDEO table. Do this in the same way as was done in the procedure above. 
--    If it does not exist, display a message that the video number provided was not found and do not write any data into the database.  
--
--  If the video number does exist, use a Count() function to ensure that the video has only one record in DETAILRENTAL for which it 
--    does not have a return date (return date of null). You will likely want to use a SELECT ... INTO query. If more than one row in 
--    DETAILRENTAL indicates the video is not returned, display an error message that the video has multiple outstanding rentals and 
--    do not write anything into the database.
--
--  If the video does not have any outstanding rentals, update the video status to “IN” for the video in the VIDEO table, and display a 
--    message that the video had no outstanding rentals but is now available for rental.  If the video has only one outstanding rental, 
--    update the return date (DETAIL_RETURNDATE in DETAILRENTAL table) to the current system date, and update the video status to “IN” for 
--    that video in the VIDEO table.  Then display a message that the video was successfully returned. 
CREATE OR REPLACE PROCEDURE PRC_RETURN_VIDEO 
(
  L_VID_NUM IN VIDEO.vid_num%TYPE 
) AS 

l_vid_count NUMBER;
l_vid_detailcount NUMBER;

BEGIN
  SELECT  COUNT(vid_num)
  INTO    l_vid_count
  FROM    VIDEO
  WHERE   vid_num = l_vid_num;
  
  IF l_vid_count = 1 THEN
    SELECT  COUNT(vid_num)
    INTO    l_vid_detailcount
    FROM    DETAILRENTAL
    WHERE   vid_num = l_vid_num
    AND     detail_returndate IS NULL;
    
    IF l_vid_detailcount > 1 THEN
      DBMS_OUTPUT.PUT_LINE('This video has multiple outstanding rentals.');
      RETURN;
    END IF;
    
    IF l_vid_detailcount = 0 THEN
      UPDATE VIDEO
      SET vid_status = 'IN'
      WHERE vid_num = l_vid_num;
      
      DBMS_OUTPUT.PUT_LINE('This video has no outstanding rentals but is now available for renting.');
      
      RETURN;
    END IF;
    
    IF l_vid_detailcount = 1 THEN
      UPDATE DETAILRENTAL
      SET detail_returndate = SYSDATE
      WHERE vid_num = l_vid_num
      AND detail_returndate IS NULL;
      
      UPDATE VIDEO
      SET vid_status = 'IN'
      WHERE vid_num = l_vid_num;
      
      DBMS_OUTPUT.PUT_LINE('The video was successfully returned.');
    END IF;
  ELSE
    DBMS_OUTPUT.PUT_LINE('Video not found. Be sure you entered the correct video number.');
  END IF;
END PRC_RETURN_VIDEO;


