package tata.JishuHozen.DTO;

import lombok.*;
import tata.JishuHozen.Entity.users;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String userId;

    private String userName;
    private users.UserRole userRole;
}