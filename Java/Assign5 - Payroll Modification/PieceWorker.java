
public class PieceWorker extends Employee
{
	private double wage;
	private int pieces;
	
	public PieceWorker(String first, String last, String ssn, double wagePerPiece, int piecesProduced)
	{
		//call the super class constructor
		super(first, last, ssn);
		
		//initialize local variables
		wage = wagePerPiece;
		pieces = piecesProduced;
	}
	
	public void setWage(double pay)
	{
		//logic check to see if the wage value is valid
		if(pay >= 0.0)
			wage = pay;
		else
			throw new IllegalArgumentException("Wage must be >= 0.0");
	}
	
	public double getWage()
	{
		return wage;
	}
	
	public void setPiecesProduced(int n)
	{
		//logic check to make sure the amount of pieces is valid
		if(n >= 0)
			pieces = n;
		else
			throw new IllegalArgumentException("Pieces produced must be >= 0");
	}
	
	public int getPiecesProduced()
	{
		return pieces;
	}
	
	//earnings() is inherited from employee, override it to make its behavior
	//specific to this sub class
	@Override
	public double earnings()
	{
		return wage * pieces;
	}
	
	//toString() is inherited from employee, override it to make its behavior
	//specific to this sub class
	@Override
	public String toString()
	{
		return String.format("%s: %s\n%s: $%,.2f; %s: %d",
				"piece worker", super.toString(),
				"wage per piece", getWage(),
				"pieces produced", getPiecesProduced());
	}
	
}
