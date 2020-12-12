package io.github.virtualmachinist.in;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Singleton
public class ChaptersParser {

  private final ObjectMapper objectMapper;

  @Inject
  public ChaptersParser(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public List<Chapter> parse(Reader reader) throws IOException {
    JsonNode node = objectMapper.reader().readTree(reader);
    JsonNode chaptersNode = node.get("chapters");
    if (chaptersNode != null && chaptersNode.isArray()) {
      return List.of(objectMapper.convertValue(chaptersNode, Chapter[].class));
    }
    return List.of();
  }

}
