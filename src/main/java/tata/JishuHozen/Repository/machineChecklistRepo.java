package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tata.JishuHozen.Entity.MachineChecklist;

import java.time.LocalDate;
import java.util.List;

import tata.JishuHozen.Entity.MachineChecklistId;

public interface machineChecklistRepo
        extends JpaRepository
        <MachineChecklist, MachineChecklistId>
{
    List<MachineChecklist>
    findByMachineId(
            String machineId);

    List<MachineChecklist>
    findByNextDueDate(
            LocalDate date);

    boolean existsByMachineIdAndFrequencyDays(
            String machineId,
            Integer frequencyDays);

}