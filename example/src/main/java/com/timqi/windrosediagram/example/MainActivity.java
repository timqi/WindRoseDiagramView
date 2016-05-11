package com.timqi.windrosediagram.example;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.timqi.windrosediagram.WindRoseClickListener;
import com.timqi.windrosediagram.WindRoseDiagramView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


  @Bind(R.id.windRoseDiagramView) WindRoseDiagramView windRoseDiagramView;
  @Bind(R.id.seekbar) SeekBar seekbar;
  @Bind(R.id.fullValueText) TextView fullValueText;
  @Bind(R.id.spinner) Spinner spinner;
  @Bind(R.id.currValueText) TextView currValueText;

  WRDAdapter adapter = new WRDAdapter();
  String[] spinnerDataList = new String[]{
      "outlineWidth",
      "anchorWidth",
      "textSize",
      "startAngle",
      "touchSlop"
  };
  String[] spinnerColorList = new String[]{
      "backgroundColor",
      "outlineColor",
      "foregroundColor",
      "anchorColor",
      "textColor"
  };
  @Bind(R.id.spinnerColor) Spinner spinnerColor;
  @Bind(R.id.currValueColor) View currValueColor;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    windRoseDiagramView.setAdapter(adapter);
    windRoseDiagramView.setWindRoseClickListener(windRoseClickListener);
    windRoseDiagramView.setOnClickListener(normalClickListener);

    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerDataList);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinner.setAdapter(spinnerAdapter);
    spinner.setOnItemSelectedListener(spinnerItemSelectedListener);

    seekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
    updateInfo();

    ArrayAdapter<String> spinnerColorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerColorList);
    spinnerColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerColor.setAdapter(spinnerColorAdapter);
    spinnerColor.setOnItemSelectedListener(spinnerColorItemSelectedListener);
    updateColor();
    currValueColor.setOnClickListener(colorPickerClickListener);
    currValueColor.setBackgroundColor(0xcc1888cc);
  }

  View.OnClickListener colorPickerClickListener
      = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      ColorPickerDialogBuilder.with(MainActivity.this)
          .setTitle("Choose color")
          .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
          .density(12)
          .setPositiveButton("OK", new ColorPickerClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
              switch (currSpinnerColorPosition) {
                case 0:
                  windRoseDiagramView.setBackgroundColor(i);
                  break;
                case 1:
                  windRoseDiagramView.setOutlineColor(i);
                  break;
                case 2:
                  windRoseDiagramView.setForegroundColor(i);
                  break;
                case 3:
                  windRoseDiagramView.setAnchorColor(i);
                  break;
                case 4:
                  windRoseDiagramView.setTextColor(i);
                  break;
              }
              currValueColor.setBackgroundColor(i);
            }
          }).setNegativeButton("CANCEL", null).build().show();
    }
  };

  private int currSpinnerColorPosition = 0;

  private void updateColor(int pos) {
    this.currSpinnerColorPosition = pos;
    updateColor();
  }

  private void updateColor() {

    switch (currSpinnerColorPosition) {
      case 0: // backgroundColor
        currValueColor.setBackgroundColor(windRoseDiagramView.getBackgroundColor());
        break;
      case 1: // outlineColor
        currValueColor.setBackgroundColor(windRoseDiagramView.getOutlineColor());
        break;
      case 2: // foregroundColor
        currValueColor.setBackgroundColor(windRoseDiagramView.getForegroundColor());
        break;
      case 3: // anchorColor
        currValueColor.setBackgroundColor(windRoseDiagramView.getAnchorColor());
        break;
      case 4: // textColor
        currValueColor.setBackgroundColor(windRoseDiagramView.getTextColor());
        break;
    }
  }


  AdapterView.OnItemSelectedListener spinnerColorItemSelectedListener
      = new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      updateColor(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
  };

  AdapterView.OnItemSelectedListener spinnerItemSelectedListener
      = new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      updateInfo(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
  };


  private int currSpinnerPosition = 0;

  private void updateInfo(int pos) {
    this.currSpinnerPosition = pos;
    updateInfo();
  }

  private void updateInfo() {

    switch (currSpinnerPosition) {
      case 0: // outlineWidth
        fullValueText.setText("20dp");
        int outlineWidth = windRoseDiagramView.getOutlineWidth();
        seekbar.setMax(dp2px(20));
        seekbar.setProgress(outlineWidth);
        currValueText.setText(px2dp(outlineWidth) + "dp");
        break;
      case 1: // anchorWidth
        fullValueText.setText("10dp");
        int anchorWidth = windRoseDiagramView.getAnchorWidth();
        seekbar.setMax(dp2px(10));
        seekbar.setProgress(anchorWidth);
        currValueText.setText(px2dp(anchorWidth) + "dp");
        break;
      case 2: // textSize
        fullValueText.setText("32dp");
        int textSize = windRoseDiagramView.getTextSize();
        seekbar.setMax(dp2px(32));
        seekbar.setProgress(textSize);
        currValueText.setText(px2dp(textSize) + "dp");
        break;
      case 3: // startAngle
        fullValueText.setText("360'");
        int startAngle = (int) (windRoseDiagramView.getStartAngle() + 0.5f);
        seekbar.setMax(360);
        seekbar.setProgress(startAngle);
        currValueText.setText(startAngle + "'");
        break;
      case 4: // touchSlop
        fullValueText.setText("52dp");
        int touchSlop = windRoseDiagramView.getTouchSlop();
        seekbar.setMax(dp2px(52));
        seekbar.setProgress(touchSlop);
        currValueText.setText(px2dp(touchSlop) + "dp");
        break;
    }
  }


  private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener
      = new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      switch (currSpinnerPosition) {
        case 0: // outlineWidth
          windRoseDiagramView.setOutlineWidth(progress);
          break;
        case 1:
          windRoseDiagramView.setAnchorWidth(progress);
          break;
        case 2:
          windRoseDiagramView.setTextSize(progress);
          break;
        case 3:
          windRoseDiagramView.setStartAngle(progress);
          break;
        case 4:
          windRoseDiagramView.setTouchSlop(progress);
          break;
      }

      updateInfo();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
  };

  private int dp2px(float dp) {
    return (int) (getResources().getDisplayMetrics().density * dp + 0.5f);
  }

  private int px2dp(float px) {
    return (int) (px / getResources().getDisplayMetrics().density + 0.5f);
  }

  private WindRoseClickListener windRoseClickListener
      = new WindRoseClickListener() {
    @Override
    public void onItemClick(int position) {
      Toast.makeText(MainActivity.this, "click: " + adapter.getName(position), Toast.LENGTH_SHORT).show();
    }
  };

  private View.OnClickListener normalClickListener
      = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
    }
  };
}
