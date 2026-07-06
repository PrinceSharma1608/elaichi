package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tata.JishuHozen.DTO.MachineDashboardDTO;
import tata.JishuHozen.Entity.machines;
import org.springframework.data.repository.query.Param;
import tata.JishuHozen.Entity.users;


import java.time.LocalDate;

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

            m.delayCount,

            s.userId,
            s.userName,

            tl.userId,
            tl.userName,

            m.maintenanceFrequencyDays,
            m.lastMaintenanceDate,
            m.nextMaintenanceDate,
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
        m.delayCount,
        s.userId,
        s.userName,
        tl.userId,
        tl.userName,
        m.maintenanceFrequencyDays,
        m.lastMaintenanceDate,
        m.nextMaintenanceDate,
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
    WHERE s.userId = :supervisorId
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
        m.delayCount,
        s.userId,
        s.userName,
        tl.userId,
        tl.userName,
        m.maintenanceFrequencyDays,
        m.lastMaintenanceDate,
        m.nextMaintenanceDate,
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
    WHERE tl.userId = :teamLeaderId
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
        m.delayCount,
        s.userId,
        s.userName,
        tl.userId,
        tl.userName,
        m.maintenanceFrequencyDays,
        m.lastMaintenanceDate,
        m.nextMaintenanceDate,
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
    WHERE j.userId = :jhOwnerId
""")
    List<MachineDashboardDTO>
    getJhOwnerDashboardMachines(
            @Param("jhOwnerId")
            String jhOwnerId);

    List<machines>
    findByMachineStatusAndNextMaintenanceDate(
            machines.MachineStatus machineStatus,
            LocalDate nextMaintenanceDate);
    Optional<machines>
    findByJhOwner(users jhOwner);
}