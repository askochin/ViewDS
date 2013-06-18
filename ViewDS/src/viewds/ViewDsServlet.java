package viewds;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class ViewDsServlet!!!!!!!
 */
@WebServlet("/viewds.txt")
public class ViewDsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewDsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
	    try {
            Context initContext = new InitialContext();
            Object jdbcEntry = initContext.lookup("java:/comp/env/jdbc");
            if (jdbcEntry == null) {
                response.getWriter().write("Not found JDBC context in JNDI");
            } else {
                NamingEnumeration<NameClassPair> list = initContext.list("java:/comp/env/jdbc");
                while (list.hasMore()) {
                    
                    NameClassPair nc = list.next();
                    response.getWriter().write("\r\n\r" + nc.getName() + ": " + nc.getClassName() + ": ");
                    
                    Object obj = initContext.lookup("java:/comp/env/jdbc/" + nc.getName());
                    if (obj instanceof DataSource) {
                        response.getWriter().write(getConnectionTestResult( (DataSource) obj));
                    } else {
                        response.getWriter().write("not a DataSource");
                    }
                }
            }
        } catch (NamingException e) {
            response.getWriter().write("NamingException occured: " + e.getMessage());
            e.printStackTrace();
        }
	}
	
	
	private String getConnectionTestResult(DataSource ds) {
	    try {
            Connection conn = ds.getConnection();
            conn.close();
            return "OK";
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
