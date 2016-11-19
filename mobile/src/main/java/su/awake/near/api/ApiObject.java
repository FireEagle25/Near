package su.awake.near.api;


import org.json.JSONException;
import org.json.JSONObject;

public class ApiObject {
    final static String url = "http://188.166.160.236/api/";

    protected JSONObject object;

    public void getInfo(String path) {

        String response = HttpRequest.get(url + path).body();
        JSONObject reader = null;

        try {
            reader = new JSONObject(response);
            object = reader.getJSONArray("object").getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
