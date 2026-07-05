package tata.JishuHozen.Auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtHelper
{
    private final JwtUtil jwtUtil;

    public String getToken(
            HttpServletRequest request)
    {
        String header =
                request.getHeader(
                        "Authorization");

        if(header == null
                ||
                !header.startsWith(
                        "Bearer "))
        {
            throw new RuntimeException(
                    "Token Missing");
        }

        return header.substring(7);
    }

    public String getUserId(
            HttpServletRequest request)
    {
        return jwtUtil.extractUserId(
                getToken(request));
    }

    public String getRole(
            HttpServletRequest request)
    {
        return jwtUtil.extractRole(
                getToken(request));
    }
}