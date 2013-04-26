import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class connection_Methods {

	private static HttpConnection connection = null;
	private static InputStream inputstream = null;
	private static OutputStream outputstream = null;
	private final static String url_documents = "http://embedded-lab.heroku.com/documents.xml";
	private static Vector read = new Vector();
	private static int type;

	// Function: List documents
	// URL: <server>/documents.xml
	// Method: GET
	// Result: A list of documents (*)

	// get the list of documents from the server
	public static Vector showList() {
		type = 1;
		try {
			read = new Vector();
			// open connection
			connection = (HttpConnection) Connector.open(url_documents);
			connection.setRequestMethod(HttpConnection.GET);
			if (connection.getResponseCode() == HttpConnection.HTTP_OK) {
				inputstream = connection.openInputStream();
				Myparser();
			}
		} catch (Exception error) {
			System.out.println("Caught IOException: " + error.toString());
		}

		try {
			inputstream.close();
			connection.close();
		} catch (Exception error) {
			System.out.println("error in connection closing");
		}

		return read;
	}

	// Function: Search documents
	// URL: <server>/documents.xml
	// Method: GET
	// params: query
	// Result: A list of documents(*)

	// search for specific content
	public static Vector Search_Documents(String query) {
		try {
			read = new Vector();
			connection = (HttpConnection) Connector
					.open("http://embedded-lab.heroku.com/documents.xml?query="
							+ query);
			connection.setRequestMethod(HttpConnection.GET);
			if (connection.getResponseCode() == HttpConnection.HTTP_OK) {
				inputstream = connection.openInputStream();
				Myparser();
			}
			inputstream.close();
			connection.close();

		} catch (Exception error) {
			System.out.println("error in connection closing");
		}
		return read;

	}

	// Function: Upload a document
	// URL: <server>/documents.xml
	// Method: POST
	// params: document[title], document[author], document[content]
	// Result: The inserted document(**)

	// add new document to the server
	public static void Upload_a_document(documents doc) {
		try {
			read = new Vector();
			HttpConnection connection = (HttpConnection) Connector
					.open("http://embedded-lab.heroku.com/documents.xml");
			connection.setRequestMethod(HttpConnection.POST);
			connection.setRequestProperty("Content-Type", "application/xml");
			connection.setRequestProperty("Connection", "close");
			outputstream = connection.openOutputStream();
			String body = "<document>\n<title>" + doc.getTitle()
					+ "</title>\n<author>" + doc.getAuthor()
					+ "</author>\n<content> " + doc.getContent()
					+ " </content>\n</document>";
			byte data[] = body.getBytes();
			outputstream.write(data);

			outputstream.close();

			// Get Response
			InputStream is = connection.openInputStream();
			InputStreamReader r = new InputStreamReader(is);
			int ch = 0;
			StringBuffer response = new StringBuffer();
			while ((ch = r.read()) != -1) {
				response.append((char) ch);
			}

			r.close();
			System.out.println(response.toString());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	// Function: Delete a document
	// URL: <server>/documents/<doc_id>.xml
	// Method: DELETE (***)
	// Result: An empty OK result
	// _method=DELETE
	// delete specific document from the server
	public static void deleteDocument(int a) {

		try {
			HttpConnection connection = (HttpConnection) Connector
					.open("http://embedded-lab.heroku.com/documents/" + a
							+ ".xml");
			connection.setRequestMethod(HttpConnection.POST);
			String body = "_method=DELETE";
			connection.setRequestProperty("Content-Length",
					Integer.toString(body.length()));
			byte data[] = body.getBytes();
			outputstream = connection.openOutputStream();
			outputstream.write(data);

			outputstream.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Function: Update a document
	// URL: <server>/documents/<doc_id>.xml
	// Method: PUT (***)
	// Result: An empty OK result
	// to update certain document on the server
	public static void Update_a_document(int a, documents doc) {
		try {
			System.out.println(a);
			connection = (HttpConnection) Connector
					.open("http://embedded-lab.heroku.com/documents/" + a
							+ ".xml");
			connection.setRequestMethod(HttpConnection.POST);

			String body = data(doc);
			System.out.println(body);
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setRequestProperty("X-HTTP-Method-Override", "PUT");
			connection.setRequestProperty("Connection", "close");
			connection.setRequestProperty("Content-Length",
					Integer.toString(body.length()));

			byte data1[] = body.getBytes();
			outputstream = connection.openOutputStream();
			outputstream.write(data1);
			outputstream.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private static String data(documents doc) {
		String res = "<document>";
		if (doc.getAuthor() != null)
			res += "<author>" + doc.getAuthor() + "</author>";
		if (doc.getContent() != null)
			res += "<content>" + doc.getContent() + "</content>";
		if (doc.getTitle() != null)
			res += "<title>" + doc.getTitle() + "</title>";
		res += "</document>";
		return res;
	}

	// show a specific document
	public static Vector showDocument(int a) {
		type = -1;
		try {
			read = new Vector();
			connection = (HttpConnection) Connector
					.open("http://embedded-lab.heroku.com/documents/" + a
							+ ".xml");
			connection.setRequestMethod(HttpConnection.GET);
			if (connection.getResponseCode() == HttpConnection.HTTP_OK) {
				inputstream = connection.openInputStream();
				Myparser();
			}
			inputstream.close();
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return read;

	}

	// xml parser to parse the server results
	private static void Myparser() {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler() {
				boolean title = false, id = false, author = false,
						content = false;
				documents temp;

				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {
					if (qName.equalsIgnoreCase("title"))
						title = true;
					else if (qName.equalsIgnoreCase("id"))
						id = true;
					else if (qName.equalsIgnoreCase("author"))
						author = true;
					else if (qName.equalsIgnoreCase("content"))
						content = true;

				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					if (qName.equalsIgnoreCase("title"))
						title = false;
					else if (qName.equalsIgnoreCase("id"))
						id = false;
					else if (qName.equalsIgnoreCase("author"))
						author = false;
					else if (qName.equalsIgnoreCase("content"))
						content = false;
				}

				public void characters(char ch[], int start, int length)
						throws SAXException {
					if (author) {
						temp = new documents();
						String s = new String(ch, start, length);
						temp.setAuthor(s);
					} else if (content) {
						String s = new String(ch, start, length);
						temp.setContent(s);
					} else if (id) {
						if (type == 1)
							temp = new documents();
						String s = new String(ch, start, length);
						temp.setId(Integer.parseInt(s));
					} else if (title) {
						String s = new String(ch, start, length);
						temp.setTitle(s);
						read.addElement(temp);
					}
				}
			};
			saxParser.parse(inputstream, handler);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}