package smartcontact.manager.Controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import smartcontact.manager.DAO.ContactRepo;
import smartcontact.manager.DAO.UserRepo;
import smartcontact.manager.Entity.Contact;
import smartcontact.manager.Entity.User;

@RestController
@RequestMapping("/api2")
public class SearchController {
    
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ContactRepo contactRepo;

    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query")String query,Principal principal)
    {
        System.out.println(query);
        User u=userRepo.getUser(principal.getName());
        List<Contact> li=contactRepo.findByNameContainingAndUser(query, u);

        
        return ResponseEntity.ok(li);
        
    }

}
