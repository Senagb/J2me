import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

public class connection extends MIDlet implements CommandListener {
	private Command exit, showList, show, delete, search, upload, update,
			finish, back, next;
	private Display display;
	private Form form, uploadForm, searchForm, numberForm, updateForm;
	private Alert alert;
	private TextField title, author, content, query;
	private int current = 0;
	private ChoiceGroup num_get_form, update_get_form;
	private String[] list;
	private Vector temp;

	public connection() {
		display = Display.getDisplay(this);
		exit = new Command("Exit", Command.EXIT, 1);
		showList = new Command("show list", Command.ITEM, 1);
		show = new Command("show", Command.SCREEN, 1);
		delete = new Command("delete", Command.OK, 1);
		upload = new Command("upload", Command.HELP, 1);
		search = new Command("search", Command.HELP, 1);
		update = new Command("update", Command.HELP, 1);
		finish = new Command("finish", Command.OK, 1);
		back = new Command("back", Command.CANCEL, 1);
		next = new Command("next", Command.OK, 1);

		form = new Form("Http Connection");
		form.addCommand(exit);
		form.addCommand(showList);
		form.addCommand(show);
		form.addCommand(delete);
		form.addCommand(upload);
		form.addCommand(search);
		form.addCommand(update);
		form.setCommandListener(this);

		uploadForm = new Form("Form");
		title = new TextField("Title :", "", 30, TextField.ANY);
		author = new TextField("Author :", "", 30, TextField.ANY);
		content = new TextField("Content :", "", 30, TextField.ANY);
		uploadForm.append(title);
		uploadForm.append(author);
		uploadForm.append(content);
		uploadForm.addCommand(finish);
		uploadForm.addCommand(back);
		uploadForm.setCommandListener(this);

		searchForm = new Form("Search");
		query = new TextField("Enter the query :", "", 30, TextField.ANY);
		searchForm.append(query);
		searchForm.addCommand(finish);
		searchForm.addCommand(back);
		searchForm.setCommandListener(this);

		updateList();

		numberForm = new Form("");
		num_get_form = new ChoiceGroup("Select Choice", Choice.POPUP, list,
				null);
		numberForm.append(num_get_form);
		numberForm.addCommand(finish);
		numberForm.addCommand(back);
		numberForm.setCommandListener(this);

		updateForm = new Form("");
		update_get_form = new ChoiceGroup("Select Choice", Choice.POPUP, list,
				null);
		updateForm.append(update_get_form);
		updateForm.addCommand(next);
		updateForm.addCommand(back);
		updateForm.setCommandListener(this);

	}

	private void updateList() {
		try {
			temp = connection_Methods.showList();
			list = new String[temp.size()];
			form.append("Document list :\n");
			String s;
			for (int i = 0; i < temp.size(); i++) {
				s = ((documents) temp.elementAt(i)).getTitle();
				list[i] = (i + 1) + " " + s;
				form.append((i + 1) + "-" + s + "\n");
			}
		} catch (Exception e) {
		}

	}

	public void startApp() {
		display.setCurrent(form);
	}

