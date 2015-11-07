package cn.yubo.servlets;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

public class UploadFileServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String filePath;    // 文件存放目录
	private String tempPath;    // 临时文件目录

	// 初始化
	public void init(ServletConfig config) throws ServletException
	{
		System.out.print("*****init******");
		super.init(config);
		// 从配置文件中获得初始化参数
		filePath = config.getInitParameter("filepath");
		tempPath = config.getInitParameter("temppath");

		ServletContext context = getServletContext();

		filePath = context.getRealPath(filePath);
		tempPath = context.getRealPath(tempPath);
		System.out.println("file saving dir ,file temp dir prepared well");
	}

	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		DiskFileItemFactory factory = new DiskFileItemFactory();
//		String path = "/Users/zhoumao/Desktop";//�ϴ��ļ��ı���·��
		factory.setRepository(new File(filePath));
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		JSONObject jobj = new JSONObject();
		try {
			List<FileItem> list = (List<FileItem>)upload.parseRequest(request);
			for(FileItem item : list){
				String name = item.getName();
				String fileName = name.substring(name.lastIndexOf("\\") + 1);
				InputStream is = item.getInputStream();
				File f = new File(filePath,fileName);
				FileOutputStream fos = new FileOutputStream(f);
				int hasRead = 0;
				byte[] buf = new byte[1024];
				while((hasRead = is.read(buf)) > 0){
					fos.write(buf, 0, hasRead);
				}
				fos.close();
				is.close();
			}
			jobj.put("result", 1);
			out.print(jobj.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		out.flush();
		out.close();
	}

}
