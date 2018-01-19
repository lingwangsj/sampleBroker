package com.arris.cloudng.wifibroker.repository;

import com.arris.cloudng.wifibroker.domain.Wlan;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Wlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WlanRepository extends JpaRepository<Wlan, Long>, JpaSpecificationExecutor<Wlan> {

}
