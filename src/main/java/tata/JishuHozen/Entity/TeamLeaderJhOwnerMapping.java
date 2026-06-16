package tata.JishuHozen.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teamleader_jhowner_mapping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamLeaderJhOwnerMapping
{
    @Id
    @Column(name = "jhowner_id")
    private String jhOwnerId;

    @Column(name = "teamleader_id", nullable = false)
    private String teamLeaderId;
}