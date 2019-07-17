package samstnet.com.kaz;



import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class ChartValueFormatter extends ValueFormatter {

    private DecimalFormat mFormat;

    public ChartValueFormatter() {
        mFormat = new DecimalFormat("###,###,###"); // use no decimals
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value);
    }
}
