package com.ren.newalbumchoose.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ren.newalbumchoose.R;


public class MainActivity extends AppCompatActivity {
    private RadioGroup choice_mode, show_mode;
    private Button picker_btn;
    private ImageView iv;
    private int SHOW_MODE;//是否显示视频
    private boolean isSingChoose = false;//是否单选，默认为false

    String str = 0 == 0 ? "" : "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choice_mode = (RadioGroup) findViewById(R.id.choice_mode);
        show_mode = (RadioGroup) findViewById(R.id.show_mode);
        picker_btn = (Button) findViewById(R.id.picker_btn);
        iv = (ImageView) findViewById(R.id.iv);


        picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (show_mode.getCheckedRadioButtonId() == R.id.showAll) {
                    SHOW_MODE = 0;
                } else if (show_mode.getCheckedRadioButtonId() == R.id.showPhoto) {
                    SHOW_MODE = 1;
                } else if (show_mode.getCheckedRadioButtonId() == R.id.showVideo) {
                    SHOW_MODE = 2;
                }
                if (choice_mode.getCheckedRadioButtonId() == R.id.singleChoose) {//单选
                    isSingChoose = true;
                } else {
                    isSingChoose = false;
                }

                Intent intent = new Intent(MainActivity.this, ChooseImageActivity.class);
                intent.putExtra("showVideo", SHOW_MODE);
                intent.putExtra("singleChoose", isSingChoose);
                startActivityForResult(intent, 30);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 30 && resultCode == Activity.RESULT_OK) {

            String data1 = data.getStringExtra("outUri");

            Toast.makeText(this, "==" + data1, Toast.LENGTH_SHORT).show();
            if (data1 == null) return;
            iv.setImageBitmap(BitmapFactory.decodeFile(data1));


        }
    }

}
