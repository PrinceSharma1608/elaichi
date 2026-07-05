package tata.JishuHozen.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateMachineConfigurationDTO
{
    private String machineId;

    private Integer maintenanceFrequencyDays;

    private LocalDate lastMaintenanceDate;

    private String flag;

    private String machineStatus;
}