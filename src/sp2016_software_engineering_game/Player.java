package sp2016_software_engineering_game;

import java.awt.Rectangle;

public class Player {
	
	int x, y, xDir, yDir;
	int health, exp, level, expToNextLevel, maxHealth;
	final int WIDTH = 30;
	final int HEIGHT = 50;
	Boolean isAlive = false;
	long tookDamageTime;
	Rectangle playerRect;
	Main game;

	public Player(Main g) 
	{
		game = g;
		isAlive = true;
		maxHealth = 100;
		health = 100;
		x = 500;
		y = 300;
		exp = 0;
		level = 1;		
		expToNextLevel = 100;
		
		playerRect = new Rectangle(x, y, WIDTH, HEIGHT);
	}
	
	public void Update()
	{
		checkIfDead();
		detectEdges();
		move(xDir, yDir);
		checkForLevel();
		playerRect.setBounds(x, y, WIDTH, HEIGHT);
	}
	
	public void checkIfDead()
	{
		if(health <= 0)
			die();
	}
	
	public void setXDirection(int newX)
	{
		xDir = newX;
	}
	
	public void setYDirection(int newY)
	{
		yDir = newY;
	}
	
	public void move(int xDir, int yDir)
	{
		x += xDir;
		y += yDir;
	}
	
	public void detectEdges()
	{
		if(x < 10)
			setXDirection(1);
		else if(x + WIDTH > 1260)
			setXDirection(-1);
		
		if(y < 50)
			setYDirection(1);
		else if(y + HEIGHT > 700)
			setYDirection(-1);
	}
	
	public void checkForLevel()
	{
		if(exp >= expToNextLevel)
		{
			exp -= expToNextLevel;
			expToNextLevel += 10;
			levelUp();
		}
	}
	
	public void levelUp()
	{
		level++;
		System.out.println("Ding! Level " + level);
		maxHealth += 50;
		health = maxHealth;
	}
	
	public void die()
	{
		isAlive = false;
		game.stop();
	}
}
