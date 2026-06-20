package tata.JishuHozen.Auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(
            @RequestBody
            LoginRequestDTO request)
    {
        return authService
                .login(request);
    }
}