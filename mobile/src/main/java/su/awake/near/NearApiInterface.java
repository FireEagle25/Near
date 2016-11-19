package su.awake.near;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import su.awake.near.apiObjects.Applet;

public interface NearApiInterface {

    @GET("/api/beacon/{beacon_id}")
    Call<Applet> getApletInfo(@Path("beacon_id") String username);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://188.166.160.236")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
