package su.awake.near.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Applet extends ApiObject{

    protected static final String suffix = "beacon/";

    private String icon;
    private String description;
    private String sourceLink;
    private String name;
    private String token;
    private String appletActions;


    public Applet() {
        super();
    }

    public Applet(String icon, String description, String sourceLink, String name, String token, String appletActions) {
        super();

        this.icon = icon;
        this.name = name;
        this.description = description;
        this.sourceLink = sourceLink;
        this.token = token;
        this.appletActions = appletActions;
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

            appletActions = "";
            JSONArray arrayAppletActions = object.getJSONArray("applet_actions");
            for(int i=0; i<arrayAppletActions.length(); i++){
                String action = (String) arrayAppletActions.getJSONObject(i).getJSONObject("actions").get("action");
                appletActions += action + (i == arrayAppletActions.length() - 1 ? "" :"|");
            }


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

    public String getAppletActions() {
        return appletActions;
    }
}
