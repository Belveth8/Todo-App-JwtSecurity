package com.bitcamp.todo.controller;

import com.bitcamp.todo.dto.ResponseDTO;
import com.bitcamp.todo.dto.TodoDTO;
import com.bitcamp.todo.model.TodoEntity;
import com.bitcamp.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service; // service는 jparepository를 상속받았음 가져다 쓴다는거임

    // 생성 (Create Todo 구현)
    @PostMapping                                // security 에서 인증된 userid 가져오기
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId,
                                        @RequestBody TodoDTO dto) {

        try {
//            String temporaryUserId = "temporary-user";

            // 1. TodoEntity 로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);

            // 2. Id를 null로 초기화 ( id는 db-table에서 생성해주는거라 생성 당시에는 id가 없어야 하기 때문 )
            entity.setId(null);

            // 3. 인증된 유저 ID를 설정해준다
            entity.setUserId(userId);

            // 4. Service를 이용해 entity를 생성한다. ( List를 반환 )
            List<TodoEntity> entities = service.create(entity);

            // 5. 자바 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO 리스트로 반환
            //                                    //람다식 인스턴스( entity -> new TodoDTO(entity) )
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 6. 변환된 TodoDTO리스트를 이용해 ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().resList(dtos).build();

            // 7. ResponseDTO를 리턴
            return ResponseEntity.ok().body(response);


        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 조회 ( CRUD -> R  Retrieve Todo 구현 )
    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
//        String temporaryUserId = "temporary-user";

        List<TodoEntity> entities = service.retrieve(userId);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().resList(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    // 수정 ( Update Todo 구현 )
    @PutMapping
    public ResponseEntity<?> updateTodo(@RequestBody TodoDTO dto) {
        String temporaryUserId = "temporary-user";

        TodoEntity entity = TodoDTO.toEntity(dto);
        entity.setUserId(temporaryUserId);
        List<TodoEntity> entities = service.update(entity);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        // 응답 객체로 변환하기
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().resList(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    // 삭제 D
    @DeleteMapping
    public ResponseEntity<?> deleteTodo (@AuthenticationPrincipal String userId,
                                         @RequestBody TodoDTO dto) {
        // 삭제할 data가 없을 경우 등 예외처리
        try {
//            String temporaryUserId = "temporary-user";

            TodoEntity entity = TodoDTO.toEntity(dto);
            entity.setUserId(userId);
            List<TodoEntity> entities = service.delete(entity);
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().resList(dtos).build();

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            String error = e.getMessage();
            // 삭제 되기 전 DTO
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }

    }

}
