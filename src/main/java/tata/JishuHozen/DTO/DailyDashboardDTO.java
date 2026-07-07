package tata.JishuHozen.DTO;

import lombok.*;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatus;

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
}