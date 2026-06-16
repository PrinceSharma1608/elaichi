package tata.JishuHozen.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class users
{
public enum UserRole
{
    LINE_INCHARGE,
    SUPERVISOR,
    TEAM_LEADER,
    JH_OWNER
}

    @Id
    @Column(
            name = "user_id",
            length = 9,
            nullable = false
    )
    private String userId;

    @Column(
            name = "user_name",
            nullable = false,
            length = 100
    )
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "user_role",
            nullable = false,
            length = 20
    )
    private UserRole userRole;

    @Column(
            name = "user_password",
            nullable = false,
            length = 100
    )
    private String userPassword;
}