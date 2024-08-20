package ntq.server.oms.util;

import org.springframework.dao.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementSetter {
  int apply(PreparedStatement ps) throws SQLException, DataAccessException;
}
