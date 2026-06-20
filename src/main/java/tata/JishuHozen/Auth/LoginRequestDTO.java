package tata.JishuHozen.Auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO
{
    private String userId;

    private String password;
}