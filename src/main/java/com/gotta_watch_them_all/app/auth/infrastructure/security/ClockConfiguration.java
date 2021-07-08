package com.gotta_watch_them_all.app.auth.infrastructure.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

@Configuration
public class ClockConfiguration {
  private final static LocalDate LOCAL_DATE = LocalDate.of(2019, 12, 17);

  @Bean
  @ConditionalOnMissingBean
  Clock getSystemDefaultZoneClock() {
    return Clock.systemDefaultZone();
  }

  @Bean
  @Profile("test")
  @Primary
  public Clock getFixedClock() {
    return Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
  }
}