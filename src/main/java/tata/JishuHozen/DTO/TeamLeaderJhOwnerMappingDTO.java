package tata.JishuHozen.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamLeaderJhOwnerMappingDTO
{
    private String teamLeaderId;
    private List<String> jhOwnerIds;
}