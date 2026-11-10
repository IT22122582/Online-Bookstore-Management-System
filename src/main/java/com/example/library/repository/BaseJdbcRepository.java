package com.example.library.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Shared JDBC operations for the application's MySQL-backed repositories. */
public abstract class BaseJdbcRepository<T> {
    protected final JdbcTemplate jdbc;
    private final String tableName;

    protected BaseJdbcRepository(JdbcTemplate jdbc, String tableName, String createTableSql) {
        this.jdbc = jdbc;
        this.tableName = tableName;
        this.jdbc.execute(createTableSql);
    }

    protected abstract RowMapper<T> rowMapper();
    public abstract T save(T item);

    public synchronized List<T> findAll() {
        return jdbc.query("SELECT * FROM " + tableName + " ORDER BY id", rowMapper());
    }

    public synchronized Optional<T> findById(String id) {
        List<T> rows = jdbc.query("SELECT * FROM " + tableName + " WHERE id = ?", rowMapper(), id);
        return rows.stream().findFirst();
    }

    public synchronized void saveAll(List<T> items) {
        jdbc.update("DELETE FROM " + tableName);
        items.forEach(this::save);
    }

    public synchronized boolean deleteById(String id) {
        return jdbc.update("DELETE FROM " + tableName + " WHERE id = ?", id) > 0;
    }

    /** Imports the original pipe-delimited file once, only when its SQL table is empty. */
    protected synchronized void importLegacyFile(String fileName, Function<String, T> deserializer) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
        Path file = Paths.get(fileName);
        if (count == null || count != 0 || !Files.isRegularFile(file)) return;
        try {
            List<T> rows;
            try (java.util.stream.Stream<String> lines = Files.lines(file)) {
                rows = lines.filter(line -> !line.trim().isEmpty())
                        .map(deserializer)
                        .filter(item -> item != null)
                        .collect(Collectors.toList());
            }
            rows.forEach(this::save);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to import legacy data from " + fileName, e);
        }
    }
}
