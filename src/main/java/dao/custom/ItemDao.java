package dao.custom;

import dao.CrudDao;
import dto.ItemDto;
import entity.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDao extends CrudDao<Item> {
//    boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException;
//    boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException;
//    boolean deleteItem(String code) throws SQLException, ClassNotFoundException;
    Item getItem(String code) throws SQLException, ClassNotFoundException;
//    List<ItemDto> allItems() throws SQLException, ClassNotFoundException;
}
