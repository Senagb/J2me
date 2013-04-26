package Characters;

import java.util.Random;

public class Map_gen {

	private int width = 0, hight = 0;

	public Map_gen(int w, int h) {
		width = w;
		hight = h;
	}

	public int[][] generator() {
		int[][] map = new int[hight][width];
		Random r = new Random();

		for (int i = 0; i < map.length; i++) {
			int times = r.nextInt();
			if (times < 0)
				times *= -1;
			while (times >= width / 2)
				times %= width / 2;
			for (int j = 0; j < times; j++) {
				int col = r.nextInt();
				if (col < 0)
					col *= -1;
				while (col >= width)
					col %= width;
				if (col % 2 == 1)
					map[i][col] = 1;
				else
					map[i][col] = 2;
			}

		}
		// make the boarders
		for (int i = 0; i < map[0].length; i++) {
			map[0][i] = 1;
			map[hight - 1][i] = 1;
		}
		for (int i = 0; i < map.length; i++) {
			map[i][0] = 1;
			map[i][width - 1] = 1;
		}
		map[1][1] = 0;
		map[1][2] = 0;
		map[2][2] = 0;
		map[2][1] = 0;
		return map;
	}

}
