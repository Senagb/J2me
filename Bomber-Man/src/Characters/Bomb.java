package Characters;

import java.io.IOException;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.lcdui.game.TiledLayer;

import MyCanvas;

public class Bomb extends characters {
	private int time_to_vanish = 0, counter = 0, time_to_explode = 0;
	private boolean exploded = false, stopper = false;
	private Image im;
	private explosion direction[] = new explosion[4];

	public Bomb(Image image, int frameWidth, int frameHeight) {
		super(image, frameWidth, frameHeight);
		try {
			im = Image.createImage("/Bomb-Explode.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		direction[0] = new explosion(im, 20, 20);// up
		direction[1] = new explosion(im, 20, 20);// down
		direction[2] = new explosion(im, 20, 20);// left
		direction[3] = new explosion(im, 20, 20);// right
	}

	public void update(String di) {
		if (counter >= 100000) {
			counter = 0;
			time_to_explode = 0;
		}
		if (counter % 5 == 0 && time_to_explode < 70)
			nextFrame();

		if (time_to_explode >= 70 && !exploded)
			explode();

		if (exploded) {
			time_to_vanish++;
			if (time_to_vanish >= 10)
				vanish();
		}
		time_to_explode++;
		counter++;
	}

	private void vanish() {
		direction[0].setVisible(false);
		direction[1].setVisible(false);
		direction[2].setVisible(false);
		direction[3].setVisible(false);
		setVisible(false);
//		int t = MyCanvas.getLevel().getNumOfBombs();
//		if (!stopper && t> 0) {
//			MyCanvas.getLevel().setNumOfBombs((t - 1));
//			stopper = true;
//		}
	}

	public void explode() {
		exploded = true;
		setImage(im, 20, 20);
		setFrame(0);
		LayerManager m = MyCanvas.getManager();
		TiledLayer t = MyCanvas.getBackground();
		int x_pos = getX() / 20, y_pos = getY() / 20;

		int up_cell = t.getCell(x_pos, y_pos - 1);
		int down_cell = t.getCell(x_pos, y_pos + 1);
		int left_cell = t.getCell(x_pos - 1, y_pos);
		int right_cell = t.getCell(x_pos + 1, y_pos);

		Player p = MyCanvas.getRobot();

		m.remove(t);
		if (up_cell != 1) {
			direction[0].setFrame(1);
			direction[0].setPosition(getX(), getY() - 20);
			m.append(direction[0]);
			if (up_cell == 2) {
				t.fillCells(x_pos, y_pos - 1, 1, 1, 0);
				Sprite e = MyCanvas.getLevel().getNum().getSprite();
				if (e != null) {
					m.append(e);
					e.setPosition(x_pos * 20, (y_pos - 1) * 20);
				}
			}
		}
		if (down_cell != 1) {
			direction[1].setFrame(3);
			direction[1].setPosition(getX(), getY() + 20);
			m.append(direction[1]);
			if (down_cell == 2) {
				t.fillCells(x_pos, y_pos + 1, 1, 1, 0);
				Sprite e = MyCanvas.getLevel().getNum().getSprite();
				if (e != null) {
					m.append(e);
					e.setPosition(x_pos * 20, (y_pos + 1) * 20);
				}
			}
		}
		if (left_cell != 1) {
			direction[2].setFrame(4);
			direction[2].setPosition(getX() - 20, getY());
			m.append(direction[2]);
			if (left_cell == 2) {
				t.fillCells(x_pos - 1, y_pos, 1, 1, 0);
				Sprite e = MyCanvas.getLevel().getNum().getSprite();
				if (e != null) {
					m.append(e);
					e.setPosition((x_pos - 1) * 20, y_pos * 20);
				}
			}
		}
		if (right_cell != 1) {
			direction[3].setFrame(2);
			direction[3].setPosition(getX() + 20, getY());
			m.append(direction[3]);
			if (right_cell == 2) {
				t.fillCells(x_pos + 1, y_pos, 1, 1, 0);
				Sprite e = MyCanvas.getLevel().getNum().getSprite();
				if (e != null) {
					m.append(e);
					e.setPosition((x_pos + 1) * 20, y_pos * 20);
				}
			}
		}
		m.append(t);
		for (int j = 0; j < MyCanvas.getLevel().getMonster().length; j++)
			if (this.collidesWith(MyCanvas.getLevel().getMonster()[j], true)
					|| direction[0].collidesWith(MyCanvas.getLevel()
							.getMonster()[j], true)
					|| direction[1].collidesWith(MyCanvas.getLevel()
							.getMonster()[j], true)
					|| direction[2].collidesWith(MyCanvas.getLevel()
							.getMonster()[j], true)
					|| direction[3].collidesWith(MyCanvas.getLevel()
							.getMonster()[j], true))
				MyCanvas.getLevel().getMonster()[j].setDie();

		if (this.collidesWith(p, false) || direction[0].collidesWith(p, true)
				|| direction[1].collidesWith(p, true)
				|| direction[2].collidesWith(p, true)
				|| direction[3].collidesWith(p, true)) {
			MyCanvas.setY(0);
			MyCanvas.setX(0);
			p.setDie();

		}
	}

	public explosion[] getDirection() {
		return direction;
	}

	public void setDirection(explosion[] direction) {
		this.direction = direction;
	}

}
