package tata.JishuHozen.Auth;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO
{
    private String token;

    private String userId;

    private String role;
}