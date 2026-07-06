package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tata.JishuHozen.Entity.AuditLogs;

public interface auditLogsRepo
        extends JpaRepository<AuditLogs,Integer>
{
}