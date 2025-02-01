package smartcontact.manager.DAO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import smartcontact.manager.Entity.Contact;
import smartcontact.manager.Entity.User;

public interface ContactRepo  extends JpaRepository<Contact,Integer> {

//pick up all the contacts from contact table where user id is below and store it inside the list

    @Query("from Contact as c where c.user.id=:user_id")
    public Page<Contact> getContactsbyUserId(@Param("user_id")int userid,Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE from Contact c where c.id=:id AND c.user.id=:user_id")
    public void deleteByUserId(@Param("id")int id,@Param("user_id")int user_id);

     //method for search functionality
     public List<Contact> findByNameContainingAndUser(String name,User user);


}
 