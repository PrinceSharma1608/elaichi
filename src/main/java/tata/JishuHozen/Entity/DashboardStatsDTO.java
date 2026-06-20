package tata.JishuHozen.Entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardStatsDTO
{
    private Long totalMachines;

    private Long pending;

    private Long completed;

    private Long missed;
}