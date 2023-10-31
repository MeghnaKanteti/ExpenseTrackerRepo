package com.jsp.et.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsp.et.dto.ExpenseDTO;
import com.jsp.et.entity.Expense;
import com.jsp.et.entity.ExpenseCategory;
import com.jsp.et.entity.User;
import com.jsp.et.repository.ExpenseCategoryRepository;
import com.jsp.et.repository.ExpenseRepository;
import com.jsp.et.repository.UserRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService 
{
	@Autowired
	private ExpenseRepository  expenseRepository;
	
	@Autowired
	private ExpenseCategoryRepository categoryRepository;
	@Autowired
	private UserRepository userRepository;
	/*
	 * Expense Table contains two foreign keys - categoryId, userId
	 * To insert value in foreign key columns, firstly programmer have to verify user & category
	 * then need to retrieve it by using name and id
	 * after then insert into expense table
	 * 
	 * 
	 */
	@Override
	public int addExpense(ExpenseDTO dto, int userId) 
	{
		//Verification and retrieval of category by using its name
		Optional<ExpenseCategory> category = categoryRepository.findByCategory(dto.getCategory());
		
		//Verification and retrieval of user by using its id
		Optional<User> user = userRepository.findById(userId);
		
		//if both are valid then insert record in table
		if (category.isPresent() && user.isPresent())
		{
			//to create the object of entity class...
			Expense expense = new Expense();
			//convert String of DTO to local date of entity
			expense.setDate(LocalDate.parse(dto.getDate()));
			
			//to transfer data from dto to expense class..dto source & expense is target
			BeanUtils.copyProperties(dto, expense);
			//to take the category and set to the entity object...
			expense.setExpenseCategory(category.get());
			expense.setUser(user.get());
		
		
			return expenseRepository.save(expense).getExpenseId();
	}
		return 0;
	}
		/*
		 * To get all the expenses based on userId because every user have different
		 * expenses, Retrieve Expenses by using userId.
		 * 
		 */

	@Override
	public List<ExpenseDTO> viewExpense(int userId) {
		//1.Find user details in User table based on id then retrieve object
		User user = userRepository.findById(userId).get();
		
		//created user-defined method is expense repository to access expenses by using
		// user-details
		
		/*
		 * findByUser returns list of expense entity object., so to copy data from
		 * expense entity list to expenseDTO list make use of stream api or foreach 
		 * loop
		 */
		return expenseRepository.findByUser(user).stream().map(t -> {
			ExpenseDTO dto = new ExpenseDTO();
			BeanUtils.copyProperties(t, dto);
			//to store category from Expense Category object to ExpenseDTO
			/*
			 * t.getExpenseCategory() gives object at ExpenseCategory entity class 
			 */
			dto.setCategory(t.getExpenseCategory().getCategory());
			
			dto.setDate(t.getDate().toString());			
			//convert Local Date of entity class into String of DtO
			dto.setCategory(t.getExpenseCategory().getCategory());
			
			
			return dto;
		}).collect(Collectors.toList());

	}
	
	/*
	 * To get expenses based on its id
	 */
	@Override
	public ExpenseDTO findByExpenseId(int id) {
		Optional<Expense> expensedb = expenseRepository.findById(id);
		if(expensedb.isPresent())
		{
			ExpenseDTO dto = new ExpenseDTO();
			BeanUtils.copyProperties(expensedb.get(), dto);
			dto.setCategory(expensedb.get().getExpenseCategory().getCategory());
			dto.setDate(expensedb.get().getDate().toString());
			return dto;
	}
		return null;
	}

	/*
	 * To update expense details
	 */
	
	@Override
	public ExpenseDTO updateExpense(ExpenseDTO dto, int expenseId) {
		//1. Find expense by using its id
		Expense expense = expenseRepository.findById(expenseId).get();
		//2. Verification
		if(expense != null)
		{
			//3. update data in retrieved object of Expense
			expense.setAmount(dto.getAmount());
			//expense.setDate(dto.getDate());
			expense.setDescription(dto.getDescription());
			//convert String to LocalDate
			expense.setDate(LocalDate.parse(dto.getDate()));
			expense.setDescription(dto.getDescription());
			
			//4.Find category from category table based on its name then update in expenses
			ExpenseCategory category = categoryRepository.findByCategory(dto.getCategory()).get();
			expense.setExpenseCategory(category);
			
			//5. update expense by using save method
			ExpenseDTO updated = new ExpenseDTO();
			BeanUtils.copyProperties(expenseRepository.save(expense), updated);
			return updated;
		}
			return null;
	}
	
		/*
		 * To delete expenses based on id
		 * 
		 */
	@Override
	public int deleteExpense(int expenseId) {
		
		//1. find expense based on id
		Optional<Expense> expenseDB = expenseRepository.findById(expenseId);
		//2. Verification
		if(expenseDB.isPresent())
		{
			//3. deletion
			expenseRepository.delete(expenseDB.get());
			return 1;
		}
		return 0;
		
	}

	/*
	 *It will retrieve data from db based on matching category amount and date make use of
	 * filter method from stream api... 
	 * 
	 */
		@Override
		public List<ExpenseDTO> filterBasedOnCategoryAmount(ExpenseDTO dto, int userId) {
			/*
			 * call viewExpense method because it contains the logic. to get all expenses 
			 * of respective user, so programmer have to just filter expenses of user as 
			 * per requirement
			 * 
			 */
			
			
			return viewExpense(userId).stream()
					.filter(t -> t.getDate().equals(dto.getDate()) && t.getAmount() == dto.getAmount()
					&& t.getCategory().equals(dto.getCategory()))
					.toList();
		}
		
		/*
		 * It will retrieve data from database based on date make use of filter
		 * method from stream api
		 * 
		 */
		
		
		
		@Override
		public List<ExpenseDTO> filterBasedOnDate(ExpenseDTO dto, int userId) 
		{	
			return viewExpense(userId).stream()
					.filter(t -> t.getDate().equals(dto.getDate())).toList();
		}
		

		/*
		 * Take range in format of String "100-200"
		 */

		@Override
		public List<ExpenseDTO> filterBasedOnAmount(String range, int userId) 
		{
			String[] arr = range.split("-");
		
			return viewExpense(userId).stream().filter(t ->{
				int start = Integer.parseInt(arr[0]);
				int end = Integer.parseInt(arr[1]);
				return start <= t.getAmount() && end >= t.getAmount();
			}).collect(Collectors.toList());
		}
		

		/*
		 * it will retrieve the data from db based on userId
		 * 
		 */
	
		@Override
		public List<ExpenseDTO> filterBasedOnCategory(ExpenseDTO dto, int userId) {
			
			return null;
		}

		
		/*
		 * it will find total of expenses between given date for respective user
		 */
		@Override
		public List<ExpenseDTO> filterExpenseBasedOnDate(LocalDate start, LocalDate end, int userId) 
		{
			/*
			 * 1. get all expenses for respective user
			 * 2. make use of stream api to filter expenses based on start and end date
			 */
			
			return viewExpense(userId).stream().filter(t -> {
				LocalDate date = LocalDate.parse(t.getDate());
				return !date.isBefore(start) && !date.isAfter(end);
			}).collect(Collectors.toList());		
				
			}
		

		@Override
		public List<ExpenseDTO> filterBasedOnCategoryDateAmount(ExpenseDTO dto, int userId) {
			// TODO Auto-generated method stub
			return null;
		}

		
		
		/*
		 * it will find total of expenses between given data for respective user
		 * 
		 */
		
		//it will find total of expenses between given date for respective user
//		@Override
//		public double getTotalExpenseBasedOnDate(String start, String end, int userId) {
//			/* 
//			 * 1.get all expenses for respective user
//			 * 2. make use of stream API to filter expenses based on given start and end date
//			 * 3. from expense DTO object take amount and convert int double list
//			 * 4.perform summation of all elements present in double list 
//			 * 
//			 * 
//			 */
//			return viewExpense(userId).stream()
//					.filter(t -> t.getDate().isBefore(start) && t.getDate().isAfter(end))
//					.mapToDouble(t -> t.getAmount())
//					.sum();
//					
//		}
//
//		@Override
//		public List<ExpenseDTO> filterBasedOnDateCategoryAmount(ExpenseDTO dto, int userId) {
//			// TODO Auto-generated method stub
//			return null;
//		}
		
	
}
