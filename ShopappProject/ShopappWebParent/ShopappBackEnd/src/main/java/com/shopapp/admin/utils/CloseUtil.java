package com.shopapp.admin.utils;

public class CloseUtil {
	
	public static void close(AutoCloseable ...autoCloseables) {
		for (AutoCloseable closeable: autoCloseables) {
			if(closeable != null) {
				try {
					closeable.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
