package bo.custom;

import bo.SuperBo;
import dto.OrderDto;

import java.sql.SQLException;

public interface OrderBo extends SuperBo {
    boolean saveOrder(OrderDto orderDto) throws SQLException, ClassNotFoundException;
    String generateID() throws SQLException, ClassNotFoundException;
}
