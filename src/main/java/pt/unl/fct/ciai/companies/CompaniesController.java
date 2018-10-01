package pt.unl.fct.ciai.companies;



import java.util.Collections;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/companies")
public class CompaniesController { //implements CompaniesApi {

	@Autowired
	private CompaniesRepository companies;

	@GetMapping
	public Iterable<Company> getCompanies(@Valid @RequestParam(value = "search", required = false) String search) {
		return search == null ? companies.findAll() : companies.search(search);
	}

	@PostMapping
	public void addCompany(@Valid @RequestBody Company company) {
		companies.save(company);
	}

	@GetMapping(value = "/{id}")
	public Company getCompany(@PathVariable("id") Long id, @Valid @RequestParam(value = "search", required = false) String search) {
		return companies.findById(id).get();
	}

	@PutMapping(value = "/{id}")
	public void updateCompany(@PathVariable("id") Long id, @Valid @RequestBody Company company) {
		if (id.equals(company.getId())) {
			companies.findById(id).ifPresent(c -> companies.save(company));
		}
	}

	@DeleteMapping(value = "/{id}")
	public void deleteCompany(@PathVariable("id") Long id) {
		companies.deleteById(id);
	}

	@GetMapping(value = "/{id}/contacts")
	public Iterable<Contact> getCompanyContacts(@PathVariable("id") Long id, @Valid @RequestParam(value = "search", required = false) String search) {
		//companies.findAll().forEach(System.out::println);
		return companies.findById(id).map(c -> c.getContacts()).orElse(Collections.emptyList());
	}

	@PostMapping(value = "/{id}/contacts")
	public void addCompanyContact(@PathVariable("id") Long id, @Valid @RequestBody Contact contact) {
		companies.findById(id).ifPresent(c -> c.addContact(contact));
		//companies.findAll().forEach(System.out::println);
	}

}
