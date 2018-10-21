package pt.unl.fct.ciai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import pt.unl.fct.ciai.company.CompaniesRepository;
import pt.unl.fct.ciai.company.Company;
import pt.unl.fct.ciai.contact.Contact;
import pt.unl.fct.ciai.contact.ContactRepository;

@SpringBootApplication
public class CiaiApplication implements CommandLineRunner {

	@Autowired
	private CompaniesRepository companies;
	@Autowired
	private ContactRepository contacts;
    @Autowired
    private PasswordEncoder encoder;
    
	public static void main(String[] args) {
		SpringApplication.run(CiaiApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		Company ecma = new Company().name("ecma").email("ecma@ecma.pt").address("lisboa");
		Contact ecmaContact = new Contact().name("ecmaContact").password(encoder.encode("password1")).company(ecma);
		ecma.addContact(ecmaContact);
		companies.save(ecma);
		contacts.save(ecmaContact);

		Company fct = new Company().name("fct").email("fct@fct.unl.pt").address("almada");
		Contact fctContact = new Contact().name("fctContact").password(encoder.encode("password2")).company(fct);
		fct.addContact(fctContact);
		companies.save(fct);
		contacts.save(fctContact);
	}
}
