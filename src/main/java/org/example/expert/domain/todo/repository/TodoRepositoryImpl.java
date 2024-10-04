package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.user.entity.QUser;


import java.util.Optional;


@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory jqf;

    @Override
    public Optional<Todo> findByIdWithUser(Long id) {
        QTodo todo = QTodo.todo;
        QUser user = QUser.user;

        Todo result = jqf
                .selectFrom(todo)
                .leftJoin(todo.user, user)
                .fetchJoin()  // User와 fetch join으로 N+1 문제 방지
                .where(todo.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }
}