package com.arris.cloudng.wifibroker.repository;

import com.arris.cloudng.wifibroker.domain.APGroup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the APGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APGroupRepository extends JpaRepository<APGroup, Long>, JpaSpecificationExecutor<APGroup> {

}
