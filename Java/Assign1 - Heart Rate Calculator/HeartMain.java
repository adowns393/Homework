/*
 * Class: 	HeartMain.java
 * Author: 	Andrew Downs
 * Date: 	January 26, 2014
 * 
 * This is the main class for the heart rate calculator.
 * This will be where HeartRates objects will be instantiated
 * and modified based on the user's input. This class will prompt the user
 * for a person's name, date of birth, and the current year. Based on those
 * criteria, a max heart rate, target heart rates, and age can be found.
 */

import java.util.Scanner;		//Imports the java.util.Scanner package.
								//Needed to access the Scanner class to read user input.

//Begin class HeartMain
public class HeartMain {
	
	//declare main method
	public static void main(String[] args){
		
		String tempFirstName;			//Declare variables
		String tempLastName;
		
		int tempBirthYear;
		int tempBirthMonth;
		int tempBirthDay;
		int tempCurrentYear;
		
		Scanner input = new Scanner(System.in);		//Create a new Scanner object called input
		
		System.out.println("Please enter the following information:");		//Prompt the user for input info
		
		System.out.print("First Name: ");	//Input First Name
		tempFirstName = input.next();
		
		System.out.print("Last Name: ");	//Input Last Name
		tempLastName = input.next();
				
		System.out.print("Birth Year: ");	//Input Birth Year
		tempBirthYear = input.nextInt();
		
		System.out.print("Birth Month: ");	//Input Birth Month
		tempBirthMonth = input.nextInt();
		
		System.out.print("Birth Day: ");	//Input Birth Day
		tempBirthDay = input.nextInt();
		
		System.out.print("Current Year");	//Input Current Year
		tempCurrentYear = input.nextInt();
		
		//Instantiate new HeartRates object called rates
		HeartRates rates = new HeartRates(tempFirstName, tempLastName, tempBirthYear, tempBirthMonth, tempBirthDay, tempCurrentYear);	
		
		//Print out results
		System.out.printf("\nName: %s %s\n", rates.GetFirstName(), rates.GetLastName());												
		System.out.printf("Date of Birth: %d/%d/%d\n", rates.GetBirthMonth(), rates.GetBirthDay(), rates.GetBirthYear());
		System.out.printf("Age: %d\n", rates.GetAge());
		System.out.printf("Max Heart Rate: %d BPM\n", rates.GetMaxHeartRate());
		System.out.printf("Heart Rate Target Range: %.0f - %.0f BPM", rates.GetMinTargetHeartRate(), rates.GetMaxTargetHeartRate());
	
	}//End main method
	
}//End class HeartMain
