package net.kibotu.android.network.availabilty.service;

import android.net.NetworkInfo;

import java.util.Locale;

/**
 * Represents the device's network connectivity type.
 */
public enum ConnectivityType {
    TYPE_NONE("none"),
    WIFI("wifi"),
    WIMAX("wimax"),
    // mobile
    MOBILE("mobile"),
    // Android L calls this Cellular, because I have no idea!
    CELLULAR("cellular"),
    // 2G network types
    GSM("gsm"),
    GPRS("gprs"),
    EDGE("edge"),
    // 3G network types
    CDMA("cdma"),
    UMTS("umts"),
    HSPA("hspa"),
    HSUPA("hsupa"),
    HSDPA("hsdpa"),
    ONEXRTT("1xrtt"),
    EHRPD("ehrpd"),
    // 4G network types
    LTE("lte"),
    UMB("umb"),
    HSPA_PLUS("hspa+"),
    // return type
    TYPE_UNKNOWN("unknown"),
    TYPE_ETHERNET("ethernet"),
    TYPE_WIFI("wifi"),
    TYPE_2G("2g"),
    TYPE_3G("3g"),
    TYPE_4G("4g");

    public final String value;

    ConnectivityType(final String value) {
        this.value = value;
    }

    public static ConnectivityType getTypeForNetworkInfo(final NetworkInfo info) {
        if (info == null)
            return TYPE_NONE;

        String type = info.getTypeName().toLowerCase(Locale.US);
        if (type.equals(WIFI.value)) {
            return TYPE_WIFI;
        } else if (type.equals(TYPE_ETHERNET.value)) {
            return TYPE_ETHERNET;
        } else if (type.equals(WIMAX.value)) {
            return WIMAX;
        } else if (type.equals(MOBILE.value) || type.equals(CELLULAR.value)) {
            type = info.getSubtypeName();
            if (type.equals(GSM.value)
                    || type.equals(GPRS.value)
                    || type.equals(EDGE.value))
                return TYPE_2G;
            else if (type.startsWith(CDMA.value)
                    || type.equals(UMTS.value)
                    || type.equals(ONEXRTT.value)
                    || type.equals(EHRPD.value)
                    || type.equals(HSUPA.value)
                    || type.equals(HSDPA.value)
                    || type.equals(HSPA.value))
                return TYPE_3G;
            else if (type.equals(LTE.value)
                    || type.equals(UMB.value)
                    || type.equals(HSPA_PLUS.value))
                return TYPE_4G;
        }

        return TYPE_UNKNOWN;
    }
}