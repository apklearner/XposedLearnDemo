package com.am.hskt;

public enum FieldEnums {

//    private String[] values = new String[]{"pkgName", "model", "deviceId", "manufacture", "product", "brand", "board", "cpu_abi", "android_id", "sdk_int", "sdk_release", "width", "height", "density", "subscriberId"};

    //    pkgName("pkgName"),
    tags("tags"),
    deviceId("deviceId"),
    id("id"),
    serial("serial"),
    model("model"),
    time("time"),
//    support_abi("support_abi"),
    type("type"),
    hardward("hardware"),
    bootloader("bootloader"),
    fingerprint("fingerprint"),
    user("user"),
    host("host"),
    device("device"),
    display("display"),
    manufacture("manufacture"),
    product("product"),
    brand("brand"),
    board("board"),
    cpu_abi("cpu_abi"),
    cpu_abi2("cup_abi2"),
    android_id("android_id"),
    sdk_int("sdk_int"),
    sdk_release("sdk_release"),
    width("width"),
    height("height"),
    density("density"),
    subscriberId("subscriberId"),
    netOperator("netOperator"),
    simOperator("simOperator"),

    lineNumber("lineNumber"),
    simserialNumber("simserialNumber"),


    telNetType("telNetType"),
    simState("simState"),
    root("root"),
    bssid("bssid"),
    ssid("ssid"),
    mac("mac"),
    wifiEnable("wifiEnable"),
    netType("netType"),
    netSubType("netSubType"),
    ipAdress("ipAdress"),
    lat("lat"),
    lng("lng");


    public String value;

    FieldEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
