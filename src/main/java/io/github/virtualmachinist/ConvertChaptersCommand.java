package io.github.virtualmachinist;

import io.github.virtualmachinist.in.Chapter;
import io.github.virtualmachinist.in.ChaptersParser;
import io.github.virtualmachinist.in.CloseSuppressingReader;
import io.github.virtualmachinist.out.ChaptersWriter;
import io.github.virtualmachinist.out.CloseSuppressingWriter;
import io.github.virtualmachinist.out.ogg.OggChaptersWriter;
import io.github.virtualmachinist.out.xml.XmlChaptersWriter;
import io.micronaut.configuration.picocli.PicocliRunner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import static io.github.virtualmachinist.WriterType.OGG;
import static io.github.virtualmachinist.WriterType.XML;
import static java.nio.charset.StandardCharsets.UTF_8;

@Command(name = "convert-chapters", version = "0.1", mixinStandardHelpOptions = true,
    description = "Converts YouTube chapters to OGG or Matroska format.", showDefaultValues = true)
public class ConvertChaptersCommand implements Callable<Integer> {

  @Option(names = {"-i", "--in"}, description = "Input file. Defaults to standard in.")
  Path inputLocation;

  @Option(names = {"-o", "--out"}, description = "Output file. Defaults to standard out.")
  Path outputLocation;

  @Option(names = {"-f", "--force"}, description = "Overwrite output file if exists.")
  boolean forceOutput;

  @Option(names = {"-t", "--type"}, required = true, defaultValue = "XML",
      description = "Output format. OGG for OGG format, XML for Matroska XML.")
  WriterType type;

  @Option(names = {"-l", "--chapter-language"}, required = true, defaultValue = "und",
      description = "Matroska chapter language. Should be a valid BCP 47 code.")
  String chapterLanguage;

  @Inject
  ChaptersParser parser;

  public static void main(String[] args) {
    Integer result = PicocliRunner.call(ConvertChaptersCommand.class, args);
    System.exit(result != null ? result : 255);
  }

  @Override
  public Integer call() throws Exception {
    try (Reader input = prepareInput();
         Writer output = prepareOutput()) {
      ChaptersWriter writer = prepareWriter();
      List<Chapter> parsed = parser.parse(input);
      writer.writeChapters(parsed, output);
      output.flush();
    }
    return 0;
  }

  private Reader prepareInput() throws IOException {
    if (inputLocation == null) {
      return CloseSuppressingReader.of(new InputStreamReader(System.in, UTF_8));
    }
    if (Files.isReadable(inputLocation)) {
      return Files.newBufferedReader(inputLocation, UTF_8);
    }
    throw new IllegalArgumentException("Unsupported location: " + inputLocation);
  }

  private Writer prepareOutput() throws IOException {
    if (outputLocation == null) {
      return CloseSuppressingWriter.of(new OutputStreamWriter(System.out, UTF_8));
    }
    Set<StandardOpenOption> options = EnumSet.of(StandardOpenOption.WRITE);
    if (forceOutput) {
      options.add(StandardOpenOption.TRUNCATE_EXISTING);
    } else {
      options.add(StandardOpenOption.CREATE_NEW);
    }
    return Files.newBufferedWriter(outputLocation, UTF_8, options.toArray(OpenOption[]::new));
  }

  private ChaptersWriter prepareWriter() {
    if (type == OGG) {
      return new OggChaptersWriter();
    } else if (type == XML) {
      return new XmlChaptersWriter(chapterLanguage);
    }
    throw new IllegalArgumentException("Unsupported type: " + type);
  }

}
