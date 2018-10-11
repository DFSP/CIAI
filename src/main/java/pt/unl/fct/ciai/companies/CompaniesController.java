package pt.unl.fct.ciai.companies;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.unl.fct.ciai.contacts.Contact;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;

@RestController
@RequestMapping("/companies")
public class CompaniesController { //implements CompaniesApi {

	private final CompaniesRepository companies;

	public CompaniesController(CompaniesRepository companies) {
		this.companies = companies;
	}
	
	@GetMapping
	public Iterable<Company> getCompanies(@RequestParam(value = "search", required = false) String search) {
		return search == null ? companies.findAll() : companies.searchCompanies(search);
	}

	@PostMapping
	public void addCompany(@RequestBody Company company) {
		companies.save(company);
	}

	@GetMapping(value = "/{id}")
	public Company getCompany(@PathVariable("id") Long id) {
		return companies.findById(id).orElseThrow(NotFoundException::new);
	}

	@PutMapping(value = "/{id}")
	public void updateCompany(@PathVariable("id") Long id, @RequestBody Company company) {
		if (id.equals(company.getId())) {
			companies.findById(id).orElseThrow(NotFoundException::new);
			companies.save(company);
		}
		else {
			throw new BadRequestException();
		}
	}

	@DeleteMapping(value = "/{id}")
	public void deleteCompany(@PathVariable("id") Long id) {
		companies.findById(id).orElseThrow(NotFoundException::new);
		companies.deleteById(id);
	}

	@GetMapping(value = "/{id}/contacts")
	public Iterable<Contact> getCompanyContacts(@PathVariable("id") Long id, @RequestParam(value = "search", required = false) String search) {
		Iterable<Contact> contacts;
		Company company = companies.findById(id).orElseThrow(NotFoundException::new);
		if (search == null) {
			contacts = company.getContacts();
		} else {
			contacts = companies.searchContacts(search);
		}
		return contacts;
	}

	@PostMapping(value = "/{id}/contacts")
	public void addCompanyContact(@PathVariable("id") Long id, @RequestBody Contact contact) {
		Company c = companies.findById(id).orElseThrow(NotFoundException::new);
		c.addContact(contact);
		companies.save(c);
	}

}
