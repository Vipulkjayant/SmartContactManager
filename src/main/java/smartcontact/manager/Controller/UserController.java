package smartcontact.manager.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.print.DocFlavor.STRING;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stripe.model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import smartcontact.manager.DAO.ContactRepo;
import smartcontact.manager.DAO.UserRepo;
import smartcontact.manager.Entity.Contact;
import smartcontact.manager.Entity.CustomerData;
import smartcontact.manager.Entity.User;
import smartcontact.manager.Services.ImageService;
import smartcontact.manager.Services.PaymentService;
import smartcontact.manager.Services.UserService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.core.Authentication;


@Controller
@RequestMapping("/user")
public class UserController{
    
  @Autowired
  private UserRepo userRepo;

  @Autowired
  private UserService userService;

  @Autowired
  private ContactRepo contactRepo;

  @Autowired
  private ImageService imageService;

  @Autowired
  private PaymentService paymentService;

  @Value("${upload.path}")
  private String uploadpath;


   //Adding common data to all url of this controller

   @ModelAttribute
   public void addcommon(Principal principal,Model m,HttpSession session)throws Exception
   {
   
     Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

     String name=(String)session.getAttribute("name");

     User ouser=userRepo.getUser(name);

      if(authentication instanceof OAuth2AuthenticationToken)
      {

          m.addAttribute("user", ouser);
          System.out.println("You are login from the Google!!!!"+name);

      }
      else{

       String username= (String)session.getAttribute("username");
       System.out.println("The form user is ...."+username);
       User user=userRepo.getUser(username);
       m.addAttribute("user",user);
        System.out.println("Login from the form!!!");
      }

   }



    @RequestMapping("/indexx")
    public String requestMethodName(Principal principal,Model m) {
 
       
        return "Index";
    }




    //payment integration

    @RequestMapping("/getCustomer")
    public String gCustomerData(Model m) throws Exception
    {
      User user= (User)m.getAttribute("user");
      boolean iffound= userService.verifyCustomerData(user.getId());
      if(!iffound)
     {
      CustomerData cData=new CustomerData();
      cData.setName(user.getName());
      cData.setEmail(user.getEmail());
      cData.setUser_id(user.getId());
      CustomerData customerData= paymentService.create(cData);
      boolean res= userService.saveCustomerData(cData);
     
      if(res)
      {
        System.out.println("Your customer data is ..........."+customerData);

      }
      else
      {
        System.out.println("Error in creating customer");
      }
    }
    else{
      System.out.println("Customer is already present into db with this user-id");
    }
      return "/payment";
    }

    @RequestMapping("/payment")
    public String payment()
    {
      return "Payment";
    }

    @RequestMapping("/processPayment")
    public String processPayment(@RequestParam("amount")Long amount,@RequestParam("currency")String currency,@RequestParam("paymentMethod")String paymentMethod,Model m )
    {

      System.out.println("You entered amount :"+amount);
      System.out.println("Your payment id :"+paymentMethod);
      System.out.println("Your select currency :"+currency);

      User user=(User)m.getAttribute("user");

     String customer_id= userService.getCustomerId(user.getId());

       String client_id=paymentService.doPayment(amount,currency,paymentMethod,customer_id);

       m.addAttribute("client_id", client_id);
       m.addAttribute("paymentMethod", client_id);
       m.addAttribute("amount", amount);
       return "/paymentDone";
    }

    @RequestMapping("/paymentDone")
    public String paymentdone()
    {
      return "PaymentSuccess";
    }

    @RequestMapping("/addContact")
    public String addcontact(Model m)
    {
        m.addAttribute("contact", new Contact());

        return "User_AddContact";
    }

    @RequestMapping("/login")
    public String logout()
    {
        return "/";
    }

