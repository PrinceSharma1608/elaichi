package tata.JishuHozen.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MachineDashboardDTO
{
    private String machineId;
    private String machineName;

    private String areaId;
    private String areaName;

    private String subarea;

    private String jhOwnerId;
    private String jhOwnerName;

    private Integer delayCount;

    private String supervisorId;
    private String supervisorName;

    private String teamLeaderId;
    private String teamLeaderName;

    private Integer maintenanceFrequencyDays;
    private java.time.LocalDate lastMaintenanceDate;
    private java.time.LocalDate nextMaintenanceDate;
    private tata.JishuHozen.Entity.machines.MachineStatus machineStatus;
    private tata.JishuHozen.Entity.machines.Flag flag;
}