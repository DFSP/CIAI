package pt.unl.fct.ciai;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import pt.unl.fct.ciai.model.*;
import pt.unl.fct.ciai.repository.CommentsRepository;
import pt.unl.fct.ciai.repository.CompaniesRepository;
import pt.unl.fct.ciai.repository.EmployeesRepository;
import pt.unl.fct.ciai.repository.ProposalsRepository;
import pt.unl.fct.ciai.repository.ReviewsRepository;
import pt.unl.fct.ciai.repository.SectionsRepository;
import pt.unl.fct.ciai.repository.UsersRepository;

@Component
public class DatabaseLoader {

	@Bean
	CommandLineRunner initDatabase(CompaniesRepository companies, UsersRepository users,
								   EmployeesRepository employees, SectionsRepository sections,
								   ReviewsRepository reviews, ProposalsRepository proposals,
								   CommentsRepository comments, PasswordEncoder encoder) {

		return args -> {

			//TODO adicionar mais entidades com os campos preenchidos
			// (se possivel usando os metodos builders (e.g. sysAdmin e ist))

			// Create system admin user
			User sysAdmin = new User()
					.username("admin")
					.password(encoder.encode("password"))
					.role(User.Role.ROLE_SYS_ADMIN);
			users.save(sysAdmin);


			//	-	-	-	-	-	-	-	COMPANIES	-	-	-	-	-	-	-
			// IST
			Company ist = new Company();
			ist.setName("IST");
			ist.setCity("Lisboa");
			ist.setZipCode("2000-100");
			ist.setAddress("Campus da Alameda");
			ist.setPhone("21 000 00 01");
			ist.setEmail("ist@ist.pt");
			ist.setFax("fax.01");
			companies.save(ist);
			// FCT
			Company fct = new Company();
			fct.setName("FCT");
			fct.setCity("Almada");
			fct.setZipCode("3000-100");
			fct.setAddress("Campus da Caparica");
			fct.setPhone("22 000 00 01");
			fct.setEmail("fct@fct.pt");
			fct.setFax("fax.02");
			companies.save(fct);
			//ISCTE
			Company iscte = new Company();
			iscte.setName("ISCTE");
			iscte.setCity("Lisboa");
			iscte.setZipCode("2000-200");
			iscte.setAddress("entre-campos");
			iscte.setPhone("+351 210 000 002");
			iscte.setEmail("iscte@iscte.pt");
			iscte.setFax("fax.03");
			companies.save(iscte);

			//	-	-	-	-	-	-	-	EMPLOYEES	-	-	-	-	-	-	-
			// EMPLOYEE IST 1
			Employee daniel = new Employee();
			daniel.setCompany(ist);
			daniel.setFirstName("daniel");
			daniel.setLastName("pimenta");
			daniel.setUsername("dpimenta");
			daniel.setPassword(encoder.encode("password"));
			daniel.setEmail("dpimenta@email.com");
			daniel.setRole(User.Role.ROLE_COMPANY_ADMIN);
			daniel.setCity("Almada");
			daniel.setAddress("Rua da Caparica");
			daniel.setZipCode("1234-999");
			daniel.setCellPhone("+351 919 999 999");
			daniel.setHomePhone("+351 221 000 000");
			daniel.setGender('M');
			daniel.setSalary(500.0);
			daniel.setBirthday(new Date());
			ist.addEmployee(daniel);
			employees.save(daniel);

			// EMPLOYEE IST 2
			Employee joao = new Employee();
			joao.setCompany(ist);
			joao.setFirstName("joao");
			joao.setLastName("reis");
			joao.setUsername("jreis");
			joao.setPassword(encoder.encode("password"));
			joao.setEmail("jreis@email.com");
			//daniel.setRole(User.Role.ROLE_COMPANY_ADMIN);
			joao.setCity("Lisboa");
			joao.setAddress("Rua de Lisboa");
			joao.setZipCode("1234-888");
			joao.setCellPhone("+351 969 999 999");
			joao.setHomePhone("+351 211 000 000");
			joao.setGender('M');
			joao.setSalary(100.0);
			joao.setBirthday(new Date());
			ist.addEmployee(joao);
			employees.save(joao);

			// EMPLOYEE FCT
			Employee luis = new Employee();
			luis.setCompany(fct);
			luis.setFirstName("luis");
			luis.setLastName("martins");
			luis.setUsername("lmartins");
			luis.setPassword(encoder.encode("password"));
			luis.setEmail("lmartins@email.com");
			luis.setRole(User.Role.ROLE_COMPANY_ADMIN);
			luis.setCity("Almada");
			luis.setAddress("Rua de Almada");
			luis.setZipCode("1234-777");
			luis.setCellPhone("+351 939 000 000");
			luis.setHomePhone("+351 221 999 999");
			luis.setGender('M');
			luis.setSalary(6000.0);
			luis.setBirthday(new Date());
			fct.addEmployee(luis);
			employees.save(luis);

			/*Optional<Employee> e1 = employees.findById(3l);
			System.out.println("employeeID > "+e1.get().getId()+" associated with user > "+e1.get().getUser().getUsername());
			Optional<Employee> e2 = employees.findById(6l);
			System.out.println("employeeID > "+e2.get().getId()+" associated with user > "+e2.get().getUser().getUsername());*/

			//	-	-	-	-	-	-	-	PROPOSALS	-	-	-	-	-	-	-

			Proposal proposal1 = new Proposal();
			proposal1.setTitle("Churrasco");
			proposal1.setDescription("Fazer um churrasco só com carne de porco.");
			proposal1.setCreationDate(new Date(21-2-2020));
			proposal1.setProposer(joao);
			joao.addMyProposal(proposal1);
			proposal1.setApprover(luis);
			luis.addApproveProposal(proposal1);


			Proposal proposal2 = new Proposal();
			proposal2.setTitle("Evento Vegan");
			proposal2.setDescription("Fazer um evento com comida vegan.");
			proposal2.setCreationDate(new Date(20-2-2020));
			proposal2.setProposer(joao);
			joao.addMyProposal(proposal2);
			proposal2.setApprover(daniel);
			daniel.addApproveProposal(proposal2);


			Proposal proposal3 = new Proposal();
			proposal3.setTitle("Evento de pesca");
			proposal3.setDescription("Evento de pesca no lago do departamental.");
			proposal3.setCreationDate(new Date(20-2-2020));
			proposal3.setProposer(luis);
			luis.addMyProposal(proposal3);
			proposal3.setApprover(daniel);
			daniel.addApproveProposal(proposal1);


			//	-	-	-	-	-	-	-	REVIEWS	-	-	-	-	-	-	-

			Review review1 = new Review();
			review1.setClassification(5);
			review1.setTitle("Review1");
			review1.setText("A review");
			review1.setSummary("Very good review");
			review1.setAuthor(luis);
			review1.setProposal(proposal1);
			proposal1.addReview(review1);

			Review review2 = new Review();
			review2.setClassification(3);
			review2.setTitle("Review2");
			review2.setText("Meh... not so good review");
			review2.setSummary("Not so good");
			review2.setAuthor(joao);
			review2.setProposal(proposal1);
			proposal1.addReview(review2);


			Review review3 = new Review();
			review3.setClassification(1);
			review3.setTitle("Review3");
			review3.setText("Very bad review");
			review3.setSummary("Horrible");
			review3.setAuthor(joao);
			review3.setProposal(proposal3);
			proposal3.addReview(review3);

			//	-	-	-	-	-	-	-	SECTIONS	-	-	-	-	-	-	-

			Section section1 = new Section();
			section1.setBudget(2000);
			section1.setGoals("WIN WIN WIN");
			section1.setMaterial("2 folhas de papel, 3 rolhas de cortiça");
			section1.setWorkPlan("1- pegar nas folhas de papel 2- pegar nas rolhas de cortiça");
			section1.setProposal(proposal1);
			proposal1.addSection(section1);
			proposals.save(proposal1);

			Section section2 = new Section();
			section2.setBudget(500);
			section2.setGoals("Acabar com os porcos");
			section2.setMaterial("3 facas do talho daquelas mesmo grandes");
			section2.setWorkPlan("1- pegar na faca do talho 2- matar porcos");
			section2.setProposal(proposal1);
			proposal1.addSection(section2);
			proposals.save(proposal1);

			Section section3 = new Section();
			section3.setBudget(100);
			section3.setGoals("Matar plantas");
			section3.setMaterial("Bue papel");
			section3.setWorkPlan("1- comer o papel");
			section3.setProposal(proposal2);
			proposal2.addSection(section3);
			proposals.save(proposal2);

			Section section4 = new Section();
			section4.setBudget(30000);
			section4.setGoals("Coiso");
			section4.setMaterial("1 disto 2 daquilo");
			section4.setWorkPlan("1- pegar nisto");
			section4.setProposal(proposal3);
			proposal3.addSection(section4);
			proposals.save(proposal3);

			//	-	-	-	-	-	-	-	COMMENTS	-	-	-	-	-	-	-

			Comment comment1 = new Comment();
			comment1.setTitle("Comment 1");
			comment1.setText("Text Text Text");
			comment1.setAuthor(joao);
			comment1.setCreationDate(new Date());
			comment1.setProposal(proposal1);
			proposal1.addComment(comment1);
			proposals.save(proposal1);

			Comment comment2 = new Comment();
			comment2.setTitle("Comment 2");
			comment2.setText("WORK WORK WORK WORK WORK");
			comment2.setAuthor(daniel);
			comment2.setCreationDate(new Date());
			comment2.setProposal(proposal2);
			proposal2.addComment(comment2);
			proposals.save(proposal2);

			Comment comment3 = new Comment();
			comment3.setTitle("Comment 3");
			comment3.setText("I came here like a wrecking ball");
			comment3.setAuthor(joao);
			comment3.setCreationDate(new Date());
			comment3.setProposal(proposal3);
			proposal3.addComment(comment3);

			Comment comment4 = new Comment();
			comment4.setTitle("Comment 4");
			comment4.setText("I ");
			comment4.setAuthor(daniel);
			comment4.setCreationDate(new Date());
			comment4.setProposal(proposal3);
			proposal3.addComment(comment4);
			proposals.save(proposal3);

		};
	}


}
