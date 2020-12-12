package io.github.virtualmachinist.out;

import java.io.FilterWriter;
import java.io.Writer;

public class CloseSuppressingWriter extends FilterWriter {

  private CloseSuppressingWriter(Writer out) {
    super(out);
  }

  @Override
  public void close() {
    // do nothing
  }

  public static CloseSuppressingWriter of(Writer writer) {
    return new CloseSuppressingWriter(writer);
  }

}
