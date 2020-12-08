package com.sherpaerp.library.borrowersmicroservice.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sherpaerp.library.borrowersmicroservice.entity.Borrowers;
import com.sherpaerp.library.borrowersmicroservice.entity.BorrowersRepository;


@RestController
@RequestMapping(path = "/borrowers")
public class BorrowersController {
	@Autowired // This means to get the bean called BorrowersRepository
				// Which is auto-generated by Spring, we will use it to handle the data
	private BorrowersRepository borrowersRepository;

	//@PostMapping(path = "/post")
	@GetMapping(path = "/add")
	public @ResponseBody String addNewBorrower(@RequestParam String firstname, @RequestParam String lastname,
			@RequestParam int borrowingLimit)
	{
		List<Borrowers> response = borrowersRepository.findByFirstnameAndLastname(firstname, lastname);
		if (response.isEmpty()) {
			Borrowers newBorrower = new Borrowers();
			newBorrower.setFirstname(firstname);
			newBorrower.setLastname(lastname);
			newBorrower.setBorrowingLimit(borrowingLimit);
			borrowersRepository.saveAndFlush(newBorrower);
			return "New borrower has just been added to the database !";
		}else {
			return "The name: " + firstname +" "+ lastname + " has already been in the database. So, the addition is canceled." ;
		}
	}

	@GetMapping(path = "/list")
	public @ResponseBody List<Borrowers> getAllBorrowers() {
		List<Borrowers> response = borrowersRepository.findAll();
		System.out.println(response.get(1).getFirstname());
		return response;
	}

	@GetMapping(path = "/find") /* find and get by id or (firstname or/and lastname) */
	public @ResponseBody List<Borrowers> getBorrowerByName(@RequestParam(required = false) String id,
			@RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname) {
		List<Borrowers> response = new ArrayList<>();
		if (id != null) {
			try {
				int intId = Integer.parseInt(id);
				response = borrowersRepository.findById(intId);
			} catch (NumberFormatException e) {
		         System.out.println("Exception thrown of the parameter id :" + e);
		    }
		} else {

			if (firstname != null && lastname == null) {
				response = borrowersRepository.findByFirstname(firstname);
			} else {
				if (firstname == null && lastname != null) {
					response = borrowersRepository.findByLastname(lastname);
				} else {
					if (firstname != null && lastname != null) {
						response = borrowersRepository.findByFirstnameAndLastname(firstname, lastname);
					}
				}
			}
		}
		return response;
	}
}