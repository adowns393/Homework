/*
 * Class: 	HeartRates.java
 * Author: 	Andrew Downs
 * Date: 	January 26, 2014
 * 
 * This is the HeartRates class for assignment one. It stores the template used
 * to create an object HeartRates that can store a person's info and calculate age and heart rates.
 * It also contains the methods that can be called from the main method to get and set those attributes.
 */

//Declare class
public class HeartRates {
	
	private String firstName;			//Declare variables. They will be private because they only need
	private String lastName;			//to be visible by other instances of HeartRates.
	
	private int birthYear;
	private int birthMonth;
	private int birthDay;
	private int currentYear;
	
	private float minTargetCoefficient = 0.5f;			//Declaring the target coefficients as variables instead of directly using their
	private float maxTargetCoefficient = 0.85f;			//values makes it easier to update the code. E.G. A study shows these coefficients
														//are wrong. Instead of searching through the code to individually change them, they can be changed from here.
	
	public HeartRates(String name, String surname, int year, int month, int day, int presentYear){		//This is the constructor for the HeartRates class.
		firstName = name;																				//It takes in arguments to assign to the class' fields
		lastName = surname;																				//when called to create an instance of HeartRates.
		birthYear = year;
		birthMonth = month;
		birthDay = day;
		currentYear = presentYear;
	}
	
	
	public int GetAge(){								//This method returns the person's age based on their year of birth and the
		return (this.currentYear - this.birthYear);		//current year. NOTE: Ages will not be exact and can lead to errors. EXAMPLE:
	}													//A person born on March 26, 1992 will return an age of 22 in January 2014 when they are 
														//actually 21. Instructor has deemed this as an acceptable degree of accuracy for this assignment.
	
	public int GetMaxHeartRate(){		//This method returns a person's max heart rate based on the formula: 220 - age
		return (220 - GetAge());
	}
	
	public float GetMaxTargetHeartRate(){						//The next two methods return a person's maximum and minimum target heart rates.
		return (GetMaxHeartRate() * maxTargetCoefficient);		//Maximum target is 85% of a person's Max heart Rate and minimum target is 50% of their
	}															//max heart rate. NOTE: Because the heart rate is an int and the coefficient is a float,
																//when multiplied, a float is created. Therefore, the method will return a float value.
	public float GetMinTargetHeartRate(){						
		return (GetMaxHeartRate() * minTargetCoefficient);
	}
	
	
	//Set attribute methods
	public void SetFirstName(String name){ this.firstName = name; }			//These methods are used to set the object's attributes
																			//For the scope of this assignment, they are not used but would
	public void SetLastName(String name){ this.lastName = name; }			//be useful if we wanted to modify an object's attributes.
																			//E.G. Give the user the option to correct any mistakes they may
	public void SetBirthYear(int year){	this.birthYear = year; }			//have made when entering the info.
	
	public void SetBirthMonth(int month){ this.birthMonth = month; }
	
	public void SetBirthDay(int day){ this.birthDay = day; }
	
	public void SetCurrentYear(int year){ this.currentYear = year; }	
	
	
	//Get attribute methods
	public String GetFirstName(){ return this.firstName; }		//The following methods get the individual attributes of the object
																//and returns the value to the place it was called from. They are set
	public String GetLastName(){ return this.lastName; }		//as public because they will be accessed from outside of this class.
	
	public int GetBirthYear(){ return this.birthYear; }
	
	public int GetBirthMonth(){ return this.birthMonth; }
	
	public int GetBirthDay(){ return this.birthDay; }
	
	public int GetCurrentYear(){ return this.currentYear; }
	
}//End class HeartRates
