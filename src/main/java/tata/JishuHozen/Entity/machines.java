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
    EC,
    WC
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

    @Column(name = "maintenance_frequency_days")
    private Integer maintenanceFrequencyDays;

    @Column(name = "last_maintenance_date")
    private LocalDate lastMaintenanceDate;

    @Column(
            name = "next_maintenance_date",
            insertable = false,
            updatable = false
    )
    private LocalDate nextMaintenanceDate;

    @Column(name = "delay_count")
    private Integer delayCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "machine_status")
    private MachineStatus machineStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "flag")
    private Flag flag;
}