	public void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
		notifyDestroyed();
	}

	public void commandAction(Command command, Displayable displayable) {
		if (command == exit) {
			showConfirmation("Confirmation", "Do you really want to exit?");
			destroyApp(true);

		} else if (command == showList) {
			form.deleteAll();

			new Thread() {
				public void run() {
					// list documents
					updateList();
				}
			}.start();

		} else if (command == show) {
			current = 3;
			display.setCurrent(get_numberForm());

		} else if (command == delete) {
			current = 4;
			display.setCurrent(get_numberForm());
		} else if (command == upload) {
			current = 1;
			display.setCurrent(uploadForm);

		} else if (command == search) {
			current = 2;
			display.setCurrent(searchForm);
		} else if (command == update) {
			current = 5;
			display.setCurrent(get_updateForm());

		} else if (command == next) {
			current = 6;
			new Thread() {
				public void run() {
					int num = update_get_form.getSelectedIndex();
					Vector temp2 = connection_Methods
							.showDocument(((documents) temp.elementAt(num))
									.getId());
					documents temp = (documents) temp2.elementAt(0);
					author.setString(temp.getAuthor());
					title.setString(temp.getTitle());
					content.setString(temp.getContent());

				}
			}.start();
			display.setCurrent(uploadForm);
		} else if (command == finish) {
			if (current == 1) { // upload
				new Thread() {
					public void run() {
						documents doc = new documents();
						doc.setTitle(title.getString());
						doc.setAuthor(author.getString());
						doc.setContent(content.getString());
						connection_Methods.Upload_a_document(doc);
					}
				}.start();

				showInformation("Uploaded done successfully", "     title:\n"
						+ "     " + title.getString() + "\n" + "     author:\n"
						+ "     " + author.getString() + "\n"
						+ "     content:\n" + "     " + content.getString());
			} else if (current == 2) { // search
				new Thread() {
					public void run() {
						String m = query.getString();
						String res = "";
						for (int i = 0; i < m.length(); i++) {
							if (m.charAt(i) == ' ')
								res += "%20";
							else
								res += m.charAt(i);
						}
						Vector temp = connection_Methods.Search_Documents(res);
						showInformation("Search results", data(temp));
					}

				}.start();

			} else if (current == 3) { // show document
				new Thread() {
					public void run() {
						int num = num_get_form.getSelectedIndex();
						Vector temp2 = connection_Methods
								.showDocument(((documents) temp.elementAt(num))
										.getId());
						documents temp = (documents) temp2.elementAt(0);
						showInformation(
								"Requested Document",
								"     title:\n" + "     " + temp.getTitle()
										+ "\n" + "     author:\n" + "     "
										+ temp.getAuthor() + "\n"
										+ "     content:\n" + "     "
										+ temp.getContent());
					}
				}.start();
			}

			else if (current == 4) { // delete
				new Thread() {
					public void run() {
						int num = num_get_form.getSelectedIndex();
						connection_Methods.deleteDocument(((documents) temp
								.elementAt(num)).getId());
						showInformation("Delete message",
								"Delete done successfully");
					}
				}.start();
			}

			else if (current == 6) { // update
				new Thread() {
					public void run() {
						int num = update_get_form.getSelectedIndex();
						documents doc = new documents();
						doc.setAuthor(author.getString());
						doc.setTitle(title.getString());
						doc.setContent(content.getString());
						connection_Methods.Update_a_document(
								((documents) temp.elementAt(num)).getId(), doc);
						showInformation("update message",
								"Update done successfully");
					}
				}.start();
			}

		} else if (command == back) {
			if (current == 1 || current == 2 || current == 3 || current == 4) {
				current = 0;
				form.deleteAll();

				new Thread() {
					public void run() {
						updateList();
					}
				}.start();
				display.setCurrent(form);

			}
		}

	}

	private Displayable get_updateForm() {
		updateList();
		updateForm = new Form("");
		update_get_form = new ChoiceGroup("Select Choice", Choice.POPUP, list,
				null);
		updateForm.append(update_get_form);
		updateForm.addCommand(next);
		updateForm.addCommand(back);
		updateForm.setCommandListener(this);
		return updateForm;
	}

	private Displayable get_numberForm() {
		updateList();
		numberForm = new Form("");
		num_get_form = new ChoiceGroup("Select Choice", Choice.POPUP, list,
				null);
		numberForm.append(num_get_form);
		numberForm.addCommand(finish);
		numberForm.addCommand(back);
		numberForm.setCommandListener(this);
		return numberForm;
	}

	private String data(Vector temp) {
		String res = "";
		for (int i = 0; i < temp.size(); i++) {
			documents doc = (documents) temp.elementAt(i);
			res += "title:\n" + "     " + doc.getTitle() + "\n";
		}
		if (res.length() == 0)
			return "keyword not found";
		return res;
	}

	private void showInformation(String string, String string2) {
		// TODO Auto-generated method stub
		alert = new Alert(string, string2, null, AlertType.ERROR);
		alert.addCommand(new Command("Ok", Command.OK, 1));
		alert.setTimeout(Alert.FOREVER);
		alert.setCommandListener(new CommandListener() {
			public void commandAction(Command c, Displayable d) {
				if (c.getLabel().equals("Ok")) {
					current = 0;
					form.deleteAll();
					updateList();
					display.setCurrent(form);
					alert = null;
				}
			}
		});
		display.setCurrent(alert, form);

	}

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
					display.setCurrent(form);
					alert = null;
				}
			}

		});
		display.setCurrent(alert, form);

	}

}