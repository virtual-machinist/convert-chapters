package io.github.virtualmachinist;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.micronaut.context.env.Environment.CLI;
import static io.micronaut.context.env.Environment.TEST;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;
import static org.assertj.core.api.Assertions.assertThat;

class ConvertChaptersCommandTest {

  @TempDir
  Path tempDir;

  private ByteArrayOutputStream out;

  @BeforeEach
  void setUp() {
    out = new ByteArrayOutputStream();
  }

  @Test
  void printsHelp() {
    redirectSystemOut();

    try (ApplicationContext ctx = ApplicationContext.run(CLI, TEST)) {
      String[] args = new String[]{"-h"};
      PicocliRunner.call(ConvertChaptersCommand.class, ctx, args);

      assertThat(out.toString(UTF_8))
          .contains("convert-chapters", "Converts YouTube chapters to OGG or Matroska format");
    }
  }

  @Test
  void readsFromSystemInWritesToSystemOutXml() {
    mockSystemIn("{\"chapters\":[{\"title\":\"Test\",\"start_time\":0.0,\"end_time\":61.0}]}");
    redirectSystemOut();

    try (ApplicationContext ctx = ApplicationContext.run(CLI, TEST)) {
      String[] args = new String[]{"-t", "XML", "-l", "eng"};
      assertThat(PicocliRunner.call(ConvertChaptersCommand.class, ctx, args)).isZero();
      assertThat(out.toString(UTF_8))
          .startsWith("<?xml")
          .contains("00:00:00.000", "00:01:01.000", "Test", "eng");
    }
  }
  @Test
  void readsFromSystemInWritesToSystemOutOgg() {
    mockSystemIn("{\"chapters\":[{\"title\":\"Test\",\"start_time\":0.0,\"end_time\":61.0}]}");
    redirectSystemOut();

    try (ApplicationContext ctx = ApplicationContext.run(CLI, TEST)) {
      String[] args = new String[]{"-t", "OGG"};
      assertThat(PicocliRunner.call(ConvertChaptersCommand.class, ctx, args)).isZero();
      assertThat(out.toString(UTF_8))
          .startsWith("CHAPTER001")
          .contains("00:00:00.000", "Test");
    }
  }

  @Test
  void failsOnNoForceAndFileExists() throws Exception {
    mockSystemIn("{\"chapters\":[]}");
    redirectSystemOut();

    Path target = tempDir.resolve("foobar.xml");
    Files.writeString(target, "test", CREATE_NEW, WRITE);

    try (ApplicationContext ctx = ApplicationContext.run(CLI, TEST)) {
      String[] args = new String[]{"-o", target.toAbsolutePath().toString(), "-t", "XML", "-l", "eng"};
      assertThat(PicocliRunner.call(ConvertChaptersCommand.class, ctx, args)).isNull();
      assertThat(out.toString()).isEmpty();
    }
  }

  @Test
  void writesToExistingFileWithForceOption() throws Exception {
    mockSystemIn("{\"chapters\":[]}");

    Path target = tempDir.resolve("foobar.xml");
    Files.writeString(target, "test", CREATE_NEW, WRITE);

    try (ApplicationContext ctx = ApplicationContext.run(CLI, TEST)) {
      String[] args = new String[]{"-o", target.toAbsolutePath().toString(), "-t", "XML", "-l", "eng", "-f"};
      assertThat(PicocliRunner.call(ConvertChaptersCommand.class, ctx, args)).isZero();
      assertThat(Files.readString(target, UTF_8))
          .startsWith("<?xml");
    }
  }

  @Test
  void readsFromFile() throws Exception {
    String json = "{\"chapters\":[]}";
    Path source = tempDir.resolve("in.json");
    Files.writeString(source, json, CREATE_NEW, WRITE);

    redirectSystemOut();

    try (ApplicationContext ctx = ApplicationContext.run(CLI, TEST)) {
      String[] args = new String[]{"-i", source.toAbsolutePath().toString(), "-t", "XML", "-l", "eng"};
      assertThat(PicocliRunner.call(ConvertChaptersCommand.class, ctx, args)).isZero();
      assertThat(out.toString(UTF_8)).startsWith("<?xml");
    }
  }

  private void redirectSystemOut() {
    System.setOut(new PrintStream(out));
  }

  private void mockSystemIn(String json) {
    System.setIn(new ByteArrayInputStream(json.getBytes(UTF_8)));
  }

}
