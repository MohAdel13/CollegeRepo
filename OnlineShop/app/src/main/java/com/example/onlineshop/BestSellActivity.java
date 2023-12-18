package com.example.onlineshop;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlineshop.databinding.ActivityBestSellBinding;
import com.example.onlineshop.pojo.Models.ProductModel;
import com.example.onlineshop.pojo.RoomDataBases.ProductDB;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class BestSellActivity extends AppCompatActivity {

    ProductDB productDB;
    ActivityBestSellBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //linking the activity with its layout
        binding = ActivityBestSellBinding.inflate(LayoutInflater.from(BestSellActivity.this));
        setContentView(binding.getRoot());

        //get instance of product database
        productDB = ProductDB.getInstance(getApplicationContext());

        //get a list of top 10 sold products
        List<ProductModel> products = productDB.productDao().getTopProducts();

        //create arrayList of BarEntry(each have x_axis, count, label)
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0;i<products.size();i++)
        {
            entries.add(new BarEntry(i, products.get(i).soldCount, products.get(i).title));
        }

        //converting the arrayList into dataset
        BarDataSet dataSet = new BarDataSet(entries, "Sample Bar Chart");

        //coloring the bars
        dataSet.setColor(getColor(R.color.orangeSecondary));

        //format the dataset
        binding.bestChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < entries.size()) {
                    // Get the label from BarEntry's data
                    String originalTitle = entries.get(index).getData().toString();

                    //if the label is greater than 20 chars it takes the first 20 only
                    return originalTitle.length() > 20 ? originalTitle.substring(0, 20) : originalTitle;
                }
                return "";
            }
        });

        //position of labels
        binding.bestChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        //intervals between labels
        binding.bestChart.getXAxis().setGranularity(1f);

        //make the label vertical
        binding.bestChart.getXAxis().setLabelRotationAngle(270);

        //make the labels in the center of the bars
        binding.bestChart.getXAxis().setCenterAxisLabels(false);

        //make the label count equals 10
        binding.bestChart.getXAxis().setLabelCount(10);

        BarData barData = new BarData(dataSet);
        binding.bestChart.setData(barData);

        //make the bars size equals the total size divide their count
        binding.bestChart.setFitBars(true);

        //animation duration (Up animation)
        binding.bestChart.animateY(1000);

        //we have no need for description or legend
        binding.bestChart.getDescription().setEnabled(false);
        binding.bestChart.getLegend().setEnabled(false);
    }
}