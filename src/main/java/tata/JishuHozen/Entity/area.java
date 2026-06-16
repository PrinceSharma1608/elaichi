package tata.JishuHozen.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "areas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class area
{
    @Id
    @Column(name = "area_id")
    private String areaId;

    @Column(name = "area_name", nullable = false)
    private String areaName;

    @OneToOne
    @JoinColumn(name = "supervisor_id")
    private tata.JishuHozen.Entity.users supervisor;
}