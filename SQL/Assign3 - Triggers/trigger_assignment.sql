--Andrew Downs

--
--A: Alter the DETAILRENTAL table to include a derived attribute name DETAIL_DAYSLATE to store integers of up to three digits.  
--   The attribute should accept null values.
--
ALTER TABLE DETAILRENTAL
ADD (DETAIL_DAYSLATE NUMBER(3, 0));

--
--B: Alter the VIDEO table to include an attribute named VID_STATUS to store character data up to four characters long.  
--   The attribute should not accept null values.  The attribute should have a constraint to enforce the domain (“IN”, “OUT”, and “LOST”) 
--   and have a default value of “IN”.
--
ALTER TABLE VIDEO
ADD (VID_STATUS VARCHAR2(4) DEFAULT 'IN' NOT NULL)
ADD (CONSTRAINT video_ck_vid_status CHECK(VID_STATUS = 'IN' OR VID_STATUS = 'OUT' OR VID_STATUS = 'LOST' ));

--
--C: Update the VID_STATUS attribute of the VIDEO table using a subquery to set the VID_STATUS to “OUT” for all videos 
--   that have a null value in the DETAIL_RETURNDATE attribute of the DETAILRENTAL table.
--
UPDATE VIDEO
SET Vid_Status = 'OUT'
WHERE vid_num IN (SELECT vid_num
                    FROM detailrental
                      WHERE detail_returndate IS NULL);

--
--D: Create a trigger named trg_late_return that will write the correct value to DETAIL_DAYSLATE in the DETAILRENTAL 
--table whenever a video is returned.  The trigger should execute as a BEFORE trigger when the DETAIL_RETURNDATE or DETAIL-DUEDATE attributes are updated.
--
--If the return date is null, then the days late should also be null.
--If the return date is not null, then the days late should determine if the video is returned late.
--If the return date is noon of the date after the return or earlier, then the video is not considered late, and the days late should have a value of zero.
--If the return date is past noon of the day after the due date, then the video is considered late, so the number of days late must be calculated and stored.
--The "before noon" grace period applies to returns on the next day only. Any returns after the grace period count as a full day late. Consider the following examples:
create or replace TRIGGER TRG_LATE_RETURN 
BEFORE UPDATE OF DETAIL_DUEDATE,DETAIL_RETURNDATE ON DETAILRENTAL
BEGIN
  UPDATE DETAILRENTAL
  SET detail_dayslate = 0
  WHERE Detail_Returndate IS NULL;
  
  UPDATE DETAILRENTAL
  SET detail_dayslate = 0
  WHERE Detail_Returndate - Detail_Duedate >= -1.5;
  
  UPDATE DETAILRENTAL
  SET detail_dayslate = TRUNC(Detail_Returndate - Detail_Duedate)
  WHERE DETAIL_Duedate - Detail_Returndate < -1.5;
END;

--
--E:Create a trigger named trg_mem_balance that will maintain the correct value in the membership balance in the MEMBERSHIP table 
--when videos are returned late. The trigger should execute as an AFTER trigger when the due date or return dates are updated in the 
--DETAILRENTAL table.  The trigger should satisfy the following conditions:
--
-- Calculate the value of the late fee prior to the update that triggered this execution of the trigger.  
--    The value of the late fee is the days late multiplied by the daily late fee.  If the previous value of the late fee was null, then treat it as zero.
--Calculate the value of the late fee after the update that triggered the execution of this trigger.  
--    If the value of the late fee is now null, then treat it as zero.
--Subtract the prior value of the late fee from the current value of the late fee to determine the change  in late fee for this video rental.
--If the amount calculated in (iii) is not zero, then update the membership balance by the amount calculated for the membership associated with this rental.

create or replace TRIGGER TRG_MEM_BALANCE 
AFTER UPDATE OF DETAIL_DUEDATE,DETAIL_RETURNDATE ON DETAILRENTAL 
FOR EACH ROW

DECLARE
  l_prev_late_fee Detailrental.Detail_Dailylatefee%TYPE;
  l_curr_late_fee Detailrental.Detail_Dailylatefee%TYPE;
  l_late_fee_diff Detailrental.Detail_Dailylatefee%TYPE;
  l_new_days_late     Detailrental.detail_dayslate%TYPE;
  l_old_days_late     Detailrental.detail_dayslate%TYPE;
BEGIN

--Find the difference between the due dates and return dates
--and store them to keep track of the number of days late.

--This stores the days late prior to the update
  IF :OLD.Detail_Duedate - :OLD.Detail_Returndate < -1.5 THEN
    l_old_days_late := TRUNC(:OLD.Detail_Returndate - :OLD.Detail_Duedate);
  ELSE
    l_old_days_late := 0;
  END IF;

--This stores the days late after the update
  IF :NEW.Detail_Duedate - :NEW.Detail_Returndate < -1.5 THEN
    l_new_days_late := TRUNC(:NEW.Detail_Returndate - :NEW.Detail_Duedate);
  ELSE
    l_new_days_late := 0;
  END IF;
  
--Check to see if the video is late and a fee needs to be charged    
  IF l_new_days_late > 0 THEN
  
  --Store the previous late fee amount prior to the update
    IF :OLD.detail_dailylatefee IS NOT NULL THEN
      l_prev_late_fee := :OLD.detail_dailylatefee * l_old_days_late;
    ELSIF :OLD.detail_dailylatefee IS NULL THEN
      l_prev_late_fee := 0;
    END IF;
    
  --Store the current late fee amount after the update
    IF :NEW.detail_dailylatefee IS NOT NULL THEN
      l_curr_late_fee := :NEW.detail_dailylatefee * l_new_days_late;
    ELSIF :NEW.detail_dailylatefee IS NULL THEN
      l_curr_late_fee := 0;
    END IF;
    
  --Calculate the difference between the previous and current late fees
    l_late_fee_diff := l_prev_late_fee - l_curr_late_fee;
    
  --If there is a difference, update the corresponging member's balance to reflect the change
    IF l_late_fee_diff <> 0 THEN
      UPDATE MEMBERSHIP
      SET mem_balance = mem_balance + l_late_fee_diff
      WHERE mem_num IN (SELECT mem_num FROM RENTAL WHERE rent_num = :NEW.rent_num);
    END IF;
    
  END IF;
END;