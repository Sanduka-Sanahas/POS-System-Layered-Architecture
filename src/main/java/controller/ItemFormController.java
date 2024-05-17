package controller;

import bo.BoFactory;
import bo.custom.ItemBo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dao.util.BoType;
import db.DBConnection;
import dto.ItemDto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import dto.tm.ItemTm;
import dao.custom.ItemDao;
import dao.custom.impl.ItemDaoImpl;

import java.io.IOException;
import java.sql.*;
import java.util.function.Predicate;

public class ItemFormController {

    @FXML
    private TreeTableColumn colCode;

    @FXML
    private TreeTableColumn colDesc;

    @FXML
    private TreeTableColumn colOption;

    @FXML
    private TreeTableColumn colQty;

    @FXML
    private TreeTableColumn colUnitPrice;

    @FXML
    private BorderPane pane;

    @FXML
    private JFXTreeTableView<ItemTm> tblItem;

    @FXML
    private JFXTextField txtCode;

    @FXML
    private JFXTextField txtDesc;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXTextField txtUnitPrice;
    private ItemBo itemBo= BoFactory.getInstance().getBo(BoType.ITEM);

    public void initialize(){
        colCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        colDesc.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        colUnitPrice.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));
        loadItemTable();

//        tblItem.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
//            setData(newValue);
//        });


        tblItem.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && (!tblItem.getSelectionModel().isEmpty())) {
                //Get the item that was clicked
                TreeItem<ItemTm> item = tblItem.getSelectionModel().getSelectedItem();
                //Set the text field to the item's value
                txtCode.setText(item.getValue().getCode());
                txtDesc.setText(item.getValue().getDesc());
                txtUnitPrice.setText(item.getValue().getUnitPrice()+"");
                txtQty.setText(item.getValue().getQty()+"");

            }
        });//

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
                tblItem.setPredicate(new Predicate<TreeItem<ItemTm>>() {
                    @Override
                    public boolean test(TreeItem<ItemTm> treeItem) {
                        return treeItem.getValue().getCode().contains(newValue) ||
                                treeItem.getValue().getDesc().contains(newValue);
                    }
                });
            }
        });
    }

//    private void setData(TreeItem<ItemTm> newValue) {
//        if (newValue != null) {
//            txtCode.setEditable(false);
//            txtId.setText(newValue.getId());
//            txtName.setText(newValue.getName());
//            txtAddress.setText(newValue.getAddress());
//            txtSalary.setText(String.valueOf(newValue.getSalary()));
//        }
//    }

    private void loadItemTable() {
        ObservableList<ItemTm> tmList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM item";

        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet result = stm.executeQuery(sql);

            while (result.next()){
                JFXButton btn = new JFXButton("Delete");

                ItemTm tm = new ItemTm(
                        result.getString(1),
                        result.getString(2),
                        result.getDouble(3),
                        result.getInt(4),
                        btn
                );

                btn.setOnAction(actionEvent -> {
                    deleteItem(tm.getCode());
                });

                tmList.add(tm);
            }

            TreeItem<ItemTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
            tblItem.setRoot(treeItem);
            tblItem.setShowRoot(false);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteItem(String code) {
        String sql = "DELETE from item WHERE code=?";

        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            pstm.setString(1,code);
            int result = pstm.executeUpdate();
            if (result>0){
                new Alert(Alert.AlertType.INFORMATION,"Item Deleted!").show();
                loadItemTable();
            }else{
                new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void saveButtonOnAction(ActionEvent event) {
        ItemDto dto = new ItemDto(txtCode.getText(),
                txtDesc.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQty.getText())
        );
        String sql = "INSERT INTO item VALUES(?,?,?,?)";

        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            pstm.setString(1,dto.getCode());
            pstm.setString(2,dto.getDesc());
            pstm.setDouble(3,dto.getUnitPrice());
            pstm.setInt(4,dto.getQty());
            int result = pstm.executeUpdate();
            if (result>0){
                new Alert(Alert.AlertType.INFORMATION,"Item Saved!").show();
                loadItemTable();
//                clearFields();
            }

        } catch (SQLIntegrityConstraintViolationException ex){
            new Alert(Alert.AlertType.ERROR,"Duplicate Entry").show();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void updateButtonOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        ItemDto itemDto=new ItemDto(txtCode.getText(),txtDesc.getText(),Double.parseDouble(txtUnitPrice.getText()),Integer.parseInt(txtQty.getText()));
        boolean isUpdated = itemBo.updateItem(itemDto);
        if (isUpdated){
            new Alert(Alert.AlertType.INFORMATION,"Update Success").show();
        }
    }

}
