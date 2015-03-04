
public class TrianglesPartB {

	public static void main(String[] args)
	{
		int numLines;
		int numStars;
		int numSpaces;
		int n = 10;			//This variable determines how many lines the triangle will be.
							//This makes it easier to modify the code to create a variable-sized triangle
		
		for(numLines = 0; numLines < n; numLines++)
		{
			//Print the next row of Triangle 1
			for(numStars = 0; numStars <= numLines; numStars++)
				System.out.print("*");
			for(numSpaces = n-1; numSpaces > numLines; numSpaces--)
				System.out.print(" ");
			
			//Print the next row of Triangle 2
			for(numStars = n-1; numStars >= numLines; numStars--)
				System.out.print("*");
			for(numSpaces = 0; numSpaces < numLines; numSpaces++)
				System.out.print(" ");
			
			//Print the next row of Triangle 3
			for(numSpaces = 0; numSpaces < numLines; numSpaces++)
				System.out.print(" ");
			for(numStars = n-1; numStars >= numLines; numStars--)
				System.out.print("*");
			
			//Print the next row of Triangle 4
			for(numSpaces = n-1; numSpaces > numLines; numSpaces--)
				System.out.print(" ");
			for(numStars = 0; numStars <= numLines; numStars++)
				System.out.print("*");
			
			//Print a new line
			System.out.println("");
		}
	}
}
