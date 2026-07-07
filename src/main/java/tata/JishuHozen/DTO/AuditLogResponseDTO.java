package tata.JishuHozen.DTO;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponseDTO
{
    private Integer auditId;

    private String machineId;

    private String machineName;

    private String auditorId;

    private String auditorName;

    private LocalDateTime auditDate;

    private String checklist;
    private Integer frequencyDays;
    private String findings;
}