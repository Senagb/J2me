package Characters;

import java.io.IOException;
import java.util.Random;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class Number {
	private Random rand = new Random();
	private Image im;
	private int[] nums;
	private Sprite[] s;
	private int counter = 0;

	public Number() {
		try {
			im = Image.createImage("/num.jpg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s = new Sprite[10];
		int[] temp = new int[10];
		nums = new int[10];
		int count = 0;

		while (count < 10) {
			int k = (rand.nextInt(9 - 0 + 1));
			if (temp[k] < Integer.MAX_VALUE) {
				nums[count] = k;
				temp[k] = Integer.MAX_VALUE;
				count++;
			}
		}

	}

	public Sprite getSprite() {
		if (counter < 10) {
			s[counter] = new Sprite(im, 20, 20);
			s[counter].setFrame(nums[counter]);
			counter++;
			return s[counter - 1];
		} else
			return null;

	}

	public Sprite[] getS() {
		return s;
	}

	public void setS(Sprite[] s) {
		this.s = s;
	}

	public int[] getNums() {
		return nums;
	}

	public void setNums(int[] nums) {
		this.nums = nums;
	}

}
