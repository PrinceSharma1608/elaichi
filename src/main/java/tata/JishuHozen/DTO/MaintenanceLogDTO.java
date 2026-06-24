package tata.JishuHozen.DTO;

import java.time.LocalDateTime;

public interface MaintenanceLogDTO
{
    Integer getLogId();

    String getMachineId();

    String getMachineName();

    String getPerformedById();

    String getPerformedByName();

    LocalDateTime getMaintenanceDate();

    String getChecklist();

    String getRemarks();

    String getOverallStatus();

    String getCompletionType();
}