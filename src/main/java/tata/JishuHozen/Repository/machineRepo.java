package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tata.JishuHozen.DTO.MachineDashboardDTO;
import tata.JishuHozen.Entity.machines;

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

}