package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics,Integer> {

    @Query(value="from course where ?1=0")
    List<Statistics> findByStudentNum(Integer studentNum);
}
