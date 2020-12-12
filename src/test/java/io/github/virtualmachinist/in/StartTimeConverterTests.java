package io.github.virtualmachinist.in;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class StartTimeConverterTests {

  private StartTimeConverter converter;

  @BeforeEach
  void setUp() {
    converter = new StartTimeConverter();
  }

  @Test
  void convertsNull() {
    assertThat(converter.convert(null)).isNull();
  }

  @CsvSource({
      "0,00:00:00",
      "1,00:00:01",
      "1.999,00:00:01.999",
      "1.9999,00:00:01.999",
      "1.5,00:00:01.500",
      "61.5,00:01:01.500",
      "3661.5,01:01:01.500",
  })
  @ParameterizedTest
  void convertsValue(BigDecimal in, LocalTime out) {
    assertThat(converter.convert(in)).isEqualTo(out);
  }

}
