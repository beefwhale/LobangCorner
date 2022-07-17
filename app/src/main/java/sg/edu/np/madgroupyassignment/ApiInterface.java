package sg.edu.np.madgroupyassignment;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("maps/api/directions/json")
    Single<Result> getDirection(@Query("mode") String mode,
                                @Query("origin") String origin,
                                @Query("destination") String destination,
                                @Query("key") String key);
}