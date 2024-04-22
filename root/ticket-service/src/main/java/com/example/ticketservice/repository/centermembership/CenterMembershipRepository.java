package com.example.ticketservice.repository.centermembership;

import com.example.ticketservice.entity.CenterMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CenterMembershipRepository extends JpaRepository<CenterMembership, Long>, CenterMembershipRepositoryCustom {
    List<CenterMembership> findAllByCenterId(Long centerId);
}
