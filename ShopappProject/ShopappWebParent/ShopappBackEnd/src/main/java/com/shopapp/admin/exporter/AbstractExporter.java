package com.shopapp.admin.exporter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractExporter {
	
	void setResponseHeader(HttpServletResponse response,String contentType, 
			String prefix, String extension) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String timestamp = format.format(new Date());
		String fileName = prefix + timestamp + extension;
		
		response.setContentType(contentType);

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; fileName=" + fileName;
		response.setHeader(headerKey, headerValue);
		
	}
}
