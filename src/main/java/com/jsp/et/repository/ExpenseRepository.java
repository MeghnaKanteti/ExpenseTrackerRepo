package com.jsp.et.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jsp.et.entity.Expense;
import com.jsp.et.entity.User;

public interface ExpenseRepository extends  JpaRepository<Expense, Integer>
{
	//to find list of expenses based on user details...
	List<Expense> findByUser(User user);
	
	//to find record based on amount...
	List<Expense> findByAmount(double amount);
	
}
