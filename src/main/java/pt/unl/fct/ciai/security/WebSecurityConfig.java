package pt.unl.fct.ciai.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String ENCODED_PASSWORD = "$2a$10$1KqWEJJWwCStGJiWh/KdQeDrG4Gy/eyyFenLpXAQz5k9DK1n4H6Vq";
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/hello/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin().permitAll()
            .and()
                .logout().permitAll()
            .and()
                .httpBasic();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
        	.passwordEncoder(passwordEncoder())
            .withUser("user")
            .password(ENCODED_PASSWORD)
            .roles("USER");
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}

