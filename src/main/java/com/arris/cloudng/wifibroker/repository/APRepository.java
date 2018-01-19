package com.arris.cloudng.wifibroker.repository;

import com.arris.cloudng.wifibroker.domain.AP;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AP entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APRepository extends JpaRepository<AP, Long>, JpaSpecificationExecutor<AP> {

}
