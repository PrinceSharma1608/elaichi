package tata.JishuHozen.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatus;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatusId;
import tata.JishuHozen.Entity.MachineChecklist;
import tata.JishuHozen.Entity.MachineChecklistId;
import tata.JishuHozen.Entity.machines;
import tata.JishuHozen.Entity.MaintenanceLogs;
import tata.JishuHozen.Repository.currentDailyMaintenanceStatusRepo;
import tata.JishuHozen.Repository.machineChecklistRepo;
import tata.JishuHozen.Repository.machineRepo;
import tata.JishuHozen.Repository.maintenanceLogsRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class Scheduler
{
    private final machineChecklistRepo
            machineChecklistRepo;

    private final currentDailyMaintenanceStatusRepo
            statusRepo;
    private final  machineRepo machineRepo;
    private final maintenanceLogsRepo logsRepo;
    /*
        Every day at 12:00 AM
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *",
            zone = "Asia/Kolkata")
    public void midnightScheduler()
    {
        log.info(
                "Running Midnight Scheduler");

        /*
            Remove completed entries.
         */
        statusRepo.deleteByMaintenanceStatus(
                CurrentDailyMaintenanceStatus
                        .MaintenanceStatus
                        .COMPLETED);

        statusRepo.deleteByMaintenanceStatus(
                CurrentDailyMaintenanceStatus
                        .MaintenanceStatus
                        .DONE_MANUALLY);

        /*
            Fetch all due checklists.
         */
        List<MachineChecklist>
                dueChecklists =
                machineChecklistRepo
                        .findByNextDueDate(
                                LocalDate.now());

        for (MachineChecklist checklist
                : dueChecklists)
        {
            CurrentDailyMaintenanceStatusId id =
                    new CurrentDailyMaintenanceStatusId(
                            checklist.getMachineId(),
                            checklist.getFrequencyDays());

            Optional<CurrentDailyMaintenanceStatus>
                    existing =
                    statusRepo.findById(id);

            /*
                If previous cycle is MISSED,
                convert it into PENDING.
             */
            if (existing.isPresent())
            {
                CurrentDailyMaintenanceStatus
                        old =
                        existing.get();

                if (old.getMaintenanceStatus()
                        ==
                        CurrentDailyMaintenanceStatus
                                .MaintenanceStatus
                                .MISSED)
                {
                    old.setMaintenanceStatus(
                            CurrentDailyMaintenanceStatus
                                    .MaintenanceStatus
                                    .PENDING);

                    old.setMaintenanceDate(
                            LocalDate.now());

                    old.setChecklist(
                            null);

                    old.setCompletedBy(
                            null);

                    statusRepo.save(
                            old);
                }
            }
            else
            {
                CurrentDailyMaintenanceStatus
                        cdms =
                        CurrentDailyMaintenanceStatus
                                .builder()
                                .machineId(
                                        checklist.getMachineId())
                                .frequencyDays(
                                        checklist.getFrequencyDays())
                                .maintenanceDate(
                                        LocalDate.now())
                                .maintenanceStatus(
                                        CurrentDailyMaintenanceStatus
                                                .MaintenanceStatus
                                                .PENDING)
                                .checklist(
                                        checklist.getChecklist())
                                .build();

                statusRepo.save(
                        cdms);
            }

            /*
                Immediately move
                next due date.
             */
            checklist.setLastCompletedDate(
                    LocalDate.now());

            checklist.setNextDueDate(
                    checklist
                            .getNextDueDate()
                            .plusDays(
                                    checklist
                                            .getFrequencyDays()));

            machineChecklistRepo
                    .save(
                            checklist);
        }

        log.info(
                "Midnight Scheduler Finished");
    }

    @Transactional
    @Scheduled(cron = "0 0 20 * * *",
            zone = "Asia/Kolkata")
    public void markMissedMaintenance()
    {
        log.info(
                "Running 8 PM scheduler");

        List<CurrentDailyMaintenanceStatus>
                pendingEntries =
                statusRepo
                        .findByMaintenanceStatus(
                                CurrentDailyMaintenanceStatus
                                        .MaintenanceStatus
                                        .PENDING);

        for (CurrentDailyMaintenanceStatus
                status : pendingEntries)
        {
            status.setMaintenanceStatus(
                    CurrentDailyMaintenanceStatus
                            .MaintenanceStatus
                            .MISSED);
            status.setRemarks("Auto marks as MISSED");
            status.setUpdatedAt(LocalDate.now().atTime(20, 0, 0));

            MachineChecklistId checklistId =
                    new MachineChecklistId(
                            status.getMachineId(),
                            status.getFrequencyDays());

            MachineChecklist checklist =
                    machineChecklistRepo.findById(checklistId)
                            .orElseThrow(
                                     () ->
                                             new RuntimeException(
                                                     "Checklist Not Found"));

            checklist.setDelayCount(
                    (checklist.getDelayCount() != null ? checklist.getDelayCount() : 0)
                            + 1);

            machineChecklistRepo.save(checklist);

            MaintenanceLogs logEntry =
                    MaintenanceLogs.builder()
                            .machine(status.getMachine())
                            .frequencyDays(status.getFrequencyDays())
                            .performedBy(null)
                            .maintenanceDate(LocalDate.now().atTime(20, 0, 0))
                            .checklist(null)
                            .remarks("Auto marks as MISSED")
                            .completionType(MaintenanceLogs.CompletionType.MISSED)
                            .build();

            logsRepo.save(logEntry);

            statusRepo
                    .save(status);
        }

        log.info(
                "8 PM scheduler completed");
    }

}