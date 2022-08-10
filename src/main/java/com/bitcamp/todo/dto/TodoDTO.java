package com.bitcamp.todo.dto;

import com.bitcamp.todo.model.TodoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {

    // 외부 클라에서 받을 그릇들
    // 아이템 생성 수정 삭제
    private String id;
    private String title;
    private boolean done;

    // 상수로 설정하면 재할당이 안됨
    // data 변형을 막아줌 * 외부에서 받아온 data 할당 시, 오염이 될 수도 있으니
    // 명시적으로 막는 의미
    public TodoDTO(final TodoEntity entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.done = entity.isDone();
        // boolean datatype은 is
    }

    // 시리얼라이즈? 할 객체를 만드는데..
    public static TodoEntity toEntity(final TodoDTO dto) {
        // builder
        return TodoEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .done(dto.isDone())
                .build();
    }



}
