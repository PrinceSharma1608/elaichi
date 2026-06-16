package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tata.JishuHozen.Entity.MaintenanceLogs;

import java.util.List;

public interface maintenanceLogsRepo
        extends JpaRepository<
        MaintenanceLogs,
        Integer>
{
    List<MaintenanceLogs>
    findByMachine_MachineId(
            String machineId
    );
}