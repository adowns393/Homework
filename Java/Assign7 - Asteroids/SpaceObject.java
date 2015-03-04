package comets;

public abstract class SpaceObject 
{
	protected double xPosition;
	protected double yPosition;
	
	protected double xVelocity;
	protected double yVelocity;
	
	protected int radius;
	
	public static int playfieldWidth;
	public static int playfieldHeight;
	
	public SpaceObject(double xCord, double yCord, double xVel, double yVel)
	{
		xPosition = xCord;
		yPosition = yCord;
		
		xVelocity = xVel;
		yVelocity = yVel;
	}
	
	public void move()
	{
		this.xPosition += this.xVelocity;
		this.yPosition += this.yVelocity;
		
		
		//Allows the space object to wrap around the screen after
		//reaching the window's border.
		if(xPosition < 0)
		{
			xPosition = playfieldWidth;
		}
		
		if(xPosition > playfieldWidth)
		{
			xPosition = 0;
		}
		
		if(yPosition < 0)
		{
			yPosition = playfieldHeight;
		}
		
		if(yPosition > playfieldHeight)
		{
			yPosition = 0;
		}
		
		
	}
	
	public boolean overlapping(SpaceObject debris)
	{
		if(Math.sqrt(Math.pow((this.xPosition - debris.xPosition), 2) + Math.pow((this.yPosition - debris.yPosition), 2)) < this.radius + debris.radius)
			return true;
		else
			return false;
	}
	
	public int getRadius() { return radius; }
	
	public double getXPosition() { return xPosition; }
	
	public double getYPosition() { return yPosition; }

}
