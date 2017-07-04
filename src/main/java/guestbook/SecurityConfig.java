package guestbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String ADMIN_USERNAME = "JAVAGB_ADMIN_USER_NAME";
	private static final String ADMIN_PASSWORD = "JAVAGB_ADMIN_PASSWORD";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/", "/seeNames").permitAll()
				.antMatchers("/showAllUsers/**").hasRole("ADMIN")
				.and()
			.formLogin()
				.loginPage("/login").failureUrl("/login-error");

		http
			.csrf().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser(System.getenv(ADMIN_USERNAME))
        .password(System.getenv(ADMIN_PASSWORD))
        .roles("ADMIN");
	}

}
