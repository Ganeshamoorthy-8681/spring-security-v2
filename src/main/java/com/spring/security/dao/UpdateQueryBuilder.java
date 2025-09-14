package com.spring.security.dao;

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
        updateSet.append(String.format("%s IS NULL", column));
      } else if (value instanceof String && ((String) value).startsWith("CURRENT_")) {
        // Handle SQL functions like CURRENT_TIMESTAMP without quotes
        updateSet.append(String.format("%s = %s", column, value));
      } else if (value instanceof String && isColumnReference((String) value)) {
        // Handle column references (like current_login) without quotes
        updateSet.append(String.format("%s = %s", column, ((String) value).substring(5)));
      } else {
        updateSet.append(String.format("%s = '%s'", column, value));
        System.out.println("Literal value detected: " + value);
      }

      if (index != updateColumnValueMap.size() - 1) {
        updateSet.append(String.format(" %s ", delimiter));
      }
      index++;
    }
    return updateSet.toString();
  }

  /**
   * Checks if a string value is likely a column reference rather than a literal value.
   */
  private boolean isColumnReference(String value) {
      return value.startsWith("$col:"); // must start lowercase
  }

}
