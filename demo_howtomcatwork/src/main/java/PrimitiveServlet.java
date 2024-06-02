import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class PrimitiveServlet implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        log.info("PrimitiveServlet init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        log.info("PrimitiveServlet service");
        PrintWriter out = servletResponse.getWriter();
//        out.println("<html><body>");
//        out.println("<h1>Call PrimitiveServlet Service method </h1>");
//        out.println("</body></html>");
        String result = "HTTP/1.1 200 OK\r\n" +
                "Service: Tomcat 1.0\r\n" +
                "Date: Mon, 27 Jul 2009 12:28:53 GMT\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<h1>Call PrimitiveServlet Service method </h1>";
        out.println(result);
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
