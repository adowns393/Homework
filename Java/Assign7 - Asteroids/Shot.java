package comets;

public class Shot extends SpaceObject 
{
	private int age;
	
	public Shot(double xCord, double yCord, double xVel, double yVel)
	{
		super(xCord, yCord, xVel, yVel);
		
		radius = 3;
		age = 0;
	}
	
	@Override
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
		
		age++;
	}
	
	public int getAge() { return age; }
}
