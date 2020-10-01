package com.imra.mytestwork.app;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.ResponseBody;

public class MyRentateamError extends Throwable {

    public MyRentateamError(ResponseBody responseBody) {super(getMessage(responseBody));}

    public static String getMessage (ResponseBody responseBody) {
        try {
            return new JSONObject(responseBody.string()).optString("message");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return "Unknown exception";
    }

}
