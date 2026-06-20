package tata.JishuHozen.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AuditLogs
{
    public enum AuditResult
    {
        PASS,
        FAIL
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Integer auditId;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private machines machine;

    @ManyToOne
    @JoinColumn(name = "audited_by")
    private users auditedBy;

    @Column(name = "audit_date")
    private LocalDateTime auditDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "audit_result")
    private AuditResult auditResult;

    @Column(name = "findings")
    private String findings;
}