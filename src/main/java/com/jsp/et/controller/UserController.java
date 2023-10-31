package com.jsp.et.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jsp.et.dto.ExpenseDTO;
import com.jsp.et.dto.ImageDTO;
import com.jsp.et.dto.TotalDTO;
import com.jsp.et.dto.UserDTO;
import com.jsp.et.service.ExpenseService;
import com.jsp.et.service.UserService;

@Controller
public class UserController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private ExpenseService expenseService;
	
	//every method is an API
	
	@PostMapping("/registration")
	public String registration(@ModelAttribute UserDTO userDTO, Model m, RedirectAttributes attributes)
	{
		int id= userService.registration(userDTO);
		if(id !=0)
		{
			//display login page
			//redirect request to login method
			m.addAttribute("msg", "Registration Succesful...");
			attributes.addFlashAttribute("msg","Registration Successful..");
			return "redirect:/expense/login";
		}
		//display registration page
		m.addAttribute("msg", "Please enter Vallid Details...");
		attributes.addFlashAttribute("msg","Please enter Vallid Details..");
		return "redirect:/expense/register";
	}
	
	
	@PostMapping("/loginoperation")
	public String login(@ModelAttribute UserDTO userDTO, Model m, RedirectAttributes attributes , HttpServletRequest request)
	{
		UserDTO dto = userService.login(userDTO);
		//System.out.println(dto.getImage().getId);
		if(dto != null)
		{
			//to store users data in session object
			request.getSession().setAttribute("user" ,dto);
			if(dto.getImage() != null)
			{
				/*
				 * store image in session object but in the form of String
				 * By using Base64 class present in java.util package -programmer can Encode
				 * byte data to String */
				
				request.getSession().setAttribute("image",
						Base64.getMimeEncoder().encodeToString(dto.getImage().getData()));
			}
			return "redirect:/expense/home";
		}
		attributes.addFlashAttribute("msg","Please enter Valid Id and Password...");
		return "redirect:/expense/login";
	}
	
	@PostMapping("/addexpenses/{id}")
	public String addExpense(@ModelAttribute ExpenseDTO dto, @PathVariable("id") int userId, RedirectAttributes attributes)
	{
		int id= expenseService.addExpense(dto, userId);
		if(id > 0)
		{
			return "redirect:/viewexpense/" +userId;
			//redirect request to viewExpense method from UserController and concat user
			//return "redirect:/expense/viewExpense";
		}
		attributes.addFlashAttribute("error", "Please enter valid details..");
		return "redirect:/expense/addExpense";
	}
	

	@GetMapping("/viewexpense/{id}")
	public String viewExpenses(@PathVariable("id") int userId, RedirectAttributes attributes)
	{
		System.out.println(userId);
		List<ExpenseDTO> expenses = expenseService.viewExpense(userId);
		if(!expenses.isEmpty())
		{
			attributes.addFlashAttribute("listOfExpense", expenses);
			return "redirect:/expense/viewExpense";
		}
		return "redirect:/expense/home";
	}
	

	
	@PostMapping("/updateexpense/{eid}")
	public String updateExpense(@ModelAttribute ExpenseDTO dto, @PathVariable("eid") int expenseId,
			HttpServletRequest request)
	{
		ExpenseDTO expense = expenseService.updateExpense(dto, expenseId);
		if(expense != null)
		{
			UserDTO userDTO= (UserDTO)request.getSession().getAttribute("user");
			return "redirect:/viewexpense/"+userDTO.getUserId();
		}
		return "redirect:/expense/home";
	}
	
	
	
	@GetMapping("/deleteExpense/{eid}")
	public String deleteExpense(@PathVariable("eid") int expenseId, HttpServletRequest request)
	{
		System.out.println(expenseId);
		int  id = expenseService.deleteExpense(expenseId);
		if( id != 0)
		{
			//Make use of Session object to get userId
			UserDTO dto = (UserDTO)request.getSession().getAttribute("user");
			//redirect to viewexpense method to display expenses by using userId;
			return "redirect:/viewexpense/"+dto.getUserId();
		}
		return "redirect:/expense/home";
	}
	
	@GetMapping("/expense/{eid}")
	public ResponseEntity<ExpenseDTO> findByExpenseId(@PathVariable("id")int expenseId)
	{
		ExpenseDTO dto=expenseService.findByExpenseId(expenseId);
		if(dto != null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	@GetMapping("/filter/{userId}")
	public String filterExpenseByCategoryDateAmount(@ModelAttribute ExpenseDTO dto,  
			@PathVariable("userId") int userId, RedirectAttributes attributes)
	{
		if(!dto.getRange().equalsIgnoreCase("0")) {
			List<ExpenseDTO> filterByAmount = expenseService.filterBasedOnAmount(dto.getRange(), userId);
			attributes.addFlashAttribute("listOfExpense", filterByAmount);
			return "redirect:/expense/viewExpense";
		}
		
		else if(dto.getCategory() != "")
		{
			List<ExpenseDTO> filterByCategory = expenseService.filterBasedOnCategory(dto, userId);
			attributes.addFlashAttribute("listOfExpense", filterByCategory);
			return "redirect:/expense/viewExpense";
		}
		else if(dto.getDate() != "")
		{
			List<ExpenseDTO> filterByDate= expenseService.filterBasedOnDate(dto, userId);
			attributes.addFlashAttribute("listOfExpense", filterByDate);
			return "redirect:/expense/viewExpense";
		}
		return "redirect:/expense/home";
	}
	
	
	
	@GetMapping("/filter/expense2/{userId}")
	public ResponseEntity<List<ExpenseDTO>> filterExpenseByDate(@RequestBody ExpenseDTO dto,  @PathVariable("userId") int userId)
	{
		List<ExpenseDTO> expense = expenseService.filterBasedOnDate(dto, userId);
		if(expense != null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(expense);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	@GetMapping("/filter/expense3/(userId)/{range}")
	public ResponseEntity<List<ExpenseDTO>> filterExpenseByAmount(@PathVariable("range") String range,  @PathVariable("userId") int userId)
	{
		List<ExpenseDTO> expense = expenseService.filterBasedOnAmount(range, userId);
		if(expense != null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(expense);
		}
	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	@GetMapping("/filter/expense4/{userId}")
	public ResponseEntity<List<ExpenseDTO>> filterExpenseByCategory(@RequestBody ExpenseDTO dto,  @PathVariable("userId") int userId)
	{
		List<ExpenseDTO> expense = expenseService.filterBasedOnCategory(dto, userId);
		if(expense != null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(expense);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	
	@GetMapping("/total/{userId}")
	public String getTotalOfExpense(@ModelAttribute TotalDTO total, @PathVariable("userId") int userId, Model m) {
		List<ExpenseDTO> dto = expenseService.filterExpenseBasedOnDate(LocalDate.parse(total.getStart()), 
				LocalDate.parse(total.getEnd()), userId);
		m.addAttribute("listOfExpense", dto);
		m.addAttribute("total", dto.stream().mapToDouble(t -> t.getAmount()).sum());
		return "viewExpense";
	}
	
	
	@GetMapping("/getuser/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable("id") int userId)
	{
		UserDTO user= userService.findByUserId(userId);
		if(user != null)
		{
			return ResponseEntity.status(HttpStatus.OK).body(user);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	@DeleteMapping("/deleteuser/{id}")
	public ResponseEntity<Integer> deleteUser(@PathVariable("id") int id)
	{
		int status = userService.deleteUserProfile(id);
		if(status != 0)
		{
			return ResponseEntity.status(HttpStatus.OK).body(status);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	
	@PostMapping("/updateuser/{id}")
	public String updateUser(@PathVariable("id") int id, @ModelAttribute UserDTO dto,
			@RequestParam("imageFile") MultipartFile file, HttpServletRequest request)
	{
		try
		{
			/*
			 * retrieve UserDTO object from session object, store at the time of Logic
			 */
			UserDTO fromSession = (UserDTO)request.getSession().getAttribute("user");
			//if user already have uploaded profile photo then update the photo
			if(fromSession.getImage() != null)
			{
				//updation logic
				fromSession.getImage().setData(file.getBytes());
				dto.setImage(fromSession.getImage());
				
				//store same image in session object
				request.getSession().setAttribute("image", 
						Base64.getMimeEncoder().encodeToString(dto.getImage().getData()));
			}
			else
			{
				// if user uploading profile photo first time 
				ImageDTO imageDto = new ImageDTO();
				imageDto.setData(file.getBytes());
				dto.setImage(imageDto);
			}
		userService.updateUserProfile(id, dto);
		return "redirect:/expense/login";
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return "redirect/expense/home";
	}
}
