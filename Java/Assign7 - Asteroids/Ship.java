package comets;

public class Ship extends SpaceObject
{
	private static double acceleration = 0.1;
	private static double turnRate = 0.1;
	private static int maxSpeed = 10;
	private double speed;
	private double angle;
	
	
	public Ship(double xCord, double yCord, double xVel, double yVel)
	{
		super(xCord, yCord, xVel, yVel);
		
		radius = 10;
		angle = 0;
		speed = 0;
	}
	
	public void accelerate()
	{		
		if(Math.abs(speed) < maxSpeed)
		{
			xVelocity += acceleration * Math.sin(getAngle());
			yVelocity += acceleration * Math.cos(getAngle());
			
			speed = Math.sqrt(Math.pow(xVelocity, 2) + Math.pow(yVelocity, 2));
		}
		else
			
		xVelocity *= 0.99;
		yVelocity *= 0.99;
		
		speed = Math.sqrt(Math.pow(xVelocity, 2) + Math.pow(yVelocity, 2));
	}
	
	public Shot fire()
	{
		return new Shot(this.xPosition, 
						this.yPosition, 
						Math.sin(this.getAngle()) * 3, 
						Math.cos(this.getAngle()) * 3);
	}
	
	public void rotateLeft()
	{
		angle += turnRate;
	}
	
	public void rotateRight()
	{
		angle -= turnRate;
	}
	
	public double getAngle() { return angle; }

}