   @PostMapping("/saveContact")
public String saveContact(@Valid @ModelAttribute Contact contact,BindingResult result,@RequestParam("file")MultipartFile image,Principal principal,Model m)
   {

     if(result.hasErrors())
     {
        m.addAttribute("msg", "Contact not saved yet !!");
        m.addAttribute("contact", new Contact()); // 💡 Reset the contact object

        return "User_AddContact";
     }

     else{
     String username=principal.getName();
     System.out.println("principal username is !!!! "+username);
        if(image!=null)
        {

       //work for cloudinary
       
       String cloud_url= imageService.uploadimg(image);
        System.out.println("Successfully saved image to cloudinary.....................");
        String imagename=image.getOriginalFilename();
        contact.setImage(cloud_url);
          boolean res= userService.saveContact(username,contact,image);
          try {
            Files.copy(image.getInputStream(), Paths.get(uploadpath,imagename),StandardCopyOption.REPLACE_EXISTING);
          } catch (Exception e) {
            
          }
          System.out.println(res);
        }
        m.addAttribute("msg", "Contact saved successfully !!");
    }
    m.addAttribute("contact", new Contact()); // 💡 Reset the contact object to avoid issues with th:object

        return "User_AddContact";

}


  @RequestMapping("viewContact/{page}")
  public String viewcontacts(@PathVariable(value = "page", required = false)int page,Model m)
  {

    //extracting above user
    if(page<0)
    {
      page=0;
    }
    User u=(User) m.getAttribute("user");

 
    //current page-->page
    //total pages-->5 

  Pageable pageable= PageRequest.of(page, 5); 
   Page<Contact> li=userService.getAllContacts(u,pageable);
   for (Contact contact : li) {
     System.out.println(contact.getName());
     System.out.println(contact.getNickname());
     System.out.println(contact.getEmail());
   }
   System.out.println("List of all contacts......."+li);
   m.addAttribute("contacts", li);
   m.addAttribute("current_page", page);
   m.addAttribute("total_pages",li.getTotalPages());
   
    return "User_viewContact";
  }


  //working on single contact

  @RequestMapping("/singleContact/{cid}")
  public String singleContact(@PathVariable("cid") int id,Model m)
   {

    System.out.println(id);
    User user=(User)m.getAttribute("user");
    int user_id=user.getId();

     
    Contact contact= userService.getSingle(id);

    //solving security bug ,for hit and trial method

    if(user_id==contact.getUser().getId())
    {
      m.addAttribute("contact",contact);


    }



     return "ContactDetails";
   }


   @GetMapping("/deleteContact/{cid}")
   public String deleteContact(@PathVariable("cid")int id,Model m,RedirectAttributes redirect)
   {

    System.out.println("The Contact id for delete the contact :"+id);
    User user=(User)m.getAttribute("user");
    int user_id=user.getId();
    
   
   boolean result= userService.deleteContact(user_id, id);
   System.out.println("The result for deleting the contact :"+result);
   if(result)
   {
     redirect.addFlashAttribute("deleteContact", "Contact deleted successfully");
  }
  else{
    redirect.addFlashAttribute("deleteContact","Contact not deleted...");
  }
  
    return "redirect:/user/viewContact/0";
   }


   @GetMapping("/updateContact/{id}")
   public String updateContact(@PathVariable("id")int id,Model m) {
    
     Contact contact= userService.getSingle(id);
     
     m.addAttribute("contact", contact);
     

       return "User_UpdateContact";

   }

   @PostMapping("/updated")
   public String updated(
    @RequestParam("name")String name,
   @RequestParam("nickname")String nickname,
   @RequestParam("phoneno")String phoneno,
   @RequestParam("email")String email,
   @RequestParam("description")String description,
   @RequestParam("work")String work,
   @RequestParam("id")int id,Model m,RedirectAttributes redirect)
   {

   System.out.println("The contact id is............"+id);
   Contact contact= contactRepo.findById(id).get();

   contact.setName(name);
   contact.setNickname(nickname);
   contact.setEmail(email);
   contact.setDescription(description);
   contact.setPhoneno(phoneno);
   contact.setWork(work);
   contactRepo.save(contact);
   redirect.addFlashAttribute("updated","Contact successfully updated !!");
    return "redirect:/user/updateContact/"+id;

   }

   //Profile Section

   @RequestMapping("/profile")
   public String profile(Model m)
   {
     User u=(User)m.getAttribute("user");
     m.addAttribute("user", u);

     return "User_Profile";
   }



}
