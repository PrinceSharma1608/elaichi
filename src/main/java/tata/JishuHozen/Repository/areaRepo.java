package tata.JishuHozen.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tata.JishuHozen.Entity.area;

public interface areaRepo
        extends JpaRepository<area,String>
{
    boolean existsBySupervisor_UserId(String userId);
}