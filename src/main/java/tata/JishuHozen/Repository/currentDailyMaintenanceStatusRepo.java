package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatus;

import java.util.List;

public interface currentDailyMaintenanceStatusRepo
        extends JpaRepository<
        CurrentDailyMaintenanceStatus,
        String>
{
    List<CurrentDailyMaintenanceStatus>
    findByMaintenanceStatus(
            CurrentDailyMaintenanceStatus.MaintenanceStatus maintenanceStatus
    );
}