package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import baidu.com.testlibproject.R;

public class TestToastActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_toast_layout);
        initView();
    }

    private void initView() {
        Button btn1 = findViewById(R.id.toast_btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastType1();
            }
        });

        Button btn2 = findViewById(R.id.toast_btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastType2();
            }
        });
    }

    private void toastType1() {
        Toast.makeText(this, "toastType1, toastType1, toastType1!!!", Toast.LENGTH_LONG).show();
    }

    private void toastType2() {
        Toast toast = new Toast(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.toast_type2_layout, null);
        TextView textView = view.findViewById(R.id.text);
        String text = "toastType2, toastType2, toastType2!!!";
        textView.setText(text);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
}
