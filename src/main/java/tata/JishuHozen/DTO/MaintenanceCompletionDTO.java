package tata.JishuHozen.DTO;

import lombok.*;

import java.util.Map;

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