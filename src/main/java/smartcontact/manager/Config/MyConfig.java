package smartcontact.manager.Config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import smartcontact.manager.DAO.UserRepo;
import smartcontact.manager.Entity.User;
import smartcontact.manager.JWT.JwtFilter;
import smartcontact.manager.JWT.JwtService;


import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class MyConfig {


   
    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtFilter jwtFilter;

    //For redirection after success login using oauth2

    @Autowired
    private OauthSuccessHandler handler;

    @Autowired
    private UserRepo userRepo;

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http
           .authorizeHttpRequests(auth -> auth
               .requestMatchers("/").permitAll() // Allow access to /user/index
               .requestMatchers("/admin/**").hasRole("ADMIN") // Protect admin
               .requestMatchers("/user/indexx").permitAll() 
               .anyRequest().permitAll() // Allow all other requests
           )

           .formLogin(form -> form
               .loginPage("/login") // Custom login page
               .loginProcessingUrl("/login")
               .usernameParameter("username")
               .passwordParameter("password")
               .successHandler(customAuthenticationSuccessHandler()) // Custom success handler
               .permitAll()
           )
           .logout(logout -> logout
               .logoutSuccessUrl("/login?logout") // Custom logout success URL
               .permitAll()
           )
           .sessionManagement(sessionManagement -> sessionManagement
               .invalidSessionUrl("/login")  
               .maximumSessions(1)
               .expiredUrl("/login")
               .and()
               .sessionFixation().migrateSession()   
           )

          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

           .csrf().disable(); // Disable CSRF (not recommended for production)

//configuration of oauth2

           http.oauth2Login(oauth2->oauth2
               .loginPage("/login")
               .successHandler(handler)  //khanii
           );

       return http.build();
   }

   @Bean
   public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
       return (request, response, authentication) -> {
           

        //Adding user for form login for index view

           String userName=authentication.getName();
           System.out.println("The username for form authentication"+userName);

           String token=jwtService.getToken(userName);
           System.out.println("Token is successfully created.............!!"+token);

          request.getSession().setAttribute("username",userName);

           if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {

            response.setHeader("Authorization","Bearer "+token);
               response.sendRedirect("/admin/dashboard"); // Redirect for ADMIN
           } else if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            response.setHeader("Authorization","Bearer "+token);
               response.sendRedirect("/user/indexx"); // Redirect for USER
           } else {
            response.setHeader("Authorization","Bearer "+token);
               response.sendRedirect("/"); // Default fallback
           }
       };
   }

   @Bean
   public UserDetailsService userDetailsService() {
       return new CustomUserService(); // Ensure CustomUserService implements UserDetailsService
   }

   @Bean
   public BCryptPasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder(); // For password encryption
   }

   @Bean 
   public DaoAuthenticationProvider authenticationProvider() {
       DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       authProvider.setUserDetailsService(this.userDetailsService());
       authProvider.setPasswordEncoder(passwordEncoder());
       return authProvider;
   }
}
