package com.example.todo.todoapi.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoModifyRequestDTO {

    @NotBlank
    @Size(min = 2, max = 10)
    private String title;
    private boolean done;
}
