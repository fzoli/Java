package jpatest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author zoli
 */
@WebServlet(name = "NodeServlet", urlPatterns = {"/Nodes"})
public class NodeServlet extends HttpServlet {
    
    @EJB
    private NodeTestBeanLocal nodeBean;

    @Override
    public void init() throws ServletException {
        super.init();
        nodeBean.createTestNodes();
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet NodeServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet NodeServlet at " + request.getContextPath() + "</h1>");
            printNodes(out);
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void printNodes(PrintWriter out) {
        List<Node> nodes = nodeBean.getTree().getChildren();
        out.println("<h2>Node list:</h2>");
        out.println("<ul>");
        for (Node node : nodes) {
            printNode(out, node);
        }
        out.println("</ul>");
    }
    
    private void printNode(PrintWriter out, Node node) {
        out.println("<li>\n" + node);
        if (node.isChildAvailable()) {
            out.println("<ul>");
            for (Node child : node.getChildren()) {
                printNode(out, child);
            }
            out.println("</ul>");
        }
        out.println("</li>");
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
