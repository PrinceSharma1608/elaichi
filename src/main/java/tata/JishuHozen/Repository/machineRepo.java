package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tata.JishuHozen.DTO.MachineDashboardDTO;
import tata.JishuHozen.Entity.machines;
import org.springframework.data.repository.query.Param;

<<<<<<< HEAD
import java.time.LocalDate;
=======
>>>>>>> 9c1d04c2262df358ef5619b7a80a261ed532b826
import java.util.List;

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
            tl.userName
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
        tl.userName
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
        tl.userName
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
        tl.userName
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
<<<<<<< HEAD
    List<machines>
    findByMachineStatusAndNextMaintenanceDateLessThanEqual(
            machines.MachineStatus machineStatus,
            LocalDate date);
=======
>>>>>>> 9c1d04c2262df358ef5619b7a80a261ed532b826
}