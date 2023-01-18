package com.example.todo.todoapi.service;

// 중간처리 잡일 담당

import com.example.todo.todoapi.dto.request.TodoCreateRequestDTO;
import com.example.todo.todoapi.dto.request.TodoModifyRequestDTO;
import com.example.todo.todoapi.dto.response.TodoDetailResponseDTO;
import com.example.todo.todoapi.dto.response.TodoListResponseDTO;
import com.example.todo.todoapi.entity.TodoEntity;
import com.example.todo.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
    // 서비스는 컨트롤러에게 받은 요청이나 응답을 적절한것으로 선택해서 보내주는 역할이다.
public class TodoService {

    // TodoRepository에 상속받는다
    private final TodoRepository todoRepository;

    // 할 일 목록 조회
    // Listentity 를  todoDetailResponse로 바꿔준다.
    public TodoListResponseDTO retrieve() {
        List<TodoEntity> entityList = todoRepository.findAll();

        List<TodoDetailResponseDTO> dtoList = entityList.stream()
                .map(TodoDetailResponseDTO::new)
                .collect(Collectors.toList());

        return TodoListResponseDTO.builder()
                .todos(dtoList)
                .build();
    }

    // 할 일 등록
    // 클라이언트가 던진 데이터를 DB에 넣는 역할이다.
    public TodoListResponseDTO create(final TodoCreateRequestDTO createRequestDTO){
        todoRepository.save(createRequestDTO.todoEntity());
        log.info("할 일이 저장되었습니다. 제목 : {}", createRequestDTO.getTitle());
        return retrieve();
    }

    // 할 일 수정 (제목, 할일 완료여부)
    // 수정하고 싶으면 id랑 수정내용을 줘라 라는 의미
    public TodoListResponseDTO update(
            // 컨트롤러에게 아이디를 받아옴
            final String id,
            final TodoModifyRequestDTO modifyRequestDTO
    ) {
        // 받아온 아이디를 조회해봄
        // DTO는 클라이언트가 보낸 데이터
        // 조회 > setter로 수정하고 다시 조회 > 저장
        Optional<TodoEntity> targetEntity = todoRepository.findById(id);
        targetEntity.ifPresent(entity ->{
            entity.setTitle(modifyRequestDTO.getTitle());
            entity.setDone(modifyRequestDTO.isDone());

            todoRepository.save(entity);
        });

        // 수정한 목록을 다시 가져다 주는것이므로
        return retrieve();
    }
    // 할 일 삭제
    public TodoListResponseDTO delete(final String id){
        try {
            todoRepository.deleteById(id);
        } catch (Exception e) {
            // 서버에 기록할 로그
            log.error("id가 존재하지 않아 삭제에 실패하였습니다. - ID: {}, err: {}", id, e.getMessage());

            // 클라이언트에게 알려줄 에러로그
            throw new RuntimeException("id가 존재하지 않아 삭제에 실패하였습니다.");
        }

        return retrieve();
    }
}