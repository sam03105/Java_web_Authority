

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;




/**
 * Servlet implementation class logincheck
 */
@WebServlet("/logincheck")
public class logincheck extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection conn = null;
    String targetDB;
    int cannotConnect = 0;
    HttpSession session;
    public void init() throws ServletException {
        String driver_type = "com.mysql.jdbc.Driver";
        String url         = "jdbc:mysql://localhost:3306/cgu?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
        String username    = "root";
        String passwd      = "root";        
        
        try{        	
          Class.forName(driver_type);
          conn = DriverManager.getConnection(url, username, passwd);
          
        }
        catch (ClassNotFoundException e) {
          cannotConnect = 1;
          System.out.println("class not found");
          System.out.println(e.toString());
        }
        catch (SQLException e){
          cannotConnect = 2;
          System.out.println("sql error");
          System.out.println(e.toString());
        }
      }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public logincheck() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("null")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");	
		String username = request.getParameter("username");
		String ps = request.getParameter("password");
		String sql="select * from user where name='"+username+"' and password='"+ ps +"'";
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		try {
			Statement stat = conn.createStatement();
			ResultSet resultSet = stat.executeQuery(sql);
			if (resultSet.next()) {
				session.setAttribute("login", true);				
				response.sendRedirect("./html/index.htm");
				out.print("login true");
			} else {
				out.print("login false");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
