package pt.unl.fct.ciai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.unl.fct.ciai.comment.CommentsRepository;
import pt.unl.fct.ciai.company.CompaniesRepository;
import pt.unl.fct.ciai.company.Company;
import pt.unl.fct.ciai.employee.Employee;
import pt.unl.fct.ciai.employee.EmployeesRepository;
import pt.unl.fct.ciai.proposal.Proposal;
import pt.unl.fct.ciai.proposal.ProposalsRepository;
import pt.unl.fct.ciai.review.Review;
import pt.unl.fct.ciai.review.ReviewsRepository;
import pt.unl.fct.ciai.user.User;
import pt.unl.fct.ciai.user.UsersRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@SpringBootApplication
public class CiaiApplication implements CommandLineRunner {

	@Autowired
	private CompaniesRepository companies;
	@Autowired
	private UsersRepository users;
	@Autowired
	private EmployeesRepository employees;
	@Autowired
	private ReviewsRepository reviews;
	@Autowired
	private ProposalsRepository proposals;
	@Autowired
	private CommentsRepository comments;
    @Autowired
    private PasswordEncoder encoder;
    
	public static void main(String[] args) {
		SpringApplication.run(CiaiApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		Company ist = new Company();
		ist.setName("ist");
		ist.setEmail("ist@ist.pt");
		ist.setAddress("lisboa");
		companies.save(ist);

		// Create user
		User istUser = new User();
		istUser.setUsername("istUser");
		istUser.setFirst_name("ist");
		istUser.setLast_name("User");
		istUser.setPassword(encoder.encode("password1"));
		users.save(istUser);

		// Create employee and link to correspondent user
		Employee istEmployee = new Employee();
		istEmployee.setCompany(ist);
		//istEmployee.setUserId(istUser.getId());
		istEmployee.setUserId(users.findByUsername("istUser").getId());
		ist.addEmployee(istEmployee);
		employees.save(istEmployee);


		Company fct = new Company();
		fct.setName("fct");
		fct.setEmail("fct@fct.pt");
		fct.setAddress("almada");
		companies.save(fct);

		// Create user
		User fctUser = new User();
		fctUser.setUsername("fctUser");
		fctUser.setFirst_name("fct");
		fctUser.setLast_name("User");
		fctUser.setPassword(encoder.encode("password2"));
		users.save(fctUser);

		Employee fctEmployee = new Employee();
		fctEmployee.setCompany(fct);
		//fctEmployee.setUserId(fctUser.getId());
		fctEmployee.setUserId(users.findByUsername("fctUser").getId());
		fct.addEmployee(fctEmployee);
		employees.save(fctEmployee);

		Review review1 = new Review();
		review1.setDate("10-12-2012");
		review1.setClassification(5);
		review1.setTitle("Review1");
		review1.setSummary("Very good review");
		reviews.save(review1);

		Proposal proposal1 = new Proposal();
		proposal1.addReview(review1);
		proposal1.setDate("12-12-2012");
		proposals.save(proposal1);

		/*Optional<Employee> e1 = employees.findById(3l);
		System.out.println("employeeID > "+e1.get().getId()+" associated with user > "+e1.get().getUser().getUsername());
		Optional<Employee> e2 = employees.findById(6l);
		System.out.println("employeeID > "+e2.get().getId()+" associated with user > "+e2.get().getUser().getUsername());*/
	}
}
