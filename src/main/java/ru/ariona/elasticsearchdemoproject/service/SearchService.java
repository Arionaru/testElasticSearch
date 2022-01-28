package ru.ariona.elasticsearchdemoproject.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch.core.CreateRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ariona.elasticsearchdemoproject.domain.Document;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ElasticsearchClient elasticsearchClient;
    private static final String INDEX_NAME = "documents";
    private static final String NAME_FIELD = "name";
    private static final String ANNOTATION_FIELD = "annotation";

    public void updateDocument(Document document) throws IOException {
        CreateRequest.Builder<Document> builder = new CreateRequest.Builder<Document>();
        CreateRequest<Document> build = builder
                .id(document.getId())
                .index(INDEX_NAME)
                .document(document)
                .build();
        elasticsearchClient.create(build);

    }

    public List<Document> searchByName(String name) throws IOException {
        SearchResponse<Document> search = getDocumentSearchResponse(NAME_FIELD, name);

        return search.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    public List<Document> searchByAnnotation(String annotation) throws IOException {
        SearchResponse<Document> search = getDocumentSearchResponse(ANNOTATION_FIELD, annotation);

        return search.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    private SearchResponse<Document> getDocumentSearchResponse(String field, String value) throws IOException {
        FieldValue fieldValue = getFieldValue(value);
        MatchQuery.Builder builder = new MatchQuery.Builder();
        MatchQuery matchQuery = builder.query(fieldValue).field(field).build();
        return elasticsearchClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(v -> v.match(matchQuery)),
                Document.class);
    }

    private FieldValue getFieldValue(String value) {
        FieldValue.Builder fieldValueBuilder = new FieldValue.Builder();
        return fieldValueBuilder.stringValue(value).build();
    }

    public List<Document> searchByPhrase(String text) throws IOException {
        MatchPhraseQuery.Builder builder = new MatchPhraseQuery.Builder();
        MatchPhraseQuery matchQuery = builder.field(NAME_FIELD).query(text).build();
        SearchResponse<Document> search = elasticsearchClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(v -> v.matchPhrase(matchQuery)),
                Document.class);
        return search.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    public List<Document> multiMatchQuery(String text) throws IOException {
        MultiMatchQuery.Builder builder = new MultiMatchQuery.Builder();
        MultiMatchQuery query = builder.fields(List.of(NAME_FIELD, ANNOTATION_FIELD)).query(text).build();
        SearchResponse<Document> search = elasticsearchClient.search(s -> s
                        .index(INDEX_NAME)
                        .query(v -> v.multiMatch(query)),
                Document.class);
        return search.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    public List<Document> multiCondition(String name, String annotation) {
//        MatchPhraseQuery.Builder builder = new MatchPhraseQuery.Builder();
//        MatchPhraseQuery matchName = builder.field(NAME_FIELD).query(name).build();
//        MatchPhraseQuery matchAnnotation = builder.field(ANNOTATION_FIELD).query(annotation).build();
//
//
//        SearchResponse<Document> search = elasticsearchClient.search(s -> s
//                        .index(INDEX_NAME)
//                        .query(v -> v.bool()),
//                Document.class);
//        return search.hits().hits().stream()
//                .map(Hit::source)
//                .collect(Collectors.toList());
        return null;
    }
}
