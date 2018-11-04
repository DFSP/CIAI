package pt.unl.fct.ciai.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;
import pt.unl.fct.ciai.repository.CompaniesRepository;
import pt.unl.fct.ciai.repository.EmployeesRepository;
import pt.unl.fct.ciai.repository.UsersRepository;

@Service
public class SecurityService {

	private UsersRepository users;
	private EmployeesRepository employees;
	private CompaniesRepository companies;

    public SecurityService(UsersRepository users, CompaniesRepository companies, EmployeesRepository employees) {
        this.users = users;
        this.employees = employees;
        this.companies = companies;
    }
    
    public boolean isPrincipal(User user, long id) {
    	Optional<pt.unl.fct.ciai.model.User> u = users.findById(id);
    	return u.isPresent() && u.get().getUsername().equals(user.getUsername());
    }
	
    public boolean isCompanyAdmin(User user, long id) {
    	Optional<Company> company = companies.findById(id);
    	return company.isPresent() && company.get().getAdmin().getUsername().equals(user.getUsername());
    }
    
    public boolean isMemberOfMyCompany(long cid, long eid) {
    	Optional<Employee> e = employees.findById(eid);
    	Optional<Company> c = companies.findById(cid);
    	return e.isPresent() && c.isPresent() && e.get().getCompany().equals(c.get());
    }
}

