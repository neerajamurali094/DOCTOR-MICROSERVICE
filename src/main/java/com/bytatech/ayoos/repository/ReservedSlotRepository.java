package com.bytatech.ayoos.repository;

import com.bytatech.ayoos.domain.ReservedSlot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReservedSlot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservedSlotRepository extends JpaRepository<ReservedSlot, Long> {

}
