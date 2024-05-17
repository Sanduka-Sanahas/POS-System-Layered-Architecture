package bo.custom.impl;

import bo.custom.ItemBo;
import dao.DaoFactory;
import dao.custom.ItemDao;
import dao.custom.impl.ItemDaoImpl;
import dao.util.DaoType;
import dto.CustomerDto;
import dto.ItemDto;
import entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBoImpl implements ItemBo {
    private ItemDao itemDao= DaoFactory.getInstance().getDao(DaoType.ITEM);

    @Override
    public boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDao.save(new Item(
                dto.getCode(),
                dto.getDesc(),dto.getUnitPrice(),
                dto.getQty()));
    }

    @Override
    public List<ItemDto> allItems() throws SQLException, ClassNotFoundException {
        List <ItemDto> itemDtoList=new ArrayList<>();
        for (Item item:itemDao.getAll()) {
            itemDtoList.add(new ItemDto(
                    item.getCode(),
                    item.getDescription(),
                    item.getUnitPrice(),
                    item.getQtyOnHand()));
        }
        return itemDtoList;
    }

    @Override
    public boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDao.update(
                new Item(
                        dto.getCode(),
                        dto.getDesc(),
                        dto.getUnitPrice(),
                        dto.getQty()
                ));
    }

    @Override
    public boolean deleteItem(String value) throws SQLException, ClassNotFoundException {
        return itemDao.delete(value);
    }
}
