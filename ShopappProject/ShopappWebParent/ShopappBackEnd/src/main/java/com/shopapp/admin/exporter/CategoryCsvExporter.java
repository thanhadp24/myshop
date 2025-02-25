package com.shopapp.admin.exporter;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopapp.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryCsvExporter extends AbstractExporter{
	
	public void exportCsvFile(List<Category> categories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", "Categories_", ".csv");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"Category ID", "Category Name"};
		String[] csvMapping = {"id", "name"};
		csvWriter.writeHeader(csvHeader);
		
		for (Category category:categories) {
			category.setName(category.getName().replaceAll("--", ""));
			csvWriter.write(category, csvMapping);
		}
		
		
		csvWriter.close();
	}
}
