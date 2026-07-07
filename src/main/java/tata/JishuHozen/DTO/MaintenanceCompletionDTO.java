package tata.JishuHozen.DTO;

import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceCompletionDTO
{
    private String machineId;

    private Integer frequencyDays;

    private List<MaintenanceChecklistItemDTO>
            checklist;

    private String remarks;
}