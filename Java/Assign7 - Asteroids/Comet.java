package comets;

import java.util.Vector;

public abstract class Comet extends SpaceObject
{
	
	public Comet(double xCord, double yCord, double xVel, double yVel)
	{
		super(xCord, yCord, xVel, yVel);
	}
	
	public abstract Vector<Comet> explode();
}
