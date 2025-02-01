package smartcontact.manager.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminController {
 
    
   //show contact page

   @RequestMapping("/Contactform")
   public String form()
   {


     return "AddContact";
   }


   //show profile page


   @RequestMapping("/profile")
   public String profile()
   {
     return "Profile";
   }

 //show all contacts page

   @RequestMapping("/showcontact")
   public String showcontact()
   {
     return "ShowContacts";
   }
}
