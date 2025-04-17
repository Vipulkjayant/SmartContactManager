package smartcontact.manager.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import smartcontact.manager.Config.CustomUserService;



@Component
public class JwtFilter extends OncePerRequestFilter{


    @Autowired
    private JwtService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

    //step.1-->Extract the Bearer token coming in request (Bearer Token....)

      String authHeader= request.getHeader("Authorization");
      String token=null;
      String userName=null;
      if(authHeader!=null && authHeader.startsWith("Bearer "))
      {

      //step.2-->Trim the bearer from the token  
        token= authHeader.substring(7); //Exclude [Bearer ]
        
      }
      else{
        filterChain.doFilter(request, response); // Proceed to next filter if no token
            return;
      }

      //step.3-->Find the username from the token
      
      userName=jwtService.extractUserName(token);


// token UserName==Database UserName

      //condition -->username not null and the user is not already authenticate
      if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null)
      {
    // step.4-->Interaction with database to validate the  token , extract the userdetails from database
 
       //Just deliver the userName to the loadUserByName(username) method, it will extract all the user details
        
      UserDetails userdetails= context.getBean(CustomUserService.class).loadUserByUsername(userName);
           

          if(jwtService.validate(token,userdetails))
          {

    //step.5-->Forward something to another filter 

      UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userdetails,null,userdetails.getAuthorities());

   //step.6-->Delivers the content of request to next filter   

      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

      SecurityContextHolder.getContext().setAuthentication(authToken);

          }
      }

     filterChain.doFilter(request, response);
      
    }
    
}
