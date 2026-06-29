package tata.JishuHozen.DTO;

import java.time.LocalDateTime;

public class MaintenanceLogDTO
{
    Integer getLogId;

    String getMachineId;

    String getMachineName;

    String getPerformedById;

    String getPerformedByName;

    LocalDateTime getMaintenanceDate;

    String getChecklist;

    String getRemarks;
    String getCompletionType;
}