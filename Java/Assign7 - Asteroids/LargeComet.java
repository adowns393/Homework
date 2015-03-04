package comets;

import java.util.Vector;

public class LargeComet extends Comet
{
	private Vector<Comet> newComets = new Vector<Comet>();
	
	public LargeComet(double xCord, double yCord, double xVel, double yVel)
	{
		super(xCord, yCord, xVel, yVel);
		
		radius = 40;
	}
	
	public Vector<Comet> explode()
	{
		//random returns a value between 0.0 inclusive and 1.0 non-inclusive
		//multiply by 20 and subtract by 10 to land in the -10 to 10 range
		newComets.add(new MediumComet(this.xPosition, 
									  this.yPosition, 
									  Math.random() * 20 - 10, 
									  Math.random() * 20 - 10));
		
		newComets.add(new MediumComet(this.xPosition, 
									  this.yPosition,
									  Math.random() * 20 - 10, 
									  Math.random() * 20 - 10));
		
		return newComets;
	}
}
