package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.servlet.file.UploadManager;
import com.servlet.file.UploadThreadManager;
import com.ssh.Shell;

public class dockerDeployServlet extends HttpServlet {

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
		String username = request.getParameter("username");
		
		UploadManager manager = UploadThreadManager.getInstance().getThreadByUserName(username);
		response.setContentType("text/html; charset=UTF-8");

		int current = manager.getStep();
		int progress = manager.getProgress();
		long time = manager.getTimeInSeconds();
		String str = "{";
		
		str += "current:"+current+",";
		str += "progress:"+progress+",";
		str += "time:" + time+"}";
		PrintWriter out = response.getWriter();

		out.print(str);		
		out.flush();	
		out.close();
	}
}
