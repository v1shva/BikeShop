package BikeShop.table;

import BikeShop.Entity.SalesEntity;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Created by Vishva on 3/29/2017.
 */

public class TableCheckBoxValueFactory implements Callback<TableColumn.CellDataFeatures<SalesEntity, CheckBox>, ObservableValue<CheckBox>> {
    @Override
    public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<SalesEntity, CheckBox> param) {
        SalesEntity sl = param.getValue();
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue(sl.getChecked());
        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
            sl.setChecked(new_val);
        });
        return new SimpleObjectProperty<>(checkBox);
    }
}