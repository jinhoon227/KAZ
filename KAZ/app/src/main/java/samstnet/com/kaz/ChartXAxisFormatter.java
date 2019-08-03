package samstnet.com.kaz;


import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class ChartXAxisFormatter extends ValueFormatter {

    private ArrayList<String> mValues;

    public ChartXAxisFormatter(ArrayList<String> values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues.get((int)value);
    }


}