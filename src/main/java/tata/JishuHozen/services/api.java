package tata.JishuHozen.services;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tata.JishuHozen.Auth.AuthController;
import tata.JishuHozen.Auth.AuthService;
import tata.JishuHozen.DTO.*;
import tata.JishuHozen.DTO.AreaResponseDTO;

import tata.JishuHozen.Entity.users;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/fetch")
@RequiredArgsConstructor
public class api
{
    private final UserService userService;

    private final MaintenanceService
            maintenanceService;

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

    @PutMapping("/maintenance/complete")
    public String completeMaintenance(
            @RequestBody
            MaintenanceCompleteDTO dto)
    {
        return maintenanceService
                .completeMaintenance(
                        dto);
    }
    @GetMapping("/areas")
    public List<AreaResponseDTO>
    getAreas()
    {
        return userService.getAreas();
    }
  /*  @GetMapping("/maintenance/logs")
    public ResponseEntity<List<MaintenanceLogDTO>>
    getMaintenanceLogs()
    {
        return ResponseEntity.ok(
                userService.getMaintenanceLogs());
    }
    @GetMapping("/audit/logs")
    public ResponseEntity<List<AuditLogDTO>>
    getAuditLogs()
    {
        return ResponseEntity.ok(
                userService.getAuditLogs());
    }*/
    @PostMapping("/done")
    public HttpStatus done(@RequestBody SendInfoDTO dto)
    {
        if(Objects.equals(dto.userId, "TEAM_LEADER"))

        return HttpStatus.OK;
        else
            return HttpStatus.BAD_REQUEST;
    }
    @PostMapping("/audit")
    public ResponseEntity<String> createAudit(
            @RequestBody AuditRequestDTO dto,
            Authentication authentication)
            throws JsonProcessingException {
        String userId = authentication.getName();

        return ResponseEntity.ok(
                userService.createAudit(
                        userId,
                        dto));
    }
}