package pt.unl.fct.ciai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pt.unl.fct.ciai.companies.CompaniesRepository;
import pt.unl.fct.ciai.companies.Company;
import pt.unl.fct.ciai.contacts.Contact;
import pt.unl.fct.ciai.contacts.ContactsRepository;

@SpringBootApplication
public class CiaiApplication implements CommandLineRunner {

	@Autowired
	private CompaniesRepository companies;
	@Autowired
	private ContactsRepository contacts;
	
	public static void main(String[] args) {
		SpringApplication.run(CiaiApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		Company ciai = new Company().name("ciai").email("ciai@fct.unl.pt").address("fct");
		companies.save(ciai);
		Contact ciaiContact = new Contact().name("contact").company(ciai);
		ciai.addContact(ciaiContact);
		companies.save(ciai);
		
		
		Company fct = new Company().name("fct").email("fct@fct.unl.pt").address("fct");
		Contact fctContact = new Contact().name("contactfct").company(fct);
		fct.addContact(fctContact);
		companies.save(fct);
	}
}
