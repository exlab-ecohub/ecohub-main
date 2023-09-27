package team.exlab.ecohub.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.exlab.ecohub.exception.AdminNotFoundException;
import team.exlab.ecohub.user.UserMapper;
import team.exlab.ecohub.user.dto.AdminDto;
import team.exlab.ecohub.user.dto.PasswordChangeDto;
import team.exlab.ecohub.user.model.ERole;
import team.exlab.ecohub.user.model.User;
import team.exlab.ecohub.user.repository.RoleRepository;
import team.exlab.ecohub.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(usernameOrEmail).orElseGet(
                () -> userRepository.findUserByEmail(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                String.format("User with username %s not found", usernameOrEmail))));
    }

    @Override
    @Transactional
    public List<AdminDto> getAllAdmins() {
        return userRepository.findUsersByRoleId(roleRepository
                        .findRoleByName(ERole.ROLE_ADMIN)
                        .orElseThrow()
                        .getId())
                .stream()
                .map(UserMapper::toAdminDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdminDto getAdminById(Long adminId) {
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));
        if (!user.isAdmin()) {
            throw new AdminNotFoundException(adminId);
        }
        return UserMapper.toAdminDto(user);
    }

    @Override
    @Transactional
    public void changeAdminPassword(Long adminId, PasswordChangeDto passwordDto) {
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));
        if (!user.isAdmin()) {
            throw new AdminNotFoundException(adminId);
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteAdmin(Long adminId) {
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));
        if (!user.isAdmin()) {
            throw new AdminNotFoundException(adminId);
        }
        userRepository.deleteById(adminId);
    }

    @Override
    @Transactional
    public void blockAdmin(Long adminId) {
        User user = userRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException(adminId));
        if (!user.isAdmin()) {
            throw new AdminNotFoundException(adminId);
        }
        user.setLockEndTime(LocalDateTime.now().plus(100, ChronoUnit.YEARS));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unblockAdmin(Long adminId) {
        User user = userRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException(adminId));
        if (!user.isAdmin()) {
            throw new AdminNotFoundException(adminId);
        }
        user.setLockEndTime(LocalDateTime.now().minus(1, ChronoUnit.HOURS));
        user.setBlockedBefore(false);
        user.setPassAttempts(3);
        userRepository.save(user);
    }
}
