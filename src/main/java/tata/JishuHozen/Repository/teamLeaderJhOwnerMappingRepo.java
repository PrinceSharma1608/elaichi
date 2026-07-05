package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tata.JishuHozen.Entity.TeamLeaderJhOwnerMapping;

import java.util.List;
import java.util.Optional;

public interface teamLeaderJhOwnerMappingRepo
        extends JpaRepository<TeamLeaderJhOwnerMapping,String>
{
    List<TeamLeaderJhOwnerMapping>
    findByTeamLeaderId(
            String teamLeaderId);

    boolean existsByJhOwnerId(
            String jhOwnerId);

    boolean existsByJhOwnerIdAndTeamLeaderId(
            String jhOwnerId,
            String teamLeaderId);
}