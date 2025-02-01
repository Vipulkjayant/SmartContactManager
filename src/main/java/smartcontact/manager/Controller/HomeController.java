 package smartcontact.manager.Controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import smartcontact.manager.Entity.User;
import smartcontact.manager.Services.EmailService;
import smartcontact.manager.Services.UserService;

@Controller
public class HomeController {

    
    @Autowired
    private UserService userService;

    //using bcrypt password encoder to encode the password and put it into database

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private EmailService emailService;

    @RequestMapping("/")
    public String home()
    {

        return "Home";
    }

     //show home page

    //show login page
    @RequestMapping("/login")
    public String login()
    {
        return "Login";
    }
   
  

    //show signup page and provide a blank user 

    @RequestMapping("/signup")
    public String signup(Model m)
    {
        User u=new User();
        m.addAttribute("user", u);
        return "Signup";
    }

    


    //take user data from form and validate it 

    //conditions-->  1.returns to profile page  2. return to same signup page with errors 

    @PostMapping("/saveuser")
    public String saveuser(
        @Valid @ModelAttribute User u,
        BindingResult eResult,
        @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
        Model m,
        HttpSession s) {
        
        boolean result = false;
    
        try {
            // Check if terms are agreed
            if (!agreement) {
                m.addAttribute("liscence", "You have not agreed to the terms and conditions");
                m.addAttribute("user", u); // Repopulate form
                return "Signup";
            }
    
            // Check for validation errors
            if (eResult.hasErrors()) {
                m.addAttribute("validationErrors", eResult.getAllErrors());
                System.out.println(eResult.getAllErrors()+"Errorss.......");
                m.addAttribute("user", u);
                return "Signup";
            }

            //saving password with the help of password encoder

            User user=new User();
            user.setName(u.getName());
            user.setEmail(u.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
            user.setRole(u.getRole());
            user.setAbout(u.getAbout());
    
            // Save user
            result = userService.saveobj(user);
    
            if (!result) {
                m.addAttribute("error", "Failed to save user. Please try again.");
                m.addAttribute("user", u); // Repopulate form
                return "Signup";
            }
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("error", "An unexpected error occurred. Please try again.");
            m.addAttribute("user", u); // Repopulate form
            return "Signup";
        }
    
        // Success
        m.addAttribute("user", u); // For displaying user info on the Profile page
        return "Index";
    }

     @RequestMapping("/forgotPassword")
     public String forgotPassword()
     {
        return "ForgetPassword";
     }

     //Module for forgot password

     @RequestMapping("/processForgetPassword")
     public String processforgetPassword(@RequestParam("email")String email)
     {
        if(email!=null)
        {
        String subject="OTP Verification";
        String code= "Your verification code id";
        int otp = (int)(Math.random() * 90000) + 10000; // Generates a 5-digit number
        String c=String.valueOf(otp);
        emailService.sendEmail(email, subject, code+":"+otp);
        }
        else{

        }

        return "OtpVerification";
    

     }


     @RequestMapping("/otpverification")
     public String otpVerify()
     {
        return "OtpVerification";
     }

    
}