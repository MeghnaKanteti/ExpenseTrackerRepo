package com.jsp.et.service;

import java.time.LocalDate;
import java.util.List;

import com.jsp.et.dto.ExpenseDTO;

public interface ExpenseService 
{
	int addExpense(ExpenseDTO dto, int userId);
	
	List<ExpenseDTO> viewExpense(int userId);
	
	ExpenseDTO findByExpenseId(int id);
	
	ExpenseDTO updateExpense(ExpenseDTO dto, int expenseId);
	
	int deleteExpense(int expenseId);
	
	List<ExpenseDTO> filterBasedOnCategoryAmount(ExpenseDTO dto, int userId);
	
	List<ExpenseDTO> filterBasedOnDate(ExpenseDTO dto, int userId);
	
	List<ExpenseDTO> filterBasedOnAmount(String range, int userId);
	
	List<ExpenseDTO> filterBasedOnCategory(ExpenseDTO dto, int userId);
	
	List<ExpenseDTO> filterExpenseBasedOnDate(LocalDate start, LocalDate end, int userId);

	List<ExpenseDTO> filterBasedOnCategoryDateAmount(ExpenseDTO dto, int userId);

	

	
	
	
	
}
