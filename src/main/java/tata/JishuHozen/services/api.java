package tata.JishuHozen.services;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tata.JishuHozen.Auth.AuthController;
import tata.JishuHozen.Auth.AuthService;
import tata.JishuHozen.Auth.JwtHelper;
import tata.JishuHozen.Auth.JwtUtil;
import tata.JishuHozen.DTO.*;
import tata.JishuHozen.DTO.AreaResponseDTO;

import tata.JishuHozen.Entity.users;
import tata.JishuHozen.Entity.TeamLeaderJhOwnerMapping;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/fetch")
@RequiredArgsConstructor
public class api
{
    private final UserService userService;
    private final JwtHelper jwtHelper;
    private final MaintenanceService maintenanceService;
    private final JwtUtil jwtUtil;

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
                .createTeamLeaderMapping(dto);
    }

    @GetMapping("/tl-jhoMap")
    public List<TeamLeaderJhOwnerMapping> getTlJhoMappings()
    {
        return userService.getTlJhoMappings();
    }

    @PutMapping("/machine-jhoMap")
    public String mapMachineToJhOwner(@RequestBody
            List<MachineJhOwnerMappingRequestDTO> dtoList)
    {
        return userService
                .mapMachineToJhOwner(dtoList);
    }

    @GetMapping("/areas")
    public List<AreaResponseDTO>
    getAreas()
    {
        return userService.getAreas();
    }
  /* @GetMapping("/maintenance/logs")
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

    @PostMapping("/audit")
    public ResponseEntity<String> createAudit(
            @RequestBody AuditRequestDTO dto,
            @RequestHeader("Authorization") String authorizationHeader)
            throws JsonProcessingException {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }
        String token = authorizationHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(token);

        return ResponseEntity.ok(
                userService.createAudit(
                        userId,
                        dto));
    }
    @PostMapping("/maintenance/complete")
    public ResponseEntity<String>
    completeMaintenance(
            @RequestBody
            MaintenanceCompletionDTO dto,
            HttpServletRequest request)
            throws JsonProcessingException
    {
        String userId =
                jwtHelper.getUserId(request);

        String role =
                jwtHelper.getRole(request);

        return ResponseEntity.ok(
                userService.completeMaintenance(
                        userId,
                        role,
                        dto
                )
        );
    }
    @PutMapping("/machine/configuration")
    public ResponseEntity<String>
    updateMachineConfiguration(
            @RequestBody
            UpdateMachineConfigurationDTO dto,
            HttpServletRequest request)
    {
        String role =
                jwtHelper.getRole(request);

        if(!role.equals("LINE_INCHARGE"))
        {
            throw new RuntimeException(
                    "Unauthorized");
        }

        return ResponseEntity.ok(
                userService
                        .updateMachineConfiguration(
                                dto));
    }
}