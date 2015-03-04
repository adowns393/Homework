package comets;

import java.util.Vector;

public class MediumComet extends Comet
{
	Vector<Comet> newComets = new Vector<Comet>();
	
	public MediumComet(double xCord, double yCord, double xVel, double yVel)
	{
		super(xCord, yCord, xVel, yVel);
		
		radius = 30;
	}
	
	public Vector<Comet> explode()
	{
		//random returns a value between 0.0 inclusive and 1.0 non-inclusive
		//multiply by 20 and subtract by 10 to land in the -10 to 10 range
		newComets.add(new SmallComet(this.xPosition, 
									 this.yPosition, 
									 Math.random() * 20 - 10, 
							    	 Math.random() * 20 - 10));
		
		newComets.add(new SmallComet(this.xPosition, 
									 this.yPosition, 
									 Math.random() * 20 - 10, 
									 Math.random() * 20 - 10));
		
		return newComets;
	}
}
