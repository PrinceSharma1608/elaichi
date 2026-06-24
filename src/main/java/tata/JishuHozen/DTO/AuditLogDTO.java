package tata.JishuHozen.DTO;

import java.time.LocalDateTime;

public interface AuditLogDTO
{
    Integer getAuditId();

    String getMachineId();

    String getMachineName();

    String getAuditedById();

    String getAuditedByName();

    LocalDateTime getAuditDate();

    String getAuditResult();

    String getFindings();
}