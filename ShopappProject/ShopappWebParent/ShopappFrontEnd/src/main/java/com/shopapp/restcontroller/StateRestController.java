package com.shopapp.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.common.dto.StateDto;
import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.State;
import com.shopapp.repository.StateRepository;

@RestController
public class StateRestController {

	@Autowired
	private StateRepository stateRepository;
	
	@GetMapping("/settings/list_states_by_country/{id}")
	public List<StateDto> listByCountry(@PathVariable("id") Integer countryId){
		List<State> states = stateRepository.findByCountryOrderByNameAsc(new Country(countryId));
		List<StateDto> dtos = new ArrayList<>();
		
		for(State state: states) {
			StateDto dto = new StateDto(state.getId(), state.getName());
			dtos.add(dto);
		}
		
		return dtos;
	}
}
