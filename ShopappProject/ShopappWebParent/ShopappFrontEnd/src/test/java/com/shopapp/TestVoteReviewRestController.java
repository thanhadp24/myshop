package com.shopapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopapp.bean.VoteResult;
import com.shopapp.repository.ReviewRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TestVoteReviewRestController {

	@Autowired private MockMvc mockMvc;
	@Autowired private ReviewRepository repository;
	@Autowired private ObjectMapper objectMapper;
	
	@Test
	public void voteWithoutLogin() throws Exception {
		Integer reviewId = 9;
		String voteType = "up";
		String url = "/vote_review/" + reviewId + "/" + voteType;
		MvcResult mvcResult = mockMvc.perform(post(url).with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String json = mvcResult.getResponse().getContentAsString();
		VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);
		
		assertThat(voteResult.isSuccessful());
	}
	
	@Test
	@WithMockUser(username = "thanhadp2402@gmail.com", password = "111111111")
	public void voteWithNoneReview() throws Exception {
		Integer reviewId = 100;
		String voteType = "up";
		String url = "/vote_review/" + reviewId + "/" + voteType;
		MvcResult mvcResult = mockMvc.perform(post(url).with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String json = mvcResult.getResponse().getContentAsString();
		VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);
		
		assertThat(voteResult.isSuccessful());
	}
	
	@Test
	@WithMockUser(username = "thanhadp2402@gmail.com", password = "111111111")
	public void voteReview() throws Exception {
		Integer reviewId = 8;
		String voteType = "up";
		String url = "/vote_review/" + reviewId + "/" + voteType;
		MvcResult mvcResult = mockMvc.perform(post(url).with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String json = mvcResult.getResponse().getContentAsString();
		VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);
		
		assertThat(voteResult.isSuccessful());
	}
	
	@Test
	@WithMockUser(username = "thanhadp2402@gmail.com", password = "111111111")
	public void unVoteReview() throws Exception {
		Integer reviewId = 8;
		String voteType = "up";
		String url = "/vote_review/" + reviewId + "/" + voteType;
		MvcResult mvcResult = mockMvc.perform(post(url).with(csrf()))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String json = mvcResult.getResponse().getContentAsString();
		VoteResult voteResult = objectMapper.readValue(json, VoteResult.class);
		
		assertThat(voteResult.isSuccessful());
	}
}
