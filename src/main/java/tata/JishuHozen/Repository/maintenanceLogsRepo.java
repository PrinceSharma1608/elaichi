package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tata.JishuHozen.DTO.MaintenanceLogDTO;
import tata.JishuHozen.Entity.MaintenanceLogs;

import java.util.List;

public interface maintenanceLogsRepo
        extends JpaRepository<MaintenanceLogs,Integer>
{
  /*  @Query("""
            SELECT
            ml.logId AS logId,
            m.machineId AS machineId,
            m.machineName AS machineName,
            u.userId AS performedById,
            u.userName AS performedByName,
            ml.maintenanceDate AS maintenanceDate,
            ml.checklist AS checklist,
            ml.remarks AS remarks,
            ml.completionType AS completionType
            FROM MaintenanceLogs ml
            LEFT JOIN ml.machine m
            LEFT JOIN ml.performedBy u
            ORDER BY ml.maintenanceDate DESC
            """)
    List<MaintenanceLogDTO> getAllLogs();*/
}