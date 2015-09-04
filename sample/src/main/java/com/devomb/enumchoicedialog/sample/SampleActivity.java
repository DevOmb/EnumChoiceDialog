package com.devomb.enumchoicedialog.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.devomb.enumchoicedialog.EnumChoiceDialog;
import com.devomb.enumchoicedialog.sample.Enums.BoolEnum;
import com.devomb.enumchoicedialog.sample.Enums.Size;


public class SampleActivity extends Activity {

    private Button openSmallDialogButton;
    private Button openLargeDialogButton;
    private TextView smallDialogChoiceTextView;
    private TextView largeDialogChoiceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        openSmallDialogButton = (Button) findViewById(R.id.open_small_dialog_button);
        openLargeDialogButton = (Button) findViewById(R.id.open_large_dialog_button);
        smallDialogChoiceTextView = (TextView) findViewById(R.id.small_dialog_choice_label);
        largeDialogChoiceTextView = (TextView) findViewById(R.id.large_dialog_choice_label);

        openSmallDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnumChoiceDialog<BoolEnum>(SampleActivity.this)
                        .title("Choose Option")
                        .titleColor(R.color.black)
                        .accentColor(R.color.red)
                        .options(BoolEnum.class)
                        .onAccept(new EnumChoiceDialog.OnAcceptListener() {
                            @Override
                            public void onAccept(Enum selectedEnum) {
                                smallDialogChoiceTextView.setText("Selected: " + selectedEnum.name());
                            }
                        })
                        .show();
            }
        });

        openLargeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnumChoiceDialog<Size>(SampleActivity.this)
                        .background(R.drawable.dialog_bg)
                        .title("Select Your Size", true)
                        .titleColor(R.color.holo_white)
                        .optionColor(R.color.holo_white)
                        .accentColor(R.color.blue)
                        .options(Size.class, 2)
                        .onAccept(new EnumChoiceDialog.OnAcceptListener() {
                            @Override
                            public void onAccept(Enum selectedEnum) {
                                largeDialogChoiceTextView.setText("Selected: " + selectedEnum.name());
                            }
                        })
                        .show();
            }
        });
    }

}
