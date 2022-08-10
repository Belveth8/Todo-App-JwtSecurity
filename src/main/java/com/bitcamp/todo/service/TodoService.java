package com.bitcamp.todo.service;

import com.bitcamp.todo.model.TodoEntity;
import com.bitcamp.todo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TodoService {

    @Autowired
    public TodoRepository repository;


    // 생성 C
    public List<TodoEntity> create(final TodoEntity entity){
        // 1. 저장할 Entity가 유효한 지 확인
        validate(entity);
        repository.save(entity);

        log.info("Entity Id : { } is saved",entity.getId());
        return repository.findByUserId(entity.getUserId());
    }


    // 조회 R
    public List<TodoEntity> retrieve(final String userId) {

        return repository.findByUserId(userId);
    }


    // 수정 U
    public List<TodoEntity> update (final TodoEntity entity) {
        validate(entity);
        final Optional<TodoEntity> original = repository.findById(entity.getId());
        // 외부에서 요청된 data가 존재하면
        original.ifPresent(todo-> {
            // 반환된 TodoEntity가 존재하면 값을 새 entity로 덮어 씌운다.
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            // DB에 새 값을 저장한다.
            repository.save(todo);
        });

        return retrieve(entity.getUserId());
    }


    // 삭제 D
    public List<TodoEntity> delete (final TodoEntity entity) {
        // 유효성 체크
        validate(entity);
        try {
         // 엔티티 삭제
         repository.delete(entity);
        } catch (Exception e) {
            // exception 발생 시 id와 exception을 로깅한다.
            log.error("Error Deleting entity", entity.getId(), e);
            throw new RuntimeException("Error Deleting entity" + entity.getId());
        }
        // 새 Todo List를 가져와 리턴 ( 삭제된 후 List 반환 )
        return retrieve(entity.getUserId());
    }

    // 예외처리
    private void validate(final TodoEntity entity) {
        // 유효성 검증
        if(entity == null) {
            log.warn("Entity can not be null");
            // 예외 처리 error가 나도 program이 비정상적으로 종료되지 않기 윟...
            throw new RuntimeException("Entity can not be null");
        }
        if (entity.getUserId() == null) {
            log.warn("Unknown user");
            throw new RuntimeException("Unknown user");
        }
    }

}
