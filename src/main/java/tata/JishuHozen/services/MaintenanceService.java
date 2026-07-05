package tata.JishuHozen.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tata.JishuHozen.DTO.MaintenanceCompletionDTO;
import tata.JishuHozen.DTO.MaintenanceCompletionDTO;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatus;
import tata.JishuHozen.Entity.MaintenanceLogs;
import tata.JishuHozen.Entity.machines;
import tata.JishuHozen.Entity.users;
import tata.JishuHozen.Repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MaintenanceService
{
    private final currentDailyMaintenanceStatusRepo statusRepo;

    private final machineRepo machineRepo;

    private final maintenanceLogsRepo logsRepo;
    private  final teamLeaderJhOwnerMappingRepo teamLeaderJhOwnerMappingRepo;


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
                                () -> new RuntimeException(
                                        "User Not Found"));

        machines machine =
                machineRepo.findById(
                                dto.getMachineId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Machine Not Found"));

        CurrentDailyMaintenanceStatus cdms =
                statusRepo
                        .findById(
                                dto.getMachineId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "No Maintenance Scheduled"));

    /*
        JH can only do his own machine
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
        TL can only do mapped JH machines
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
        TL is always manual
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
        If already MISSED,
        completion becomes manual.
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

        statusRepo
                .save(cdms);

        machineRepo.save(machine);

        logsRepo.save(log);

        return "Maintenance Completed Successfully";
    }
}