package BikeShop.table;

import BikeShop.Entity.SalesEntity;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

/**
 * Created by Vishva on 3/29/2017.
 */
public class TableCheckBoxValueFactory implements Callback {
    @Override
    public TableCell call(Object param) {
        CheckBoxTableCell<SalesEntity,Boolean> checkBoxCell = new CheckBoxTableCell();
        return checkBoxCell;
    }
}
