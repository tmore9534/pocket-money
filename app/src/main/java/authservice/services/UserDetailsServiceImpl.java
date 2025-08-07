package authservice.services;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import authservice.entities.UserInfo;
import authservice.model.UserInfoDto;
import authservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("could not found user..!!");
        }
        return new CustomUserDetails(user);
    }

    private UserInfo checkIfUserAlreadyExist(UserInfoDto userInfoDto) {
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto) {
        // validation of username and passsword.

        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if (Objects.nonNull(checkIfUserAlreadyExist(userInfoDto))) {
            return false;
        }
        String userId = UUID.randomUUID().toString();
        // Define roles later. (empty hashset for now)
        userRepository
                .save(new UserInfo(userId, userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<>()));
        // pushEventToQueue
        return true;
    }

}
