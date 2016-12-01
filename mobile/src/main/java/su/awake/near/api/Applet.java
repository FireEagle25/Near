package su.awake.near.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import su.awake.near.App;
import su.awake.near.activites.MainActivity;

public class Applet extends ApiObject{

    protected static final String suffix = "beacon/";

    private String icon;
    private String description;
    private String sourceLink;
    private String name;
    private String token;
    private String appletActions;
    private int likeCount;
    private boolean isLiked;
    private int applet_id;


    public Applet() {
        super();
    }

    public void getInfo(String token) {
        super.getInfo(suffix + token + "&" + MainActivity.IMEI);

        try {
            this.token = token;
            applet_id = object.getInt("id");
            JSONObject apletContent = object.getJSONObject("applet_content");

            icon = String.valueOf(apletContent.get("icon"));
            if (!icon.substring(0, 3).equals("http"))
                icon = "http://188.166.160.236/" + icon;
            name = String.valueOf(apletContent.get("name"));
            sourceLink = String.valueOf(apletContent.get("source_link"));
            description = String.valueOf(apletContent.get("description"));
            likeCount = object.getInt("likes_count");
            isLiked = object.getInt("user_like") > 0;

            appletActions = "скидки | акции| онлайн-покупки";
            JSONArray arrayAppletActions = object.getJSONArray("applet_actions");
            for(int i=0; i<arrayAppletActions.length(); i++){
                String action = (String) arrayAppletActions.getJSONObject(i).getJSONObject("actions").get("action");
                appletActions += action + (i == arrayAppletActions.length() - 1 ? "" :" |");
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

    public int getLikeCount() {
        return likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public String getApplet_id() {
        return String.valueOf(applet_id);
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }
}
