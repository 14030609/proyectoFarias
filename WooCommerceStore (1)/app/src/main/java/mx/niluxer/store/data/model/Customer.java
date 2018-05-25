package mx.niluxer.store.data.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer {

    @SerializedName("href")
    @Expose
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
        /*return "{" +
                "src:'" + src + '\'' +
                '}';*/
    }

}