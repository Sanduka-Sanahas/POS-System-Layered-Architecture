package controller;

import bo.BoFactory;
import bo.custom.CustomerBo;
import bo.custom.ItemBo;
import bo.custom.OrderBo;
import bo.custom.impl.ItemBoImpl;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dao.util.BoType;
import dto.CustomerDto;
import dto.ItemDto;
import dto.OrderDetailsDto;
import dto.OrderDto;
import dto.tm.ItemTm;
import dto.tm.OrderTm;
import entity.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import dao.custom.CustomerDao;
import dao.custom.ItemDao;
import dao.custom.OrderDao;
import dao.custom.impl.CustomerDaoImpl;
import dao.custom.impl.ItemDaoImpl;
import dao.custom.impl.OrderDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderFormController {
    public JFXComboBox cmbCustId;
    public JFXComboBox cmbItemCode;
    public JFXTextField txtCustName;
    public JFXTextField txtDesc;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQty;
    public JFXTreeTableView<OrderTm> tblOrder;
    public TreeTableColumn colCode;
    public TreeTableColumn colDesc;
    public TreeTableColumn colQty;
    public TreeTableColumn colAmount;
    public TreeTableColumn colOption;
    public Label lblTotal;
    public Label lblOrderId;
    public JFXButton backBtn;

    private List<CustomerDto> customers;
    private List<ItemDto> items;
    private double tot = 0;

    private CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    ItemBo itemBo= BoFactory.getInstance().getBo(BoType.ITEM);
    private OrderBo orderBo=BoFactory.getInstance().getBo(BoType.ORDER);
    private ObservableList<OrderTm> tmList = FXCollections.observableArrayList();

    public void initialize() throws SQLException, ClassNotFoundException {
        colCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        colDesc.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colAmount.setCellValueFactory(new TreeItemPropertyValueFactory<>("amount"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        setOrderID();
        loadCustomerIds();
        loadItemCodes();


        cmbCustId.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, id) -> {
            for (CustomerDto dto:customers) {
                if (dto.getId().equals(id)){
                    txtCustName.setText(dto.getName());
                }
            }
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, code) -> {
            for (ItemDto dto:items) {
                if (dto.getCode().equals(code)){
                    txtDesc.setText(dto.getDesc());
                    txtUnitPrice.setText(String.format("%.2f",dto.getUnitPrice()));
                }
            }
        });
    }

    private void setOrderID() throws SQLException, ClassNotFoundException {
        lblOrderId.setText(orderBo.generateID());
    }


    private void loadItemCodes() {
        try {
            items = itemBo.allItems();
            ObservableList list = FXCollections.observableArrayList();
            for (ItemDto dto:items) {
                list.add(dto.getCode());
            }
            cmbItemCode.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomerIds() throws SQLException, ClassNotFoundException {
        customers=new ArrayList<>();
        customers.addAll(customerBo.allCustomers());

        ObservableList list = FXCollections.observableArrayList();
        for (CustomerDto dto:customers) {
            list.add(dto.getId());
        }
        cmbCustId.setItems(list);
    }

    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage= (Stage)backBtn.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashBoardForm.fxml"))));
    }

    public void addToCartButtonOnAction(ActionEvent actionEvent) {
        //            double amount = itemModel.getItem(cmbItemCode.getValue().toString()).getUnitPrice() * Integer.parseInt(txtQty.getText());
        JFXButton btn = new JFXButton("Delete");

        OrderTm tm = new OrderTm(
                cmbItemCode.getValue().toString(),
                txtDesc.getText(),
                Integer.parseInt(txtQty.getText()),
                Integer.parseInt(txtQty.getText())*Double.parseDouble(txtUnitPrice.getText()),
                btn
        );

        btn.setOnAction(actionEvent1 -> {
            tmList.remove(tm);
            tot -= tm.getAmount();
            tblOrder.refresh();
            lblTotal.setText(String.format("%.2f",tot));
        });

        boolean isExist = false;

        for (OrderTm order:tmList) {
            if (order.getCode().equals(tm.getCode())){
                order.setQty(order.getQty()+tm.getQty());
                order.setAmount(order.getAmount()+tm.getAmount());
                isExist = true;
                tot+=tm.getAmount();
            }
        }

        if (!isExist){
            tmList.add(tm);
            tot+= tm.getAmount();
        }

        TreeItem<OrderTm> treeObject = new RecursiveTreeItem<OrderTm>(tmList, RecursiveTreeObject::getChildren);
        tblOrder.setRoot(treeObject);
        tblOrder.setShowRoot(false);

        lblTotal.setText(String.format("%.2f",tot));

    }



    public void placeOrderButtonOnAction(ActionEvent actionEvent) {
        List<OrderDetailsDto> list = new ArrayList<>();
        for (OrderTm tm:tmList) {
            list.add(new OrderDetailsDto(
                    lblOrderId.getText(),
                    tm.getCode(),
                    tm.getQty(),
                    tm.getAmount()/tm.getQty()
            ));
        }
//        if (!tmList.isEmpty()){
            boolean isSaved = false;
            try {
                isSaved = orderBo.saveOrder(new OrderDto(
                        lblOrderId.getText(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")),
                        cmbCustId.getValue().toString(),
                        list
                ));
                if (isSaved){
                    new Alert(Alert.AlertType.INFORMATION,"Order Saved!").show();
                }else{
                    new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


//        }
    }

}
