package tata.JishuHozen.services;

import jakarta.transaction.Transactional;
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
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Scheduler

{
    private final machineRepo machineRepo;

    private final currentDailyMaintenanceStatusRepo
            statusRepo;

    private final maintenanceLogsRepo logsRepo;
    @Scheduled(cron = "0 00 00  * * *",
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
            Optional<
                                CurrentDailyMaintenanceStatus>
                    existing =
                    statusRepo.findById(
                            machine.getMachineId());

            if(existing.isPresent())
            {
                CurrentDailyMaintenanceStatus
                        cdms =
                        existing.get();

                if(cdms.getMaintenanceStatus()
                        ==
                        CurrentDailyMaintenanceStatus
                                .MaintenanceStatus
                                .MISSED)
                {
                    cdms.setMaintenanceStatus(
                            CurrentDailyMaintenanceStatus
                                    .MaintenanceStatus
                                    .PENDING);

                    cdms.setMaintenanceDate(
                            LocalDate.now());

                    cdms.setAudited(false);

                    cdms.setCompletedBy(null);

                    statusRepo.save(cdms);
                }

                continue;
            }

            CurrentDailyMaintenanceStatus
                    status =
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
            cron = "0 0 0 * * *",
            zone = "Asia/Kolkata")
    @Transactional
    public void clearCompletedTasks()
    {
        statusRepo.deleteByMaintenanceStatusIn(
                List.of(
                        CurrentDailyMaintenanceStatus
                                .MaintenanceStatus
                                .COMPLETED,

                        CurrentDailyMaintenanceStatus
                                .MaintenanceStatus
                                .DONE_MANUALLY
                ));
    }
    @Scheduled(
            cron = "0 00 20 * * *",
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
                            .completionType(MaintenanceLogs.CompletionType.MISSED).build();

            statusRepo.save(task);

            machineRepo.save(machine);

            logsRepo.save(log);
        }
    }
}