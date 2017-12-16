package com.toptal.demo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.toptal.demo.entities.Jogging;

public interface JoggingRepository extends PagingAndSortingRepository<Jogging, Long> {

//    @Query(nativeQuery = true, value = "Select From Jogging where j.email = :email")
    List<Jogging> findByUserEmail(@Param("email") final String email, Pageable pageReguest);

    List<Jogging> findByUserEmailAndDateAfterAndDateBeforeOrderByDate(final String email, final Date startDate, final Date endDate);

    List<Jogging> findByUserEmailAndDateAfterAndDateBefore(final String email, final Date startDate, final Date endDate);

    @Query(nativeQuery = true, value = "select j.date, j.distance from user u inner join jogging j on u.id = j.user_id where u.email = :email order by j.distance desc limit 1")
    List<Object[]> getMaxDistanceDateForAUser(@Param("email") final String email);

    @Query(nativeQuery = true, value = "select coalesce(sum(period_in_minutes), 0) from jogging inner join user where user.id = jogging.user_id and user.email = :email group by user.id")
    Double getSumTimeForAUser(@Param("email") final String email);

}
