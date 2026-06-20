package tata.JishuHozen.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaResponseDTO
{
    private String areaId;

    private String areaName;

    private String supervisorId;

    private String supervisorName;
}