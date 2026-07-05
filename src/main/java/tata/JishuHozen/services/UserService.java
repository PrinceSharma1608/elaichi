package tata.JishuHozen.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tata.JishuHozen.DTO.*;

import tata.JishuHozen.Entity.*;
import tata.JishuHozen.DTO.AreaResponseDTO;
import tata.JishuHozen.Entity.TeamLeaderJhOwnerMapping;
import tata.JishuHozen.Entity.area;
import tata.JishuHozen.Entity.machines;
import tata.JishuHozen.Entity.users;

import tata.JishuHozen.Repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService
{
    @Autowired
    private  userRepo userRepo;
    @Autowired
    private machineRepo machineRepo;
    @Autowired
    private  areaRepo areaRepo;
    @Autowired
    private teamLeaderJhOwnerMappingRepo mappingRepo;
    @Autowired
    private currentDailyMaintenanceStatusRepo currentDailyMaintenanceStatusRepo;
    @Autowired
    private  auditLogsRepo auditLogsRepo;
    @Autowired
    private maintenanceLogsRepo maintenanceLogsRepo;
    @Autowired
    private  teamLeaderJhOwnerMappingRepo teamLeaderJhOwnerMappingRepo;
    public List<DailyDashboardDTO>
    getDailyDashboard(
            String userId)
    {
        users user =
                userRepo.findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User Not Found"));

        switch(user.getUserRole())
        {
            case LINE_INCHARGE:
                return currentDailyMaintenanceStatusRepo
                        .getDailyDashboard();

            case SUPERVISOR:
                return currentDailyMaintenanceStatusRepo
                        .getSupervisorDailyDashboard(
                                userId);

            case TEAM_LEADER:
                return currentDailyMaintenanceStatusRepo
                        .getTeamLeaderDailyDashboard(
                                userId);

            case JH_OWNER:
                return currentDailyMaintenanceStatusRepo
                        .getJhOwnerDailyDashboard(
                                userId);

            default:
                throw new RuntimeException(
                        "Invalid Role");
        }

    }
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
    getDashboardMachines(
            String userId)
    {
        users user =
                userRepo.findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User Not Found"));

        switch(user.getUserRole())
        {
            case LINE_INCHARGE:
                return machineRepo
                        .getDashboardMachines();

            case SUPERVISOR:
                return machineRepo
                        .getSupervisorDashboardMachines(
                                userId);

            case TEAM_LEADER:
                return machineRepo
                        .getTeamLeaderDashboardMachines(
                                userId);

            case JH_OWNER:
                return machineRepo
                        .getJhOwnerDashboardMachines(
                                userId);

            default:
                throw new RuntimeException(
                        "Invalid Role");
        }
    }
    public String mapSupervisor(
            AreaSupervisorMappingDTO dto)
    {
        area area =
                areaRepo.findById(
                                dto.getAreaId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Area Not Found"));

        users supervisor =
                userRepo.findById(
                                dto.getSupervisorId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Supervisor Not Found"));

        if(supervisor.getUserRole()
                != users.UserRole.SUPERVISOR)
        {
            throw new RuntimeException(
                    "User Is Not A Supervisor");
        }

        if(areaRepo.existsBySupervisor_UserId(
                dto.getSupervisorId()))
        {
            throw new RuntimeException(
                    "Supervisor Already Assigned");
        }

        area.setSupervisor(
                supervisor);

        areaRepo.save(area);

        return "Supervisor Mapped Successfully";
    }

    public String mapTeamLeaderToJhOwner(
            TeamLeaderJhOwnerMappingDTO dto)
    {
        users teamLeader =
                userRepo.findById(
                                dto.getTeamLeaderId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Team Leader Not Found"));

        users jhOwner =
                userRepo.findById(
                                dto.getJhOwnerId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "JH Owner Not Found"));

        if(teamLeader.getUserRole()
                != users.UserRole.TEAM_LEADER)
        {
            throw new RuntimeException(
                    "Invalid Team Leader");
        }

        if(jhOwner.getUserRole()
                != users.UserRole.JH_OWNER)
        {
            throw new RuntimeException(
                    "Invalid JH Owner");
        }

        if(mappingRepo.existsByJhOwnerId(
                dto.getJhOwnerId()))
        {
            throw new RuntimeException(
                    "JH Owner Already Mapped");
        }

        TeamLeaderJhOwnerMapping mapping =
                new TeamLeaderJhOwnerMapping();

        mapping.setJhOwnerId(
                dto.getJhOwnerId());

        mapping.setTeamLeaderId(
                dto.getTeamLeaderId());

        mappingRepo.save(mapping);

        return "Mapping Successful";
    }
    public String mapMachineToJhOwner(
            List<MachineJhOwnerMappingRequestDTO> dtoList)
    {
        // Step 1: Pre-process to clear previous JHO mappings for machines in this batch.
        // This avoids transient 1:1 conflicts during bulk swaps/reassignments.
        for(MachineJhOwnerMappingRequestDTO dto : dtoList)
        {
            machines machine =
                    machineRepo.findById(
                                    dto.getMachineId())
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Machine Not Found: " + dto.getMachineId()));
            machine.setJhOwner(null);
            machineRepo.saveAndFlush(machine);
        }

        // Step 2: Apply new JHO assignments
        for(MachineJhOwnerMappingRequestDTO dto : dtoList)
        {
            machines machine =
                    machineRepo.findById(
                                    dto.getMachineId())
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Machine Not Found: " + dto.getMachineId()));

            // If target JHO is null, empty, or "null", it remains unassigned
            if(dto.getJhOwnerId() == null || dto.getJhOwnerId().trim().isEmpty() || dto.getJhOwnerId().equalsIgnoreCase("null"))
            {
                continue;
            }

            users jhOwner =
                    userRepo.findById(
                                    dto.getJhOwnerId())
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "JH Owner Not Found: " + dto.getJhOwnerId()));

            if(jhOwner.getUserRole()
                    != users.UserRole.JH_OWNER)
            {
                throw new RuntimeException(
                        "Invalid JH Owner : "
                                + dto.getJhOwnerId());
            }

            // Verify 1:1 constraint in DB (exclude the current machine)
            machines existingMachine =
                    machineRepo.findByJhOwner_UserId(
                            dto.getJhOwnerId());

            if(existingMachine != null && !existingMachine.getMachineId().equals(machine.getMachineId()))
            {
                throw new RuntimeException(
                        "JH Owner Already Assigned : "
                                + dto.getJhOwnerId());
            }

            machine.setJhOwner(jhOwner);
            machineRepo.save(machine);
        }

        return "Mapping Successful";
    }
    public List<AreaResponseDTO> getAreas()
    {
        return areaRepo.findAll()
                .stream()
                .map(area ->
                        AreaResponseDTO.builder()
                                .areaId(
                                        area.getAreaId())
                                .areaName(
                                        area.getAreaName())
                                .supervisorId(
                                        area.getSupervisor() != null
                                                ? area.getSupervisor().getUserId()
                                                : null)
                                .supervisorName(
                                        area.getSupervisor() != null
                                                ? area.getSupervisor().getUserName()
                                                : null)
                                .build())
                .toList();
    }
    @Transactional
    public String createAudit(
            String userId,
            AuditRequestDTO dto)
    {
        users auditor =
                userRepo.findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Auditor Not Found"));

        if(auditor.getUserRole()
                != users.UserRole.SUPERVISOR
                &&
                auditor.getUserRole()
                        != users.UserRole.LINE_INCHARGE)
        {
            throw new RuntimeException(
                    "Only Supervisor and Line Incharge can audit");
        }

        machines machine =
                machineRepo.findById(
                                dto.getMachineId().toUpperCase())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Machine Not Found"));

        String checklistJson;
        try {
            checklistJson = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(dto.getChecklist());
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize checklist map to JSON", e);
        }

        AuditLogs audit =
                AuditLogs.builder()
                        .machine(machine)
                        .auditedBy(auditor)
                        .auditDate(
                                LocalDateTime.now())
                        .checklist(checklistJson)
                        .findings(
                                dto.getFindings())
                        .build();

        auditLogsRepo.save(audit);
        CurrentDailyMaintenanceStatus cdms = currentDailyMaintenanceStatusRepo.findById(dto.getMachineId().toUpperCase()).orElseThrow(() -> new RuntimeException("Machine Not Found"));
                    cdms.setAudited(true);
        return "Audit Saved Successfully";
    }

    @Transactional
    public String completeMaintenance(
            String userId,
            String role,
            MaintenanceCompletionDTO dto)
            throws JsonProcessingException
    {
        if(!role.equals("JH_OWNER")
                &&
                !role.equals("TEAM_LEADER"))
        {
            throw new RuntimeException(
                    "Unauthorized");
        }

        users user =
                userRepo.findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User Not Found"));

        machines machine =
                machineRepo.findById(
                                dto.getMachineId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Machine Not Found"));

        CurrentDailyMaintenanceStatus cdms =
                currentDailyMaintenanceStatusRepo
                        .findById(
                                dto.getMachineId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "No Maintenance Scheduled"));

    /*
        JH can only do his machine
     */
        if(role.equals("JH_OWNER"))
        {
            if(machine.getJhOwner() == null
                    ||
                    !machine.getJhOwner()
                            .getUserId()
                            .equals(userId))
            {
                throw new RuntimeException(
                        "Unauthorized");
            }
        }

    /*
        TL can only do mapped JH machines
     */
        if(role.equals("TEAM_LEADER"))
        {
            boolean allowed =
                    teamLeaderJhOwnerMappingRepo
                            .existsByJhOwnerIdAndTeamLeaderId(
                                    machine.getJhOwner()
                                            .getUserId(),
                                    userId);

            if(!allowed)
            {
                throw new RuntimeException(
                        "Unauthorized");
            }
        }

        ObjectMapper mapper =
                new ObjectMapper();

        String checklistJson =
                mapper.writeValueAsString(
                        dto.getChecklist());

        MaintenanceLogs.CompletionType
                completionType;

    /*
        TL is always manual.
     */
        if(role.equals("TEAM_LEADER"))
        {
            completionType =
                    MaintenanceLogs
                            .CompletionType
                            .DONE_MANUALLY;

            cdms.setMaintenanceStatus(
                    CurrentDailyMaintenanceStatus
                            .MaintenanceStatus
                            .DONE_MANUALLY);
        }
    /*
        If machine already MISSED,
        then completion is manual.
     */
        else if(
                cdms.getMaintenanceStatus()
                        ==
                        CurrentDailyMaintenanceStatus
                                .MaintenanceStatus
                                .MISSED)
        {
            completionType =
                    MaintenanceLogs
                            .CompletionType
                            .DONE_MANUALLY;

            cdms.setMaintenanceStatus(
                    CurrentDailyMaintenanceStatus
                            .MaintenanceStatus
                            .DONE_MANUALLY);
        }
    /*
        Normal completion.
     */
        else
        {
            completionType =
                    MaintenanceLogs
                            .CompletionType
                            .COMPLETED;

            cdms.setMaintenanceStatus(
                    CurrentDailyMaintenanceStatus
                            .MaintenanceStatus
                            .COMPLETED);
        }

        machine.setDelayCount(0);

        machine.setLastMaintenanceDate(
                LocalDate.now());

        cdms.setCompletedBy(user);

        MaintenanceLogs log =
                MaintenanceLogs.builder()
                        .machine(machine)
                        .performedBy(user)
                        .maintenanceDate(
                                LocalDateTime.now())
                        .checklist(
                                checklistJson)
                        .remarks(
                                dto.getRemarks())
                        .completionType(
                                completionType)
                        .build();

        maintenanceLogsRepo.save(log);

        machineRepo.save(machine);

        currentDailyMaintenanceStatusRepo
                .save(cdms);

        return
                "Maintenance Completed Successfully";
    }
}

