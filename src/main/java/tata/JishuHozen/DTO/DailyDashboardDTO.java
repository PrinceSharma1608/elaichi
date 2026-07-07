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
}