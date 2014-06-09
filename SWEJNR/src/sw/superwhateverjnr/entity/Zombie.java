package sw.superwhateverjnr.entity;

import java.util.Map;

import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.util.Rectangle;
import sw.superwhateverjnr.world.Location;

public class Zombie extends Entity
{
	//-------------------------- animation ------------------------------
	private final static float armMaxDegreeDeltaMoving = 4;
	private final static float armMaxDegreeDeltaStanding = 4;
	private final static float armMoveConstantMoving = 0.075F;
	private final static float armMoveConstantStanding = 0.075F;
	public float getArmMaxDegreeDeltaMoving()
	{
		return armMaxDegreeDeltaMoving;
	}
	public float getArmMaxDegreeDeltaStanding()
	{
		return armMaxDegreeDeltaStanding;
	}
	public float getArmMoveConstantMoving()
	{
		return armMoveConstantMoving;
	}
	public float getArmMoveConstantStanding()
	{
		return armMoveConstantStanding;
	}
	
	//-------------------------- movement ------------------------------
	private final static double runningMin = 1.5;
	private final static double runningMax = 4.5;
	private final static double runPower = 0.0015;
	private final static double jumpPower = 7.0;
	private final static double radius = 6.0;
	
	private Player player;
	private static boolean isindistance = false;
	
	public Zombie(int id, EntityType type, Location location, Map<String, Object> extraData)
	{
		super(EntityType.ZOMBIE, location, extraData);
	}

	@Override
	protected void die()
	{
		
	}
	
	@Override
	public void tick()
	{
		super.tick();
		trigger();
		// randomJump();
	}
	
	protected void trigger()
	{
		
	}
	/*
	protected void randomJump()
	{
		if (isindistance)
		{
			//do some cranky stuff
		}
	}
	*/
	private void tickMove()
	{
		if(location==null || world()==null)
		{
			return;
		}
		Rectangle bounds=getHitBox();
		long now=System.currentTimeMillis();
		long time=now-getLastMoveTime();
		
		double vx=velocity.getX();
		if(isMovingleft() && !isMovingright())
		{
			if(vx>-runningMin)
			{
				vx=-runningMin;
			}
			else
			{
				vx*=(1+runPower*time*(runningMax+vx));
			}
		}
		else if(isMovingright() && !isMovingleft())
		{
			if(vx<runningMin)
			{
				vx=runningMin;
			}
			else
			{
				vx*=(1+runPower*time*(runningMax-vx));
			}
		}
		else //x decelerate
		{
			double d=runPower*time*(Math.abs(vx)+runningMin);
			d*=3;
			if(d>1)
			{
				d=1;
			}
			else if(d<0)
			{
				d=0;
			}
			
			vx*=(1-d);
		}

		getVelocity().setX(vx);
		setLastMoveTime(now);

		
		
		
		
		float multiplier=0.01F;
		float playerwidth=(float) (Math.abs(bounds.getMin().getX()-bounds.getMax().getX()));
		
		//world check
		double x=location.getX();
		x+=velocity.getX()*multiplier;
		if(x<0)
		{
			x=0;
			velocity.setX(0);
		}
		if(x>=world().getWidth())
		{
			x=world().getWidth()-0.0000001;
			velocity.setX(0);
		}
		
		//block check
		try
		{
		Location l1=new Location(x-playerwidth/2,location.getY());
		Location l2=new Location(x-playerwidth/2,location.getY()+1);
		Block b1=world().getBlockAt(l1);
		Block b2=world().getBlockAt(l2);
		if(b1.getType().isSolid() || b2.getType().isSolid())
		{
			if(velocity.getX()<0)
			{
				x=Math.ceil(x-playerwidth/2)+playerwidth/2;
				velocity.setX(0);
			}
		}
		}catch(Exception e){}
		
		try
		{
		Location l3=new Location(x+playerwidth/2,location.getY());
		Location l4=new Location(x+playerwidth/2,location.getY()+1);
		Block b3=world().getBlockAt(l3);
		Block b4=world().getBlockAt(l4);
		if(b3.getType().isSolid() || b4.getType().isSolid())
		{
			if(velocity.getX()>0)
			{
				x=Math.floor(x+playerwidth/2)-playerwidth/2;
				velocity.setX(0);
			}
		}
	}catch(Exception e){}
		
		location.setX(x);
	}
}
