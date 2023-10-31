package com.jsp.et.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.et.entity.ExpenseCategory;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Integer>
{
	//here category is taken bcz in expense category we took category as a variable...
	Optional<ExpenseCategory> findByCategory(String category);
}
