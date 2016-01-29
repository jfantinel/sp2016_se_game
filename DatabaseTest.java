package sp2016_software_engineering_game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.*;
import java.sql.*;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
	 
	 public void initNewAccountMenu()
	 {
		 
	 }

	 public static void main(String[] args) 
	 {
		 JTextField username = new JTextField();
		 JTextField password = new JTextField();
         JOptionPane op = new JOptionPane();
         final JComponent[] inputs = new JComponent[] {
                    new JLabel("Username"),
                    username,
                    
                    new JLabel("Password"),
                    password
         };
         
         JOptionPane.showMessageDialog(null, inputs);
         
         System.out.println("Player entered:");
         System.out.println(username.getText());
         System.out.println(password.getText());
         System.out.println();
         
         Main game = new Main();
         game.start();
          
         try {
               Class.forName("com.mysql.jdbc.Driver").newInstance();
         }
         catch(ClassNotFoundException ex) {
               System.out.println("Error: unable to load driver class!");
               System.exit(1);
         }
         catch(IllegalAccessException ex) {
               System.out.println("Error: access problem while loading!");
               System.exit(2);
         }
         catch(InstantiationException ex) {
               System.out.println("Error: unable to instantiate driver!");
               System.exit(3);
         }
          
         String URL = "jdbc:mysql://sql5.freesqldatabase.com:3306/sql5104581";
         String USER = "sql5104581";
         String PASS = "8CHGkEPagu";
         Connection con = null;
         Statement stmt = null;
         try
         {
            con = DriverManager.getConnection(URL, USER, PASS);
            stmt = con.createStatement();String sql;
            sql = "SELECT * from AccountsTable where Username = \'" + username.getText() + "\';";
            ResultSet rs = stmt.executeQuery(sql);
             
            //If there is no results for the entered username
            if(!rs.next())
            {
            	//initNewAccountMenu();
            }
            while(rs.next())
            {
                int value1 = rs.getInt("AccountNumber");
                String value2 = rs.getString("Username");
                String value3 = rs.getString("Password");
                
                System.out.println("Account Number: "+ value1);
                System.out.println("Username: "+ value2);
                System.out.println("Password: "+ value3);
                System.out.println();
            }
            //Closes the ResultSet/statement/connection we opened earlier
            rs.close();
            stmt.close();
            con.close();
         } 
         catch (SQLException e) {
            e.printStackTrace();
         }
         catch (Exception e) {
             e.printStackTrace();
         }
         finally
         {  
             //closing the resources in case they weren't closed earlier
             try {
             if (stmt != null)
                     stmt.close();
             }
             catch (SQLException e2) {
                e2.printStackTrace();
             }
             try {
                 if (con != null) {
                     con.close();
                  }
             } 
             catch (SQLException e2) {
                    e2.printStackTrace();
             }
         }
     }  
}
