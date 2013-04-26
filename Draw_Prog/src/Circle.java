
class Circle {
	int radius, x, y;
	int red, blue, green;
	boolean[] choice = new boolean[2];

	public Circle(int radi, int x1, int y1, int r, int b, int g, boolean[] fill) {
		radius = radi;
		x = x1;
		y = y1;
		red = r;
		blue = b;
		green = g;
		choice = fill;

	}
}