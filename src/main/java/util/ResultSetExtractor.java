package util;

import java.sql.ResultSet;

/**
 * 结果集
 * @author 阿尔卑斯狗 2019-3-22
 * @param <T>
 */
public interface ResultSetExtractor <T>{
    public abstract T extractData(ResultSet rs);
}
