package smartcontact.manager.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import smartcontact.manager.Entity.CustomerData;

public interface CustomerRepo extends JpaRepository<CustomerData,String>{
 
    @Query("select count(c) > 0 from CustomerData as c where c.User_id = :n")
    boolean findUserById(@Param("n") int id);

    @Query("SELECT c.Customer_Id from CustomerData c where c.User_id=:id")
    public String findCustomerIdByUserId(@Param("id")int id);
}
