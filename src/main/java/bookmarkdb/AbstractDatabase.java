package bookmarkdb;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Interface for {@link Database}.
 */
public interface AbstractDatabase {

    void init();

    Map<String, List<String>> query(String query, Object... params) throws SQLException;

    void setDebugMode(boolean d);

    int update(String updateQuery, Object... params) throws SQLException;

}
