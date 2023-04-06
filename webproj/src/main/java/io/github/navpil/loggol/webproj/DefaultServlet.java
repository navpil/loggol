package io.github.navpil.loggol.webproj;

import io.github.navpil.loggol.ourlib.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "default", urlPatterns = {"/"})
public class DefaultServlet extends HttpServlet {

    private static final Logger LOG = LogManager.getLogger(DefaultServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.error("This will be logged");
        try {
            Main.callWithWarning();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resp.getWriter().append("OK");
    }
}
