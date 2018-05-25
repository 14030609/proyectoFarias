package mx.niluxer.store.data.remote;

import java.util.List;

import mx.niluxer.store.data.model.Customers;
import mx.niluxer.store.data.model.Orders;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WooCommerceService {
    @GET("customers?per_page=100")
    Call<List<Customers>> getCustomers();

    @GET("customers/")
    Call<Customers> getCustomers(@Query("id") int id);


    @GET("orders?")
    Call<List<Orders>> getOrders(@Query("customer") int id);

}
