package tata.JishuHozen.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogDTO
{
    private Integer auditId;

    private String machineId;

    private String machineName;

    private String auditedById;

    private String auditedByName;

    private LocalDateTime auditDate;

    private String checklist;

    private String findings;
}