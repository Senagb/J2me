package Characters;

import java.io.IOException;

import javax.microedition.lcdui.Image;
import MyCanvas;

public class Player extends characters {
	boolean isAlive = true, first = true, hbl = false;
	int counter = 0;
	String direction = "right";

	public Player(Image image, int width, int height) {
		super(image, width, height);
		// TODO Auto-generated constructor stub
		this.image = image;
		this.width = width;
		this.height = height;
		this.speed = 20;
		this.setPosition(20, 20);
		setFrameSequence(new int[] { 9, 10, 11 });
	}

	public void update(String di) {
		if (counter > 1000000)
			counter = 0;
		if (isAlive) {
			if (!di.equalsIgnoreCase(direction)) {
				direction = di;
				if (di.equalsIgnoreCase("right"))
					setFrameSequence(new int[] { 9, 10, 11 });
				else if (di.equalsIgnoreCase("left"))
					setFrameSequence(new int[] { 8, 7, 6 });
				else if (di.equalsIgnoreCase("up"))
					setFrameSequence(new int[] { 5, 4, 3 });
				else if (di.equalsIgnoreCase("down"))
					setFrameSequence(new int[] { 2, 1, 0 });

			}
			if (counter % 5 == 0)
				nextFrame();
			counter++;
		} else {

			try {
				if (first) {
					setImage(Image.createImage("/explosion.PNG"), 20, 20);
					first = false;
				}
				counter++;
				if (counter % 5 == 0)
					nextFrame();
				int number = getFrame();
				if (number == 6) {
					setVisible(false);
					hbl = true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (MyCanvas.getLevel().getSpirit() > 0 && hbl) {
				setPosition(20, 20);
				setImage(image, 20, 20);
				setVisible(true);
				isAlive = true;
				first = true;
				hbl = false;
				MyCanvas.setX(0);
				MyCanvas.setY(0);
			}
		}

	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public void setDie() {
		if (isAlive) {
			isAlive = false;
			MyCanvas.getLevel().setSpirit(MyCanvas.getLevel().getSpirit() - 1);
			if (MyCanvas.getLevel().getSpirit() == 0)
				MyCanvas.setLose(true);
		}
	}
}
