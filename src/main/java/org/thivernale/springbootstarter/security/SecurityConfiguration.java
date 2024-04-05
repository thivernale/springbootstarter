package org.thivernale.springbootstarter.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.thivernale.springbootstarter.jwt.filters.JwtRequestFilter;

import javax.sql.DataSource;

/**
 * @see https://docs.spring.io/spring-security/site/docs/current/reference/html5
 */

/**
 * Tell Spring that this is a web security configuration
 */
@EnableWebSecurity
@SuppressWarnings(value = {"deprecation", "unused"})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    /**
     * Configure Authentication mechanism
     * Set your Configuration on the auth object:
     * <p>
     * {@link AuthenticationManager} authenticate() -> {@link AuthenticationProvider} authenticate() supports() ->
     * {@link UserDetailsService} loadUserByUsername()
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //configureInMemoryAuthentication(auth);

        //configureJdbcAuthenticationH2Default(auth);

        //configureJdbcAuthenticationH2Files(auth);

        configureJpaAuthentication(auth);
    }

    /**
     * Configure as in-memory authentication with user, password and role
     *
     * @param auth
     * @throws Exception
     */
    private void configureInMemoryAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user")
            .password("pass")
            .roles("USER")
            .and()
            .withUser("foo")
            .password("bar")
            .roles("ADMIN")
        ;
    }

    /**
     * Configure JDBC authentication with default schema and inline defined used:
     *
     * @param auth
     * @throws Exception
     */
    private void configureJdbcAuthenticationH2Default(AuthenticationManagerBuilder auth) throws Exception {
        // when the application starts up, these instructions tell Spring
        // Security to populate the database with certain schema and data
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .withDefaultSchema()
            .withUser(
                User.withUsername("user")
                    .password("pass")
                    .roles("USER"))
            .withUser(
                User.withUsername("admin")
                    .password("pass")
                    .roles("ADMIN"));
    }

    /**
     * Configure JDBC authentication with database schema and users defined in *.sql files:
     *
     * @param auth
     * @throws Exception
     */
    private void configureJdbcAuthenticationH2Files(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
        //.usersByUsernameQuery("...")
        //.authoritiesByUsernameQuery("...")
        ;
    }

    /**
     * Configure db authentication: connect to MySQL using JPA and get data
     * from database.
     *
     * @param auth
     * @throws Exception
     */
    private void configureJpaAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    /**
     * Spring Security requires a password encoder
     *
     * @return a Spring Bean of type PasswordEncoder
     */
    @Bean
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
            .antMatchers("/admin")
            .hasRole("ADMIN")
            .antMatchers("/user")
            .hasAnyRole("ADMIN", "USER")
            // specify that URLs are allowed by everyone
            .antMatchers("/", "/authenticate" /*, "/static/css", "/static/js"*/)
            .permitAll()
            .anyRequest()
            .authenticated()
            //.and()
            //.formLogin()

            //        http.headers().frameOptions().disable()
            .and()
            .csrf()
            .disable();

        // do not create session
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // apply filter processing JWT before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers("/h2-console/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
