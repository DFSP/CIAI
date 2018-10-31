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
import pt.unl.fct.ciai.repository.UsersRepository;


@Service
public class MyUserDetailsService implements UserDetailsService {

	private final UsersRepository users;

    public MyUserDetailsService(UsersRepository users) {
        this.users  = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Some in-memory user authentication with priority 1
        if (username.equalsIgnoreCase("user")) {
            return new User("user", encoder().encode("password"), Collections.emptyList());
        }
        else {
            // Now for the database user searching
            pt.unl.fct.ciai.model.User user = users.findByUsername(username);
            if (user == null) {
            	throw new UsernameNotFoundException(username);
            }
            return new User(username, user.getPassword(), Collections.emptyList());
        }
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
	
}
