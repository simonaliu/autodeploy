package com.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.servlet.file.UploadManager;
import com.servlet.file.UploadThreadManager;

public class LoginServlet extends HttpServlet{
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
		process(req,resp);
		
	}
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException{
		process(req,resp);
		String stayTime = req.getParameter("stayTime");
		if(stayTime!=null){
		    System.out.println(stayTime);
		}
	}
	private void process(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		String username = req.getParameter("username");
		String upassword = req.getParameter("upassword");
		req.setAttribute("username", username);
		
		UploadManager manager = new UploadManager(username);
		UploadThreadManager.getInstance().addThread(manager);
		
		//resp.sendRedirect("/stayTime/ShowUserInfo.jsp");
		RequestDispatcher rd = req.getRequestDispatcher("/ShowUserInfo.jsp");
		rd.forward(req, resp);
		}
	}


