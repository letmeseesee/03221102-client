package util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集
 * @author 阿尔卑斯狗 2019-3-22
 * @param <T>
 */
public interface RowMapper<T> {
    /**
     *
     * @param rs 结果集
     * @param index 行数
     * @return T
     * @throws SQLException
     */
    public abstract T mapRow(ResultSet rs, int index) throws SQLException;
}
