package tata.JishuHozen.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MachineJhOwnerMappingRequestDTO
{
    private String machineId;

    private String jhOwnerId;
}