package com.arris.cloudng.wifibroker.repository;

import com.arris.cloudng.wifibroker.domain.WlanGroup;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the WlanGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WlanGroupRepository extends JpaRepository<WlanGroup, Long>, JpaSpecificationExecutor<WlanGroup> {
    @Query("select distinct wlan_group from WlanGroup wlan_group left join fetch wlan_group.members")
    List<WlanGroup> findAllWithEagerRelationships();

    @Query("select wlan_group from WlanGroup wlan_group left join fetch wlan_group.members where wlan_group.id =:id")
    WlanGroup findOneWithEagerRelationships(@Param("id") Long id);

}
