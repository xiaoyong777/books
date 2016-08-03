package com.zxy.books.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zxy.books.R;
import com.zxy.books.util.ColorUtil;
import com.zxy.books.util.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy on 2015/12/31.
 * 阅读界面字体背景设置对话框
 */
public class ReadSettingDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private MyTextView preview;
    private NumberPicker mDateSpinner;
    private ImageButton btTextSizeAdd;
    private ImageButton btTextSzieReduce;
    private CheckBox textCololCheckbox1, textCololCheckbox2, textCololCheckbox3, textCololCheckbox4,
            textCololCheckbox5, textCololCheckbox6, textCololCheckbox7, textCololCheckbox8;
    private ImageButton btClose;
    private CheckBox bgCheckBox1, bgCheckBox2, bgCheckBox3, bgCheckBox4,
            bgCheckBox5, bgCheckBox6, bgCheckBox7, bgCheckBox8,
            bgCheckBox9, bgCheckBox10, bgCheckBox11, bgCheckBox12;
    private List<CheckBox> textColorCheckBox;
    private List<CheckBox> bgCheckBox;
    private CheckBox currentTextColor;
    private CheckBox currentbg;
    private Handler mHandler;
    private SeekBar seekBar;
    private CheckBox brightCheckBox;

    public ReadSettingDialog(Context context, Handler mHandler) {
        super(context, R.style.Dialog);
        this.context = context;
        this.mHandler = mHandler;
        textColorCheckBox = new ArrayList<>();
        bgCheckBox = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_read_setting);
        initFindViewById();
        initView();
        setOnClick();
        mDateSpinner.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                preview.setmTextSize(newVal);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               // brightCheckBox.setChecked(false);
            }
        });
        super.onCreate(savedInstanceState);
    }

    private void initFindViewById() {
        preview = (MyTextView) findViewById(R.id.preview);
        btTextSizeAdd = (ImageButton) findViewById(R.id.add);
        btTextSzieReduce = (ImageButton) findViewById(R.id.reduce);
        btClose = (ImageButton) findViewById(R.id.close);
        mDateSpinner = (NumberPicker) findViewById(R.id.hourpicker);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        brightCheckBox = (CheckBox) findViewById(R.id.brightCheckBox);
        textCololCheckbox1 = (CheckBox) findViewById(R.id.textCololCheckbox1);
        textColorCheckBox.add(textCololCheckbox1);
        textCololCheckbox2 = (CheckBox) findViewById(R.id.textCololCheckbox2);
        textColorCheckBox.add(textCololCheckbox2);
        textCololCheckbox3 = (CheckBox) findViewById(R.id.textCololCheckbox3);
        textColorCheckBox.add(textCololCheckbox3);
        textCololCheckbox4 = (CheckBox) findViewById(R.id.textCololCheckbox4);
        textColorCheckBox.add(textCololCheckbox4);
        textCololCheckbox5 = (CheckBox) findViewById(R.id.textCololCheckbox5);
        textColorCheckBox.add(textCololCheckbox5);
        textCololCheckbox6 = (CheckBox) findViewById(R.id.textCololCheckbox6);
        textColorCheckBox.add(textCololCheckbox6);
        textCololCheckbox7 = (CheckBox) findViewById(R.id.textCololCheckbox7);
        textColorCheckBox.add(textCololCheckbox7);
        textCololCheckbox8 = (CheckBox) findViewById(R.id.textCololCheckbox8);
        textColorCheckBox.add(textCololCheckbox8);
        bgCheckBox1 = (CheckBox) findViewById(R.id.bgCheckBox1);
        bgCheckBox.add(bgCheckBox1);
        bgCheckBox2 = (CheckBox) findViewById(R.id.bgCheckBox2);
        bgCheckBox.add(bgCheckBox2);
        bgCheckBox3 = (CheckBox) findViewById(R.id.bgCheckBox3);
        bgCheckBox.add(bgCheckBox3);
        bgCheckBox4 = (CheckBox) findViewById(R.id.bgCheckBox4);
        bgCheckBox.add(bgCheckBox4);
        bgCheckBox5 = (CheckBox) findViewById(R.id.bgCheckBox5);
        bgCheckBox.add(bgCheckBox5);
        bgCheckBox6 = (CheckBox) findViewById(R.id.bgCheckBox6);
        bgCheckBox.add(bgCheckBox6);
        bgCheckBox7 = (CheckBox) findViewById(R.id.bgCheckBox7);
        bgCheckBox.add(bgCheckBox7);
        bgCheckBox8 = (CheckBox) findViewById(R.id.bgCheckBox8);
        bgCheckBox.add(bgCheckBox8);
        bgCheckBox9 = (CheckBox) findViewById(R.id.bgCheckBox9);
        bgCheckBox.add(bgCheckBox9);
        bgCheckBox10 = (CheckBox) findViewById(R.id.bgCheckBox10);
        bgCheckBox.add(bgCheckBox10);
        bgCheckBox11 = (CheckBox) findViewById(R.id.bgCheckBox11);
        bgCheckBox.add(bgCheckBox11);
        bgCheckBox12 = (CheckBox) findViewById(R.id.bgCheckBox12);
        bgCheckBox.add(bgCheckBox12);
    }

    private void initView() {
        mDateSpinner.setMinValue(20);
        mDateSpinner.setMaxValue(150);
        mDateSpinner.setValue(ConfigUtil.getTextSize(context));
        mDateSpinner.setWrapSelectorWheel(false);
        currentTextColor = textColorCheckBox.get(ConfigUtil.getTextColor(context));
        currentTextColor.setChecked(true);
        currentbg = bgCheckBox.get(ConfigUtil.getReadBg(context));
        currentbg.setChecked(true);
//        if (ConfigUtil.getbright(context) == -1) {
//            brightCheckBox.setChecked(true);
//        }else{
//            seekBar.setProgress(ConfigUtil.getbright(context));
//
//        }
    }

    private void setOnClick() {
        btClose.setOnClickListener(this);
        btTextSizeAdd.setOnClickListener(this);
        btTextSzieReduce.setOnClickListener(this);
        textCololCheckbox1.setOnClickListener(this);
        textCololCheckbox2.setOnClickListener(this);
        textCololCheckbox3.setOnClickListener(this);
        textCololCheckbox4.setOnClickListener(this);
        textCololCheckbox5.setOnClickListener(this);
        textCololCheckbox6.setOnClickListener(this);
        textCololCheckbox7.setOnClickListener(this);
        textCololCheckbox8.setOnClickListener(this);
        bgCheckBox1.setOnClickListener(this);
        bgCheckBox2.setOnClickListener(this);
        bgCheckBox3.setOnClickListener(this);
        bgCheckBox4.setOnClickListener(this);
        bgCheckBox5.setOnClickListener(this);
        bgCheckBox6.setOnClickListener(this);
        bgCheckBox7.setOnClickListener(this);
        bgCheckBox8.setOnClickListener(this);
        bgCheckBox9.setOnClickListener(this);
        bgCheckBox10.setOnClickListener(this);
        bgCheckBox11.setOnClickListener(this);
        bgCheckBox12.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.close:
                setConfig();
                dismiss();
                break;
            case R.id.add:
                if ((preview.getmTextSize() + 1) > 150)
                    preview.setmTextSize(150);
                else
                    preview.setmTextSize(preview.getmTextSize() + 1);
                mDateSpinner.setValue(preview.getmTextSize());
                break;
            case R.id.reduce:
                if ((preview.getmTextSize() - 1) < 20)
                    preview.setmTextSize(20);
                else
                    preview.setmTextSize(preview.getmTextSize() - 1);
                mDateSpinner.setValue(preview.getmTextSize());
                break;
            case R.id.textCololCheckbox1:
                currentTextColor.setChecked(false);
                currentTextColor = textCololCheckbox1;
                preview.setTextColorId(0);
                break;
            case R.id.textCololCheckbox2:
                currentTextColor.setChecked(false);
                currentTextColor = textCololCheckbox2;
                preview.setTextColorId(1);
                break;
            case R.id.textCololCheckbox3:
                currentTextColor.setChecked(false);
                currentTextColor = textCololCheckbox3;
                preview.setTextColorId(2);
                break;
            case R.id.textCololCheckbox4:
                currentTextColor.setChecked(false);
                currentTextColor = textCololCheckbox4;
                preview.setTextColorId(3);
                break;
            case R.id.textCololCheckbox5:
                currentTextColor.setChecked(false);
                currentTextColor = textCololCheckbox5;
                preview.setTextColorId(4);
                break;
            case R.id.textCololCheckbox6:
                currentTextColor.setChecked(false);
                currentTextColor = textCololCheckbox6;
                preview.setTextColorId(5);
                break;
            case R.id.textCololCheckbox7:
                currentTextColor.setChecked(false);
                currentTextColor = textCololCheckbox7;
                preview.setTextColorId(6);
                break;
            case R.id.textCololCheckbox8:
                currentTextColor.setChecked(false);
                currentTextColor = textCololCheckbox8;
                preview.setTextColorId(7);
                break;
            case R.id.bgCheckBox1:
                currentbg.setChecked(false);
                currentbg = bgCheckBox1;
                preview.setbGId(0);
                break;
            case R.id.bgCheckBox2:
                currentbg.setChecked(false);
                currentbg = bgCheckBox2;
                preview.setbGId(1);
                break;
            case R.id.bgCheckBox3:
                currentbg.setChecked(false);
                currentbg = bgCheckBox3;
                preview.setbGId(2);
                break;
            case R.id.bgCheckBox4:
                currentbg.setChecked(false);
                currentbg = bgCheckBox4;
                preview.setbGId(3);
                break;
            case R.id.bgCheckBox5:
                currentbg.setChecked(false);
                currentbg = bgCheckBox5;
                preview.setbGId(4);
                break;
            case R.id.bgCheckBox6:
                currentbg.setChecked(false);
                currentbg = bgCheckBox6;
                preview.setbGId(5);
                break;
            case R.id.bgCheckBox7:
                currentbg.setChecked(false);
                currentbg = bgCheckBox7;
                preview.setbGId(6);
                break;
            case R.id.bgCheckBox8:
                currentbg.setChecked(false);
                currentbg = bgCheckBox8;
                preview.setbGId(7);
                break;
            case R.id.bgCheckBox9:
                currentbg.setChecked(false);
                currentbg = bgCheckBox9;
                preview.setbGId(8);
                break;
            case R.id.bgCheckBox10:
                currentbg.setChecked(false);
                currentbg = bgCheckBox10;
                preview.setbGId(9);
                break;
            case R.id.bgCheckBox11:
                currentbg.setChecked(false);
                currentbg = bgCheckBox11;
                preview.setbGId(10);
                break;
            case R.id.bgCheckBox12:
                currentbg.setChecked(false);
                currentbg = bgCheckBox12;
                preview.setbGId(11);
                break;
        }
    }

    private void setConfig() {
        ConfigUtil.setTextSize(context, preview.getmTextSize());
        ConfigUtil.setreadbg(context, preview.getbGId());
        ConfigUtil.setTextColor(context, preview.getTextColorId());
        mHandler.sendEmptyMessage(100002);
    }
}
