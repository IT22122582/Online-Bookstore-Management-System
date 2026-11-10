package com.example.library.repository;

import com.example.library.model.Author;
import com.example.library.model.GuestAuthor;
import com.example.library.model.PermanentAuthor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AuthorRepository extends BaseJdbcRepository<Author> {
    public AuthorRepository(JdbcTemplate jdbc) {
        super(jdbc, "authors", "CREATE TABLE IF NOT EXISTS authors (" +
                "id VARCHAR(32) PRIMARY KEY, name VARCHAR(150) NOT NULL, bio TEXT, author_type VARCHAR(20) NOT NULL, " +
                "institution VARCHAR(255), visit_duration_days INT, contract_period_years INT, office_number VARCHAR(80))");
        importLegacyFile("data/authors.txt", this::deserializeLegacy);
    }

    @Override
    protected RowMapper<Author> rowMapper() {
        return (rs, rowNum) -> "GUEST".equalsIgnoreCase(rs.getString("author_type"))
                ? new GuestAuthor(rs.getString("id"), rs.getString("name"), rs.getString("bio"),
                        rs.getString("institution"), rs.getInt("visit_duration_days"))
                : new PermanentAuthor(rs.getString("id"), rs.getString("name"), rs.getString("bio"),
                        rs.getInt("contract_period_years"), rs.getString("office_number"));
    }

    @Override
    public Author save(Author author) {
        boolean guest = author instanceof GuestAuthor;
        String type = guest ? "GUEST" : "PERMANENT";
        String institution = guest ? ((GuestAuthor) author).getInstitution() : null;
        Integer duration = guest ? ((GuestAuthor) author).getVisitDurationDays() : null;
        Integer contract = guest ? null : ((PermanentAuthor) author).getContractPeriodYears();
        String office = guest ? null : ((PermanentAuthor) author).getOfficeNumber();
        jdbc.update("INSERT INTO authors (id, name, bio, author_type, institution, visit_duration_days, contract_period_years, office_number) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE name=VALUES(name), bio=VALUES(bio), " +
                        "author_type=VALUES(author_type), institution=VALUES(institution), visit_duration_days=VALUES(visit_duration_days), " +
                        "contract_period_years=VALUES(contract_period_years), office_number=VALUES(office_number)",
                author.getId(), author.getName(), author.getBio(), type, institution, duration, contract, office);
        return author;
    }

    private Author deserializeLegacy(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 6) return null;
        return "GUEST".equalsIgnoreCase(p[3])
                ? new GuestAuthor(p[0], p[1], p[2], p[4], Integer.parseInt(p[5]))
                : new PermanentAuthor(p[0], p[1], p[2], Integer.parseInt(p[4]), p[5]);
    }
}
