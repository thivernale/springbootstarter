package org.thivernale.springbootstarter.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thivernale.springbootstarter.jwt.models.AuthenticationRequest;
import org.thivernale.springbootstarter.jwt.models.AuthenticationResponse;
import org.thivernale.springbootstarter.jwt.util.JwtUtil;
import org.thivernale.springbootstarter.security.services.CustomUserDetailsService;

/**
 * The web layer of a Spring application leverages Spring MVC.
 * It lets you build server-side code which maps to URLs and provides responses.
 *
 */
@RestController
public class HelloController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Maps to a GET request by default
     * @return
     */
    @RequestMapping("/hello")
    public String sayHi() {
        return "Hi";
    }

    /**
     * Authorization Use Case 1:
     *
     * @return
     */
    @GetMapping("/")
    public String home() {
        return ("<h1>Welcome</h1>");
    }

    /**
     * Authorization Use Case 2:
     *
     * @return
     */
    @GetMapping("/user")
    public String user() {
        return ("<h1>Welcome User</h1>");
    }

    /**
     * Authorization Use Case 3:
     *
     * @return
     */
    @GetMapping("/admin")
    public String admin() {
        return ("<h1>Welcome Admin</h1>");
    }

    /**
     * Authentication endpoint which takes payload from request body (username
     * and password) and returns JWT on success
     *
     * @param authenticationRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
