package com.example.library.repository;

import com.example.library.model.AdminUser;
import com.example.library.model.RegularUser;
import com.example.library.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends BaseJdbcRepository<User> {
    public UserRepository(JdbcTemplate jdbc) {
        super(jdbc, "users", "CREATE TABLE IF NOT EXISTS users (" +
                "id VARCHAR(32) PRIMARY KEY, username VARCHAR(100) NOT NULL, password VARCHAR(255) NOT NULL, " +
                "email VARCHAR(255) NOT NULL, role VARCHAR(20) NOT NULL, admin_level VARCHAR(50), " +
                "membership_type VARCHAR(50), max_books_allowed INT)");
        importLegacyFile("data/users.txt", this::deserializeLegacy);
    }

    @Override
    protected RowMapper<User> rowMapper() {
        return (rs, rowNum) -> {
            String role = rs.getString("role");
            if ("ADMIN".equalsIgnoreCase(role)) {
                return new AdminUser(rs.getString("id"), rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("admin_level"));
            }
            RegularUser user = new RegularUser(rs.getString("id"), rs.getString("username"), rs.getString("password"),
                    rs.getString("email"), rs.getString("membership_type"));
            user.setMaxBooksAllowed(rs.getInt("max_books_allowed"));
            return user;
        };
    }

    @Override
    public User save(User user) {
        String adminLevel = user instanceof AdminUser ? ((AdminUser) user).getAdminLevel() : null;
        String membershipType = user instanceof RegularUser ? ((RegularUser) user).getMembershipType() : null;
        Integer maxBooks = user instanceof RegularUser ? ((RegularUser) user).getMaxBooksAllowed() : null;
        jdbc.update("INSERT INTO users (id, username, password, email, role, admin_level, membership_type, max_books_allowed) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE username=VALUES(username), " +
                        "password=VALUES(password), email=VALUES(email), role=VALUES(role), admin_level=VALUES(admin_level), " +
                        "membership_type=VALUES(membership_type), max_books_allowed=VALUES(max_books_allowed)",
                user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(), adminLevel, membershipType, maxBooks);
        return user;
    }

    private User deserializeLegacy(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 6) return null;
        return "ADMIN".equalsIgnoreCase(p[4])
                ? new AdminUser(p[0], p[1], p[2], p[3], p[5])
                : new RegularUser(p[0], p[1], p[2], p[3], p[5]);
    }
}
