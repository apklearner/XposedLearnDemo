package com.am.hskt;

public enum FieldEnmus {

//    private String[] values = new String[]{"pkgName", "model", "deviceId", "manufacture", "product", "brand", "board", "cpu_abi", "android_id", "sdk_int", "sdk_release", "width", "height", "density", "subscriberId"};

//    pkgName("pkgName"),
    model("model"),
    deviceId("deviceId"),
    manufacture("manufacture"),
    product("product"),
    brand("brand"),
    board("board"),
    cpu_abi("cpu_abi"),
    android_id("android_id"),
    sdk_int("sdk_int"),
    sdk_release("sdk_release"),
    width("width"),
    height("height"),
    density("density"),
    subscriberId("subscriberId"),
    root("root");


    String value;

    FieldEnmus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
