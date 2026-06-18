package tata.JishuHozen.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyDashboardDTO
{
    private String machineId;

    private String machineName;

    private String maintenanceStatus;

    private Boolean audited;
}