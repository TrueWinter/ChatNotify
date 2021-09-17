package dev.truewinter.chatnotify;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;
import java.util.*;

public class Database {
    Config config = null;
    String pluginDataDir = null;
    String url = null;
    JavaPlugin plugin = null;

    // Every letter in tab complete caused a database lookup. This cache prevents that
    private Map<UUID, Set<Word>> playerWordCache = new HashMap<>();

    public Database(JavaPlugin plugin, Config config) {
        this.plugin = plugin;
        this.config = config;
        this.pluginDataDir = plugin.getDataFolder().getAbsolutePath();
        this.url = "jdbc:sqlite:" + pluginDataDir + File.separator + "database.db";

        createTables();
    }

    private void runCreateTableSQL(String sql) {
        try (Connection conn = DriverManager.getConnection(this.url);
            Statement stmt = conn.createStatement()){
            stmt.execute(sql);
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }
    }

    private void createTables() {
        if (config.isInitSetupDone()) {
            return;
        }
        String sqlWords = "CREATE TABLE IF NOT EXISTS words ("
                + "id integer PRIMARY KEY, "
                + "uuid text, "
                + "word text, "
                + "approved integer"
                + ");";

        String sqlPlayers = "CREATE TABLE IF NOT EXISTS players ("
                + "id integer PRIMARY KEY, "
                + "uuid text, "
                + "optedin integer, "
                + "playsound integer, "
                + "notifymsg integer"
                + ");";

        String sqlMods = "CREATE TABLE IF NOT EXISTS mods ("
                + "id integer PRIMARY KEY, "
                + "uuid text, "
                + "optedin integer"
                + ");";

        runCreateTableSQL(sqlWords);
        plugin.getLogger().info("Created words table");
        runCreateTableSQL(sqlPlayers);
        plugin.getLogger().info("Created players table");
        runCreateTableSQL(sqlMods);
        plugin.getLogger().info("Created mods table");

        config.setInitSetupDone(true);
    }

    private int booleanToInt(boolean bool) {
        if (bool) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean intToBoolean(int integer) {
        if (integer == 1) {
            return true;
        } else {
            return false;
        }
    }

    public Set<Word> getAllWords() {
        String sql = "SELECT * FROM words";
        Set<Word> words = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(this.url);
             Statement stmt = conn.createStatement()) {
             ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                int thisId = rs.getInt("id");
                UUID thisUUID = UUID.fromString(rs.getString("uuid"));
                String thisWord = rs.getString("word");
                boolean approved = intToBoolean(rs.getInt("approved"));

                Word word = new Word(plugin, conn, thisId, thisUUID, thisWord, approved);
                words.add(word);
            }
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }

