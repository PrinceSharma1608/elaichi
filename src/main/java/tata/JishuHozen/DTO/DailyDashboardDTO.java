package tata.JishuHozen.DTO;

import lombok.*;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatus;
import tata.JishuHozen.Entity.machines;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyDashboardDTO
{
    private String machineId;

    private String machineName;

    private Integer frequencyDays;

    private CurrentDailyMaintenanceStatus
            .MaintenanceStatus
            maintenanceStatus;

    private String subarea;

    private machines.Flag flag;

    private LocalDate nextDueDate;

    private Integer delayCount;

    private String areaName;

    private java.time.LocalDateTime completedAt;

    private String remarks;

    private String checklist;

    // 10-args constructor for backward compatibility
    public DailyDashboardDTO(String machineId, String machineName, Integer frequencyDays,
                             CurrentDailyMaintenanceStatus.MaintenanceStatus maintenanceStatus,
                             String subarea, machines.Flag flag, LocalDate nextDueDate,
                             Integer delayCount, String areaName, java.time.LocalDateTime completedAt) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.frequencyDays = frequencyDays;
        this.maintenanceStatus = maintenanceStatus;
        this.subarea = subarea;
        this.flag = flag;
        this.nextDueDate = nextDueDate;
        this.delayCount = delayCount;
        this.areaName = areaName;
        this.completedAt = completedAt;
        this.remarks = null;
        this.checklist = null;
    }
}