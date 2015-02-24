package Test_apiary;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Dispatcher of HTTP-requests.
 * <br>
 * One object for all requests.
 * <br>
 * Each request is performed in a separate thread.
 */
public class ServletDispatcher extends HttpServlet {
    private static String username;
    private static String password;

    @Override
    public String getServletInfo() {
        return "Test_apiary";
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Test_apiary hello");

        ServletContext sc = getServletContext();
        String resourcesPath = sc.getInitParameter("GitHubWebhooks_resources");

        try {
            File dataFile = new File(resourcesPath + File.separator + "settings.xml");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(dataFile);

            Element root = doc.getDocumentElement();

            NodeList account;
            account = root.getElementsByTagName("account");

            if (account.getLength() > 0) {
                Element extractElem = (Element) account.item(0);

                username = extractElem.getAttribute("username");
                password = extractElem.getAttribute("password");
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        //processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        System.out.println("hello");
        StringBuilder builder = new StringBuilder();
        String aux = "";

        while ((aux = request.getReader().readLine()) != null) {
            builder.append(aux);
        }

        String text = builder.toString();
        System.out.println(text);
        sendEmail(text);
    }

    public static void sendEmail(String str) {
        // Recipient's email ID needs to be mentioned.
        String to = username;

        // Sender's email ID needs to be mentioned
        String from = username;

        // Get system properties
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(
            properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            }
        );

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(
                Message.RecipientType.TO, new InternetAddress(to)
            );

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText(str);

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
