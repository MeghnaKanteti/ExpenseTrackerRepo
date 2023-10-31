package com.jsp.et.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsp.et.dto.ExpenseDTO;
import com.jsp.et.service.ExpenseService;

@Controller
@RequestMapping("/expense")
public class AppController 
{
	@RequestMapping("/index")
	public String index()
	{
		return "Index";
	}
	
	@RequestMapping("/home")
	public String home()
	{
		return "Home";
	}
	
	@RequestMapping("/viewExpense" )
	public String viewExpense(@ModelAttribute("listOfExpense") List<ExpenseDTO> expenses)
	{
		return "viewExpense";
	}
	
//	@RequestMapping("/login")
//	public String login()
//	{
//		return "Login";
//	}
	
	@RequestMapping("/login")
	public String login(@ModelAttribute("msg") String message)
	{
		return "Login";
	}
	
//	@RequestMapping("/register")
//	public String register()
//	{
//		return "Register";
//	}

	@RequestMapping("/register")
	public String register(@ModelAttribute("msg") String message)
	{
		return "Register";
	}	
	
//	@RequestMapping("/addExpenses")
//	public String addExpenses()
//	{
//		return "addExpenses";
//	}
	
	@RequestMapping("/addExpenses")
	public String addExpenses(@ModelAttribute("error") String message)
	{
		return "addExpenses";
	}
	
	@Autowired
	private ExpenseService service;
	
	//to display UpdateExpense page with expense details by using expenseId
	@RequestMapping("/updateExpense/{id}")
	public String updateExpense(@PathVariable("id") int id, Model m)
	{
		ExpenseDTO dto = service.findByExpenseId(id);
		m.addAttribute("expense", dto);
		return "updateExpense";
	}
	
	@RequestMapping("/filterExpense")
	public String filterExpense()
	{
		return "filterExpense";
	}
	
	@RequestMapping("/totalExpense")
	public String totalExpense()
	{
		return "totalExpense";
	}	
}