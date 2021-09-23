package org.openhab.binding.miio.internal.json;

import org.eclipse.jdt.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import org.eclipse.jdt.annotation.NonNullByDefault;
import java.util.List;


@NonNullByDefault
public class GatewayDevicesList {
    @SerializedName("code")
    @Nullable
    public Integer code;

    @SerializedName("result")
    @Nullable
    public List<GatewayDeviceItem> result;
}
