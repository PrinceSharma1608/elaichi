package tata.JishuHozen.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMachineConfigurationDTO
{
    private String machineId;

    private String jhOwnerId;

    private Integer maintenanceFrequencyDays;

    private String subarea;

    private String machineStatus;

    private String flag;
}