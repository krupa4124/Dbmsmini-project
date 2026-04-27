package crime;

import java.sql.*;
import java.util.*;

/**
 * Data Access Object for ALL tables in the Crime Management System.
 * Tables: crimes, suspects, officers, victims, evidence, crime_suspect (junction)
 */
public class CrimeDAO {

    // ══════════════════════════════════════════════════════════════════════════
    //  CRIMES
    // ══════════════════════════════════════════════════════════════════════════

    public boolean addCrime(String title, String type, String description,
                             String location, String crimeDate, String status,
                             String severity, int officerId) {
        String sql = "INSERT INTO crimes(title, type, description, location, crime_date, " +
                     "status, severity, officer_id) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title);       ps.setString(2, type);
            ps.setString(3, description); ps.setString(4, location);
            ps.setString(5, crimeDate);   ps.setString(6, status);
            ps.setString(7, severity);
            if (officerId > 0) ps.setInt(8, officerId); else ps.setNull(8, Types.INTEGER);
            ps.executeUpdate(); return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateCrime(int id, String title, String type, String description,
                                String location, String crimeDate, String status,
                                String severity, int officerId) {
        String sql = "UPDATE crimes SET title=?, type=?, description=?, location=?, " +
                     "crime_date=?, status=?, severity=?, officer_id=? WHERE crime_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title);       ps.setString(2, type);
            ps.setString(3, description); ps.setString(4, location);
            ps.setString(5, crimeDate);   ps.setString(6, status);
            ps.setString(7, severity);
            if (officerId > 0) ps.setInt(8, officerId); else ps.setNull(8, Types.INTEGER);
            ps.setInt(9, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean deleteCrime(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM crimes WHERE crime_id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<Object[]> getAllCrimes() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT c.*, CONCAT(o.name,' (',o.badge_no,')') AS officer_name " +
                     "FROM crimes c LEFT JOIN officers o ON c.officer_id=o.officer_id " +
                     "ORDER BY c.crime_id DESC";
        try (Connection con = DBConnection.getConnection();
             ResultSet rs = con.createStatement().executeQuery(sql)) {
            while (rs.next()) list.add(crimeRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> searchCrimes(String kw) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT c.*, CONCAT(o.name,' (',o.badge_no,')') AS officer_name " +
                     "FROM crimes c LEFT JOIN officers o ON c.officer_id=o.officer_id " +
                     "WHERE c.title LIKE ? OR c.type LIKE ? OR c.location LIKE ? OR c.status LIKE ? OR c.severity LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + kw + "%";
            for (int i = 1; i <= 5; i++) ps.setString(i, k);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(crimeRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> filterCrimesByStatus(String status) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT c.*, CONCAT(o.name,' (',o.badge_no,')') AS officer_name " +
                     "FROM crimes c LEFT JOIN officers o ON c.officer_id=o.officer_id " +
                     "WHERE c.status=? ORDER BY c.crime_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status); ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(crimeRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private Object[] crimeRow(ResultSet rs) throws SQLException {
        return new Object[]{
            rs.getInt("crime_id"),      rs.getString("title"),
            rs.getString("type"),       rs.getString("location"),
            rs.getString("crime_date"), rs.getString("status"),
            rs.getString("severity"),   rs.getString("officer_name"),
            rs.getString("description")
        };
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SUSPECTS
    // ══════════════════════════════════════════════════════════════════════════

    public boolean addSuspect(String name, String age, String gender, String nationality,
                               String address, String phone, String priorRecord,
                               String status, String notes, int crimeId) {
        String sql = "INSERT INTO suspects(name, age, gender, nationality, address, phone, " +
                     "prior_record, status, notes, crime_id) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);       ps.setString(2, age);
            ps.setString(3, gender);     ps.setString(4, nationality);
            ps.setString(5, address);    ps.setString(6, phone);
            ps.setString(7, priorRecord);ps.setString(8, status);
            ps.setString(9, notes);
            if (crimeId > 0) ps.setInt(10, crimeId); else ps.setNull(10, Types.INTEGER);
            ps.executeUpdate(); return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateSuspect(int id, String name, String age, String gender,
                                  String nationality, String address, String phone,
                                  String priorRecord, String status, String notes, int crimeId) {
        String sql = "UPDATE suspects SET name=?, age=?, gender=?, nationality=?, address=?, " +
                     "phone=?, prior_record=?, status=?, notes=?, crime_id=? WHERE suspect_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);       ps.setString(2, age);
            ps.setString(3, gender);     ps.setString(4, nationality);
            ps.setString(5, address);    ps.setString(6, phone);
            ps.setString(7, priorRecord);ps.setString(8, status);
            ps.setString(9, notes);
            if (crimeId > 0) ps.setInt(10, crimeId); else ps.setNull(10, Types.INTEGER);
            ps.setInt(11, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean deleteSuspect(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM suspects WHERE suspect_id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<Object[]> getAllSuspects() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT s.*, c.title AS crime_title FROM suspects s " +
                     "LEFT JOIN crimes c ON s.crime_id=c.crime_id ORDER BY s.suspect_id DESC";
        try (Connection con = DBConnection.getConnection();
             ResultSet rs = con.createStatement().executeQuery(sql)) {
            while (rs.next()) list.add(suspectRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> searchSuspects(String kw) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT s.*, c.title AS crime_title FROM suspects s " +
                     "LEFT JOIN crimes c ON s.crime_id=c.crime_id " +
                     "WHERE s.name LIKE ? OR s.nationality LIKE ? OR s.status LIKE ? OR c.title LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + kw + "%";
            for (int i = 1; i <= 4; i++) ps.setString(i, k);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(suspectRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private Object[] suspectRow(ResultSet rs) throws SQLException {
        return new Object[]{
            rs.getInt("suspect_id"),    rs.getString("name"),
            rs.getString("age"),        rs.getString("gender"),
            rs.getString("nationality"),rs.getString("phone"),
            rs.getString("prior_record"),rs.getString("status"),
            rs.getString("crime_title"),rs.getString("notes")
        };
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  OFFICERS
    // ══════════════════════════════════════════════════════════════════════════

    public boolean addOfficer(String name, String badge, String rank,
            String dept, String phone, String email, String status) {
String sql = "INSERT INTO officers (name, badge_no, `rank`, department, phone, email, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

try (Connection con = DBConnection.getConnection();
PreparedStatement ps = con.prepareStatement(sql)) {

ps.setString(1, name);
ps.setString(2, badge);
ps.setString(3, rank);
ps.setString(4, dept);
ps.setString(5, phone);
ps.setString(6, email);
ps.setString(7, status);

int rows = ps.executeUpdate();
return rows > 0;

} catch (Exception e) {
e.printStackTrace();  // 🔥 VERY IMPORTANT
return false;
}
}

    public boolean updateOfficer(int id, String name, String badgeNo, String rank,
                                  String department, String phone, String email, String status) {
        String sql = "UPDATE officers SET name=?, badge_no=?, rank=?, department=?, " +
                     "phone=?, email=?, status=? WHERE officer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);  ps.setString(2, badgeNo);
            ps.setString(3, rank);  ps.setString(4, department);
            ps.setString(5, phone); ps.setString(6, email);
            ps.setString(7, status);ps.setInt(8, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean deleteOfficer(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM officers WHERE officer_id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<Object[]> getAllOfficers() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT o.*, COUNT(c.crime_id) AS case_count FROM officers o " +
                     "LEFT JOIN crimes c ON o.officer_id=c.officer_id " +
                     "GROUP BY o.officer_id ORDER BY o.officer_id DESC";
        try (Connection con = DBConnection.getConnection();
             ResultSet rs = con.createStatement().executeQuery(sql)) {
            while (rs.next()) list.add(officerRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> searchOfficers(String kw) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT o.*, COUNT(c.crime_id) AS case_count FROM officers o " +
                     "LEFT JOIN crimes c ON o.officer_id=c.officer_id " +
                     "WHERE o.name LIKE ? OR o.badge_no LIKE ? OR o.rank LIKE ? OR o.department LIKE ? " +
                     "GROUP BY o.officer_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + kw + "%";
            for (int i = 1; i <= 4; i++) ps.setString(i, k);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(officerRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /** Returns officer options for dropdown: "id: Name (Badge)" */
    public String[] getOfficerOptions() {
        List<String> opts = new ArrayList<>();
        opts.add("Select Officer");
        try (Connection con = DBConnection.getConnection();
             ResultSet rs = con.createStatement().executeQuery(
                 "SELECT officer_id, name, badge_no FROM officers WHERE status='Active' ORDER BY name")) {
            while (rs.next())
                opts.add(rs.getInt("officer_id") + ":  " +
                         rs.getString("name") + "  (" + rs.getString("badge_no") + ")");
        } catch (Exception e) { e.printStackTrace(); }
        return opts.toArray(new String[0]);
    }

    private Object[] officerRow(ResultSet rs) throws SQLException {
        return new Object[]{
            rs.getInt("officer_id"),  rs.getString("name"),
            rs.getString("badge_no"), rs.getString("rank"),
            rs.getString("department"),rs.getString("phone"),
            rs.getString("email"),    rs.getString("status"),
            rs.getInt("case_count")
        };
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  VICTIMS
    // ══════════════════════════════════════════════════════════════════════════

    public boolean addVictim(String name, String age, String gender, String contact,
                              String address, String injuryLevel, String statement, int crimeId) {
        String sql = "INSERT INTO victims(name, age, gender, contact, address, injury_level, statement, crime_id) " +
                     "VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);      ps.setString(2, age);
            ps.setString(3, gender);    ps.setString(4, contact);
            ps.setString(5, address);   ps.setString(6, injuryLevel);
            ps.setString(7, statement);
            if (crimeId > 0) ps.setInt(8, crimeId); else ps.setNull(8, Types.INTEGER);
            ps.executeUpdate(); return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateVictim(int id, String name, String age, String gender,
                                 String contact, String address, String injuryLevel,
                                 String statement, int crimeId) {
        String sql = "UPDATE victims SET name=?, age=?, gender=?, contact=?, address=?, " +
                     "injury_level=?, statement=?, crime_id=? WHERE victim_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);      ps.setString(2, age);
            ps.setString(3, gender);    ps.setString(4, contact);
            ps.setString(5, address);   ps.setString(6, injuryLevel);
            ps.setString(7, statement);
            if (crimeId > 0) ps.setInt(8, crimeId); else ps.setNull(8, Types.INTEGER);
            ps.setInt(9, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean deleteVictim(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM victims WHERE victim_id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<Object[]> getAllVictims() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT v.*, c.title AS crime_title FROM victims v " +
                     "LEFT JOIN crimes c ON v.crime_id=c.crime_id ORDER BY v.victim_id DESC";
        try (Connection con = DBConnection.getConnection();
             ResultSet rs = con.createStatement().executeQuery(sql)) {
            while (rs.next()) list.add(victimRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> searchVictims(String kw) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT v.*, c.title AS crime_title FROM victims v " +
                     "LEFT JOIN crimes c ON v.crime_id=c.crime_id " +
                     "WHERE v.name LIKE ? OR v.injury_level LIKE ? OR c.title LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + kw + "%";
            for (int i = 1; i <= 3; i++) ps.setString(i, k);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(victimRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private Object[] victimRow(ResultSet rs) throws SQLException {
        return new Object[]{
            rs.getInt("victim_id"),     rs.getString("name"),
            rs.getString("age"),        rs.getString("gender"),
            rs.getString("contact"),    rs.getString("injury_level"),
            rs.getString("crime_title"),rs.getString("statement")
        };
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  EVIDENCE
    // ══════════════════════════════════════════════════════════════════════════

    public boolean addEvidence(String type, String description, String collectedBy,
                                String collectedDate, String location, String status,
                                String labStatus, int crimeId) {
        String sql = "INSERT INTO evidence(type, description, collected_by, collected_date, " +
                     "location_found, status, lab_status, crime_id) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, type);          ps.setString(2, description);
            ps.setString(3, collectedBy);   ps.setString(4, collectedDate);
            ps.setString(5, location);      ps.setString(6, status);
            ps.setString(7, labStatus);
            if (crimeId > 0) ps.setInt(8, crimeId); else ps.setNull(8, Types.INTEGER);
            ps.executeUpdate(); return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean updateEvidence(int id, String type, String description, String collectedBy,
                                   String collectedDate, String location, String status,
                                   String labStatus, int crimeId) {
        String sql = "UPDATE evidence SET type=?, description=?, collected_by=?, collected_date=?, " +
                     "location_found=?, status=?, lab_status=?, crime_id=? WHERE evidence_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, type);        ps.setString(2, description);
            ps.setString(3, collectedBy); ps.setString(4, collectedDate);
            ps.setString(5, location);    ps.setString(6, status);
            ps.setString(7, labStatus);
            if (crimeId > 0) ps.setInt(8, crimeId); else ps.setNull(8, Types.INTEGER);
            ps.setInt(9, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public boolean deleteEvidence(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM evidence WHERE evidence_id=?")) {
            ps.setInt(1, id); return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<Object[]> getAllEvidence() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT e.*, c.title AS crime_title FROM evidence e " +
                     "LEFT JOIN crimes c ON e.crime_id=c.crime_id ORDER BY e.evidence_id DESC";
        try (Connection con = DBConnection.getConnection();
             ResultSet rs = con.createStatement().executeQuery(sql)) {
            while (rs.next()) list.add(evidenceRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> searchEvidence(String kw) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT e.*, c.title AS crime_title FROM evidence e " +
                     "LEFT JOIN crimes c ON e.crime_id=c.crime_id " +
                     "WHERE e.type LIKE ? OR e.description LIKE ? OR e.status LIKE ? OR c.title LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + kw + "%";
            for (int i = 1; i <= 4; i++) ps.setString(i, k);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(evidenceRow(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private Object[] evidenceRow(ResultSet rs) throws SQLException {
        return new Object[]{
            rs.getInt("evidence_id"),      rs.getString("type"),
            rs.getString("description"),   rs.getString("collected_by"),
            rs.getString("collected_date"),rs.getString("location_found"),
            rs.getString("status"),        rs.getString("lab_status"),
            rs.getString("crime_title")
        };
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  DASHBOARD STATS
    // ══════════════════════════════════════════════════════════════════════════

    public int count(String table) {
        try (Connection con = DBConnection.getConnection();
             ResultSet rs = con.createStatement().executeQuery("SELECT COUNT(*) FROM " + table)) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int countWhere(String table, String col, String val) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT COUNT(*) FROM " + table + " WHERE " + col + "=?")) {
            ps.setString(1, val); ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public Map<String, Integer> getCrimeTypeCounts() {
        Map<String, Integer> map = new LinkedHashMap<>();
        try (Connection con = DBConnection.getConnection();
             ResultSet rs = con.createStatement().executeQuery(
                 "SELECT type, COUNT(*) cnt FROM crimes GROUP BY type ORDER BY cnt DESC LIMIT 6")) {
            while (rs.next()) map.put(rs.getString("type"), rs.getInt("cnt"));
        } catch (Exception e) { e.printStackTrace(); }
        return map;
    }

    /** Returns crime options "id: Title" for linking suspects/victims/evidence */
    public String[] getCrimeOptions() {
        List<String> opts = new ArrayList<>();
        opts.add("Select Crime");
        try (Connection con = DBConnection.getConnection();
             ResultSet rs = con.createStatement().executeQuery(
                 "SELECT crime_id, title FROM crimes ORDER BY crime_id DESC")) {
            while (rs.next())
                opts.add(rs.getInt("crime_id") + ":  " + rs.getString("title"));
        } catch (Exception e) { e.printStackTrace(); }
        return opts.toArray(new String[0]);
    }
}
