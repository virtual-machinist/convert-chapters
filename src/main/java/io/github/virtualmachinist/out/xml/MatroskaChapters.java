package io.github.virtualmachinist.out.xml;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static io.github.virtualmachinist.out.xml.MatroskaChapters.EditionEntry;
import static io.github.virtualmachinist.out.xml.MatroskaChapters.EditionEntry.ChapterAtom;
import static io.github.virtualmachinist.out.xml.MatroskaChapters.EditionEntry.ChapterAtom.ChapterDisplay;

@Data
// @Introspected doesn't work with nested static classes after ChapterAtom (i.e. ChapterDisplay is skipped). a bug?
@Introspected(classes = {MatroskaChapters.class, EditionEntry.class, ChapterAtom.class, ChapterDisplay.class})
@JacksonXmlRootElement(localName = "Chapters")
public class MatroskaChapters {

  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "EditionEntry")
  private List<EditionEntry> editions = new ArrayList<>();

  @Data
  public static class EditionEntry {

    @JacksonXmlProperty(localName = "EditionUID")
    private Long editionUid;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "ChapterAtom")
    private List<ChapterAtom> chapterAtoms = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChapterAtom {

      @JsonFormat(pattern = "HH:mm:ss.SSS")
      @JacksonXmlProperty(localName = "ChapterTimeStart")
      private LocalTime chapterTimeStart;

      @JsonFormat(pattern = "HH:mm:ss.SSS")
      @JacksonXmlProperty(localName = "ChapterTimeEnd")
      private LocalTime chapterTimeEnd;

      @JacksonXmlProperty(localName = "ChapterDisplay")
      private ChapterDisplay chapterDisplay;

      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      public static class ChapterDisplay {

        @JacksonXmlProperty(localName = "ChapterString")
        private String chapterString;

        @JacksonXmlProperty(localName = "ChapterLanguage")
        private String chapterLanguage;

      }

    }

  }

}
