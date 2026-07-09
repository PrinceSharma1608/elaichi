package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tata.JishuHozen.DTO.DailyDashboardDTO;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatus;
import tata.JishuHozen.Entity.CurrentDailyMaintenanceStatusId;

import java.util.List;
import java.util.Optional;

public interface currentDailyMaintenanceStatusRepo
        extends JpaRepository
        <
                CurrentDailyMaintenanceStatus,
                CurrentDailyMaintenanceStatusId
                >
{
    List<CurrentDailyMaintenanceStatus>
    findByMaintenanceStatus(
            CurrentDailyMaintenanceStatus
                    .MaintenanceStatus
                    status);

    Optional<CurrentDailyMaintenanceStatus>
    findByMachineIdAndFrequencyDays(
            String machineId,
            Integer frequencyDays);

    @Query("""
        SELECT new tata.JishuHozen.DTO.DailyDashboardDTO(
            m.machineId,
            m.machineName,
            c.frequencyDays,
            c.maintenanceStatus,
            m.subarea,
            m.flag,
            mc.nextDueDate,
            mc.delayCount,
            a.areaName,
            c.updatedAt
        )
        FROM CurrentDailyMaintenanceStatus c
        JOIN c.machine m
        LEFT JOIN m.area a
        JOIN MachineChecklist mc
            ON mc.machineId = c.machineId
            AND mc.frequencyDays = c.frequencyDays
    """)
    List<DailyDashboardDTO>
    getDailyDashboard();

    @Query("""
        SELECT new tata.JishuHozen.DTO.DailyDashboardDTO(
            m.machineId,
            m.machineName,
            c.frequencyDays,
            c.maintenanceStatus,
            m.subarea,
            m.flag,
            mc.nextDueDate,
            mc.delayCount,
            a.areaName,
            c.updatedAt
        )
        FROM CurrentDailyMaintenanceStatus c
        JOIN c.machine m
        JOIN m.area a
        JOIN a.supervisor s
        JOIN MachineChecklist mc
            ON mc.machineId = c.machineId
            AND mc.frequencyDays = c.frequencyDays
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
            c.frequencyDays,
            c.maintenanceStatus,
            m.subarea,
            m.flag,
            mc.nextDueDate,
            mc.delayCount,
            a.areaName,
            c.updatedAt
        )
        FROM CurrentDailyMaintenanceStatus c
        JOIN c.machine m
        LEFT JOIN m.area a
        JOIN m.jhOwner j
        JOIN TeamLeaderJhOwnerMapping map
            ON map.jhOwnerId = j.userId
        JOIN MachineChecklist mc
            ON mc.machineId = c.machineId
            AND mc.frequencyDays = c.frequencyDays
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
            c.frequencyDays,
            c.maintenanceStatus,
            m.subarea,
            m.flag,
            mc.nextDueDate,
            mc.delayCount,
            a.areaName,
            c.updatedAt
        )
        FROM CurrentDailyMaintenanceStatus c
        JOIN c.machine m
        LEFT JOIN m.area a
        JOIN m.jhOwner j
        JOIN MachineChecklist mc
            ON mc.machineId = c.machineId
            AND mc.frequencyDays = c.frequencyDays
        WHERE j.userId = :jhOwnerId
    """)
    List<DailyDashboardDTO>
    getJhOwnerDailyDashboard(
            @Param("jhOwnerId")
            String jhOwnerId);

    long countByMaintenanceStatus(
            CurrentDailyMaintenanceStatus
                    .MaintenanceStatus
                    status);

    void deleteByMaintenanceStatusIn(
            List<
                    CurrentDailyMaintenanceStatus
                            .MaintenanceStatus
                    > statuses);

    void deleteByMaintenanceStatus(
            CurrentDailyMaintenanceStatus
                    .MaintenanceStatus
                    status);
}