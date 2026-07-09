package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tata.JishuHozen.DTO.MachineDashboardDTO;
import tata.JishuHozen.DTO.MachineDirectoryDTO;
import tata.JishuHozen.Entity.machines;
import tata.JishuHozen.Entity.users;

import java.util.List;
import java.util.Optional;

public interface machineRepo
        extends JpaRepository<machines,String>
{

    @Query("""
        SELECT new tata.JishuHozen.DTO.MachineDashboardDTO(
            m.machineId,
            m.machineName,
            a.areaId,
            a.areaName,
            m.subarea,
            j.userId,
            j.userName,
            COALESCE(MAX(mc.delayCount), 0),
            s.userId,
            s.userName,
            tl.userId,
            tl.userName,
            COALESCE(MAX(mc.frequencyDays), 0),
            MAX(mc.lastCompletedDate),
            MAX(mc.nextDueDate),
            m.machineStatus,
            m.flag
        )
        FROM machines m
        LEFT JOIN m.area a
        LEFT JOIN a.supervisor s
        LEFT JOIN m.jhOwner j
        LEFT JOIN TeamLeaderJhOwnerMapping map
            ON map.jhOwnerId = j.userId
        LEFT JOIN users tl
            ON tl.userId = map.teamLeaderId
        LEFT JOIN MachineChecklist mc
            ON mc.machineId = m.machineId
        GROUP BY m.machineId, m.machineName, a.areaId, a.areaName, m.subarea, j.userId, j.userName, s.userId, s.userName, tl.userId, tl.userName, m.machineStatus, m.flag
    """)
    List<MachineDashboardDTO> getDashboardMachines();

    boolean existsByJhOwner_UserId(
            String userId);

    machines findByJhOwner_UserId(
            String userId);

    @Query("""
    SELECT new tata.JishuHozen.DTO.MachineDashboardDTO(
        m.machineId,
        m.machineName,
        a.areaId,
        a.areaName,
        m.subarea,
        j.userId,
        j.userName,
        COALESCE(MAX(mc.delayCount), 0),
        s.userId,
        s.userName,
        tl.userId,
        tl.userName,
        COALESCE(MAX(mc.frequencyDays), 0),
        MAX(mc.lastCompletedDate),
        MAX(mc.nextDueDate),
        m.machineStatus,
        m.flag
    )
    FROM machines m
    LEFT JOIN m.area a
    LEFT JOIN a.supervisor s
    LEFT JOIN m.jhOwner j
    LEFT JOIN TeamLeaderJhOwnerMapping map
        ON map.jhOwnerId = j.userId
    LEFT JOIN users tl
        ON tl.userId = map.teamLeaderId
    LEFT JOIN MachineChecklist mc
        ON mc.machineId = m.machineId
    WHERE s.userId = :supervisorId
    GROUP BY m.machineId, m.machineName, a.areaId, a.areaName, m.subarea, j.userId, j.userName, s.userId, s.userName, tl.userId, tl.userName, m.machineStatus, m.flag
""")
    List<MachineDashboardDTO>
    getSupervisorDashboardMachines(
            @Param("supervisorId")
            String supervisorId);

    @Query("""
    SELECT new tata.JishuHozen.DTO.MachineDashboardDTO(
        m.machineId,
        m.machineName,
        a.areaId,
        a.areaName,
        m.subarea,
        j.userId,
        j.userName,
        COALESCE(MAX(mc.delayCount), 0),
        s.userId,
        s.userName,
        tl.userId,
        tl.userName,
        COALESCE(MAX(mc.frequencyDays), 0),
        MAX(mc.lastCompletedDate),
        MAX(mc.nextDueDate),
        m.machineStatus,
        m.flag
    )
    FROM machines m
    LEFT JOIN m.area a
    LEFT JOIN a.supervisor s
    LEFT JOIN m.jhOwner j
    LEFT JOIN TeamLeaderJhOwnerMapping map
        ON map.jhOwnerId = j.userId
    LEFT JOIN users tl
        ON tl.userId = map.teamLeaderId
    LEFT JOIN MachineChecklist mc
        ON mc.machineId = m.machineId
    WHERE tl.userId = :teamLeaderId
    GROUP BY m.machineId, m.machineName, a.areaId, a.areaName, m.subarea, j.userId, j.userName, s.userId, s.userName, tl.userId, tl.userName, m.machineStatus, m.flag
""")
    List<MachineDashboardDTO>
    getTeamLeaderDashboardMachines(
            @Param("teamLeaderId")
            String teamLeaderId);

    @Query("""
    SELECT new tata.JishuHozen.DTO.MachineDashboardDTO(
        m.machineId,
        m.machineName,
        a.areaId,
        a.areaName,
        m.subarea,
        j.userId,
        j.userName,
        COALESCE(MAX(mc.delayCount), 0),
        s.userId,
        s.userName,
        tl.userId,
        tl.userName,
        COALESCE(MAX(mc.frequencyDays), 0),
        MAX(mc.lastCompletedDate),
        MAX(mc.nextDueDate),
        m.machineStatus,
        m.flag
    )
    FROM machines m
    LEFT JOIN m.area a
    LEFT JOIN a.supervisor s
    LEFT JOIN m.jhOwner j
    LEFT JOIN TeamLeaderJhOwnerMapping map
        ON map.jhOwnerId = j.userId
    LEFT JOIN users tl
        ON tl.userId = map.teamLeaderId
    LEFT JOIN MachineChecklist mc
        ON mc.machineId = m.machineId
    WHERE j.userId = :jhOwnerId
    GROUP BY m.machineId, m.machineName, a.areaId, a.areaName, m.subarea, j.userId, j.userName, s.userId, s.userName, tl.userId, tl.userName, m.machineStatus, m.flag
""")
    List<MachineDashboardDTO>
    getJhOwnerDashboardMachines(
            @Param("jhOwnerId")
            String jhOwnerId);

    @Query("""
        SELECT new tata.JishuHozen.DTO.MachineDirectoryDTO(
            m.machineId,
            m.machineName,
            s.userId,
            s.userName,
            tl.userId,
            tl.userName,
            j.userId,
            j.userName,
            COALESCE(SUM(mc.delayCount), 0L),
            MAX(mc.lastCompletedDate)
        )
        FROM machines m
        LEFT JOIN m.area a
        LEFT JOIN a.supervisor s
        LEFT JOIN m.jhOwner j
        LEFT JOIN TeamLeaderJhOwnerMapping map
            ON map.jhOwnerId = j.userId
        LEFT JOIN users tl
            ON tl.userId = map.teamLeaderId
        LEFT JOIN MachineChecklist mc
            ON mc.machineId = m.machineId
        GROUP BY m.machineId, m.machineName, s.userId, s.userName, tl.userId, tl.userName, j.userId, j.userName
    """)
    List<MachineDirectoryDTO> getMachineDirectory();

    Optional<machines>
    findByJhOwner(users jhOwner);
}