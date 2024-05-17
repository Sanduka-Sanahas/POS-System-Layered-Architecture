package bo.custom.impl;

import bo.custom.OrderBo;
import dao.DaoFactory;
import dao.custom.OrderDao;
import dao.custom.impl.OrderDaoImpl;
import dao.util.DaoType;
import dto.OrderDto;

import java.sql.SQLException;

public class OrderBoImpl implements OrderBo {
    OrderDao orderDao= DaoFactory.getInstance().getDao(DaoType.ORDER);
    @Override
    public boolean saveOrder(OrderDto orderDto) throws SQLException, ClassNotFoundException {
        return orderDao.save(orderDto);
    }

    @Override
    public String generateID() throws SQLException, ClassNotFoundException {
            try {
                String id = orderDao.lastOrder().getOrderId();
                if (id!=null){
                    int num = Integer.parseInt(id.split("[D]")[1]);
                    num++;
                    return String.format("D%03d",num);
                }else{
                    return "D001";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        return null;
    }
}
