package ch.zhaw.bait17.audio_signal_processing_toolbox.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ch.zhaw.bait17.audio_signal_processing_toolbox.DecoderException;
import ch.zhaw.bait17.audio_signal_processing_toolbox.R;
import ch.zhaw.bait17.audio_signal_processing_toolbox.WaveDecoder;
import ch.zhaw.bait17.audio_signal_processing_toolbox.model.Song;

public class AchartengineChartActivity extends AppCompatActivity {

    private GraphicalView mChart;
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    private XYSeries mCurrentSeries;
    private XYSeriesRenderer mCurrentRenderer;
    private Song song;
    private WaveDecoder decoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achartengine_chart);

        Bundle bundle = getIntent().getExtras();
        song = bundle.getParcelable(MediaListActivity.KEY_SONG);

        try {
            InputStream is = getContentResolver().openInputStream(song.getUri());
            decoder = new WaveDecoder(is);
        } catch (FileNotFoundException | DecoderException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
        if (mChart == null) {
            initChart();
            addData();
            //mChart = ChartFactory.getLineChartView(this, mDataset, mRenderer);
            mChart = ChartFactory.getCubeLineChartView(this, mDataset, mRenderer, 0.3f);
            layout.addView(mChart);
        } else {
            mChart.repaint();
        }
    }

    private void initChart() {
        mCurrentSeries = new XYSeries("Sample Data");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);
    }

    private void addData() {
        byte[] data = null;
        try {
            data = decoder.getRawPCM();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 200; i++) {
            mCurrentSeries.add(i, data[i]);
        }
    }

}
