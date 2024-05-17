package dao.util;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudUtil {
    public static <T>T execute(String sql,Object...args) throws SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement(sql);
        if (sql.startsWith("SELECT")||sql.startsWith("select")){
            return (T)preparedStatement.executeQuery();
        }

        for (int i=0; i<args.length; i++){
            preparedStatement.setObject((i+1),args[i]);
        }


        return (T)(Boolean) (preparedStatement.executeUpdate()>0);
    }
}
