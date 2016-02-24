package com.servlet;

import com.servlet.file.UploadManager;
import com.servlet.file.UploadThreadManager;
import com.ssh.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.websocket.Session;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.ServerEndpoint;

public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String UPLOAD_DIRECTORY = "upload";
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40;
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50;
	// static int ins_num = 1;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// 表单是否符合文件上传规则
		// 判断客户端请求是否为POST，并且enctype属性是否是“multipart/form-data"
		if (!ServletFileUpload.isMultipartContent(request)) {
			PrintWriter writer = response.getWriter();
			writer.println("Error: Form must has enctype=multipart/form-data.");
			writer.flush();
			return;
		}
		// 文件工厂类 判定初始文件内存以及文件超过内存时临时存放的位置
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 以byte为单位设定文件使用多少内存量后，将文件存入临时存储
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		// 设定临时文件的存储路径
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		// ServletFileUpload 处理同意HTML文件中多文件上传的类，继承自FileUpload
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置允许上传文件的最大大小
		upload.setSizeMax(MAX_REQUEST_SIZE);

		String uploadPath = getServletContext().getRealPath("")
				+ File.separator + UPLOAD_DIRECTORY;

		int ins_num = 0;
		String username = "";
		
		// 根据存储路径生成文件夹
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		try {
			List<FileItem> formItems = upload.parseRequest(request);

			if (formItems != null && formItems.size() > 0) { // 取上传的war包
				for (FileItem item : formItems) {
					System.out.println(item);
					if (!item.isFormField()) {
						String fileName = new File(item.getName()).getName();
						String filePath = uploadPath + File.separator
								+ fileName;
						System.out.println("filePath" + filePath);
						File storeFile = new File(filePath);
						// 写入文件
						item.write(storeFile);
						//request.setAttribute("message",
								//"Upload has been done successfully!");
					} else { // 取所需运行的实例数
						// System.out.println("表单的参数名称：" + item.getFieldName()
						// + ",对应的参数值：" + item.getString("UTF-8"));
						if (item.getFieldName().equals("ins_num")) {
							String ins_input = item.getString("UTF-8");
							ins_num = Integer.parseInt(ins_input);
							System.out.println(ins_num);
						}
						else if(item.getFieldName().equals("username")){
							username = item.getString("UTF-8");
							System.out.println(username);
						}
					}
				}
			}
		} catch (Exception ex) {
			request.setAttribute("message",
					"There was an error: " + ex.getMessage());
		}
		
		getServletContext().getRequestDispatcher("/message.jsp").forward(
				request, response);
		
		UploadManager manager = UploadThreadManager.getInstance().getThreadByUserName(username);
		System.out.println("username:"+username + ",insnum:"+ins_num);
		manager.setInsNum(ins_num);
		
		this.startUpLoad(manager);
	}
	
	private void startUpLoad(UploadManager manager){
		if(manager.flag){
			System.out.println("start");
			new Thread(manager).start();
		}
		else{
			startUpLoad(manager);
		}
	}
}
