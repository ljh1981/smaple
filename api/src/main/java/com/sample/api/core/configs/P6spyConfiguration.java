package com.sample.api.core.configs;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.function.Predicate;

import static java.util.Arrays.stream;

@Configuration
public class P6spyConfiguration implements MessageFormattingStrategy {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(this.getClass().getName());
    }

    private static final String NEW_LINE = System.lineSeparator();
    private static final String CREATE = "create";
    private static final String ALTER = "alter";
    private static final String DROP = "drop";
    private static final String COMMENT = "comment";
    private static final String PLUS_0900 = "+0900";

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {

        return sqlFormatToUpper(sql, category, getMessage(connectionId, elapsed));
    }

    private String sqlFormatToUpper(String sql, String category, String message) {
        if (sql.trim().isEmpty()) {
            return "";
        }
        return NEW_LINE + sqlFormatToUpper(sql, category) + message;
    }

    private String sqlFormatToUpper(String sql, String category) {
        if (isStatementDDL(sql, category)) {
            return FormatStyle.DDL.getFormatter().format(sql).replace(PLUS_0900, "");
        }

        if (isMybatisCall()) {
            return FormatStyle.NONE.getFormatter().format(sql).replace(PLUS_0900, "");
        }
        return FormatStyle.BASIC.getFormatter().format(sql).replace(PLUS_0900, "");
    }

    private boolean isStatementDDL(String sql, String category) {
        return isStatement(category) && isDDL(sql.trim().toLowerCase(Locale.ROOT));
    }

    private boolean isStatement(String category) {
        return Category.STATEMENT.getName().equals(category);
    }

    private boolean isDDL(String lowerSql) {
        return lowerSql.startsWith(CREATE) || lowerSql.startsWith(ALTER) || lowerSql.startsWith(DROP) || lowerSql.startsWith(COMMENT);
    }

    private String getMessage(int connectionId, long elapsed) {
        return NEW_LINE + NEW_LINE + "\t"
                + String.format("Connection ID: %s", connectionId)
                + NEW_LINE + "\t"
                + String.format("Execution Time: %s ms", elapsed)
                + NEW_LINE + NEW_LINE
                + "----------------------------------------------------------------------------------------------------";
    }

    private boolean isMybatisCall() {
        return stream(new Throwable().getStackTrace()).map(StackTraceElement::toString).anyMatch(isNativePackages());
    }

    private Predicate<String> isNativePackages() {
        return charSequence -> charSequence.startsWith("org.apache.ibatis.binding");
    }
}
