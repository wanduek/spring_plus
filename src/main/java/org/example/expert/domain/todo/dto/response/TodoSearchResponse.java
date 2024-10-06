package org.example.expert.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoSearchResponse {
    private String title;
    private Long userCounts;
    private Long CommentCounts;
}
