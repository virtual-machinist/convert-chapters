package io.github.virtualmachinist.out.ogg;

import io.github.virtualmachinist.in.Chapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OggChaptersWriterTests {

  private OggChaptersWriter writer;
  private StringWriter out;

  @BeforeEach
  void setUp() {
    writer = new OggChaptersWriter();
    out = new StringWriter();
  }

  @Test
  void writesNoChapters() {
    writer.writeChapters(List.of(), out);
    assertThat(out.toString()).isEmpty();
  }

  @Test
  void writeChapterWritesSingleChapter() {
    Chapter chapter = new Chapter("name", LocalTime.of(1, 1, 1), LocalTime.of(2, 2, 2).plus(Duration.ofMillis(500)));
    writer.writeChapter(999, chapter, new PrintWriter(out));
    assertThat(out.toString()).isEqualTo("CHAPTER999=01:01:01.000%nCHAPTER999NAME=name%n", new Object[0]);
  }

}