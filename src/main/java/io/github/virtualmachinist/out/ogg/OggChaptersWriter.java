package io.github.virtualmachinist.out.ogg;

import io.github.virtualmachinist.in.Chapter;
import io.github.virtualmachinist.out.ChaptersWriter;

import java.io.PrintWriter;
import java.io.Writer;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OggChaptersWriter implements ChaptersWriter {

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

  public void writeChapters(List<Chapter> chapters, Writer writer) {
    PrintWriter printer = new PrintWriter(writer);
    for (int i = 0; i < chapters.size(); i++) {
      int chapterNumber = i + 1;
      Chapter chapter = chapters.get(i);
      writeChapter(chapterNumber, chapter, printer);
    }
    printer.flush();
  }

  void writeChapter(int chapterNumber, Chapter chapter, PrintWriter printer) {
    printer.printf("CHAPTER%03d=%s%n", chapterNumber, TIME_FORMATTER.format(chapter.getStartTime()));
    printer.printf("CHAPTER%03dNAME=%s%n", chapterNumber, chapter.getTitle());
  }

}
