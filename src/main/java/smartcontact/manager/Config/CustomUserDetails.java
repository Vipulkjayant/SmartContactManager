package smartcontact.manager.Config;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import smartcontact.manager.Entity.User;

public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return a single GrantedAuthority based on the user's role
        return List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole())); // Use the role directly
    }
    
    @Override
    public String getPassword() {
        return user.getPassword(); // Return user's hashed password
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Return user's email as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account is not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials are not expired
    }

 
}
