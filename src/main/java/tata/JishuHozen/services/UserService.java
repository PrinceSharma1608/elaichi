package tata.JishuHozen.services;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tata.JishuHozen.DTO.*;
import tata.JishuHozen.Entity.TeamLeaderJhOwnerMapping;
import tata.JishuHozen.Entity.area;
import tata.JishuHozen.Entity.machines;
import tata.JishuHozen.Entity.users;
import tata.JishuHozen.Repository.areaRepo;
import tata.JishuHozen.Repository.machineRepo;
import tata.JishuHozen.Repository.teamLeaderJhOwnerMappingRepo;
import tata.JishuHozen.Repository.userRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService
{
    @Autowired
    private  userRepo userRepo;
    @Autowired
    private machineRepo machineRepo;
    @Autowired
    private  areaRepo areaRepo;
    @Autowired
    private teamLeaderJhOwnerMappingRepo
            mappingRepo;


    public List<UserResponseDTO> getUsers(
            users.UserRole role)
    {
        List<users> userList;

        if(role == null)
        {
            userList = userRepo.findAll();
        }
        else
        {
            userList =
                    userRepo.findByUserRole(role);
        }

        return userList.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private UserResponseDTO convertToDTO(
            users user)
    {
        return UserResponseDTO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userRole(user.getUserRole())
                .build();
    }


    public List<MachineDashboardDTO>
    getDashboardMachines()
    {
        return machineRepo.getDashboardMachines();
    }
    public String mapSupervisor(
            AreaSupervisorMappingDTO dto)
    {
        area area =
                areaRepo.findById(
                                dto.getAreaId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Area Not Found"));

        users supervisor =
                userRepo.findById(
                                dto.getSupervisorId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Supervisor Not Found"));

        if(supervisor.getUserRole()
                != users.UserRole.SUPERVISOR)
        {
            throw new RuntimeException(
                    "User Is Not A Supervisor");
        }

        if(areaRepo.existsBySupervisor_UserId(
                dto.getSupervisorId()))
        {
            throw new RuntimeException(
                    "Supervisor Already Assigned");
        }

        area.setSupervisor(
                supervisor);

        areaRepo.save(area);

        return "Supervisor Mapped Successfully";
    }

    public String mapTeamLeaderToJhOwner(
            TeamLeaderJhOwnerMappingDTO dto)
    {
        users teamLeader =
                userRepo.findById(
                                dto.getTeamLeaderId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Team Leader Not Found"));

        users jhOwner =
                userRepo.findById(
                                dto.getJhOwnerId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "JH Owner Not Found"));

        if(teamLeader.getUserRole()
                != users.UserRole.TEAM_LEADER)
        {
            throw new RuntimeException(
                    "Invalid Team Leader");
        }

        if(jhOwner.getUserRole()
                != users.UserRole.JH_OWNER)
        {
            throw new RuntimeException(
                    "Invalid JH Owner");
        }

        if(mappingRepo.existsByJhOwnerId(
                dto.getJhOwnerId()))
        {
            throw new RuntimeException(
                    "JH Owner Already Mapped");
        }

        TeamLeaderJhOwnerMapping mapping =
                new TeamLeaderJhOwnerMapping();

        mapping.setJhOwnerId(
                dto.getJhOwnerId());

        mapping.setTeamLeaderId(
                dto.getTeamLeaderId());

        mappingRepo.save(mapping);

        return "Mapping Successful";
    }
    public String mapMachineToJhOwner(
            List<MachineJhOwnerMappingRequestDTO> dtoList)
    {
        for(MachineJhOwnerMappingRequestDTO dto
                : dtoList)
        {
            machines machine =
                    machineRepo.findById(
                                    dto.getMachineId())
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Machine Not Found"));

            users jhOwner =
                    userRepo.findById(
                                    dto.getJhOwnerId())
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "JH Owner Not Found"));

            if(jhOwner.getUserRole()
                    != users.UserRole.JH_OWNER)
            {
                throw new RuntimeException(
                        "Invalid JH Owner : "
                                + dto.getJhOwnerId());
            }

            if(machine.getJhOwner() != null)
            {
                throw new RuntimeException(
                        "Machine Already Assigned : "
                                + dto.getMachineId());
            }

            if(machineRepo.existsByJhOwner_UserId(
                    dto.getJhOwnerId()))
            {
                throw new RuntimeException(
                        "JH Owner Already Assigned : "
                                + dto.getJhOwnerId());
            }

            machine.setJhOwner(jhOwner);

            machineRepo.save(machine);
        }

        return "Mapping Successful";
    }
}

