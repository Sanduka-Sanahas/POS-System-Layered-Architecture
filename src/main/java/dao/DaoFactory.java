package dao;

import dao.custom.impl.CustomerDaoImpl;
import dao.custom.impl.ItemDaoImpl;
import dao.custom.impl.OrderDaoImpl;
import dao.custom.impl.OrderDetailsDaoImpl;
import dao.util.DaoType;

public class DaoFactory {
    private DaoFactory(){}
    private static DaoFactory daoFactory;
    public static DaoFactory getInstance(){
        return daoFactory!=null? daoFactory:(daoFactory=new DaoFactory());
    }
    public <T extends SuperDao>T getDao(DaoType type){
        switch (type){
            case CUSTOMER:return (T)new CustomerDaoImpl();
            case ORDER:return (T)new OrderDaoImpl();
            case ITEM:return (T)new ItemDaoImpl();
            case ORDER_DETAIL:return (T)new OrderDetailsDaoImpl();
        }
        return null;
    }
}
