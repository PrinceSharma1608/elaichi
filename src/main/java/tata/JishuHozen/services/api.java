package tata.JishuHozen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tata.JishuHozen.DTO.*;
import tata.JishuHozen.Entity.users;

import java.util.List;

@RestController
@RequestMapping("/fetch")
@RequiredArgsConstructor
public class api
{
    private final UserService userService;

    @GetMapping("/users")
    public List<UserResponseDTO> getUsers(
            @RequestParam(required = false)
            users.UserRole role)
    {
        return userService.getUsers(role);
    }

    @GetMapping("/machines")
    public List<MachineDashboardDTO>
    getDashboardMachines(@RequestParam  String userId)
    {
        return userService.getDashboardMachines(userId);
    }
    @PutMapping("/a-sMap")
    public String mapSupervisor(
            @RequestBody AreaSupervisorMappingDTO dto)
    {
        return userService.mapSupervisor(dto);
    }
    @PutMapping("/tl-jhoMap")
    public String mapTeamLeaderToJhOwner(
            @RequestBody
            TeamLeaderJhOwnerMappingDTO dto)
    {
        return userService
                .mapTeamLeaderToJhOwner(dto);
    }
    @PutMapping("/machine-jhoMap")
    public String mapMachineToJhOwner(@RequestBody
            List<MachineJhOwnerMappingRequestDTO> dtoList)
    {
        return userService
                .mapMachineToJhOwner(dtoList);
    }
}