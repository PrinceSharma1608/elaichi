package tata.JishuHozen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tata.JishuHozen.DTO.MaintenanceCompleteDTO;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatus;
import tata.JishuHozen.Entity.MaintenanceLogs;
import tata.JishuHozen.Entity.machines;
import tata.JishuHozen.Entity.users;
import tata.JishuHozen.Repository.currentDailyMaintenanceStatusRepo;
import tata.JishuHozen.Repository.machineRepo;
import tata.JishuHozen.Repository.maintenanceLogsRepo;
import tata.JishuHozen.Repository.userRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MaintenanceService
{
    private final currentDailyMaintenanceStatusRepo statusRepo;

    private final machineRepo machineRepo;

    private final maintenanceLogsRepo logsRepo;

    private final userRepo userRepo;

    @Transactional
    public String completeMaintenance(
            MaintenanceCompleteDTO dto)
    {
        users jho =
                userRepo.findById(
                                dto.getUserId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User Not Found"));

        if(jho.getUserRole()
                != users.UserRole.JH_OWNER)
        {
            throw new RuntimeException(
                    "Only JH Owner Can Complete Maintenance");
        }

        machines machine =
                machineRepo.findById(
                                dto.getMachineId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Machine Not Found"));

        if(machine.getJhOwner() == null)
        {
            throw new RuntimeException(
                    "Machine Not Assigned");
        }

        if(!machine.getJhOwner()
                .getUserId()
                .equals(dto.getUserId()))
        {
            throw new RuntimeException(
                    "Machine Does Not Belong To User");
        }

        CurrentDailyMaintenanceStatus status =
                statusRepo.findByMachine_MachineId(
                                dto.getMachineId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Maintenance Task Not Found"));

        if(status.getMaintenanceStatus()
                != CurrentDailyMaintenanceStatus
                .MaintenanceStatus.PENDING)
        {
            throw new RuntimeException(
                    "Maintenance Window Closed");
        }

        status.setMaintenanceStatus(
                CurrentDailyMaintenanceStatus
                        .MaintenanceStatus.COMPLETED);

        status.setCompletedBy(
                jho);

        status.setUpdatedAt(
                LocalDateTime.now());

        machine.setDelayCount(
                0);

        machine.setLastMaintenanceDate(
                LocalDate.now());

        machine.setNextMaintenanceDate(
                LocalDate.now()
                        .plusDays(
                                machine
                                        .getMaintenanceFrequencyDays()));

        MaintenanceLogs log =
                MaintenanceLogs.builder()
                        .machine(machine)
                        .performedBy(jho)
                        .maintenanceDate(
                                LocalDateTime.now())
                        .checklist(
                                dto.getChecklist())
                        .remarks(
                                dto.getRemarks())
                        .overallStatus(
                                MaintenanceLogs
                                        .OverallStatus
                                        .GOOD)
                        .build();

        statusRepo.save(status);

        machineRepo.save(machine);

        logsRepo.save(log);

        return "Maintenance Completed Successfully";
    }
}