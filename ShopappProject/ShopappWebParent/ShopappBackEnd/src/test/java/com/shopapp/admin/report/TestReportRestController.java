package com.shopapp.admin.report;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TestReportRestController {

	@Autowired private MockMvc mockMvc;
	
	@Test
	@WithMockUser(username = "user1", password = "111", authorities = {"Admin"})
	public void testGetReport7Days() throws Exception {
		String requestURL = "/reports/sales_by_date/last_7_days";
		
		mockMvc.perform(get(requestURL)).andExpect(status().isOk()).andDo(print());
	}
	
	
	@Test
	@WithMockUser(username = "user1", password = "111", authorities = {"Admin"})
	public void testGetReport6Months() throws Exception {
		String requestURL = "/reports/sales_by_date/last_6_months";
		
		mockMvc.perform(get(requestURL)).andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	@WithMockUser(username = "user1", password = "111", authorities = {"Admin"})
	public void testGetReportByDateRange() throws Exception {
		String startDate = "2025-02-01";
		String endDate = "2025-02-27";
		String requestURL = "/reports/sales_by_date/" + startDate + "/" + endDate;
		
		mockMvc.perform(get(requestURL)).andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	@WithMockUser(username = "user1", password = "111", authorities = {"Admin"})
	public void testGetReportByCategory() throws Exception {
		String requestURL = "/reports/category/last_7_days";
		
		mockMvc.perform(get(requestURL)).andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	@WithMockUser(username = "user1", password = "111", authorities = {"Admin"})
	public void testGetReportByProduct() throws Exception {
		String requestURL = "/reports/product/last_7_days";
		
		mockMvc.perform(get(requestURL)).andExpect(status().isOk()).andDo(print());
	}
}
