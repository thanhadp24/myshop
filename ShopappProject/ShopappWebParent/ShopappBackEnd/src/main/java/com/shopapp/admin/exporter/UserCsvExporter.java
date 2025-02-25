package com.shopapp.admin.exporter;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopapp.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter extends AbstractExporter{

	public void exportCsvFile(List<User> users, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", "Users_", ".csv");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"User Id", "Email", "First Name", "Last Name", "Roles", "Enabled"};
		String[] csvMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
		csvWriter.writeHeader(csvHeader);
		
		for (User user:users) {
			csvWriter.write(user, csvMapping);
		}
		
		
		csvWriter.close();
	}
}
