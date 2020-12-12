package io.github.virtualmachinist.in;

import com.fasterxml.jackson.databind.util.StdConverter;
import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;

@Introspected
public class StartTimeConverter extends StdConverter<BigDecimal, LocalTime> {

  private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);

  @Override
  public LocalTime convert(BigDecimal value) {
    if (value == null) {
      return null;
    }
    BigDecimal seconds = value.setScale(0, RoundingMode.FLOOR);
    BigDecimal millis = value.subtract(seconds).multiply(THOUSAND).setScale(0, RoundingMode.FLOOR);
    Duration startDuration = Duration.ofSeconds(seconds.longValue()).plusMillis(millis.longValue());
    return LocalTime.MIDNIGHT.plus(startDuration);
  }

}
