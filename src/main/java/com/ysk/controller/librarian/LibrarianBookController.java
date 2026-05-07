package com.ysk.controller.librarian;

import com.ysk.dto.book.AladinItemDto;
import com.ysk.service.book.BookApiService;
import com.ysk.service.book.BookService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/librarian/book")
@RequiredArgsConstructor
public class LibrarianBookController {

  private final BookApiService bookApiService;
  private final BookService bookService;


    /**
     * 사서용 알라딘 도서 검색 페이지 이동 및 결과 처리
     */
    @GetMapping("/search")
    public String searchAladinBooks(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        
        // 검색어가 입력되었을 때만 알라딘 API를 호출합니다.
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<AladinItemDto> searchResults = bookApiService.searchBooks(keyword);
            
            // 화면에 검색 결과 리스트와, 검색창에 유지할 키워드를 넘겨줍니다.
            model.addAttribute("searchResults", searchResults);
            model.addAttribute("keyword", keyword);
        }

        // templates/librarian/book/search.html 로 이동
        return "librarian/book/search"; 
    }


  /**
   * 사서가 버튼을 클릭하면 실행되는 입고 처리 로직 메서드
   */
  @PostMapping("/save")
  public String saveBook(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("isbn13") String isbn13,
            @RequestParam("coverUrl") String coverUrl){

    bookService.saveBookToLibrary(title, author, isbn13, coverUrl);
    return "redirect:/libiarian/book/search";
  }
}