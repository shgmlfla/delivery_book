package com.example.dbook.book.service;

import com.example.dbook.book.dto.HotTrendBookDto;
import com.example.dbook.main.service.BookApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookApiService {

    private final BookApiClient bookApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<HotTrendBookDto> getHotTrendBooks(String searchDt) {
        String json = bookApiClient.getHotTrend(searchDt);

        try {
            JsonNode root = objectMapper.readTree(json);

            System.out.println(root.toPrettyString());

            JsonNode docs = root
                    .path("response")
                    .path("results")
                    .get(0)
                    .path("result")
                    .path("docs");

            List<HotTrendBookDto> list = new ArrayList<>();
            for (JsonNode node : docs){
                JsonNode docNode = node.path("doc");
                list.add(objectMapper.treeToValue(docNode, HotTrendBookDto.class));
            }
            System.out.println("list"+list);
            return list;

        } catch (Exception e) {
            throw new RuntimeException("HotTrend JSON 파싱 실패: " + e.getMessage());
        }
    }

}

