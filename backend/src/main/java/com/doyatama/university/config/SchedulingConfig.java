package com.doyatama.university.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Konfigurasi untuk mengaktifkan Spring Scheduling
 * Diperlukan untuk auto-submit scheduler
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {
    // Configuration akan otomatis detect @Scheduled methods
}