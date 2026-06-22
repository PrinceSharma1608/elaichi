package tata.JishuHozen.Entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceLogs
{
public enum OverallStatus
{
    GOOD,
    WARNING,
    CRITICAL
}

    public enum CompletionType
    {
        COMPLETED,
        MISSED,
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private machines machine;

    @ManyToOne
    @JoinColumn(name = "performed_by")
    private users performedBy;

    @Column(name = "maintenance_date")
    private LocalDateTime maintenanceDate;

    @Column(
            name = "checklist"
    )
    private String checklist;

    @Column(name = "remarks")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "overall_status")
    private OverallStatus overallStatus;


    @Enumerated(EnumType.STRING)
    @Column(name = "completion_type")
    private CompletionType completionType;

}