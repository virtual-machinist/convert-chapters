package io.github.virtualmachinist.out.xml;

import io.github.virtualmachinist.in.Chapter;
import io.github.virtualmachinist.out.xml.MatroskaChapters.EditionEntry.ChapterAtom;
import io.github.virtualmachinist.out.xml.MatroskaChapters.EditionEntry.ChapterAtom.ChapterDisplay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmlunit.assertj3.XmlAssert;
import org.xmlunit.builder.Input;
import org.xmlunit.validation.Languages;
import org.xmlunit.validation.Validator;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.StringWriter;
import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class XmlChaptersWriterTests {

  private XmlChaptersWriter writer;
  private StringWriter out;
  private Validator validator;

  @BeforeEach
  void setUp() {
    URL resource = Objects.requireNonNull(getClass().getClassLoader().getResource("matroskachapters.dtd"));
    writer = new XmlChaptersWriter("und");
    out = new StringWriter();
    validator = Validator.forLanguage(Languages.XML_DTD_NS_URI);
    validator.setSchemaSources(new StreamSource(resource.toString()));
  }

  @Test
  void convertConvertsChapters() {
    List<Chapter> chapters = List.of(
        new Chapter("name 1", LocalTime.of(1, 1, 1), LocalTime.of(2, 2, 2)),
        new Chapter("name 2", LocalTime.of(2, 2, 2), LocalTime.of(3, 3, 3))
    );
    MatroskaChapters converted = writer.convert(chapters);
    assertThat(converted.getEditions()).hasSize(1);
    assertThat(converted.getEditions().get(0).getChapterAtoms())
        .containsExactly(
            new ChapterAtom(LocalTime.of(1, 1, 1), LocalTime.of(2, 2, 2), new ChapterDisplay("name 1", "und")),
            new ChapterAtom(LocalTime.of(2, 2, 2), LocalTime.of(3, 3, 3), new ChapterDisplay("name 2", "und"))
        );
  }

  @Test
  void writeChapterWritesValidXmlForEmptyChapters() throws Exception {
    writer.writeChapters(List.of(), out);
    assertThat(validator.validateInstance(Input.fromString(out.toString()).build()).isValid())
        .isTrue();
  }

  @Test
  void writeChapterWritesValidXmlForSingleChapter() throws Exception {
    writer.writeChapters(List.of(new Chapter("name 1", LocalTime.of(1, 1, 1), LocalTime.of(2, 2, 2))), out);
    Source xml = Input.fromString(out.toString()).build();
    assertThat(validator.validateInstance(xml).isValid())
        .isTrue();
    XmlAssert.assertThat(xml)
        .valueByXPath("Chapters/EditionEntry/ChapterAtom/ChapterDisplay/ChapterString/text()")
        .isEqualTo("name 1");
    XmlAssert.assertThat(xml)
        .valueByXPath("Chapters/EditionEntry/ChapterAtom/ChapterDisplay/ChapterLanguage/text()")
        .isEqualTo("und");
    XmlAssert.assertThat(xml)
        .valueByXPath("Chapters/EditionEntry/ChapterAtom/ChapterTimeStart/text()")
        .isEqualTo("01:01:01.000");
    XmlAssert.assertThat(xml)
        .valueByXPath("Chapters/EditionEntry/ChapterAtom/ChapterTimeEnd/text()")
        .isEqualTo("02:02:02.000");
  }

}