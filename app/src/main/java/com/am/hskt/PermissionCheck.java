package com.am.hskt;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by ly on 2018/10/19.
 */

public class PermissionCheck {

    public static boolean checkPermission(Context ctx, String permission) {
        return ctx.checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }
}
