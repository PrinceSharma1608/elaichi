package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tata.JishuHozen.Entity.AuditLogs;

import java.util.List;

public interface auditLogsRepo
        extends JpaRepository<
        AuditLogs,
        Integer>
{
    List<AuditLogs>
    findByMachine_MachineId(
            String machineId
    );
}