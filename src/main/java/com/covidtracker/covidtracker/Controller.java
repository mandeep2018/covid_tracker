package com.covidtracker.covidtracker;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.covidtracker.covidtracker.model.LocationStats;
import com.covidtracker.covidtracker.service.DataFetcherService;

@org.springframework.stereotype.Controller
public class Controller {

	@Autowired
	private DataFetcherService dataFetcherService;
	
	@RequestMapping(path="/", method=RequestMethod.GET )
	public String homePage(Model model) {
		List<LocationStats> allStats = dataFetcherService.getAllStats();
		int totalCases = allStats.stream().mapToInt(LocationStats::getActiveCases).sum();
		
		model.addAttribute("allStats", allStats);
		model.addAttribute("totalReportedCases", totalCases);
		return "home";
	}
	
}
