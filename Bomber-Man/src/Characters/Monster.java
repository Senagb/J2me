package Characters;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.TiledLayer;

import MyCanvas;

public class Monster extends characters {
	private int value, counter = 0, n = 0;
	boolean isAlive = true, first = true;

	public Monster(Image image, int width, int height) {
		super(image, width, height);
		// TODO Auto-generated constructor stub
		Random rand = new Random();
		this.image = image;
		this.width = width;
		this.height = height;
		this.speed = rand.nextInt(4 - 1 + 1);
		this.value = rand.nextInt(5 - 0 + 1) % 4;
	}

	public void update(String di) {
		if (isAlive) {
			Player p = MyCanvas.getRobot();
			TiledLayer t = MyCanvas.getBackground();
			Vector bombs = MyCanvas.getLevel().getBombs();

			boolean hit = false;

			// move right
			if (this.value == 0) {
				moveRight();
				for (int i = 0; i < bombs.size(); i++) {
					Bomb b = (Bomb) bombs.elementAt(i);
					if (b.isVisible() && this.collidesWith(b, false)) {
						hit = true;
						break;
					}
				}
				if (this.collidesWith(t, false) || hit) {
					moveLeft();
					this.value = 1;
				}
			}
			// move left
			else if (this.value == 1) {
				moveLeft();
				for (int i = 0; i < bombs.size(); i++) {
					Bomb b = (Bomb) bombs.elementAt(i);
					if (b.isVisible() && this.collidesWith(b, false)) {
						hit = true;
						break;
					}
				}
				if (this.collidesWith(t, false) || hit) {
					moveRight();

					this.value = 2;
				}
			}
			// move up
			else if (this.value == 2) {
				moveUp();
				for (int i = 0; i < bombs.size(); i++) {
					Bomb b = (Bomb) bombs.elementAt(i);
					if (b.isVisible() && this.collidesWith(b, false)) {
						hit = true;
						break;
					}
				}
				if (this.collidesWith(t, false) || hit) {
					moveDown();
					this.value = 3;
				}
			}
			// move down
			else if (this.value == 3) {
				moveDown();
				for (int i = 0; i < bombs.size(); i++) {
					Bomb b = (Bomb) bombs.elementAt(i);
					if (b.isVisible() && this.collidesWith(b, false)) {
						hit = true;
						break;
					}
				}
				if (this.collidesWith(t, false) || hit) {
					moveUp();
					this.value = 0;
				}
			}

			// moveRight();
			if (counter % 5 == 0)
				this.nextFrame();
			counter++;
			if (this.collidesWith(p, true)) {
				p.setDie();
			}

		} else {

			try {
				if (first) {
					setImage(Image.createImage("/Ghost-Explode1.png"), 20, 20);
					first = false;
				}
				counter++;
				if (counter % 5 == 0)
					nextFrame();
				int number = getFrame();
				if (number == 4) {
					setVisible(false);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void move(TiledLayer t) {

	}

	public void setDie() {
		if (isAlive) {
			isAlive = false;
			int i = MyCanvas.getLevel().getScore();
			MyCanvas.getLevel().setScore(i + 100);
		}
	}

}
