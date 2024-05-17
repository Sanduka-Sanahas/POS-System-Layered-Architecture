package dao.custom.impl;

import dao.util.CrudUtil;
import db.DBConnection;
import dto.OrderDetailsDto;
import dao.custom.OrderDetailsDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailsDaoImpl implements OrderDetailsDao {
    @Override
    public boolean saveDetailsList(List<OrderDetailsDto> list) throws SQLException, ClassNotFoundException {
        boolean isDetailsSaved = true;
        for (OrderDetailsDto dto:list) {
            String sql = "INSERT INTO orderdetail VALUES(?,?,?,?)";
            boolean isSaved = CrudUtil.execute(
                    sql,
                    dto.getOrderId(),
                    dto.getItemCode(),
                    dto.getQty(),
                    dto.getUnitPrice()
            );

            if(!isSaved){
                isDetailsSaved = false;
            }
        }
        return isDetailsSaved;
    }



    @Override
    public boolean save(OrderDetailsDto entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public List<OrderDetailsDto> getAll() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public boolean update(OrderDetailsDto entity) throws SQLException, ClassNotFoundException {
        return false;
    }


    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        return false;
    }
}
