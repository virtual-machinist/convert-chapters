package io.github.virtualmachinist.in;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.inject.Inject;
import java.io.StringReader;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class ChaptersParserTests {

  @Inject
  ChaptersParser parser;

  @ValueSource(strings = {
      "{}",
      "{\"chapters\": null}",
      "{\"chapters\": []}",
      "{\"chapters\": {}}",
  })
  @ParameterizedTest
  void parsesEmptyChapters(String json) throws Exception {
    assertThat(parser.parse(new StringReader(json))).isEmpty();
  }

  @Test
  void parsesChapter() throws Exception {
    String json = "{\"chapters\":[{\"title\":\"Test\",\"start_time\":0.0,\"end_time\":61.0}]}";
    assertThat(parser.parse(new StringReader(json)))
        .hasSize(1)
        .first()
        .isEqualTo(new Chapter("Test", LocalTime.MIN, LocalTime.of(0, 1, 1)));
  }

}
