package tata.JishuHozen.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceCompleteDTO
{
    private String machineId;

    private String userId;

    private String checklist;

    private String remarks;
}