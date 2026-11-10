package com.example.library.repository;

import com.example.library.model.Book;
import com.example.library.model.EBook;
import com.example.library.model.PrintedBook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository extends BaseJdbcRepository<Book> {
    public BookRepository(JdbcTemplate jdbc) {
        super(jdbc, "books", "CREATE TABLE IF NOT EXISTS books (" +
                "id VARCHAR(32) PRIMARY KEY, title VARCHAR(255) NOT NULL, author_id VARCHAR(32) NOT NULL, " +
                "isbn VARCHAR(32), genre VARCHAR(100), available BOOLEAN NOT NULL, book_type VARCHAR(20) NOT NULL, " +
                "file_size_mb DOUBLE, download_url TEXT, shelf_location VARCHAR(100), weight_kg DOUBLE)");
        importLegacyFile("data/books.txt", this::deserializeLegacy);
    }

    @Override
    protected RowMapper<Book> rowMapper() {
        return (rs, rowNum) -> {
            String id = rs.getString("id");
            String title = rs.getString("title");
            String authorId = rs.getString("author_id");
            String isbn = rs.getString("isbn");
            String genre = rs.getString("genre");
            boolean available = rs.getBoolean("available");
            if ("EBOOK".equalsIgnoreCase(rs.getString("book_type"))) {
                return new EBook(id, title, authorId, isbn, genre, available,
                        rs.getDouble("file_size_mb"), rs.getString("download_url"));
            }
            return new PrintedBook(id, title, authorId, isbn, genre, available,
                    rs.getString("shelf_location"), rs.getDouble("weight_kg"));
        };
    }

    @Override
    public Book save(Book book) {
        String type = book instanceof EBook ? "EBOOK" : "PRINTED";
        Double fileSize = book instanceof EBook ? ((EBook) book).getFileSizeMB() : null;
        String downloadUrl = book instanceof EBook ? ((EBook) book).getDownloadUrl() : null;
        String shelf = book instanceof PrintedBook ? ((PrintedBook) book).getShelfLocation() : null;
        Double weight = book instanceof PrintedBook ? ((PrintedBook) book).getWeightKg() : null;
        jdbc.update("INSERT INTO books (id, title, author_id, isbn, genre, available, book_type, file_size_mb, download_url, shelf_location, weight_kg) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE title=VALUES(title), " +
                        "author_id=VALUES(author_id), isbn=VALUES(isbn), genre=VALUES(genre), available=VALUES(available), " +
                        "book_type=VALUES(book_type), file_size_mb=VALUES(file_size_mb), download_url=VALUES(download_url), " +
                        "shelf_location=VALUES(shelf_location), weight_kg=VALUES(weight_kg)",
                book.getId(), book.getTitle(), book.getAuthorId(), book.getIsbn(), book.getGenre(), book.isAvailable(),
                type, fileSize, downloadUrl, shelf, weight);
        return book;
    }

    private Book deserializeLegacy(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 9) return null;
        boolean available = Boolean.parseBoolean(p[5]);
        return "EBOOK".equalsIgnoreCase(p[6])
                ? new EBook(p[0], p[1], p[2], p[3], p[4], available, Double.parseDouble(p[7]), p[8])
                : new PrintedBook(p[0], p[1], p[2], p[3], p[4], available, p[7], Double.parseDouble(p[8]));
    }
}
