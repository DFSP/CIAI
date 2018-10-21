package pt.unl.fct.ciai.company;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.unl.fct.ciai.employee.Employee;
import pt.unl.fct.ciai.exceptions.BadRequestException;
import pt.unl.fct.ciai.exceptions.NotFoundException;

@RestController
@RequestMapping("/companies")
public class CompanyController { //implements CompaniesApi {
	
	private final CompaniesRepository companies;

	public CompanyController(CompaniesRepository companies) {
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
		return findCompanyOrThrowException(id);
	}

	@PutMapping(value = "/{id}")
	public void updateCompany(@PathVariable("id") Long id, @RequestBody Company company) {
		if (id.equals(company.getId())) {
			findCompanyOrThrowException(id);
			companies.save(company);
		}
		else {
			throw new BadRequestException(String.format("Path id %d does not match company id %d", id, company.getId()));
		}
	}

	@DeleteMapping(value = "/{id}")
	public void deleteCompany(@PathVariable("id") Long id) {
		findCompanyOrThrowException(id);
		companies.deleteById(id);
	}

	@GetMapping(value = "/{id}/employees")
	public Iterable<Employee> getCompanyEmployees(@PathVariable("id") Long id, @RequestParam(value = "search", required = false) String search) {
		return search == null ? findCompanyOrThrowException(id).getEmployees() : companies.searchEmployees(search);
	}

	@PostMapping(value = "/{id}/employees")
	public void addCompanyEmployee(@PathVariable("id") Long id, @RequestBody Employee employee) {
		Company company = findCompanyOrThrowException(id);
		employee.setCompany(company);
		company.addEmployee(employee);
		companies.save(company);
	}
	
	private Company findCompanyOrThrowException(Long id) {
		return companies.findById(id).orElseThrow(() -> new NotFoundException(String.format("Company with id %d not found", id)));
	}

}
