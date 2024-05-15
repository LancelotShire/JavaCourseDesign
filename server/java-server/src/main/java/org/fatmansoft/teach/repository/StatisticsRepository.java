package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics,Integer> {

    @Query(value="from statistics where ")
}
