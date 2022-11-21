

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class forgetPWD
 */
@WebServlet("/forgetPWD")
public class forgetPWD extends HttpServlet {
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
    public forgetPWD() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//doGet(request, response);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");	
		String username = request.getParameter("email");
		//String ps = request.getParameter("password");
		String sql="select * from user where name='"+username+"'";
		PrintWriter out = response.getWriter();
		//aukxuhfqpybpqutl
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(sql);
			if (rs.next()) {							
				String pwd= rs.getString("password");
				final String fromEmail = "sam42898"; //requires valid gmail id
		        final String password = "bajmxdpaqcrajctf"; // correct password for gmail id
			      String to = username;
			 
			      // Sender's email ID needs to be mentioned
			      String from = "sam42898@gmail.com";
			 
			      // Assuming you are sending email from localhost
			      String host = "localhost";
			 
			      // Get system properties
			      Properties props = new Properties();
			        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
			        props.put("mail.smtp.port", "587"); //TLS Port
			        props.put("mail.smtp.auth", "true"); //enable authentication
			        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
			 
			      // Get the default Session object.
			        Authenticator auth = new Authenticator() {
			            //override the getPasswordAuthentication method
			            protected PasswordAuthentication getPasswordAuthentication() {
			                return new PasswordAuthentication(fromEmail, password);
			            }
			        };
			        Session session = Session.getInstance(props, auth);	
			      
				  // Set response content type
			      response.setContentType("text/html");			     

			      try{
			         // Create a default MimeMessage object.
			         MimeMessage message = new MimeMessage(session);
			         // Set From: header field of the header.
			         message.setFrom(new InternetAddress(from));
			         // Set To: header field of the header.
			         message.addRecipient(Message.RecipientType.TO,
			                                  new InternetAddress(to));
			         // Set Subject: header field
			         message.setSubject("Your login password");
			         // Now set the actual message
			         message.setText("Here is pwd:"+pwd);
			         // Send message
			         Transport.send(message);
			         String title = "Send Email";
			         String res = "Sent message successfully....";
			         String docType =
			         "<!doctype html public \"-//w3c//dtd html 4.0 " +
			         "transitional//en\">\n";
			         out.println(docType +
			         "<html>\n" +
			         "<head><title>" + title + "</title></head>\n" +
			         "<body bgcolor=\"#f0f0f0\">\n" +
			         "<h1 align=\"center\">" + title + "</h1>\n" +
			         "<p align=\"center\">" + res + "</p>\n" +
			         "</body></html>");
			      }catch (MessagingException mex) {
			         mex.printStackTrace();
			      }
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
