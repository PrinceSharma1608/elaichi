package tata.JishuHozen.Entity;

import jakarta.persistence.*;
import lombok.*;
import tata.JishuHozen.Entity.MachineChecklistId;

import java.time.LocalDate;

@Entity
@Table(name = "machine_checklists")
@IdClass(MachineChecklistId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MachineChecklist
{
    @Id
    @Column(name = "machine_id")
    private String machineId;

    @Id
    @Column(name = "frequency_days")
    private Integer frequencyDays;

    @Column(name = "last_completed_date")
    private LocalDate lastCompletedDate;

    @Column(name = "next_due_date")
    private LocalDate nextDueDate;

    @Builder.Default
    @Column(name = "delay_count")
    private Integer delayCount = 0;

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(
            name = "checklist",
            columnDefinition = "jsonb")
    private String checklist;
}