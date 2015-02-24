package Test_apiary;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    }
}
