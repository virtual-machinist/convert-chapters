package io.github.virtualmachinist.out.xml;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.virtualmachinist.in.Chapter;
import io.github.virtualmachinist.out.ChaptersWriter;
import io.github.virtualmachinist.out.xml.MatroskaChapters.EditionEntry;
import io.github.virtualmachinist.out.xml.MatroskaChapters.EditionEntry.ChapterAtom;
import io.github.virtualmachinist.out.xml.MatroskaChapters.EditionEntry.ChapterAtom.ChapterDisplay;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Random;

public class XmlChaptersWriter implements ChaptersWriter {

  private static final String NEW_LINE = String.format("%n");

  private final String language;
  private final XmlMapper xmlMapper;
  private final Random random;

  public XmlChaptersWriter(String language) {
    this.language = language;
    this.xmlMapper = XmlMapper.xmlBuilder()
        .addModule(new JavaTimeModule())
        .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
        .build();
    this.random = new Random();
  }

  @Override
  public void writeChapters(List<Chapter> chapters, Writer writer) throws IOException {
    writer
        .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(NEW_LINE)
        .append("<!DOCTYPE Chapters SYSTEM \"matroskachapters.dtd\">").append(NEW_LINE);
    xmlMapper.writeValue(writer, convert(chapters));
  }

  MatroskaChapters convert(List<Chapter> chapters) {
    EditionEntry edition = new EditionEntry();
    edition.setEditionUid(random.nextLong());
    for (Chapter chapter : chapters) {
      ChapterAtom atom = new ChapterAtom();
      atom.setChapterDisplay(new ChapterDisplay(chapter.getTitle(), language));
      atom.setChapterTimeStart(chapter.getStartTime());
      atom.setChapterTimeEnd(chapter.getEndTime());
      edition.getChapterAtoms().add(atom);
    }
    MatroskaChapters matroskaChapters = new MatroskaChapters();
    matroskaChapters.getEditions().add(edition);
    return matroskaChapters;
  }

}
