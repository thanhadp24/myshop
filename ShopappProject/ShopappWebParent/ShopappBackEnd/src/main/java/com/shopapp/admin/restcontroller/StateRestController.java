package com.shopapp.admin.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopapp.admin.repository.StateRepository;
import com.shopapp.common.dto.StateDto;
import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.State;

@RestController
public class StateRestController {

	@Autowired
	private StateRepository stateRepository;
	
	@GetMapping("/states/countries/{id}")
	public List<StateDto> listByCountry(@PathVariable("id") Integer countryId){
		List<State> states = stateRepository.findByCountryOrderByNameAsc(new Country(countryId));
		List<StateDto> dtos = new ArrayList<>();
		
		for(State state: states) {
			StateDto dto = new StateDto(state.getId(), state.getName());
			dtos.add(dto);
		}
		
		return dtos;
	}
	
	@PostMapping("/states/save")
	public String save(@RequestBody State state) {
		State savedState = stateRepository.save(state);
		return String.valueOf(savedState.getId());
	}
	
	@DeleteMapping("/states/delete/{id}")
	public void delete(@PathVariable("id") Integer id) {
		stateRepository.deleteById(id);
	}
}
