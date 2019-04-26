package com.bytatech.ayoos.repository;

import com.bytatech.ayoos.domain.SessionInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SessionInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SessionInfoRepository extends JpaRepository<SessionInfo, Long> {

}
