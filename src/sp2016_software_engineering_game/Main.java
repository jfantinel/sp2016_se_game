package sp2016_software_engineering_game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.*;

import javax.swing.JFrame;

public class Main extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 1L;
	private Image dbImage;
	private Graphics dbGraphics;
 
	final int WIDTH = 1280;
	final int HEIGHT = 720;
	int fps, ups;
	
	Thread thread;
	Boolean running = false;
	Boolean started = false;
	
	Font normalfont = new Font("Arial", Font.BOLD, 20);
	Font bigfont = new Font("Arial", Font.BOLD, 40);
	
	Player me;

	public Main(){
		me = new Player(this);
		addKeyListener(new KeyListener(this, me));
		setTitle("SoftwareEngineering Game");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.GREEN);
	}
	
	public void paint(Graphics g){
		dbImage = createImage(getWidth(), getHeight());
		dbGraphics = dbImage.getGraphics();
		paintComponent(dbGraphics);
		g.drawImage(dbImage, 0, 0, this);
	}
	
	public void paintComponent(Graphics g)
	{
		drawGame(g);
	}
	
	public void drawGame(Graphics g)
	{
		g.setFont(normalfont);
		g.setColor(Color.BLACK);
		g.drawString("HP: " + me.health, 30, 60);
		g.drawString("Level: " + me.level, 30, 90);
		g.drawString("Exp: " + me.exp, 140, 60);
		g.drawString("Next level in: " + me.expToNextLevel, 140, 90);
		
		g.setColor(Color.BLUE);
		if(me.isAlive)
			g.fillRect(me.x, me.y, me.WIDTH, me.HEIGHT);
			
		repaint(); //Calls the paintComponent method again to keep updating
	}
	
	 public void start()
	 {
	     running = true;
	     thread = new Thread(this); 
	     thread.start();
	 }
	    
	 public synchronized void stop()
	 {
	     running = false;
	     try
	     {
	         thread.join();
	     }
	     catch(Exception e)
	     {
	         System.out.println("Error Game.stop() " + e);
	     }
	 }
	    
	 public void run()
	 {
	     long lastTime = System.nanoTime();
	     long timer = System.currentTimeMillis();
	     final double ns = 1000000000.0 / 60.0;
	     double delta = 0;
	     int frames = 0;
	     int updates = 0;
	    
	     while(running)
	     {
	         long now = System.nanoTime();
	         delta += (now - lastTime) / ns;
	         lastTime = now;
	         while(delta >= 1)
	         {
	             update();
	             updates++;
	             delta --;
	         }
	         frames++;
	            
	         if(System.currentTimeMillis() - timer > 1000)
	         {
	             timer += 1000;
	         //  System.out.println(updates + " updates, " + frames + "fps");
	             fps = frames;
	             ups = updates;
	             updates = 0;
	             frames = 0;
	         }
	     }
	     stop();
	 }
	    
	 public void update()
	 {
	     me.Update();
	 }	    

	 public static void main(String[] args) 
	 {
	     Main game = new Main();
	     game.start();
	 }
}
