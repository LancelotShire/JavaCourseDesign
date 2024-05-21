package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics,Integer> {

    @Query(value = "from Statistics s where ?1 = '' or s.statisticsId = ?1")
    List<Statistics> findByStatisticsId(String statisticsId);

    @Query(value = "from Statistics s where ?1 = '' or s.studentName like %?1% or s.studentNum like %?1%")
    List<Statistics> findByStudentNumName(String numName);
}
