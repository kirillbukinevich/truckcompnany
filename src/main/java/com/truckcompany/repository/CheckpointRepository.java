package com.truckcompany.repository;

import com.truckcompany.domain.Checkpoint;
import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.Waybill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CheckpointRepository extends JpaRepository<Checkpoint,Long>{
    Optional<Checkpoint> findOneById(Long checkpointId);

    List<Checkpoint> deleteAllInBatchByRouteList(RouteList routeList);

    @Query(value = "select distinct checkpoint from Checkpoint as checkpoint " +
        "left join fetch checkpoint.routeList as routeList " +
        "where checkpoint.routeList=?1")
    List<Checkpoint> findByRouteList(RouteList routeList);

    List<Checkpoint> findByRouteListId(Long id);//// TODO: 20.11.2016
}
