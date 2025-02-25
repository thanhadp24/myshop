package com.shopapp;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopapp.bean.OrderReturnRequest;

import jakarta.inject.Inject;

@SpringBootTest
@AutoConfigureMockMvc
public class TestOrder {

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper mapper;
	
	@Test
	@WithMockUser(username = "add", password = "dfa")
	public void testSendOrderReturnRequestFail() throws JsonProcessingException, Exception {
		Integer orderId = 11;
		OrderReturnRequest request = new OrderReturnRequest(orderId, "Update address", "Thank you");
		String url = "/orders/return";
		
		mockMvc.perform(post(url)
						.with(csrf())
						.contentType("application/json")
						.content(mapper.writeValueAsString(request)))
				.andExpect(status().isNotFound())
				.andDo(print());
	}
	
	@Test
	@WithUserDetails(value = "thanhadp2402@gmail.com")
	public void testSendOrderReturnRequestSuccess() throws JsonProcessingException, Exception {
		Integer orderId = 15;
		OrderReturnRequest request = new OrderReturnRequest(orderId, "Update address", "Thank you");
		String url = "/orders/return";
		
		mockMvc.perform(post(url)
						.with(csrf())
						.contentType("application/json")
						.content(mapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andDo(print());
	}
}
