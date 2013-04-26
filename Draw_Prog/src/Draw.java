import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

/**
 * this program to draw shapes (Rectangle -Circle ) on screen by the given
 * dimension and color and position
 * */
public class Draw extends MIDlet implements CommandListener, ItemStateListener {
	// variable used in the program
	private Display display;
	private List list;
	private Command cm_Back, cm_Next, cm_Finish;
	private CanvasDraw canvasDraw;
	int listItemIndex, current_Screen, c_red, c_blue, c_green;
	private TextField length, width, radius, rec_x, rec_y, cir_x, cir_y, mRed,
			mBlue, mGreen;
	private Form rec_form, cir_form, colors;
	private boolean REC, CIR;
	private Vector shapes;
	private Alert alert;
	private ChoiceGroup choiceGroup;
	private int screenWidth;
	private int screenHeight;
	private Gauge ga_Red, ga_Blue, ga_Green;

	// default constructor used to inislize the variables

	public Draw() {

		display = Display.getDisplay(this);
		shapes = new Vector();
		// commands
		cm_Back = new Command("Back", Command.BACK, 2);
		cm_Next = new Command("Next", Command.SCREEN, 2);
		cm_Finish = new Command("Finish", Command.SCREEN, 2);

		current_Screen = 0;
		canvasDraw = new CanvasDraw();

		// textFeilds dimension
		length = new TextField("width :", "", 30, TextField.NUMERIC);
		width = new TextField("height :", "", 30, TextField.NUMERIC);
		radius = new TextField("radius :", "", 30, TextField.NUMERIC);
		rec_x = new TextField("left corner x :", "", 30, TextField.NUMERIC);
		rec_y = new TextField("left corner y :", "", 30, TextField.NUMERIC);
		cir_x = new TextField("center x :", "", 30, TextField.NUMERIC);
		cir_y = new TextField("center y :", "", 30, TextField.NUMERIC);
		// length = new TextField("Length :", "", 30, TextField.NUMERIC);

		// Forms
		rec_form = new Form("Dimension and Position :");
		cir_form = new Form("Dimension and Position :");
		// list
		list = new List("Select shape to be drawn:", List.IMPLICIT);
		// TextFileds for colors
		// Red = new TextField("RED :", "", 30, TextField.NUMERIC);
		colors = new Form("Enter the Color Values :");

		ga_Red = new Gauge("Red Value", true, 255, 0);
		mRed = new TextField("Values", "", 30, 0);
		ga_Blue = new Gauge("Blue Value", true, 255, 0);
		mBlue = new TextField("Values", "", 30, 0);
		ga_Green = new Gauge("Green Value", true, 255, 0);
		mGreen = new TextField("Values", "", 30, 0);

		choiceGroup = new ChoiceGroup(null, Choice.EXCLUSIVE, new String[] {
				"fill shape", "don't fill shape" }, null);
		itemStateChanged(ga_Red);
		shapes_list();
		form_dim();
	}

	// set the forms attributes
	private void form_dim() {
		// rectangle dimension form
		rec_form.append(length);
		rec_form.append(width);
		rec_form.append(rec_x);
		rec_form.append(rec_y);
		rec_form.addCommand(cm_Back);
		rec_form.addCommand(cm_Next);
		rec_form.setCommandListener(this);
		// circle dimension form
		cir_form.append(cir_x);
		cir_form.append(cir_y);
		cir_form.append(radius);
		cir_form.addCommand(cm_Back);
		cir_form.addCommand(cm_Next);
		cir_form.setCommandListener(this);
		// color form
		colors.append(ga_Red);
		colors.append(mRed);
		colors.append(ga_Blue);
		colors.append(mBlue);
		colors.append(ga_Green);
		colors.append(mGreen);

		colors.append(choiceGroup);
		colors.addCommand(cm_Back);
		colors.addCommand(cm_Finish);
		colors.setCommandListener(this);
		colors.setItemStateListener(this);
	}

	// set the list attributes
	private void shapes_list() {
		list.append("Rectangle", null);
		list.append("Circle", null);
		list.addCommand(cm_Back);
		list.addCommand(cm_Next);
		list.setCommandListener(this);
	}

	protected void startApp() throws MIDletStateChangeException {
		display.setCurrent(canvasDraw);
	}

