package org.thivernale.springbootstarter.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Tell Spring that this is a web security configuration
 *
 */
@SuppressWarnings("deprecation")
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource;

    /**
     * Configure Authentication mechanism
     * Set your Configuration on the auth object:
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // configure as in-memory authentication with user, password and role
        /*auth.inMemoryAuthentication()
            .withUser("user")
            .password("pass")
            .roles("USER")
            .and()
            .withUser("foo")
            .password("bar")
            .roles("ADMIN")
            ;*/

        // configure JDBC authentication
        // when the application starts up, these instructions tell Spring
        // Security to populate the database with certain schema and data
        /*auth.jdbcAuthentication()
        .dataSource(dataSource)
        .withDefaultSchema()
        .withUser(
            User.withUsername("user")
            .password("pass")
            .roles("USER"))
        .withUser(
            User.withUsername("admin")
            .password("pass")
            .roles("ADMIN"));*/

        auth.jdbcAuthentication()
        .dataSource(dataSource)
        //.usersByUsernameQuery("...")
        //.authoritiesByUsernameQuery("...")
        ;
    }

    /**
     * Spring Security requires a password encoder
     * @return a Spring Bean of type PasswordEncoder
     */
    @Bean
    @SuppressWarnings(value = { "deprecation" })
    public PasswordEncoder getPasswordEncoder() {
        // a password encoder which does nothing
        // not recommended for real applications, but this is a tutorial
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Override Authorization configuration
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        //.antMatchers("/**").hasRole("ADMIN")
        // start from most restrictive rule
        .antMatchers("/admin").hasRole("ADMIN")
        .antMatchers("/user").hasAnyRole("ADMIN", "USER")
        // specify that URLs are allowed by everyone
        .antMatchers("/"/*, "/static/css", "/static/js"*/).permitAll()
        .and()
        .formLogin()
        ;
    }
}
