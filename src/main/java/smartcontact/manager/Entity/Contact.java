package smartcontact.manager.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Contact {
    

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "Name cannot be null")
    private String name;
    private String nickname;
    private String work;
    @NotBlank(message = "Email cannot be null")
    private String email;
    private String image;
    private String description;
    private String phoneno;

    @ManyToOne
    @JsonIgnore
    private User user;

    public Contact() {
    }

    public Contact(int id, @NotBlank(message = "Name cannot be null") String name, String nickname, String work,
            @NotBlank(message = "Email cannot be null") String email,  String description, String phoneno,String image,
            User user) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.work = work;
        this.email = email;
        this.image = image;
        this.description = description;
        this.phoneno = phoneno;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    
    

}
