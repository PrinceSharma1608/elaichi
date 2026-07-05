package tata.JishuHozen.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "current_daily_maintenance_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentDailyMaintenanceStatus {
    public enum MaintenanceStatus
    {
        PENDING,
        COMPLETED,
        MISSED,
        DONE_MANUALLY
    }

    @Id
    @Column(name = "machine_id")
    private String machineId;

    @OneToOne
    @JoinColumn(
            name = "machine_id",
            referencedColumnName = "machine_id",
            insertable = false,
            updatable = false
    )
    private machines machine;

    @Column(name = "maintenance_date")
    private LocalDate maintenanceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "maintenance_status")
    private MaintenanceStatus maintenanceStatus;

    @Column(name = "audited")
    private Boolean audited;

    @ManyToOne
    @JoinColumn(name = "completed_by")
    private users completedBy;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}