package Characters;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

abstract class characters extends Sprite {
	public characters(Image image, int frameWidth, int frameHeight) {
		super(image, frameWidth, frameHeight);
		// TODO Auto-generated constructor stub
	}

	protected Image image;
	protected int width;
	protected int height;
	protected int speed;
	protected int frame;

	public void moveRight() {
		this.move(speed, 0);
	}

	public void moveLeft() {
		this.move(-speed, 0);

	}

	public void moveUp() {
		this.move(0, -speed);

	}

	public void moveDown() {
		this.move(0, speed);

	}

	public abstract void update(String di);
}
