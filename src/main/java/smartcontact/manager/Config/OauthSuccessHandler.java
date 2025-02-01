package smartcontact.manager.Config;

import java.io.IOException;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import smartcontact.manager.Services.UserService;
import smartcontact.manager.Entity.User;;



@Component
public class OauthSuccessHandler implements AuthenticationSuccessHandler {


    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
         

    HttpSession session=request.getSession();

       var oauth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;         

       //check that it is google,github,.....

       //tarika oauth2 se info nikalne k 
       String client = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
       DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
       
       if (client.equalsIgnoreCase("google")) {
           String name = user.getAttribute("name").toString();
           String email = user.getAttribute("email").toString();
           String pic = user.getAttribute("picture").toString();
           System.out.println("OAuth2 provides the user name: " + name);
           System.out.println("OAuth2 provides the email: " + email);
           System.out.println("OAuth2 provides the profile picture: " + pic);
       
           // Verify whether the user exists in the database

           boolean res = userService.verifyobj(email);
           if (!res) {
                User user2 = new User();
                user2.setName(name);
            user2.setPassword("OAuthUser"); // Set a default password or handle it based on your logic
            user2.setEmail(email);
               userService.saveobj(user2);
               System.out.println("User saved from OAuth Google");
               session.setAttribute("name", email);
           }
           else{
            System.out.println("else works...."+name);
            session.setAttribute("name", email);
            response.sendRedirect("/user/indexx");
           }
       
       } else if (client.equalsIgnoreCase("github")) {
           
        String email=user.getAttribute("email")!=null?user.getAttribute("email").toString():user.getAttribute("login").toString()+"@gmail.com";
        String name=user.getAttribute("name") !=null ? user.getAttribute("name").toString() :"userr";
        // Verify whether the user exists in the database
        boolean res = userService.verifyobj(email);
       
        if (!res) {
            User user2 = new User();
            user2.setName(name);
            user2.setPassword("OAuthUser"); // Set a default password or handle it based on your logic
            user2.setEmail(email);
            userService.saveobj(user2);
            System.out.println("User saved from OAuth github");
        }
        else{
            System.out.println("else works...."+name);
            session.setAttribute("name", email);
            response.sendRedirect("/user/indexx");
           }
       
       } else if (client.equalsIgnoreCase("facebook")) {
           String name = user.getAttribute("name").toString();
           String email = user.getAttribute("email").toString();
         
           System.out.println("OAuth2 provides the user name: " + name);
           System.out.println("OAuth2 provides the email: " + email);
       
           boolean res = userService.verifyobj(email);
       
           if (!res) {
               User user2 = new User();
               user2.setName(name);
               user2.setPassword("OAuthUser");
               user2.setEmail(email);
               userService.saveobj(user2);
               System.out.println("User saved from OAuth Facebook");
           }
       
       } else if (client.equalsIgnoreCase("linkedin")) {
           String name = user.getAttribute("localizedFirstName").toString() + " " + user.getAttribute("localizedLastName").toString();
           String email = user.getAttribute("emailAddress").toString();
           System.out.println("OAuth2 provides the user name: " + name);
           System.out.println("OAuth2 provides the email: " + email);
       
           boolean res = userService.verifyobj(email);
       
           if (!res) {
               User user2 = new User();
               user2.setName(name);
               user2.setPassword("OAuthUser");
               user2.setEmail(email);
               userService.saveobj(user2);
               System.out.println("User saved from OAuth LinkedIn");
           }
       
       } else {
           System.out.println("Unknown OAuth2 provider: " + client);
       }
       
 
    }
    
}
