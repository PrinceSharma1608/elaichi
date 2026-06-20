package tata.JishuHozen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tata.JishuHozen.DTO.*;
<<<<<<< HEAD
import tata.JishuHozen.Entity.DashboardStatsDTO;
=======
>>>>>>> 9c1d04c2262df358ef5619b7a80a261ed532b826
import tata.JishuHozen.Entity.users;

import java.util.List;

@RestController
@RequestMapping("/fetch")
@RequiredArgsConstructor
public class api
{
    private final UserService userService;
<<<<<<< HEAD
    private final MaintenanceService
            maintenanceService;
=======

>>>>>>> 9c1d04c2262df358ef5619b7a80a261ed532b826
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
    @GetMapping("/daily-dashboard")
    public List<DailyDashboardDTO>
    getDailyDashboard(
            @RequestParam
            String userId)
    {
        return userService
                .getDailyDashboard(
                        userId);
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
<<<<<<< HEAD
    @PutMapping("/maintenance/complete")
    public String completeMaintenance(
            @RequestBody
            MaintenanceCompleteDTO dto)
    {
        return maintenanceService
                .completeMaintenance(
                        dto);
    }
    @GetMapping("/dashboard-stats")
    public DashboardStatsDTO
    getDashboardStats()
    {
        return userService
                .getDashboardStats();
    }
=======
>>>>>>> 9c1d04c2262df358ef5619b7a80a261ed532b826
}