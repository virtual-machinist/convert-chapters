package io.github.virtualmachinist.in;

import java.io.FilterReader;
import java.io.Reader;

public class CloseSuppressingReader extends FilterReader {

  private CloseSuppressingReader(Reader in) {
    super(in);
  }

  @Override
  public void close() {
    // do nothing
  }

  public static CloseSuppressingReader of(Reader reader) {
    return new CloseSuppressingReader(reader);
  }

}
