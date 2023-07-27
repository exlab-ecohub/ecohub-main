package team.exlab.ecohub.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.exlab.ecohub.user.AuthUtils;
import team.exlab.ecohub.user.UserMapper;
import team.exlab.ecohub.user.dto.UserDto;
import team.exlab.ecohub.user.model.User;
import team.exlab.ecohub.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getCurrentUser() {
        User user = userRepository.findById(AuthUtils.getCurrentUserId()).get();
        return UserMapper.toUserDto(user);
    }
}
