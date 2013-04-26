import java.io.IOException;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

public class Main_Midlet extends MIDlet implements CommandListener {

	static MyCanvas myCanvas; // Contains a custom Canvas class

	private static final Command CMD_EXIT = new Command("Exit", Command.EXIT, 1);
	Alert alert;
	private static Display display;

	public Main_Midlet() {
		myCanvas = new MyCanvas(); // COnstruct the canvas
	}

	public void startApp() {
		display = Display.getDisplay(this);
		myCanvas.addCommand(CMD_EXIT); // Add commands to the canvas
		myCanvas.setCommandListener(this);
		try {
			showConfirmation("Bomber Man", "   welcome to bomber man ");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// display.setCurrent(myCanvas); // Add canvas to screen
		myCanvas.repaint(); // force repaint of the canvas
	}

	private void showConfirmation(String string, String string2)
			throws IOException {
		// TODO Auto-generated method stub
		Image img = Image.createImage("/Bomberman.gif");
		alert = new Alert(string, string2, img, AlertType.CONFIRMATION);
		alert.setImage(img);
		alert.addCommand(new Command("Start game", Command.OK, 1));
		alert.addCommand(new Command("Exit", Command.EXIT, 1));

		alert.setTimeout(Alert.FOREVER);
		alert.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c.getLabel().equals("Start game")) {
					// display.setCurrent(myCanvas);
					// alert = null;
					showEquation();
				}
				if (c.getLabel().equals("Exit")) {
					notifyDestroyed();
				}
			}

		});
		display.setCurrent(alert, myCanvas);

	}

	private void showEquation() {
		// TODO Auto-generated method stub
		alert = new Alert("Equation", myCanvas.getLevel().getEqu()
				.getEquation(), null, AlertType.CONFIRMATION);
		alert.setTimeout(10000);
		display.setCurrent(alert, myCanvas);
	}

	public void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
	}

	// Handle softkeys
	public void commandAction(Command c, Displayable d) {
		if (c == CMD_EXIT) {
			destroyApp(false);
			notifyDestroyed();
		}

	}

	public static Display getDisplay() {
		return display;
	}

	public static MyCanvas getMyCanvas() {
		return myCanvas;
	}

	public void setMyCanvas(MyCanvas myCanvas) {
		this.myCanvas = myCanvas;
	}

}
