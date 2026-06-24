package tata.JishuHozen.DTO;

import lombok.*;
import tata.JishuHozen.Entity.MaintenanceLogs;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceLogResponseDTO
{
    private Integer logId;

    private String machineId;

    private String machineName;

    private String performedById;

    private String performedByName;

    private LocalDateTime maintenanceDate;

    private String checklist;

    private String remarks;

    private MaintenanceLogs.OverallStatus overallStatus;

    private MaintenanceLogs.CompletionType completionType;
}