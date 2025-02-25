package com.shopapp.admin.helper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.shopapp.admin.repository.SearchRepository;

public class PagingAndSortingHelper {

	private ModelAndViewContainer model;
	private String listName;
	private String sortDir;
	private String sortField;
	private String keyword;
	
	public PagingAndSortingHelper(ModelAndViewContainer model, String listName,
			String sortDir, String sortField, String keyword) {
		this.model = model;
		this.listName = listName;
		this.sortDir = sortDir;
		this.sortField = sortField;
		this.keyword = keyword;
	}
	
	public void updateModel(int pageNum, Page<?> page) {

		long totalItems = page.getTotalElements();
		int pageSize = page.getSize();
		
		long startCount = (pageNum - 1) * pageSize + 1;
		long endCount = startCount + pageSize - 1;
		
		if(endCount > totalItems) {
			endCount = totalItems;
		}
		
		
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("currentPage", pageNum);
		
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", totalItems);
		
		model.addAttribute(listName, page.getContent());
		
	}
	
	public void listEntities(int pageNum, int pageSize, SearchRepository<?, Integer> repo) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending(): sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
		Page<?> page = null;
		
		if (keyword != null) {
			page = repo.findAll(keyword, pageable);
		}else {
			page = repo.findAll(pageable);
		}
		
		updateModel(pageNum, page);
	}
	
	public Pageable createPagable(int pageSize, int pageNum) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		return PageRequest.of(pageNum - 1, pageSize, sort);
	}

	public String getKeyword() {
		return keyword;
	}

	public String getSortDir() {
		return sortDir;
	}

	public String getSortField() {
		return sortField;
	}
	
	
}
