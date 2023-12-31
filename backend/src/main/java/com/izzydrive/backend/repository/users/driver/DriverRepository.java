package com.izzydrive.backend.repository.users.driver;

import com.izzydrive.backend.model.users.driver.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long>, DriverRepositoryCustom {

    Optional<Driver> findByEmail(String email);

    @Query("select d from Driver d left join fetch d.reservedFromClientDriving rc left join fetch rc.route rcr left join fetch d.currentDriving c left join fetch c.route r where d.active = true")
    List<Driver> findAllActiveDrivers();

    @Query("select d from Driver d left join fetch d.workingIntervals w where d.email = ?1")
    Optional<Driver> findByEmailWithWorkingIntervals(String email);

    @Query("select d from Driver d left join fetch d.currentDriving cd left join fetch d.nextDriving nd left join fetch d.reservedFromClientDriving rd where d.email = ?1")
    Optional<Driver> findByEmailWithAllDrivings(String email);

    @Query("select d from Driver d left join fetch d.currentDriving cd left join fetch cd.locations l left join fetch cd.route r where d.email = ?1")
    Optional<Driver> findByEmailWithCurrentDrivingAndLocations(String email);

    @Query(value="select d from Driver d left join fetch d.nextDriving nd left join fetch nd.locations ln left join fetch nd.route rn where d.email = ?1")
    Optional<Driver> findByEmailWithNextDrivingAndLocations(String email);

    @Query("select d from Driver d" +
            " left join fetch d.currentDriving cd" +
            " left join fetch cd.passengers pp" +
            " left join fetch d.nextDriving nd" +
            " where d.email = ?1")
    Optional<Driver> findByEmailWithCurrentDriving(String email);

    @Query("select d from Driver d left join fetch d.nextDriving nd where d.email = ?1")
    Optional<Driver> findByEmailWithNextDriving(String email);

    @Query("select d from Driver d" +
            " left join fetch d.currentDriving cd left join fetch cd.route cr" +
            " left join fetch d.nextDriving nd left join fetch nd.route nr" +
            " left join fetch d.reservedFromClientDriving rd left join fetch rd.route rr" +
            " where d.email = ?1")
    Optional<Driver> findByEmailWithCurrentNextAndReservedDriving(String email);

    @Query("select d from Driver d left join fetch d.reservedFromClientDriving r where d.email = ?1")
    Optional<Driver> findByEmailWithReservation(String driverEmail);
}
