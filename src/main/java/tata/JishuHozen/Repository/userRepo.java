package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tata.JishuHozen.Entity.users;

import java.util.List;
import java.util.Optional;

public interface userRepo
        extends JpaRepository<users,String>
{
    List<users> findByUserRole(users.UserRole userRole);
    Optional<users> findByUserId(
            String userId);

    boolean existsByUserIdAndUserRole(
            String userId,
            users.UserRole userRole);


}