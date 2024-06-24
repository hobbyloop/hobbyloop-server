package com.example.ticketservice.ticket.repository.centermembership;

import com.example.ticketservice.ticket.entity.CenterMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CenterMembershipRepository extends JpaRepository<CenterMembership, Long>, CenterMembershipRepositoryCustom {
    List<CenterMembership> findAllByCenterId(Long centerId);

    int countByCenterId(Long centerId);

    boolean existsByMemberId(Long memberId);

    CenterMembership findByMemberIdAndCenterId(Long memberId, Long centerId);

    @Modifying(clearAutomatically = true)
    @Query("update CenterMembership cm set cm.memberName = :memberName, cm.phoneNumber = :phoneNumber, cm.birthday = :birthday where cm.memberId = :memberId")
    void updateMemberInfo(@Param("memberId") Long memberId,
                          @Param("memberName") String memberName,
                          @Param("phoneNumber")String phoneNumber,
                          @Param("birthday") LocalDate birthday);
}
