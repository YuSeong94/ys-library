package com.ysk.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ysk.entity.book.Book;

public interface BookRepository extends JpaRepository<Book, Long>{ 
  // JpaRepository<>를 상속받으면 기본적인 CRUD 기능이 자동 생성된다. 
}
