package BikeShop.table;

import BikeShop.Entity.SalesEntity;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

/**
 * Created by Vishva on 3/29/2017.
 */
public class TableDocListValueFactory implements Callback {
    @Override
    public TableCell call(Object param) {
        TableCell<SalesEntity,String> checkBoxCell = new TableCell();
        return checkBoxCell;
    }
}
