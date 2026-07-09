package tata.JishuHozen.DTO;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MachineDirectoryDTO {
    private String machineId;
    private String machineName;
    private String supervisorId;
    private String supervisorName;
    private String teamLeaderId;
    private String teamLeaderName;
    private String jhOwnerId;
    private String jhOwnerName;
    private Long delay;
    private LocalDate lastMaintenanceDate;
}
