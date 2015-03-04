
public class TrianglesPartA {
	
	public static void main(String[] args)
	{
		int numLines;
		int numStars;
		int numSpaces;
		int n = 10;			//This variable determines how many lines the triangle will be.
		                    //This makes it easier to modify the code to create a variable-sized triangle
		
		//Print Triangle 1
		for (numLines = 0; numLines < n; numLines++)
		{
			for(numStars = 0; numStars <= numLines; numStars++)
				System.out.print("*");
			
			//Advance to the next line
			System.out.println("");
		}
		
		System.out.println("");
		
		//Print Triangle 2
		for (numLines = n; numLines > 0; numLines--)
		{
			for(numStars = 0; numStars <= numLines - 1; numStars++)
				System.out.print("*");
			
			//Advance to the next line
			System.out.println("");
		}
		
		System.out.println("");
		
		//Print Triangle 3
		for (numLines = n; numLines > 0; numLines--)
		{
			for(numSpaces = n; numSpaces > numLines; numSpaces--)
				System.out.print(" ");
			for(numStars = 0; numStars <= numLines - 1; numStars++)
				System.out.print("*");
			
			//Advance to the next line
			System.out.println("");
		}
		
		System.out.println("");
		
		//Print Triangle 4
		for (numLines = 0; numLines < n; numLines++)
		{
			for(numSpaces = n-1; numSpaces > numLines; numSpaces--)
				System.out.print(" ");
			for(numStars = 0; numStars <= numLines; numStars++)
				System.out.print("*");
			
			//Advance to the next line
			System.out.println("");
		}
	}
}
