package com.ysk.service.book;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ysk.entity.book.Book;
import com.ysk.repository.book.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
  
  private final BookRepository bookRepository;

  @Transactional
  public void saveBookToLibrary(String title, String author, String isbn13, String coverUrl){

    Book newBook = Book.builder()
                        .title(title)
                        .author(author)
                        .isbn(isbn13)
                        .coverUrl(coverUrl)
                        .totalQuantity(1)
                        .availableQuantity(1)
                        .build();

    bookRepository.save(newBook);
  }

}
