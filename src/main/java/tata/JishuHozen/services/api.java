package tata.JishuHozen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tata.JishuHozen.DTO.MachineDashboardDTO;
import tata.JishuHozen.DTO.UserResponseDTO;
import tata.JishuHozen.Entity.users;

import java.util.List;

@RestController
@RequestMapping("/fetch")
public class api {
@Autowired
    private  UserService userService;

    @GetMapping("/users")
    public List<UserResponseDTO> getUsers(
            @RequestParam(required = false)
            users.UserRole role) {
        return userService
                .getUsers(role);

    }
    @GetMapping("/machines")
    public List<MachineDashboardDTO>
    getDashboardMachines()
    {
        return userService.getDashboardMachines();
    }
}