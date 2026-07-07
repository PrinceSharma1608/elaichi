package tata.JishuHozen.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MachineChecklistDTO
{
    private String machineId;

    private Integer frequencyDays;

    private List<String>
            checklist;
}