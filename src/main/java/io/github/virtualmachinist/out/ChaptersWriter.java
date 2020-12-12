package io.github.virtualmachinist.out;

import io.github.virtualmachinist.in.Chapter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface ChaptersWriter {

  void writeChapters(List<Chapter> chapters, Writer writer) throws IOException;

}
