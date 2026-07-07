package tata.JishuHozen.DTO;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditRequestDTO {
    private String machineId;

    private Integer frequencyDays;

    private List<MaintenanceChecklistItemDTO>
            checklist;

    private String findings;
}