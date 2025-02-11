package smartcontact.manager.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import smartcontact.manager.DAO.UserRepo;
import smartcontact.manager.Entity.User;

public class CustomUserService implements UserDetailsService{


   @Autowired
   private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      
       User user= userRepo.getUser(username);

       if(user==null)
       {
         throw new UsernameNotFoundException("User not found");
          
       }

       CustomUserDetails customUserDetails=new CustomUserDetails(user);


       return customUserDetails;
    }
    
}
