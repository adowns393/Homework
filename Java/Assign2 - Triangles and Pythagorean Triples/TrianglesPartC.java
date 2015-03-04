
public class TrianglesPartC {

	public static void main(String[] args)
	{
		int sideOne;
		int sideTwo;
		int hypotenuse;
		
		System.out.print("Side 1	Side 2		Hypotenuse\n");
		
		for(hypotenuse = 1; hypotenuse <= 500; hypotenuse++)
			for(sideOne = 1; sideOne < hypotenuse; sideOne++)
				for(sideTwo = 1; sideTwo < sideOne; sideTwo++)
				{
					if((sideOne * sideOne) + (sideTwo * sideTwo) == (hypotenuse * hypotenuse))
						System.out.printf("%d	%d		%d\n", sideOne, sideTwo, hypotenuse);
				}
	}
}
