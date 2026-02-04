package com.ysk.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(updatable = false)
    private LocalDateTime regDatetime;  // 등록일자

    private LocalDateTime modDatetime;  // 수정일자

    // Insert 되기 직전에 실행
    @PrePersist
    public void onPrePersist() {
        // 1. 나노초(소수점)를 0으로 싹 뚝 자릅니다. (초 단위까지만 저장)
        this.regDatetime = LocalDateTime.now().withNano(0);
        
        // 2. modDatetime은 여기서 설정을 안 하니까 NULL로 들어갑니다!
    }

    // 2. Update 되기 직전에 실행
    @PreUpdate
    public void onPreUpdate() {
        // 수정될 때만 modDatetime에 값 저장 And 소수점 제거
        this.modDatetime = LocalDateTime.now().withNano(0);
    }
}