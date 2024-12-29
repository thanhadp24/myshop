package com.shopapp.admin.country;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopapp.common.entity.Country;

@SpringBootTest
@AutoConfigureMockMvc	
public class TestCoutryController {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;
	
	@Test
	@WithMockUser(username = "thanhadp2402@gmail.com", password = "111", roles = "ADMIN")
	public void test() throws Exception {
		String url = "/countries/list";
		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String json= result.getResponse().getContentAsString();
		System.out.println(json);
		
		Country[] coutries = mapper.readValue(json, Country[].class);
		System.out.println(Arrays.toString(coutries));
	}
	
	@Test
	@WithMockUser(username = "thanhadp2402@gmail.com", password = "111", roles = "ADMIN")
	public void save() throws Exception {
		String url = "/countries/save";
		Country country = new Country("America", "USA");
		MvcResult result = mockMvc.perform(post(url)
					.contentType("application/json")
					.content(mapper.writeValueAsString(country))
					.with(csrf()))
				.andExpect(status().isOk())
				.andReturn();
				
			
		String contentAsString = result.getResponse().getContentAsString();
		System.out.println(contentAsString);
	}
	
	@Test
	@WithMockUser(username = "thanhadp2402@gmail.com", password = "111", roles = "ADMIN")
	public void delete() throws Exception {
		String url = "/countries/delete/1";
		MvcResult result = mockMvc.perform(get(url))
				.andExpect(status().isOk())
				.andReturn();
				
			
		String contentAsString = result.getResponse().getContentAsString();
		System.out.println(contentAsString);
	}
}
