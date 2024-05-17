package dao.custom.impl;

import dao.util.CrudUtil;
import db.DBConnection;
import dto.ItemDto;
import dao.custom.ItemDao;
import entity.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemDaoImpl implements ItemDao {

    @Override
    public Item getItem(String code) throws SQLException, ClassNotFoundException {
        return null;
    }


    @Override
    public boolean save(Item entity) throws SQLException, ClassNotFoundException {
        String updateQuery="INSERT INTO item VALUES(?,?,?,?)";
        return CrudUtil.execute(
                updateQuery,
                entity.getCode(),
                entity.getDescription(),
                entity.getUnitPrice(),
                entity.getQtyOnHand()
        );
    }

    @Override
    public List<Item> getAll() throws SQLException, ClassNotFoundException {
        List<Item> list = new ArrayList<>();//its better to pass dto to bo
        String sql = "SELECT * FROM item";
        ResultSet resultSet = CrudUtil.execute(sql);

        while (resultSet.next()){
            list.add(new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            ));
        }

        return list;
    }

    @Override
    public boolean update(Item entity) throws SQLException, ClassNotFoundException {
        String updateQuery="UPDATE item SET description= ?, unitPrice=?, qtyOnHand=? WHERE code=?";

        return CrudUtil.execute(
                updateQuery,
                entity.getDescription(),
                entity.getUnitPrice(),
                entity.getQtyOnHand(),
                entity.getCode()
        );

    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        String deleteQuery="DELETE FROM item WHERE code=?";
        return CrudUtil.execute(
                deleteQuery,
                value
        );
    }
}
