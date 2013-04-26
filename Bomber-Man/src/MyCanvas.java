import java.util.Random;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

import Characters.Bomb;
import Characters.Level;
import Characters.Monster;
import Characters.Player;

public class MyCanvas extends GameCanvas implements Runnable, CommandListener {

	private static Image player;
	private Image tile;
	private static TiledLayer background;
	private static LayerManager manager;
	private static Player robot;
	static final int update = 50;
	private boolean done = false, new_level = false, win = false;
	private static boolean lose = false;
	private int current_level = 1;
	private static int x = 0, y = 0;
	private int dimension = 10;
	private static Level level;
	private final int[] numMonsters = { 5, 7, 10 };
	private final int[] numBomb = { 2, 2, 3 };

	public boolean isLose() {
		return lose;
	}

	public static void setLose(boolean lose1) {
		lose = lose1;
	}

	public MyCanvas() {
		super(true);
		setCommandListener(this);
		try {
			player = Image.createImage("/ball.png");
			tile = Image.createImage("/terrain.png");
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() {
		new_level = false;
		lose = false;
		win = false;
		x = 0;
		y = 0;

		level = new Level(numMonsters[current_level - 1],
				numBomb[current_level - 1], dimension + 10 * current_level,
				dimension + 10 * current_level);

		// Construct the layout manager
		manager = new LayerManager();
		// Create robot sprite
		robot = new Player(player, 20, 20);
		robot.setPosition(20, 20);
		background = new TiledLayer(level.getMap().length,
				level.getMap()[0].length, tile, 20, 20);

		for (int i = 0; i < level.getMap().length; i++) {
			for (int j = 0; j < level.getMap()[i].length; j++) {
				background.setCell(i, j, level.getMap()[i][j]);
			}

		}

		// To make layers and sprites part of the manager, we append them to
		// that object.
		manager.append(robot);
		manager.append(background);
		Monster[] mon = level.getMonster();
		Random rand = new Random();

		for (int i = 0; i < mon.length; i++) {
			int x = (rand.nextInt(level.getMap()[0].length - 0 + 1))
					% level.getMap()[0].length, y = (rand.nextInt(level
					.getMap().length - 0 + 1)) % level.getMap().length;
			while (level.getMap()[y][x] != 0 || x <= 5 || y <= 5) {
				x = (rand.nextInt(level.getMap()[0].length - 0 + 1))
						% level.getMap()[0].length;
				y = (rand.nextInt(level.getMap().length - 0 + 1))
						% level.getMap().length;
			}
			mon[i].setPosition(y * 20, x * 20);
			manager.append(mon[i]);
		}
		// Fire up the thread that runs the animation

		Thread runner = new Thread(this);
		runner.start();

	}

	public void run() {
		done = false;

		// Keep looping until done.
		while (!done) {
			if (level.getWrong_number() >= 3 && !lose) {
				lose = true;
			}
			if (!new_level && !lose && !win) {
				level.update();
				robot.update("");
				checkUserInput();
				updateScreen();
				try {
					Thread.sleep(update);
				} catch (Exception e) {
				}
			} else if (new_level) {
				if (current_level == 3) {
					win = true;
					new_level = false;
				} else {
					updateScreen();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					current_level++;
					initialize();
					new_level = false;
					lose = false;
					win = false;

				}

			} else if (lose) {
				updateScreen();
			} else if (win) {
				updateScreen();
			}
		}

	}

	private void updateScreen() {
		Graphics g = getGraphics(); // Get the graphics component
		if (!new_level && !lose && !win) {
			g.setColor(0, 0, 0);
			g.fillRect(0, 0, getWidth(), 30);
			g.setColor(255, 255, 255);
			Font f2 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
					Font.SIZE_LARGE);
			g.setFont(f2);
			g.drawString("Score = " + level.getScore(), 10, 1, g.TOP | g.LEFT);
			g.drawString(level.getEqu().getEquation(), 100, 1, g.TOP | g.LEFT);
			g.drawString("Spirits = " + level.getSpirit(), 200, 1, g.TOP
					| g.LEFT);

			g.setColor(0, 128, 0);
			g.fillRect(0, 30, getWidth(), getHeight());

			if (robot.getX() > 20
					&& (level.getMap().length - robot.getX() / 20) * 20 > (getWidth() - 50))
				x = robot.getX() - 40;

			if (robot.getY() > 20
					&& (level.getMap().length - robot.getY() / 20) * 20 >= getHeight() - 70)
				y = robot.getY() - 40;
			manager.setViewWindow(x, y, getWidth(), getHeight() - 20);
			manager.paint(g, 0, 30);

			flushGraphics();
		} else if (new_level) {
			g.setColor(0, 0, 0);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(255, 255, 255);
			Font f2 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
					Font.SIZE_LARGE);
			g.setFont(f2);
			g.drawString("You passed to level " + (current_level + 1), 0,
					getHeight() / 2, g.TOP | g.LEFT);
			flushGraphics();

		} else if (lose) {
			g.setColor(0, 0, 0);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(255, 255, 255);
			Font f2 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
					Font.SIZE_LARGE);
			g.setFont(f2);
			g.drawString(" you lost ", getWidth() / 2 - 100, getHeight() / 2,
					g.TOP | g.LEFT);
			flushGraphics();
		} else if (win) {
			g.setColor(0, 0, 0);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(255, 255, 255);
			Font f2 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD,
					Font.SIZE_LARGE);
			g.setFont(f2);
			g.drawString("You Won ..... :) your score :" + level.getScore(),
					getWidth() / 2 - 100, getHeight() / 2, g.TOP | g.LEFT);
			flushGraphics();

		}

	}

	private void checkUserInput() {
		if (robot.isAlive()) {
			int key = getKeyStates();
			if ((key & GameCanvas.RIGHT_PRESSED) != 0) {
				boolean right = true;
				robot.moveRight();
				if (robot.collidesWith(background, false)) {
					robot.moveLeft();
					right = false;
				}
				if (right)
					robot.update("right");

			} else if ((key & GameCanvas.LEFT_PRESSED) != 0) {
				boolean left = true;
				robot.moveLeft();
				if (robot.collidesWith(background, false)) {
					robot.moveRight();
					left = false;
				}
				if (left)
					robot.update("left");

			} else

			if ((key & GameCanvas.UP_PRESSED) != 0) {
				robot.moveUp();
				boolean up = true;
				if (robot.collidesWith(background, false)) {
					robot.moveDown();
					up = false;
				}
				if (up) {
					robot.update("up");
				}
			} else if ((key & GameCanvas.DOWN_PRESSED) != 0) {
				boolean down = true;
				robot.moveDown();
				if (robot.collidesWith(background, false)) {
					robot.moveUp();
					down = false;

				}
				if (down) {
					robot.update("down");
				}
			} 
			else if ((key & GameCanvas.GAME_A_PRESSED) != 0) {
				System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
				Bomb bomb = level.createBomb();
				if (bomb != null) {
					bomb.setPosition(robot.getX(), robot.getY());
					//bomb.setVisible(true);
					manager.append(bomb);
					repaint();
				}

			}
			Monster[] mon = level.getMonster();
			for (int i = 0; i < mon.length; i++) {
				if (robot.collidesWith(mon[i], false) && robot.isAlive()
						&& mon[i].isVisible()) {
					robot.setDie();
					break;
				}
			}
			Sprite s[] = level.getNum().getS();
			int number_req = level.getEqu().getTrueValue();
			for (int i = 0; i < s.length; i++) {
				if (s[i] != null && s[i].collidesWith(robot, false)
						&& robot.isAlive()) {
					int current_number = (s[i].getFrame() + 1) % 10;
					if (current_number == number_req) {
						new_level = true;
					} else {
						level.setWrong_number(level.getWrong_number() + 1);
					}
					s[i].setVisible(false);

				}
			}
		}
	}

	public int getX() {
		return x;
	}

	public static void setX(int x1) {
		x = x1;
	}

	public int getY() {
		return y;
	}

	public static void setY(int y1) {
		y = y1;
	}

	public static Player getRobot() {
		return robot;
	}

	public static Image getPlayer() {
		return player;
	}

	public static TiledLayer getBackground() {
		return background;
	}

	public static LayerManager getManager() {
		return manager;
	}

	public static Level getLevel() {
		return level;
	}

	public void commandAction(Command e, Displayable arg1) {

		if (e.getCommandType() == Command.EXIT)
			System.exit(0);
	}

}
