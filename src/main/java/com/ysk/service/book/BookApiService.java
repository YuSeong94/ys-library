// package com.ysk.service.book;

// import lombok.RequiredArgsConstructor;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.util.UriComponentsBuilder;

// import com.ysk.dto.book.AladinItemDto;
// import com.ysk.dto.book.AladinResponseDto;

// import java.net.URI;
// import java.util.Collections;
// import java.util.List;

// @Service
// @RequiredArgsConstructor
// public class BookApiService {

//     private final RestTemplate restTemplate;

//     @Value("${aladin.api.key}")
//     private String ttbKey;

//     @Value("${aladin.api.search-url}")
//     private String searchUrl;

//     /**
//      * 알라딘 상품 검색 API 호출
//      * @param keyword 
//      * @return 
//      */
//     public List<AladinItemDto> searchBooks(String keyword) {
        
//         URI uri = UriComponentsBuilder.fromHttpUrl(searchUrl)
//                 .queryParam("ttbkey", ttbKey)
//                 .queryParam("Query", keyword)
//                 .queryParam("QueryType", "Keyword")
//                 .queryParam("MaxResults", 10)
//                 .queryParam("start", 1)
//                 .queryParam("SearchTarget", "Book")
//                 .queryParam("output", "js") // JSON
//                 .queryParam("Version", "20131101")
//                 .build()
//                 .encode()
//                 .toUri();

//       // 여기서 String.class 대신 AladinResponseDto.class로 받으면 자동 파싱
//       AladinResponseDto response = restTemplate.getForObject(uri, AladinResponseDto.class);

//       // 결과가 비어있지 않으면 item 리스트를 반환, 없으면 빈 리스트 반환
//       return response != null && response.getItem() != null ? response.getItem() : Collections.emptyList();
//     }
// }