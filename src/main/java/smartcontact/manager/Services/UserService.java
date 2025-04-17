package smartcontact.manager.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import smartcontact.manager.DAO.ContactRepo;
import smartcontact.manager.DAO.CustomerRepo;
import smartcontact.manager.DAO.UserRepo;
import smartcontact.manager.Entity.Contact;
import smartcontact.manager.Entity.CustomerData;
import smartcontact.manager.Entity.User;

@Service
public class UserService {
    

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ContactRepo contactRepo;

    @Autowired
    private CustomerRepo customerRepo;

    public boolean saveobj(User u)
    {

      userRepo.save(u);
     

      return true;
         
    }

    public boolean verifyobj(String email)
    {
       User user= userRepo.getUser(email);
       if(user==null)
       {
        return false;
       }

       return true;

    }

    public boolean saveContact(String username,Contact contact,MultipartFile file)
    {
       User user=userRepo.getUser(username);
       if(user!=null)
       { 
      
        contact.setUser(user);
       user.getContacts().add(contact);
       userRepo.save(user);
       }
       return true;
       
    }

   public Page<Contact> getAllContacts(User u,Pageable pageable)
   {
     
        int userid=u.getId();
       
       return contactRepo.getContactsbyUserId(userid,pageable);

      
   }

   public boolean deleteContact(int userId,int ContactId)
   {
      Optional<Contact> contact=contactRepo.findById(ContactId);
      Contact c=contact.get();

      //unlinking the user , so that contact can be deleted because of fetch type

      
      if(userId==c.getUser().getId())
      {
      c.setUser(null);
      contactRepo.delete(c);
      return true;
      }
      
      return false;
   }

   public Contact getSingle(int id)
   {
     Optional<Contact> contact=contactRepo.findById(id);
     
     return contact.get();
   }

public boolean saveCustomerData(CustomerData cData) {
    // TODO Auto-generated method stub

    CustomerData customerData= customerRepo.save(cData);
    if(customerData!=null)
    {
      return true;
    }

    return false;

}

public boolean verifyCustomerData(int id) {
   
   

  return customerRepo.findUserById(id);
}

public String getCustomerId(int id) {

    return customerRepo.findCustomerIdByUserId(id);
}

   
}
