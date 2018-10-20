package pt.unl.fct.ciai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Autowired
    private PasswordEncoder encoder;
    
	public static void main(String[] args) {
		SpringApplication.run(CiaiApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		Company ciai = new Company().name("ciai").email("ciai@fct.unl.pt").address("fct");
		Contact ciaiContact = new Contact().name("contact").password(encoder.encode("password1")).company(ciai);
		ciai.addContact(ciaiContact);
		companies.save(ciai);
		contacts.save(ciaiContact);
		
		Company fct = new Company().name("fct").email("fct@fct.unl.pt").address("fct");
		Contact fctContact = new Contact().name("contactfct").password(encoder.encode("password2")).company(fct);
		fct.addContact(fctContact);
		companies.save(fct);
		contacts.save(fctContact);
	}
}
