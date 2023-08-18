package team.exlab.ecohub.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.exlab.ecohub.exception.UserNotFoundException;
import team.exlab.ecohub.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(usernameOrEmail).orElseGet(
                () -> userRepository.findUserByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UserNotFoundException(usernameOrEmail)));
    }
}
