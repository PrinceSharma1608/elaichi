package tata.JishuHozen.Entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MachineChecklistId
        implements Serializable
{
    private String machineId;

    private Integer frequencyDays;
}