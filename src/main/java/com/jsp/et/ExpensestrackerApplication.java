package com.jsp.et;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jsp.et.entity.ExpenseCategory;
import com.jsp.et.repository.ExpenseCategoryRepository;

@SpringBootApplication
public class ExpensestrackerApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ExpensestrackerApplication.class, args);
	}
	
	@Autowired
	private ExpenseCategoryRepository repository;

	@Override
	public void run(String... args) throws Exception {
		 //to execute any code at the starting of application
//		List<ExpenseCategory> category = Arrays.asList(
//				new ExpenseCategory("utilities"),
//				new ExpenseCategory("Transportation"),
//				new ExpenseCategory("Groceries"),
//				new ExpenseCategory("Dining Out"),
//				new ExpenseCategory("Health Care"),
//				new ExpenseCategory("Entertainment"),
//				new ExpenseCategory("Education"),
//				new ExpenseCategory("Personal Care"),
//				new ExpenseCategory("Clothing"),
//				new ExpenseCategory("Home Maintenance"),
//				new ExpenseCategory("Gifts And Donation"),
//				new ExpenseCategory("Savings & Investments"),
//				new ExpenseCategory("Tax"),
//				new ExpenseCategory("Others")
//				);
//		//store all list elements in the database
//		repository.saveAll(category);
		
	}

}



