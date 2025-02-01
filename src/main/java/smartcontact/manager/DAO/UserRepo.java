package smartcontact.manager.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import smartcontact.manager.Entity.User;

@Repository
public interface UserRepo extends JpaRepository<User,Integer>{
    

    @Query("select u from User u where u.email=:n")
    public User getUser(@Param("n")String email);

}
