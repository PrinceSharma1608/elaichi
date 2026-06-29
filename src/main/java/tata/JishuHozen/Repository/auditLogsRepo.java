package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tata.JishuHozen.DTO.AuditLogDTO;
import tata.JishuHozen.Entity.AuditLogs;

import java.util.List;

public interface auditLogsRepo
        extends JpaRepository<AuditLogs,Integer>
{
    @Query("""
SELECT
al.auditId AS auditId,
m.machineId AS machineId,
m.machineName AS machineName,
u.userId AS auditedById,
u.userName AS auditedByName,
al.auditDate AS auditDate,
al.checklist AS checklist,
al.findings AS findings
FROM AuditLogs al
JOIN al.machine m
JOIN al.auditedBy u
ORDER BY al.auditDate DESC
""")
    List<AuditLogDTO> getAllLogs();
}