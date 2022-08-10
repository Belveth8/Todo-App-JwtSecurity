package com.bitcamp.todo.model;

//model / dto
// model : db table ( orm )
// to : data transfer obj ( 응답객체  payload[data] 상태정보 )
// service : interface
//ui -> controller -> service -> model(field.db)  --변환--> dt

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity // java.Class를 엔티티로 지정
@Table(name="Todo")  // Table 이름지정 (대소문자상관없음)
public class TodoEntity {

    // Object Id @Id는 기본 키가 될 필드에 지정된다 field 데이터타입은 오브젝트형이여야 함
    // ( TodoEntity를 식별할 수 있는 고유식별자 id )
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // 정수자동생성 하고 필드 integer 로
    @GeneratedValue(generator = "uuid2") //범용 고유식별자 ID 자동생성 ( uuid= 자동생성 방식 )
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    private String id;

    // 맨위 클래스에 어노테이션으로 @Entity를 써놓으면
    // 프라이머리 키값을 제외하곤 @Column 생략해도 된다
    private String userId;
    private String title;
    private boolean done;
}
