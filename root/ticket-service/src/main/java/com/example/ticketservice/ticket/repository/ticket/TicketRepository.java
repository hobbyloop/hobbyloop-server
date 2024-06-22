package com.example.ticketservice.ticket.repository.ticket;

import com.example.ticketservice.ticket.entity.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {

    List<Ticket> findAllByCenterId(long centerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Ticket t where t.id = :ticketId")
    Optional<Ticket> findForUpdate(@Param("ticketId") long ticketId);

    List<Ticket> findAllByCenterIdAndIsUploadTrueOrderByCalculatedPriceAsc(long centerId);

    List<Ticket> findAllByCenterIdAndIsUploadTrueOrderByCreatedAtDesc(long centerId);

    boolean existsByCenterId(long centerId);

    List<Ticket> findAllByCenterIdOrderByCalculatedPrice(long centerId);

    @Modifying(clearAutomatically = true)
    @Query("update Ticket t set t.centerName = :centerName, t.logoImageUrl = :logoImageUrl, t.address = :address, t.latitude = :latitude, t.longitude = :longitude where t.centerId = :centerId")
    void updateCenterAddressInfo(@Param("centerId") Long centerId,
                                 @Param("centerName") String centerName,
                                 @Param("logoImageUrl") String logoImageUrl,
                                 @Param("address") String address,
                                 @Param("latitude") Double latitude,
                                 @Param("longitude") Double longitude);
}
