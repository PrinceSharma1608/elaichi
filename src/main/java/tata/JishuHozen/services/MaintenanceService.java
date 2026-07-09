package tata.JishuHozen.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tata.JishuHozen.DTO.MaintenanceChecklistItemDTO;
import tata.JishuHozen.DTO.MaintenanceCompletionDTO;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatus;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatusId;
import tata.JishuHozen.Entity.MaintenanceLogs;
import tata.JishuHozen.Entity.machines;
import tata.JishuHozen.Entity.users;
import tata.JishuHozen.Repository.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import tata.JishuHozen.Entity.MachineChecklist;
import tata.JishuHozen.Entity.MachineChecklistId;
import tata.JishuHozen.Repository.machineChecklistRepo;
@Service
@RequiredArgsConstructor
public class MaintenanceService
{
    private final currentDailyMaintenanceStatusRepo statusRepo;

    private final machineRepo machineRepo;
    private final machineChecklistRepo
            machineChecklistRepo;
    private final maintenanceLogsRepo logsRepo;

    private final teamLeaderJhOwnerMappingRepo
            teamLeaderJhOwnerMappingRepo;

    private final userRepo userRepo;

    @Transactional
    public String completeMaintenance(
            String userId,
            String role,
            MaintenanceCompletionDTO dto)
            throws JsonProcessingException
    {
        if (!role.equals("JH_OWNER")
                &&
                !role.equals("TEAM_LEADER"))
        {
            throw new RuntimeException(
                    "Only JH Owner or Team Leader can complete maintenance");
        }

        users user =
                userRepo.findById(userId)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "User Not Found"));

        machines machine =
                machineRepo.findById(
                                dto.getMachineId())
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Machine Not Found"));

        CurrentDailyMaintenanceStatusId id =
                new CurrentDailyMaintenanceStatusId(
                        dto.getMachineId(),
                        dto.getFrequencyDays());

        CurrentDailyMaintenanceStatus cdms =
                statusRepo.findById(id)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "No Maintenance Scheduled"));

        /*
            JH can only do his own machine.
         */
        if (role.equals("JH_OWNER"))
        {
            if (machine.getJhOwner() == null
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
            TL can only do mapped machines.
         */
        if (role.equals("TEAM_LEADER"))
        {
            if (machine.getJhOwner() == null)
            {
                throw new RuntimeException(
                        "Machine has no JH Owner");
            }

            boolean allowed =
                    teamLeaderJhOwnerMappingRepo
                            .existsByJhOwnerIdAndTeamLeaderId(
                                    machine.getJhOwner()
                                            .getUserId(),
                                    userId);

            if (!allowed)
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

        MaintenanceLogs.CompletionType completionType;

        /*
            Team Leader is always manual.
         */
        if (role.equals("TEAM_LEADER"))
        {
            completionType =
                    MaintenanceLogs.CompletionType
                            .DONE_MANUALLY;

            cdms.setMaintenanceStatus(
                    CurrentDailyMaintenanceStatus
                            .MaintenanceStatus
                            .DONE_MANUALLY);
        }
        /*
            Completing a missed task is manual.
         */
        else if (
                cdms.getMaintenanceStatus()
                        ==
                        CurrentDailyMaintenanceStatus
                                .MaintenanceStatus
                                .MISSED)
        {
            completionType =
                    MaintenanceLogs.CompletionType
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
                    MaintenanceLogs.CompletionType
                            .COMPLETED;

            cdms.setMaintenanceStatus(
                    CurrentDailyMaintenanceStatus
                            .MaintenanceStatus
                            .COMPLETED);
        }


        /*
            Update machine flag.
         */
        machine.setFlag(
                calculateFlag(
                        dto.getChecklist()));

        cdms.setCompletedBy(user);

        cdms.setChecklist(
                checklistJson);

        cdms.setUpdatedAt(LocalDateTime.now());
        cdms.setRemarks(dto.getRemarks());

        MaintenanceLogs log =
                MaintenanceLogs.builder()
                        .machine(machine)
                        .frequencyDays(
                                dto.getFrequencyDays())
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

        statusRepo.save(cdms);

        machineRepo.save(machine);

        logsRepo.save(log);

        MachineChecklistId checklistId =
                new MachineChecklistId(
                        dto.getMachineId(),
                        dto.getFrequencyDays());

        MachineChecklist checklist =
                machineChecklistRepo
                        .findById(
                                checklistId)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Checklist Not Found"));

        if (checklist.getDelayCount() != null && checklist.getDelayCount() > 0)
        {
            checklist.setDelayCount(checklist.getDelayCount() - 1);
        }

        checklist.setLastCompletedDate(
                LocalDate.now());

        machineChecklistRepo.save(
                checklist);

        return "Maintenance Completed Successfully";
    }

    private machines.Flag
    calculateFlag(
            List<MaintenanceChecklistItemDTO>
                    checklist)
    {
        long ok =
                checklist.stream()
                        .filter(
                                x ->
                                        x.getStatus()
                                                .equals(
                                                        "OK"))
                        .count();

        long green =
                checklist.stream()
                        .filter(
                                x ->
                                        x.getStatus()
                                                .equals(
                                                        "GREEN"))
                        .count();

        long red =
                checklist.stream()
                        .filter(
                                x ->
                                        x.getStatus()
                                                .equals(
                                                        "RED"))
                        .count();

        /*
            Priority:
            OK > GREEN > RED
         */
        if (ok >= green
                &&
                ok >= red)
        {
            return machines.Flag.OK;
        }

        if (green >= red)
        {
            return machines.Flag.GREEN;
        }

        return machines.Flag.RED;
    }
}