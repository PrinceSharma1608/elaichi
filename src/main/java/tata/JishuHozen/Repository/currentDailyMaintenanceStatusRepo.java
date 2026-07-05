package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tata.JishuHozen.DTO.DailyDashboardDTO;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatus;

import java.util.List;

import java.util.Optional;


public interface currentDailyMaintenanceStatusRepo
        extends JpaRepository<
        CurrentDailyMaintenanceStatus,
        String>
{
    List<CurrentDailyMaintenanceStatus>
    findByMaintenanceStatus(
            CurrentDailyMaintenanceStatus.MaintenanceStatus maintenanceStatus
    );

    @Query("""
        SELECT new tata.JishuHozen.DTO.DailyDashboardDTO(
            m.machineId,
            m.machineName,
            c.maintenanceStatus,
            c.audited
        )
        FROM CurrentDailyMaintenanceStatus c
        JOIN c.machine m
    """)
    List<DailyDashboardDTO>
    getDailyDashboard();


    @Query("""
        SELECT new tata.JishuHozen.DTO.DailyDashboardDTO(
            m.machineId,
            m.machineName,
            c.maintenanceStatus,
            c.audited
        )
        FROM CurrentDailyMaintenanceStatus c
        JOIN c.machine m
        JOIN m.area a
        JOIN a.supervisor s
        WHERE s.userId = :supervisorId
    """)
    List<DailyDashboardDTO>
    getSupervisorDailyDashboard(
            @Param("supervisorId")
            String supervisorId);


    @Query("""
        SELECT new tata.JishuHozen.DTO.DailyDashboardDTO(
            m.machineId,
            m.machineName,
            c.maintenanceStatus,
            c.audited
        )
        FROM CurrentDailyMaintenanceStatus c
        JOIN c.machine m
        JOIN m.jhOwner j
        JOIN TeamLeaderJhOwnerMapping map
            ON map.jhOwnerId = j.userId
        WHERE map.teamLeaderId = :teamLeaderId
    """)
    List<DailyDashboardDTO>
    getTeamLeaderDailyDashboard(
            @Param("teamLeaderId")
            String teamLeaderId);


    @Query("""
        SELECT new tata.JishuHozen.DTO.DailyDashboardDTO(
            m.machineId,
            m.machineName,
            c.maintenanceStatus,
            c.audited
        )
        FROM CurrentDailyMaintenanceStatus c
        JOIN c.machine m
        JOIN m.jhOwner j
        WHERE j.userId = :jhOwnerId
    """)
    List<DailyDashboardDTO>
    getJhOwnerDailyDashboard(
            @Param("jhOwnerId")
            String jhOwnerId);

    Optional<CurrentDailyMaintenanceStatus>
    findByMachine_MachineId(
            String machineId);
    long countByMaintenanceStatus(
            CurrentDailyMaintenanceStatus
                    .MaintenanceStatus
                    status);
    void deleteByMaintenanceStatusIn(
            List<CurrentDailyMaintenanceStatus
                    .MaintenanceStatus> statuses);
}