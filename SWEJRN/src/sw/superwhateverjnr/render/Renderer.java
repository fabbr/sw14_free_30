package sw.superwhateverjnr.render;

import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.block.Block;
import sw.superwhateverjnr.texture.Texture;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.util.IdAndSubId;
import sw.superwhateverjnr.util.Point;
import sw.superwhateverjnr.world.World;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class Renderer
{
	private World world;
	private Game game;
	
	public void nextFrame()
	{
		Canvas c=null;
		Paint p=null;
		
		//backgroud
		c=drawBackgroud(c, p);
		
		//world
		drawWorld(c, p);
	}
	
	private Canvas drawBackgroud(Canvas c, Paint p)
	{
		p.setColor(0xFF4444FF);
		c.drawRect(0, 0, 0, 0, p);
		
		return c;
	}
	
	private void drawWorld(Canvas c, Paint p)
	{
		PointF min=game.getMinDisplayPoint();
		float x1=min.x;
		float y1=min.y;
		float x2=x1+game.getDisplayWidth();
		float y2=y1+game.getDisplayHeight();
		
		for(int x=(int) Math.floor(x1);x<Math.ceil(x2);x++)
		{
			for(int y=(int) Math.floor(y1);y<Math.ceil(y2);y++)
			{
				Block b=world.getBlockAt(x, y);
				IdAndSubId ref=new IdAndSubId(b.getType().getId(),b.getSubid());
				Texture tex=TextureMap.getTexture(ref);
				//if(tex)
			}
		}
		
	}
}