	// action listener
	/**
	 * current_Screen =0 canava current_Screen =1 shapes list current_Screen =2
	 * dimensions list current_Screen =3 color list
	 * */
	public void commandAction(Command c, Displayable d) {
		int value = 0;

		if (c == cm_Back) {
			if (current_Screen == 1) {
				display.setCurrent(canvasDraw);
				current_Screen--;
			} else if (current_Screen == 2) {
				display.setCurrent(list);
				current_Screen--;
			} else if (current_Screen == 3) {
				if (listItemIndex == 0)
					display.setCurrent(rec_form);
				else
					display.setCurrent(cir_form);
				current_Screen--;
			}
		} else if (c == cm_Next) {
			if (current_Screen == 1) {
				listItemIndex = list.getSelectedIndex();
				if (listItemIndex == 0) {
					display.setCurrent(rec_form);
					REC = true;
					CIR = false;
				} else {
					display.setCurrent(cir_form);
					REC = false;
					CIR = true;
				}
				current_Screen++;
			} else if (current_Screen == 2) {
				current_Screen++;
				display.setCurrent(colors);
			}
		} else if (c == cm_Finish) {
			boolean get[] = new boolean[choiceGroup.size()];
			try {
				// get the colors entered values
				c_blue = ga_Blue.getValue();
				c_red = ga_Red.getValue();
				c_green = ga_Green.getValue();
				choiceGroup.getSelectedFlags(get);
			} catch (Exception e) {
				value = -1;
				showError("Error",
						"You should fill colour fields with integer values ");
				System.out.println("Parsing ............ !!");
			}
			// check the color values
			if (value == 0) {
				boolean check = false;
				check = check_color(c_blue);
				if (check)
					check = check_color(c_green);
				if (check)
					check = check_color(c_red);
				Object temp = null;
				if (check) {
					try {
						if (REC) {
							Rectangle rectangle = new Rectangle(
									Integer.parseInt(length.getString()),
									Integer.parseInt(width.getString()),
									Integer.parseInt(rec_x.getString()),
									Integer.parseInt(rec_y.getString()), c_red,
									c_blue, c_green, get);
							temp = rectangle;
						} else if (CIR) {

							Circle circle = new Circle(Integer.parseInt(radius
									.getString()), Integer.parseInt(cir_x
									.getString()), Integer.parseInt(cir_y
									.getString()), c_red, c_blue, c_green, get);
							temp = circle;

						}
						shapes.addElement(temp);
						if (checkBoundraies(temp)) {
							System.out.println("trueeeeee");
							// add shapes to drawn vector
							current_Screen = 0;
							display.setCurrent(canvasDraw);
						} else {
							System.out.println("falseeee");
							alert = new Alert(
									"Warning",
									"Boundraies of the shape you are going to draw exceed the screen.Press ok to continue any way",
									null, AlertType.WARNING);
							alert.setTimeout(Alert.FOREVER);
							alert.addCommand(new Command("Ok", Command.OK, 1));
							alert.addCommand(new Command("Back",
									Command.CANCEL, 1));
							alert.setCommandListener(new CommandListener() {
								public void commandAction(Command c,
										Displayable d) {
									if (c.getLabel().equals("Ok")) {
										current_Screen = 0;
										display.setCurrent(canvasDraw);
									}
									if (c.getLabel().equals("Back")) {
										shapes.removeElementAt(shapes.size() - 1);
										display.setCurrent(colors);
										alert = null;
									}
								}

							});
							display.setCurrent(alert, canvasDraw);
						}
					} catch (Exception e) {

						showError("Error", "Check the Entered dimensions :) ");

					}
				} else {
					showError("Error",
							"Values of colours should be between 0 and 255");

				}
			}
		}

	}
   // check boundaries of the shape going to be drawn to warn user if there is any problem
	private boolean checkBoundraies(Object temp) {
		if (temp instanceof Rectangle) {
			Rectangle rec = (Rectangle) temp;
			if (rec.x > screenWidth || rec.y > screenHeight
					|| rec.length > screenWidth || rec.width > screenHeight
					|| (rec.x + rec.length) > screenWidth
					|| (rec.y + rec.width) > screenHeight)
				return false;

		} else if (temp instanceof Circle) {
			Circle cir = (Circle) temp;
			if (cir.x > screenWidth || cir.y > screenHeight
					|| (cir.x + cir.radius) > screenWidth
					|| (cir.y + cir.radius) > screenHeight
					|| (cir.x - cir.radius) < 0 || (cir.y - cir.radius < 0))
				return false;

		}
		return true;

	}
  //make an alert to show error message
	private void showError(String string, String string2) {
		// TODO Auto-generated method stub
		alert = new Alert(string, string2, null, AlertType.ERROR);
		alert.addCommand(new Command("Ok", Command.OK, 1));
		alert.setTimeout(Alert.FOREVER);
		alert.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c.getLabel().equals("Ok")) {
					display.setCurrent(colors);
					alert = null;
				}
			}
		});
		display.setCurrent(alert, colors);

	}
     //check the validation of colour values as ecery colour should have value between 0 and 255
	private boolean check_color(int y) {
		return y >= 0 && y <= 255;
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {

	}

	protected void pauseApp() {

	}

	// canava class
	class CanvasDraw extends Canvas implements CommandListener {

		private Command cm_Draw, cm_Exit, cm_Clear;

		public CanvasDraw() {
			screenWidth = getWidth();
			screenHeight = getHeight();
			cm_Draw = new Command("Draw", Command.OK, 0);
			cm_Exit = new Command("Exit", Command.EXIT, 2);
			cm_Clear = new Command("Clear Canvas", Command.SCREEN, 5);
			addCommand(cm_Exit);
			addCommand(cm_Clear);
			addCommand(cm_Draw);
			setCommandListener(this);
		}
  // Go through all the shapes and draw them
		public void paint(Graphics g) {
			g.setColor(255, 255, 255);
			g.fillRect(0, 0, getWidth(), getHeight());

			for (int i = 0; i < shapes.size(); i++) {
				Object temp = shapes.elementAt(i);
				if (temp instanceof Rectangle) {
					Rectangle rec = (Rectangle) temp;
					g.setColor(rec.red, rec.green, rec.blue);
					if (rec.choice[0])
						g.fillRect(rec.x, rec.y, rec.length, rec.width);
					else
						g.drawRect(rec.x, rec.y, rec.length, rec.width);
				} else if (temp instanceof Circle) {
					Circle circle = (Circle) temp;
					g.setColor(circle.red, circle.green, circle.blue);
					if (circle.choice[0])
						g.fillArc(circle.x - circle.radius, circle.y
								- circle.radius, circle.radius * 2,
								circle.radius * 2, 0, 360);
					else
						g.drawArc(circle.x - circle.radius, circle.y
								- circle.radius, circle.radius * 2,
								circle.radius * 2, 0, 360);
				}
			}

		}
    //make effect of selecting commands
		public void commandAction(Command c, Displayable d) {
			if (c == cm_Draw) {
				current_Screen++;
				display.setCurrent(list);
			} else if (c == cm_Clear) {

				shapes.removeAllElements();
				repaint();

			} else if (c == cm_Exit) {
				showConfirmation("Confirmation", "Do you really want to exit?");
				try {
					destroyApp(true);
				} catch (MIDletStateChangeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
    // make a confirmation alert
		private void showConfirmation(String string, String string2) {
			alert = new Alert(string, string2, null, AlertType.CONFIRMATION);
			alert.addCommand(new Command("Yes", Command.OK, 1));
			alert.addCommand(new Command("No", Command.CANCEL, 1));
			alert.setCommandListener(new CommandListener() {
				public void commandAction(Command c, Displayable d) {
					if (c.getLabel().equals("Yes")) {
						notifyDestroyed();
					}
					if (c.getLabel().equals("No")) {
						closeAlert();
					}
				}

			});
			display.setCurrent(alert, canvasDraw);

		}

		private void closeAlert() {
			display.setCurrent(canvasDraw);
			alert = null;

		}
	}

	public void itemStateChanged(Item item) {
		try {
			if (item == ga_Red)
				mRed.setString("" + ga_Red.getValue());
			else if (item == ga_Blue)
				mBlue.setString("" + ga_Blue.getValue());
			else if (item == ga_Green)
				mGreen.setString("" + ga_Green.getValue());
			else if (item == mRed) {
				int res = calculate(mRed.getString());
				if (res != -1) {
					ga_Red.setValue(res);
					mRed.setString("" + res);
				}
			} else if (item == mBlue) {
				int res = calculate(mBlue.getString());
				if (res != -1) {
					ga_Blue.setValue(res);
					mBlue.setString("" + res);
				}
			} else if (item == mGreen) {
				int res = calculate(mGreen.getString());
				if (res != -1) {
					ga_Green.setValue(res);
					mGreen.setString("" + res);
				}
			}
		} catch (Exception e) {
			showError("Error",
					"You should fill colour fields with integer values ");
		}

	}

	private int calculate(String g) {
		if (!g.equals("")) {

			int temp = Integer.parseInt(g);
			if (temp > 255)
				return 255;
			else if (temp <= 0)
				return 0;
			else
				return temp;

		}
		return -1;
	}
}
