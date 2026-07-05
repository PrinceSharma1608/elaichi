package tata.JishuHozen.DTO;

import lombok.*;
import org.springframework.stereotype.Service;

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

    private Map<String, Boolean> checklist;

    private String remarks;
}