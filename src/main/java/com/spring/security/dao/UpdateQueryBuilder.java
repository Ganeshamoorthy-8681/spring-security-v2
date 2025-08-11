package com.spring.security.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

/**
 * UpdateQueryBuilder is a utility class that builds SQL update queries dynamically. It uses
 * MyBatis's SQL builder to create an update statement based on the provided table name, columns to
 * be updated, and conditions for the update.
 */
@Slf4j
public class UpdateQueryBuilder {

  public String update(
      String tableName, Map<String, Object> updates, Map<String, Object> conditions) {

    String query =
        new SQL() {
          {
            UPDATE(tableName);
            SET(getQuerySet(updates, ","));
            WHERE(getQuerySet(conditions, "AND"));
          }
        }.toString();
    log.info("Generated query: {}", query);
    return query;
  }

  private String getQuerySet(Map<String, Object> updateColumnValueMap, String delimiter) {
    StringBuilder updateSet = new StringBuilder();
    int index = 0;
    for (Map.Entry<String, Object> updateColumnValueEntry : updateColumnValueMap.entrySet()) {
      String column = updateColumnValueEntry.getKey();
      Object value = updateColumnValueEntry.getValue();

      if (Objects.isNull(value)) {
        updateSet.append(String.format("%s = %s", column, value));
      } else if (value instanceof LocalDateTime) {
        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        updateSet.append(
            String.format("%s = '%s'", column, ((LocalDateTime) value).format(formatter)));
      } else {
        updateSet.append(String.format("%s = '%s'", column, value));
      }

      if (index != updateColumnValueMap.size() - 1) {
        updateSet.append(String.format(" %s ", delimiter));
      }
      index++;
    }
    return updateSet.toString();
  }
}
