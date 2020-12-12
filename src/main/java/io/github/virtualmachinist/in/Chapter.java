package io.github.virtualmachinist.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Introspected
public class Chapter {

  private String title;

  @JsonProperty("start_time")
  @JsonDeserialize(converter = StartTimeConverter.class)
  private LocalTime startTime;

  @JsonProperty("end_time")
  @JsonDeserialize(converter = StartTimeConverter.class)
  private LocalTime endTime;

}
