package com.example.cmpshop.config;

import java.time.LocalDate;
/** contains values, configured for the application */
public class AppConfig {
    public static final int MIN_YEAR = 1970;
    public static final int CURRENT_YEAR;

    static {
        CURRENT_YEAR = LocalDate.now().getYear(); // Thiết lập tại thời điểm khởi động ứng dụng
    }
    public static final long MIN_PRICE_LIMIT = 0L;
    public static final long MAX_PRICE_LIMIT = 50000000000L;
}