class Rectangle {

		int length, width, x, y;
		int red, blue, green;
		boolean[] choice = new boolean[2];

		public Rectangle(int len, int wid, int x1, int y1, int r, int b, int g,
				boolean[] fill) {
			length = len;
			width = wid;
			x = x1;
			y = y1;
			red = r;
			blue = b;
			green = g;
			choice = fill;
		}
}