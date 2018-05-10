package ct7liang.android.phonehelper;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.ct7liang.tangyuan.utils.ScreenInfoUtil;

import ct7liang.android.phonehelper.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void findView() {
        initStatusBar();
        findViewById(R.id.title_back_ground).setBackgroundResource(R.color.colorPrimary);
        ((TextView)findViewById(R.id.center_text)).setText("PhoneHelper");
        findViewById(R.id.left_image).setOnClickListener(this);
        findViewById(R.id.set_ringTong).setOnClickListener(this);
    }

    @Override
    protected void setStatusBar() {
        findViewById(R.id.title_back_ground).setPadding(0, ScreenInfoUtil.getStatusHeight(this), 0, 0);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initFinish() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_image:
                finish();
                break;
            case R.id.set_ringTong:
                startActivity(new Intent(this, SetRingtoneActivity.class));
                break;
        }
    }
}