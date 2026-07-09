package tata.JishuHozen.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "machines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class machines
{

public enum MachineStatus
{
    ACTIVE,
    INACTIVE
}
public enum Flag
{
    OK,
    GREEN,
    RED
}

    @Id
    @Column(name = "machine_id")
    private String machineId;

    @Column(name = "machine_name", nullable = false)
    private String machineName;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private area area;

    @OneToOne
    @JoinColumn(name = "jhowner_id")
    private users jhOwner;

    @Column(name = "subarea")
    private String subarea;

    @Enumerated(EnumType.STRING)
    @Column(name = "machine_status")
    private MachineStatus machineStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "flag")
    private Flag flag;
}