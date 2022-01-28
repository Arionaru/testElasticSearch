package ru.ariona.elasticsearchdemoproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.ariona.elasticsearchdemoproject.domain.Document;
import ru.ariona.elasticsearchdemoproject.service.SearchService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    //метод для добавления документов в индекс
    @PutMapping("/documents")
    public String addDocument(@RequestBody Document document) throws IOException {
        String uuid = UUID.randomUUID().toString();
        document.setId(uuid);
        searchService.updateDocument(document);
        return uuid;
    }

    //поиск по имени
    @GetMapping("/byName")
    public List<Document> searchByName(@RequestParam String name) throws IOException {
        return searchService.searchByName(name);
    }

    //поиск по описанию
    @GetMapping("/byAnnotation")
    public List<Document> searchByAnnotation(@RequestParam String annotation) throws IOException {
        return searchService.searchByAnnotation(annotation);
    }

    //поиск в имени по фразе
    @GetMapping("/byPhrase")
    public List<Document> searchByPhrase(@RequestParam String text) throws IOException {
        return searchService.searchByPhrase(text);
    }

    //поиск по имени и описанию
    @GetMapping("/multi")
    public List<Document> multiSearch(@RequestParam String text) throws IOException {
        return searchService.multiMatchQuery(text);
    }
}

