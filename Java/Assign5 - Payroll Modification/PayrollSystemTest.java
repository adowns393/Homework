/*
 * Andrew Downs
 * COP3330 Eisler
 * Assignment 5
 * a2890722
 */
public class PayrollSystemTest {

	public static void main(String[] args) 
	{
		Employee employees[] = new Employee[5];
		
		employees[0] = new SalariedEmployee				("John", 	"Smith", "111-11-1111", 800.00);
		employees[1] = new HourlyEmployee				("Karen", 	"Price", "222-22-2222", 16.75, 	40);
		employees[2] = new CommissionEmployee			("Sue", 	"Jones", "333-33-3333", 10000., .06);
		employees[3] = new BasePlusCommissionEmployee	("Bob", 	"Lewis", "444-44-4444", 5000, 	.04, 300);
		employees[4] = new PieceWorker					("Rick", 	"Bridges", "555-55-5555", 2.25, 400);
		
		System.out.println("Employees processed polymorphically:\n");
		
		for(Employee currentEmployee : employees)
		{
			System.out.println(currentEmployee);
			System.out.printf("earned $%,.2f\n\n", currentEmployee.earnings());
		}
	}
}
