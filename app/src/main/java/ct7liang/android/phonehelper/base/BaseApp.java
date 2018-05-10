package ct7liang.android.phonehelper.base;

import android.app.Application;

import com.ct7liang.tangyuan.AppFolder;
import com.ct7liang.tangyuan.utils.LogUtils;
import com.ct7liang.tangyuan.utils.SpUtils;
import com.ct7liang.tangyuan.utils.ToastUtils;
import com.ct7liang.tangyuan.utils.crash.CrashUtils;

/**
 * Created by Administrator on 2018-05-07.
 *
 */

public class BaseApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ToastUtils.setIsShowTestEnable(true);
        LogUtils.setTag("PhoneHelper");
        LogUtils.setShowLocationEnable(false);
        LogUtils.setLogEnable(true);
        SpUtils.init(this, "phone_helper");
        AppFolder.createAppFolder("A_PHONE_HELPER");
        CrashUtils.init(getApplicationContext());
    }
}
