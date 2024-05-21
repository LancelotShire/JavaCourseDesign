package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Integer> {
    @Query(value = "from Activity where ?1='' or student.person.num like %?1% or student.person.name like %?1% ")
    List<Activity> findActivityListByNumName(String numName);

    @Query(value = "from Activity where ?1='' or student.person.num like %?1% or student.person.name like %?1% ",
            countQuery = "SELECT count(student.person.num) from Activity where ?1='' or student.person.num like %?1% or student.person.name like %?1% ")
    Page<Activity> findActivityPageByNumName(String numName, Pageable pageable);

}
