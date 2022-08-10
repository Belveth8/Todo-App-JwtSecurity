package com.bitcamp.todo.persistence;

import com.bitcamp.todo.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
// 쿼리를 실행할 인터페이스
// jparepository 란 interface를 상속 받아 다른 곳에서도 쓸 수 있게 repository 인터페이스에 상속
public interface TodoRepository extends JpaRepository<TodoEntity, String> {


    List<TodoEntity> findByUserId(String userId);

//    // ?1 은 메서드의 매개변수의 순서 위치
//    @Query("SELECT t FROM TodoEntity t WHERE t.userId = ?1")
//    TodoEntity findByUserIdQuery (String userId);

}
