package org.j2auth.test.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.j2auth.filters.CookieVerifier;
import org.j2auth.main.AuthContext;
import org.j2auth.util.Encoder;
import org.j2auth.util.SimpleEncoder;

/**
 * test servlet
 */
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Home() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String methodName = request.getParameter("method");
		try {
			Method m = this.getMethod(methodName);
			m.invoke(this, request,response);
		}catch(Exception e){
			response.getWriter().println("no such method:" + methodName);
		}
	}
	
	public void hello(HttpServletRequest req, HttpServletResponse res){
		try {
			res.getWriter().println("fuck");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setcookie(HttpServletRequest req, HttpServletResponse res) throws IOException{
		Cookie c_name = new Cookie(AuthContext.COOKIE_USER_ACCOUNT,"zhangyu");
		Cookie c_pass = new Cookie(AuthContext.COOKIE_USER_PASSWORD,(new SimpleEncoder()).encode("zhangyu"));
		c_name.setPath("/");
		c_pass.setPath("/");
		res.addCookie(c_name);
		res.addCookie(c_pass);
		res.getWriter().println("done.");
	}
	
	public void authinfo(HttpServletRequest req, HttpServletResponse res) throws IOException{
		HttpSession s = req.getSession(true);
		AuthContext ac = (AuthContext) s.getAttribute(AuthContext.SESSION);
		res.getWriter().println(ac);
	}
	
	public void forword(HttpServletRequest req,HttpServletResponse res) throws ServletException, IOException{
		req.getRequestDispatcher("login.jsp").forward(req, res);
	}
	
	private Method getMethod(String methodName){
		Method m = null;
		try {
			m = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return m;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
