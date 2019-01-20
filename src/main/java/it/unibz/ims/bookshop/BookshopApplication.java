package it.unibz.ims.bookshop;

import it.unibz.ims.bookshop.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
@EnableOAuth2Sso
public class BookshopApplication extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomerService customerService;

	public static void main(String[] args) {
		SpringApplication.run(BookshopApplication.class, args);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				/*.requiresChannel()
					.anyRequest()
						.requiresSecure()

				.and()*/

				.authorizeRequests()
					.antMatchers("/userprofile/**", "/admin/**")
						.authenticated()
					.anyRequest()
						.permitAll()

				.and()

				.logout()
					.logoutSuccessUrl("/")
					.permitAll()

				.and()

				.headers()
					.contentSecurityPolicy("Content-Security-Policy: default-src 'self'");
	}

	@EventListener
	public void authSuccessEventListener(AuthenticationSuccessEvent authorizedEvent){
		customerService.handleAuthenticatedCustomer( authorizedEvent.getAuthentication() );
	}
}
