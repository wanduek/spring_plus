package org.example.expert.domain.todo.service;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;

import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final JPAQueryFactory jpaQueryFactory;

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    @Transactional(readOnly = true)
    public Page<TodoResponse> getTodos(int page, int size, LocalDate startDate, LocalDate endDate, String weather) {
        Pageable pageable = PageRequest.of(page - 1, size);

       if (startDate != null && endDate != null) {
           LocalDateTime startDateTime = startDate.atStartOfDay();
           LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
           return todoRepository.findAllByCreatedAtBetween(startDateTime, endDateTime, pageable)
                   .map(todo -> new TodoResponse(
                           todo.getId(),
                           todo.getTitle(),
                           todo.getContents(),
                           todo.getWeather(),
                           new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                           todo.getCreatedAt(),
                           todo.getModifiedAt()
                   ));
       }

       // 날씨 조건만 주어진 경우
       if (weather != null) {
           return todoRepository.findTodoByWeather(weather, pageable)
                   .map(todo -> new TodoResponse(
                           todo.getId(),
                           todo.getTitle(),
                           todo.getContents(),
                           todo.getWeather(),
                           new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                           todo.getCreatedAt(),
                           todo.getModifiedAt()
                   ));
       }

        return todoRepository.findAllByOrderByModifiedAtDesc(pageable)
              .map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    @Transactional(readOnly = true)
    public Page<TodoSearchResponse> searchTodos(String keyword, LocalDateTime startDate, LocalDateTime endDate, String nickname, Pageable pageable) {
        QTodo todo = QTodo.todo;
        QComment comment = QComment.comment;

        List<TodoSearchResponse> results = jpaQueryFactory
                .select(Projections.constructor(TodoSearchResponse.class,
                        todo.title,
                        todo.user.count(),
                        comment.count()
                ))
                .from(todo)
                .leftJoin(todo.user)
                .leftJoin(todo.comments, comment)
                .where(
                        todo.title.containsIgnoreCase(keyword)
                                .or(todo.user.nickname.containsIgnoreCase(nickname))
                                .and(todo.createdAt.between(startDate, endDate))
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        todo.title.containsIgnoreCase(keyword)
                                .or(todo.user.nickname.containsIgnoreCase(nickname))
                                .and(todo.createdAt.between(startDate, endDate))
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }


    @Transactional(readOnly = true)
    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }
}
