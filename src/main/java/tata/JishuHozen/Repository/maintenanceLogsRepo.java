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
<<<<<<< HEAD

=======
>>>>>>> 9c1d04c2262df358ef5619b7a80a261ed532b826
}