        return words;
    }

    public Set<Word> getAllWordsForPlayer(UUID uuid) {
        if (playerWordCache.containsKey(uuid)) {
            return playerWordCache.get(uuid);
        }

        String sql = "SELECT * FROM words WHERE uuid = ?";
        Set<Word> words = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());

            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                int thisId = rs.getInt("id");
                UUID thisUUID = UUID.fromString(rs.getString("uuid"));
                String thisWord = rs.getString("word");
                boolean approved = intToBoolean(rs.getInt("approved"));

                Word word = new Word(plugin, conn, thisId, thisUUID, thisWord, approved);
                words.add(word);
            }
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }

        playerWordCache.put(uuid, words);
        return words;
    }

    public Word getWordForPlayer(UUID uuid, String word) {
        String sql = "SELECT * FROM words WHERE uuid = ? AND word = ?";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, word);

            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                int thisId = rs.getInt("id");
                UUID thisUUID = UUID.fromString(rs.getString("uuid"));
                String thisWord = rs.getString("word");
                boolean approved = intToBoolean(rs.getInt("approved"));

                return new Word(plugin, conn, thisId, thisUUID, thisWord, approved);
            }
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }

        return null;
    }

    public Set<Word> getAllWordsRequiringApproval() {
        String sql = "SELECT * FROM words WHERE approved = 0";
        Set<Word> words = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                int thisId = rs.getInt("id");
                UUID thisUUID = UUID.fromString(rs.getString("uuid"));
                String thisWord = rs.getString("word");
                boolean approved = intToBoolean(rs.getInt("approved"));

                Word word = new Word(plugin, conn, thisId, thisUUID, thisWord, approved);
                words.add(word);
            }
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }

        return words;
    }

    public boolean playerHasWord(UUID uuid, String word) {
        String sql = "SELECT * FROM words WHERE uuid = ? AND word = ?";
        boolean hasWord = false;

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, word);

            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            if (rs.next()) {
                hasWord = true;
            }
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }

        return hasWord;
    }

    public boolean playerExistsInDB(UUID uuid) {
        String sql = "SELECT * FROM players WHERE uuid = ?";
        boolean exists = false;

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());

            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            if (rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }

        return exists;
    }

    public boolean modExistsInDB(UUID uuid) {
        String sql = "SELECT * FROM mods WHERE uuid = ?";
        boolean exists = false;

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());

            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            if (rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }

        return exists;
    }

    public void addWord(UUID uuid, String word, boolean approved) {
        String sql = "INSERT INTO words(uuid, word, approved) VALUES(?,?,?)";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, word);
            pstmt.setInt(3, booleanToInt(approved));

            pstmt.executeUpdate();

            // Clear cache for this player, if they exist in the cache
            playerWordCache.remove(uuid);
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }
    }

    public void addPlayer(UUID uuid) {
        String sql = "INSERT INTO players(uuid, optedin, playsound, notifymsg) VALUES(?,?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setInt(2, 1);
            pstmt.setInt(3, 1);
            pstmt.setInt(4, 1);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }
    }

    public void addMod(UUID uuid) {
        String sql = "INSERT INTO mods(uuid, optedin) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setInt(2, 1);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }
    }

    public void removeWord(UUID uuid, String word) {
        String sql = "DELETE FROM words WHERE uuid = ? AND word = ?";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, word);

            pstmt.executeUpdate();

            playerWordCache.remove(uuid);
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }
    }

    public boolean hasWordAwaitingApproval(UUID uuid, String word) {
        String sql = "SELECT * FROM words WHERE uuid = ? AND word = ? AND approved = 0";
        boolean hasWord = false;

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            pstmt.setString(2, word);

            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            if (rs.next()) {
                hasWord = true;
            }
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }

        return hasWord;
    }

    public void setApproved(boolean approved, UUID uuid, String word) {
        String sql = "UPDATE words SET approved = ? WHERE uuid = ? AND word = ?";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, booleanToInt(approved));
            pstmt.setString(2, uuid.toString());
            pstmt.setString(3, word);

            pstmt.executeUpdate();

            playerWordCache.remove(uuid);
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }
    }

    public boolean playerIsOptedIn(UUID uuid) {
        String sql = "SELECT * FROM players WHERE uuid = ? AND optedin = 1";
        boolean optedIn = false;

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());

            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            if (rs.next()) {
                optedIn = true;
            }
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }

        return optedIn;
    }

    public boolean playerSoundOn(UUID uuid) {
        String sql = "SELECT * FROM players WHERE uuid = ? AND playsound = 1";
        boolean optedIn = false;

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());

            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            if (rs.next()) {
                optedIn = true;
            }
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }

        return optedIn;
    }

    public boolean modIsOptedIn(UUID uuid) {
        String sql = "SELECT * FROM mods WHERE uuid = ? AND optedin = 1";
        boolean optedIn = false;

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());

            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            if (rs.next()) {
                optedIn = true;
            }
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }

        return optedIn;
    }

    public void setPlayerOptIn(UUID uuid, boolean optin) {
        String sql = "UPDATE players SET optedin = ? WHERE uuid = ?";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, booleanToInt(optin));
            pstmt.setString(2, uuid.toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }
    }

    public void setModOptIn(UUID uuid, boolean optin) {
        String sql = "UPDATE mods SET optedin = ? WHERE uuid = ?";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, booleanToInt(optin));
            pstmt.setString(2, uuid.toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }
    }

    public void setPlayerSound(UUID uuid, boolean sound) {
        String sql = "UPDATE players SET playsound = ? WHERE uuid = ?";

        try (Connection conn = DriverManager.getConnection(this.url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, booleanToInt(sound));
            pstmt.setString(2, uuid.toString());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            String error = "";
            StackTraceElement[] stackTraceLines = e.getStackTrace();
            for (int i = 0; i < stackTraceLines.length; i++) {
                error += stackTraceLines[i].toString() + "\n";
            }
            plugin.getLogger().severe(error);
        }
    }
}