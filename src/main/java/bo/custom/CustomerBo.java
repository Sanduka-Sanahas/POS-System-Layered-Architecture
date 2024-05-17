package bo.custom;

import bo.SuperBo;
import dto.CustomerDto;

import java.util.List;
import java.sql.SQLException;

public interface CustomerBo extends SuperBo {
    boolean saveCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException;
    List<CustomerDto> allCustomers() throws SQLException, ClassNotFoundException;
    boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException;
    boolean deleteCustomer(String value) throws SQLException, ClassNotFoundException;
}
