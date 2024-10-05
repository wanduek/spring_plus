package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom, QuerydslPredicateExecutor<Todo> {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    // 기간에 맞는 데이터를 페이지네이션과 함께 조회
    @Query("SELECT t FROM Todo t WHERE " +
            "(t.createdAt >= :startDate AND t.createdAt < :endDate)" +
            "ORDER BY t.modifiedAt DESC ")
    Page<Todo> findAllByCreatedAtBetween(@RequestParam("startDate") LocalDateTime startDate,
                                         @RequestParam("endDate") LocalDateTime endDate,
                                         Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE t.weather = :weather ORDER BY t.modifiedAt DESC")
    Page<Todo> findTodoByWeather(@Param("weather") String weather, Pageable pageable);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);


}
