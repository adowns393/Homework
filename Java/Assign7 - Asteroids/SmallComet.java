package comets;

import java.util.Vector;

public class SmallComet extends Comet
{
	Vector<Comet> newComets = new Vector<Comet>();
	
	public SmallComet(double xCord, double yCord, double xVel, double yVel)
	{
		super(xCord, yCord, xVel, yVel);
		
		radius = 20;
	}
	
	public Vector<Comet> explode()
	{
		return newComets;
	}
}
