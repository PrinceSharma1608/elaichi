package tata.JishuHozen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatus;
import tata.JishuHozen.Entity.MaintenanceLogs;
import tata.JishuHozen.Entity.machines;
import tata.JishuHozen.Repository.currentDailyMaintenanceStatusRepo;
import tata.JishuHozen.Repository.machineRepo;
import tata.JishuHozen.Repository.maintenanceLogsRepo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyMaintenanceScheduler
{
    private final machineRepo machineRepo;

    private final currentDailyMaintenanceStatusRepo
            statusRepo;

    private final maintenanceLogsRepo logsRepo;
    @Scheduled(
            cron = "0 0 00 * * *",
            zone = "Asia/Kolkata")
    public void populateDailyTasks()
    {
        List<machines> dueMachines =
                machineRepo
                        .findByMachineStatusAndNextMaintenanceDate(
                                machines.MachineStatus.ACTIVE,
                                LocalDate.now());

        for(machines machine : dueMachines)
        {
            CurrentDailyMaintenanceStatus status =
                    CurrentDailyMaintenanceStatus
                            .builder()
                            .machineId(
                                    machine.getMachineId())
                            .maintenanceDate(
                                    LocalDate.now())
                            .maintenanceStatus(
                                    CurrentDailyMaintenanceStatus
                                            .MaintenanceStatus
                                            .PENDING)
                            .audited(false)
                            .completedBy(null)
                            .updatedAt(null)
                            .build();

            statusRepo.save(status);

            machine.setNextMaintenanceDate(
                    machine.getNextMaintenanceDate()
                            .plusDays(
                                    machine
                                            .getMaintenanceFrequencyDays()));

            machineRepo.save(machine);
        }
    }
    @Scheduled(
            cron = "0 0 20 * * *",
            zone = "Asia/Kolkata")
    public void markMissedMachines()
    {
        List<CurrentDailyMaintenanceStatus>
                pendingTasks =
                statusRepo.findByMaintenanceStatus(
                        CurrentDailyMaintenanceStatus
                                .MaintenanceStatus
                                .PENDING);

        for(CurrentDailyMaintenanceStatus task
                : pendingTasks)
        {
            machines machine =
                    task.getMachine();

            task.setMaintenanceStatus(
                    CurrentDailyMaintenanceStatus
                            .MaintenanceStatus
                            .MISSED);

            machine.setDelayCount(
                    machine.getDelayCount() + 1);

            MaintenanceLogs log =
                    MaintenanceLogs
                            .builder()
                            .machine(machine)
                            .performedBy(null)
                            .maintenanceDate(
                                    LocalDateTime.now())
                            .checklist(null)
                            .remarks(
                                    "Auto Marked As MISSED")
                            .overallStatus(
                                    MaintenanceLogs
                                            .OverallStatus
                                            .CRITICAL)
                            .completionType(
                                    MaintenanceLogs
                                            .CompletionType
                                            .MISSED)
                            .build();

            statusRepo.save(task);

            machineRepo.save(machine);

            logsRepo.save(log);
        }
    }
}