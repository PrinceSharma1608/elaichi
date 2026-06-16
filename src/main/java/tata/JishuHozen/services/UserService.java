package tata.JishuHozen.services;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tata.JishuHozen.DTO.MachineDashboardDTO;
import tata.JishuHozen.DTO.UserResponseDTO;
import tata.JishuHozen.Entity.users;
import tata.JishuHozen.Repository.machineRepo;
import tata.JishuHozen.Repository.userRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService
{
    @Autowired
    private final userRepo userRepo;
    @Autowired
    private machineRepo machineRepo;

    public List<UserResponseDTO> getUsers(
            users.UserRole role)
    {
        List<users> userList;

        if(role == null)
        {
            userList = userRepo.findAll();
        }
        else
        {
            userList =
                    userRepo.findByUserRole(role);
        }

        return userList.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private UserResponseDTO convertToDTO(
            users user)
    {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userRole(user.getUserRole())
                .build();
    }


    public List<MachineDashboardDTO>
    getDashboardMachines()
    {
        return machineRepo.getDashboardMachines();
    }
}