package su.awake.near.apiObjects;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class Applet extends ApiObject{
    protected static final String suffix = "beacon/";
    private String icon;
    private String description;
    private String sourceLink;
    private String name;
    private String token;

    public Applet() {
        super();
    }

    public void getInfo(String token) {
        super.getInfo(suffix + token);

        try {
            this.token = token;
            JSONObject apletContent = object.getJSONObject("applet_content");
            icon = String.valueOf(apletContent.get("icon"));
            name = String.valueOf(apletContent.get("name"));
            sourceLink = String.valueOf(apletContent.get("source_link"));
            description = String.valueOf(apletContent.get("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return token + " " + name + " " + description;
    }
}
