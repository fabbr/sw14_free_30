package sw.superwhateverjnr.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import lombok.Getter;
import sw.superwhateverjnr.Game;
import sw.superwhateverjnr.entity.EntityType;
import sw.superwhateverjnr.render.GLRenderer;
import sw.superwhateverjnr.render.GLTex;
import sw.superwhateverjnr.render.RenderThread;
import sw.superwhateverjnr.texture.Texture;
import sw.superwhateverjnr.texture.TextureMap;
import sw.superwhateverjnr.texture.entity.PlayerTexture;
import sw.superwhateverjnr.util.IdAndSubId;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

public class GLGameView extends GLSurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, GLSurfaceView.Renderer
{
	private RenderThread rt;
	private GLRenderer renderer;
	
	@Getter
	private boolean paused;
	
	@Getter
	private int fps=0;
	private int frames=0;
	private long fpsmeasurelast=0;
	
	private List<GLTex> bindable;
	private Map<Object, GLTex> textures;
	
	public GLGameView(Context context)
	{
		super(context);
		setup();
	}
	public GLGameView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setup();
	}
	private void setup()
	{
		bindable=new ArrayList<GLTex>();
		textures=new HashMap<Object, GLTex>();
		
		loadTextures();
		
		
		getHolder().addCallback(this);
		setFocusable(true);
		rt=new RenderThread(true);
		renderer=(GLRenderer) rt.getRenderer();
//		renderer.setDwidth(1920);
//		renderer.setDheight(1080);
		this.setOnTouchListener(this);
		
		this.setRenderer(this);
	}
	
	private boolean bla=false;
	
	public void setPaused(boolean paused)
	{
		this.paused = paused;
		
		if(paused)
		{
			this.onPause();
		}
		else
		{
			if(!bla)
			{
				
				bla=true;
			}
			this.onResume();
		}
	}
	
	public boolean onTouch(View v, MotionEvent event)
	{
		if(v!=this)
		{
			return false;
		}
		
		Game.getInstance().handleGameTouchEvent(event);
		return true;
	}
	@Override
	public void onDrawFrame(GL10 gl)
	{
		renderer.nextFrame(gl);
		
		long now=System.currentTimeMillis();
		if(now-fpsmeasurelast>=1000)
		{
			fpsmeasurelast+=1000;
			fps=frames;
			frames=0;
//			System.out.println(fps);
		}
		frames++;
	}
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
        renderer.setDwidth(width);
        renderer.setDheight(height);
        
		gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrthof(0, 0, width, height, -1F, 1);
        gl.glLoadIdentity();
	}
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		fpsmeasurelast=System.currentTimeMillis();
		
		//bind texures
		textures.clear();
		bindTextures(gl);
		
		renderer.setTextures(textures);
	}
	
	
	private void loadTextures()
	{
		for(Entry<IdAndSubId, Texture> e:TextureMap.getBlocks().entrySet())
		{
			GLTex gltex=new GLTex(e.getKey(), e.getValue().getOrgimage());
			bindable.add(gltex);
		}
		
//		PlayerTexture pt = (PlayerTexture) TextureMap.getTexture(EntityType.PLAYER);
//		GLTex gltex=new GLTex(e.getKey(), e.getValue().getOrgimage());
//		
//		
//		
//		for(Entry<IdAndSubId, Texture> e:TextureMap.getBlocks().entrySet())
//		{
//			GLTex gltex=new GLTex(e.getKey(), e.getValue().getOrgimage());
//			bindable.add(gltex);
//		}
	}
	
	private void bindTextures(GL10 gl)
	{
		textures.clear();
		for(GLTex e:bindable)
		{
			e.upload(gl);
			textures.put(e.getRef(), e);
		}
	}
	public void drawNextFrame(){}
}