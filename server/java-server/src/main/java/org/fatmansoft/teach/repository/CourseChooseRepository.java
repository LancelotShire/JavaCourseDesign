package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.CourseChoose;
import org.fatmansoft.teach.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseChooseRepository extends JpaRepository<CourseChoose,Integer> {
    //precise searching
    @Query(value = "from CourseChoose where (?1=0 or student.studentId=?1) and (?2=0 or course.courseId=?2) ")
    List<CourseChoose> findByStudentCourse(Integer studentId, Integer courseId);

    @Query(value = "from CourseChoose where student.studentId=?1 and (?2=0 or course.name like %?2%)")
    List<CourseChoose> findByStudentCourse(Integer studentId, String courseName);

    @Query(value = "from CourseChoose where ?1=0 or student.studentId=?1")
    List<CourseChoose> findByStudentId(Integer studentId);

    @Query(value = "from CourseChoose where (?1=0 or student.person.num=?1) and (?2=0 or course.num=?2)")
    List<CourseChoose> findByStudentNumCourseNum(Integer studentNum, Integer courseNum);

    List<CourseChoose> findByStudent(Student student);
    Optional<CourseChoose> findByStudentAndCourse(Student student, Course course);

    List<CourseChoose> findByCourse(Course c);
}
