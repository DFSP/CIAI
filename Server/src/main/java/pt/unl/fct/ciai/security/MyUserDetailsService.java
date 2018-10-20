package pt.unl.fct.ciai.security;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pt.unl.fct.ciai.contacts.Contact;
import pt.unl.fct.ciai.contacts.ContactsRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private final ContactsRepository contacts;

    public MyUserDetailsService(ContactsRepository contacts) {
        this.contacts = contacts;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Some in-memory user authentication with priority 1
        if (username.equalsIgnoreCase("user")) {
            return new User("user", encoder().encode("password"), Collections.emptyList());
        }
        else {
            // Now for the database user searching
            Contact contact = contacts.findByName(username);
            if (contact == null) {
            	throw new UsernameNotFoundException(username);
            }
            return new User(username, contact.getPassword(), Collections.emptyList());
        }
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
	
}
