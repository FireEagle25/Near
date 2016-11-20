package su.awake.near.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import su.awake.near.activites.MainActivity;


public class Like {
    public static String url = ApiObject.url;

    protected static final String suffix = "like/";
    private String like;

    public Like() {
        super();
    }

    public void getInfo(String token) {
        String response = HttpRequest.get(url + suffix + MainActivity.IMEI + "&" + token).body();
        JSONObject reader = null;

        try {
            reader = new JSONObject(response);
            like = reader.getString("object");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getLike() {
        return like;
    }
}
