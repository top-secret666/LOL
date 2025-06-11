package by.vstu.zamok.lab4.security;

import org.springframework.security.core.userdetails.User;
import by.vstu.zamok.lab4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository repository;

    public CustomUserDetailService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        by.vstu.zamok.lab4.entity.User userEntity = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();

        String authority = userEntity.getAuthority();
        // Убеждаемся, что роль начинается с ROLE_
        if (!authority.startsWith("ROLE_")) {
            authority = "ROLE_" + authority;
        }
        grantedAuthorities.add(new SimpleGrantedAuthority(authority));

        return new User(userEntity.getUsername(), userEntity.getPassword(), grantedAuthorities);
    }
}
