package dao;
import java.sql.SQLException;
import java.util.List;



public interface CrudDao<T> extends SuperDao{
    boolean save(T entity) throws SQLException, ClassNotFoundException;//if bo<-dto->dao here entity should be renamed as dto
    List<T> getAll() throws SQLException, ClassNotFoundException;
    boolean update(T entity) throws SQLException, ClassNotFoundException;
    boolean delete(String value) throws SQLException, ClassNotFoundException;
}
