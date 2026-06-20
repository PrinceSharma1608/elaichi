package tata.JishuHozen.Auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tata.JishuHozen.Entity.users;
import tata.JishuHozen.Repository.userRepo;

@Service
@RequiredArgsConstructor
public class AuthService
{
    private final userRepo userRepo;

    private final JwtUtil jwtUtil;

    public LoginResponseDTO login(
            LoginRequestDTO request)
    {
        users user =
                userRepo.findById(
                                request.getUserId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Invalid User Id"));

        if(!user.getUserPassword()
                .equals(request.getPassword()))
        {
            throw new RuntimeException(
                    "Invalid Password");
        }

        String token =
                jwtUtil.generateToken(
                        user);

        return LoginResponseDTO
                .builder()
                .token(token)
                .userId(
                        user.getUserId())
                .role(
                        user.getUserRole()
                                .name())
                .build();
    }
}