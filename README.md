## convert-chapters 

---
Converts chapters extracted from YouTube videos using `youtube-dl` to a format suitable for MKVToolNix.

###Background

Some time ago YouTube added a feature that lets creators break up their videos into chapters.  
`youtube-dl` tool has the option to add chapters to the video being downloaded using `--write-metadata`, but AFAIK there
is no way to fetch chapters separately in a format supported by MKVToolNix/mkvmerge (i.e. OGG or Matroska XML).

This project was created mainly to explore development options with Micronaut and GraalVM. More precisely I wanted to
see how good does the native image feature work with Jackson and Picocli.
_(SPOILER: works fine, albeit not without quirks)._

###Building

Building requires at least Java 11.

The application can be built as a fat JAR using `shadowJar` Gradle task: `./gradlew shadowJar`.
The JAR can be found in `build/libs/convert-chapters-0.1-all.jar`.

For native image build a GraalVM SDK is required (tested using GraalVM Java 11 20.3.0).
Build is executed with `nativeImage` Gradle task: `./gradlew nativeImage`.
The executable native image is found in `build/native-image`.

###Using

```
Usage: convert-chapters [-fhV] [-i=<inputLocation>] [-l=<chapterLanguage>]
                        [-o=<outputLocation>] [-t=<type>]
Converts YouTube chapters to OGG or Matroska format.
  -f, --force                Overwrite output file if exists.
  -h, --help                 Show this help message and exit.
  -i, --in=<inputLocation>   Input file. Defaults to standard in.
  -l, --chapter-language=<chapterLanguage>
                             Matroska chapter language. Should be a valid BCP
                               47 code.
                               Default: und
  -o, --out=<outputLocation> Output file. Defaults to standard out.
  -t, --type=<type>          Output format. OGG for OGG format, XML for
                               Matroska XML.
                               Default: XML
  -V, --version              Print version information and exit.
```

The application uses the JSON metadata of a video that can be obtained using `youtube-dl --print-json -s <URL>`.  
If redirected to the application, an XML is written to standard out:
`youtube-dl --print-json -s <URL> | convert-chapters`

###License and Legal Disclaimer

This software is licensed under MIT License. Full text can be found in [LICENSE](LICENSE). By using
the software you agree with the license terms.  
This project is in no way affiliated with `youtube-dl` or any other software products.  
All trademarks and product names mentioned here belong to their respective owners.
