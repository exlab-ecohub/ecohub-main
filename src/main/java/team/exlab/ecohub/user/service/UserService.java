package team.exlab.ecohub.user.service;

import team.exlab.ecohub.user.dto.AdminDto;
import team.exlab.ecohub.user.dto.PasswordChangeDto;

import java.util.List;

public interface UserService {

    List<AdminDto> getAllAdmins();

    AdminDto getAdminById(Long adminId);

    void deleteAdmin(Long adminId);

    void blockAdmin(Long adminId);

    void unblockAdmin(Long adminId);

    void changeAdminPassword(Long adminId, PasswordChangeDto passwordDto);
}
