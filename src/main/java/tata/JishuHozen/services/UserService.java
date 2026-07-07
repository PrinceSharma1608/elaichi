package tata.JishuHozen.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tata.JishuHozen.DTO.*;

import tata.JishuHozen.Entity.*;
import tata.JishuHozen.DTO .AreaResponseDTO;
import tata.JishuHozen.Entity.TeamLeaderJhOwnerMapping;
import tata.JishuHozen.Entity.area;
import tata.JishuHozen.Entity.machines;
import tata.JishuHozen.Entity.users;

import tata.JishuHozen.Repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private  machineChecklistRepo machineChecklistRepo;
    @Autowired
    private maintenanceLogsRepo maintenanceLogsRepo;
    @Autowired
    private  teamLeaderJhOwnerMappingRepo teamLeaderJhOwnerMappingRepo;
    @Autowired
    private MaintenanceService maintenanceService;
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

    @Transactional
    public String createTeamLeaderMapping(
            TeamLeaderJhOwnerMappingDTO dto)
    {
        users teamLeader =
                userRepo.findById(
                                dto.getTeamLeaderId())
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Team Leader Not Found"));

        if(teamLeader.getUserRole()
                != users.UserRole.TEAM_LEADER)
        {
            throw new RuntimeException(
                    "User is not a Team Leader");
        }

        // 1. Delete all existing mappings for this Team Leader
        List<TeamLeaderJhOwnerMapping> existingMappings =
                teamLeaderJhOwnerMappingRepo.findByTeamLeaderId(dto.getTeamLeaderId());
        if (existingMappings != null && !existingMappings.isEmpty()) {
            teamLeaderJhOwnerMappingRepo.deleteAll(existingMappings);
            teamLeaderJhOwnerMappingRepo.flush();
        }

        // 2. Save the new mappings
        for(String jhId :
                dto.getJhOwnerIds())
        {
            users jhOwner =
                    userRepo.findById(jhId)
                            .orElseThrow(
                                    () ->
                                            new RuntimeException(
                                                    "JH Owner Not Found : "
                                                            + jhId));

            if(jhOwner.getUserRole()
                    != users.UserRole.JH_OWNER)
            {
                throw new RuntimeException(
                        jhId +
                                " is not a JH Owner");
            }

            if(teamLeaderJhOwnerMappingRepo
                    .existsByJhOwnerId(
                            jhId))
            {
                throw new RuntimeException(
                        "JH Owner "
                                + jhId
                                + " is already mapped");
            }

            TeamLeaderJhOwnerMapping
                    mapping =
                    TeamLeaderJhOwnerMapping
                            .builder()
                            .jhOwnerId(
                                    jhId)
                            .teamLeaderId(
                                    dto.getTeamLeaderId())
                            .build();

            teamLeaderJhOwnerMappingRepo
                    .save(mapping);
        }

        return "Mapping created successfully";
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
                                () ->
                                        new RuntimeException(
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
                                dto.getMachineId()
                                        .toUpperCase())
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Machine Not Found"));

        String checklistJson;

        try
        {
            checklistJson =
                    new ObjectMapper()
                            .writeValueAsString(
                                    dto.getChecklist());
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(
                    "Checklist Serialization Failed");
        }

        CurrentDailyMaintenanceStatusId id =
                new CurrentDailyMaintenanceStatusId(
                        dto.getMachineId(),
                        dto.getFrequencyDays());

        currentDailyMaintenanceStatusRepo
                .findById(id)
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "Maintenance Task Not Found"));

        AuditLogs audit =
                AuditLogs.builder()
                        .machine(machine)
                        .frequencyDays(
                                dto.getFrequencyDays())
                        .auditedBy(auditor)
                        .auditDate(
                                LocalDateTime.now())
                        .checklist(
                                checklistJson)
                        .findings(
                                dto.getFindings())
                        .build();

        auditLogsRepo.save(audit);

        return "Audit Saved Successfully";
    }
    @Transactional
    public String completeMaintenance(
            String userId,
            String role,
            MaintenanceCompletionDTO dto)
            throws JsonProcessingException
    {
        return maintenanceService
                .completeMaintenance(
                        userId,
                        role,
                        dto);
    }
    public List<TeamLeaderJhOwnerMapping> getTlJhoMappings() {
        return teamLeaderJhOwnerMappingRepo.findAll();
    }
    @Transactional
    public String updateMachineConfiguration(
            UpdateMachineConfigurationDTO dto)
    {
        machines machine =
                machineRepo.findById(
                                dto.getMachineId())
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Machine Not Found"));

    /*
        Update JH Owner
     */
        if(dto.getJhOwnerId() != null)
        {
            users jhOwner =
                    userRepo.findById(
                                    dto.getJhOwnerId())
                            .orElseThrow(
                                    () ->
                                            new RuntimeException(
                                                    "JH Owner Not Found"));

            if(jhOwner.getUserRole()
                    != users.UserRole.JH_OWNER)
            {
                throw new RuntimeException(
                        "User is not a JH Owner");
            }

            Optional<machines>
                    assignedMachine =
                    machineRepo.findByJhOwner(
                            jhOwner);

            if(assignedMachine.isPresent()
                    &&
                    !assignedMachine.get()
                            .getMachineId()
                            .equals(
                                    machine.getMachineId()))
            {
                throw new RuntimeException(
                        "JH Owner already assigned to another machine");
            }

            machine.setJhOwner(
                    jhOwner);
        }

    /*
        Update Frequency
     */
        if(dto.getMaintenanceFrequencyDays()
                != null)
        {
            machine.setMaintenanceFrequencyDays(
                    dto.getMaintenanceFrequencyDays());
        }

    /*
        Update Subarea
     */
        if(dto.getSubarea() != null)
        {
            machine.setSubarea(
                    dto.getSubarea());
        }

    /*
        Update Machine Status
     */
        if(dto.getMachineStatus() != null)
        {
            machine.setMachineStatus(
                    machines.MachineStatus
                            .valueOf(
                                    dto.getMachineStatus()));
        }

    /*
        Update Flag
     */
        if(dto.getFlag() != null)
        {
            machine.setFlag(
                    machines.Flag
                            .valueOf(
                                    dto.getFlag()));
        }

    /*
        Recalculate Next Maintenance Date
     */

        LocalDate nextDate =
                machine.getLastMaintenanceDate()
                        .plusDays(
                                machine.getMaintenanceFrequencyDays());

        while(nextDate.isBefore(
                LocalDate.now()))
        {
            nextDate =
                    nextDate.plusDays(
                            machine.getMaintenanceFrequencyDays());
        }

        machine.setNextMaintenanceDate(
                nextDate);

        machineRepo.save(machine);

        return
                "Machine Configuration Updated Successfully";
    }
    @Transactional(readOnly = true)
    public List<MaintenanceLogResponseDTO>
    getAllMaintenanceLogs()
    {
        return maintenanceLogsRepo
                .findAll()
                .stream()
                .map(log ->
                        MaintenanceLogResponseDTO
                                .builder()
                                .logId(
                                        log.getLogId())
                                .machineId(
                                        log.getMachine()
                                                .getMachineId())
                                .machineName(
                                        log.getMachine()
                                                .getMachineName())
                                .frequencyDays(
                                        log.getFrequencyDays())
                                .performedById(
                                        log.getPerformedBy() != null ?
                                                log.getPerformedBy().getUserId() : null)
                                .performedByName(
                                        log.getPerformedBy() != null ?
                                                log.getPerformedBy().getUserName() : "Unknown")
                                .maintenanceDate(
                                        log.getMaintenanceDate())
                                .checklist(
                                        log.getChecklist())
                                .remarks(
                                        log.getRemarks())
                                .completionType(
                                        log.getCompletionType()
                                                .name())
                                .build())
                .toList();
    }
    @Transactional(readOnly = true)
    public List<AuditLogResponseDTO>
    getAllAuditLogs()
    {
        return auditLogsRepo
                .findAll()
                .stream()
                .map(log ->
                        AuditLogResponseDTO
                                .builder()
                                .auditId(
                                        log.getAuditId())
                                .machineId(
                                        log.getMachine()
                                                .getMachineId())
                                .machineName(
                                        log.getMachine()
                                                .getMachineName())
                                .frequencyDays(
                                        log.getFrequencyDays())
                                .auditorId(
                                        log.getAuditedBy() != null ?
                                                log.getAuditedBy().getUserId() : null)
                                .auditorName(
                                        log.getAuditedBy() != null ?
                                                log.getAuditedBy().getUserName() : "Unknown")
                                .auditDate(
                                        log.getAuditDate())
                                .checklist(
                                        log.getChecklist())
                                .findings(
                                        log.getFindings())
                                .build())
                .toList();
    }

    @Transactional
    public String createChecklist(
            String userId,
            String role,
            MachineChecklistDTO dto)
            throws JsonProcessingException
    {
        machines machine =
                machineRepo.findById(
                                dto.getMachineId())
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Machine Not Found"));

    /*
        Supervisor can only
        create checklist for
        machines in his area.
     */
        if(role.equals("SUPERVISOR"))
        {
            if(machine.getArea()
                    .getSupervisor()
                    == null
                    ||
                    !machine.getArea()
                            .getSupervisor()
                            .getUserId()
                            .equals(userId))
            {
                throw new RuntimeException(
                        "Unauthorized");
            }
        }

        if(machineChecklistRepo
                .existsByMachineIdAndFrequencyDays(
                        dto.getMachineId(),
                        dto.getFrequencyDays()))
        {
            throw new RuntimeException(
                    "Checklist already exists for this frequency");
        }

        ObjectMapper mapper =
                new ObjectMapper();

        String checklistJson =
                mapper.writeValueAsString(
                        dto.getChecklist());

        LocalDate today =
                LocalDate.now();

        MachineChecklist checklist =
                MachineChecklist.builder()
                        .machineId(
                                dto.getMachineId())
                        .frequencyDays(
                                dto.getFrequencyDays())
                        .lastCompletedDate(
                                today)
                        .nextDueDate(
                                today.plusDays(
                                        dto.getFrequencyDays()))
                        .checklist(
                                checklistJson)
                        .build();

        machineChecklistRepo
                .save(checklist);

        return
                "Checklist Created Successfully";
    }
}

