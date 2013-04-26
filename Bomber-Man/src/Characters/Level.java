package Characters;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.lcdui.Image;

public class Level {

	private Monster[] monster;
	private Vector bombs;
	private int numOfBombs;
	private int score;
	private int spirit, wrong_number;
	private Equation equ;
	private int[][] map;
	private Image im_odd, im_even, bombImage;
	private Number num;

	public int getWrong_number() {
		return wrong_number;
	}

	public void setWrong_number(int wrong_number) {
		this.wrong_number = wrong_number;
	}

	public Level(int numMonsters, int numBomb, int map_x, int map_y) {
		num = new Number();
		numOfBombs = numBomb;
		monster = new Monster[numMonsters];
		bombs = new Vector();
		score = 0;
		spirit = 3;
		wrong_number = 0;
		equ = new Equation();
		Map_gen m = new Map_gen(map_x, map_y);
		map = m.generator();
		try {
			im_odd = Image.createImage("/a.png");
			im_even = Image.createImage("/Ghost-Rectangle.png");
			bombImage = Image.createImage("/backup.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inisilaze();

	}

	public Number getNum() {
		return num;
	}

	public void setNum(Number num) {
		this.num = num;
	}

	private void inisilaze() {

		for (int i = 0; i < monster.length; i++) {
			if (i % 2 == 1)
				monster[i] = new Monster(im_odd, 20, 20);
			else
				monster[i] = new Monster(im_even, 20, 20);
		}

	}

	public int getNumOfBombs() {
		return numOfBombs;
	}

	public void setNumOfBombs(int numOfBombs) {
		this.numOfBombs = numOfBombs;
	}

	public Bomb createBomb() {
	//	System.out.println("bomb is created");
		System.out.println("bombs size = "+bombs.size());
		for (int i = 0; i < bombs.size(); i++) {
			Bomb b = (Bomb) bombs.elementAt(i);
			if (!b.isVisible()){
	//			System.out.println("entered");
				bombs.removeElementAt(i);
			}
		}
		if (bombs.size() < numOfBombs) {
		//	System.out.println("can create a bomb");
			Bomb bomb = new Bomb(bombImage, 20, 20);
			bombs.addElement(bomb);
		//	System.out.println("-----------------");
			return bomb;
		}
		 return null;
	}

	public void update() {

		for (int i = 0; i < monster.length; i++) {
			monster[i].update("");
		}
		for (int i = 0; i < bombs.size(); i++) {
			Bomb b = (Bomb) bombs.elementAt(i);
			b.update("");
			if (!b.isVisible())
				bombs.removeElementAt(i);
		}

	}

	public Monster[] getMonster() {
		return monster;
	}

	public void setMonster(Monster[] monster) {
		this.monster = monster;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getSpirit() {
		return spirit;
	}

	public void setSpirit(int spirit) {
		this.spirit = spirit;
	}

	public Equation getEqu() {
		return equ;
	}

	public void setEqu(Equation equ) {
		this.equ = equ;
	}

	public int[][] getMap() {
		return map;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public Vector getBombs() {
		return bombs;
	}

}
