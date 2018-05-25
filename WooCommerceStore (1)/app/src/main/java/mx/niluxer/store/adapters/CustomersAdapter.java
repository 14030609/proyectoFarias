package mx.niluxer.store.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import mx.niluxer.store.R;
import mx.niluxer.store.data.model.Customers;
import mx.niluxer.store.utils.CustomPicasso;
import mx.niluxer.store.utils.UnsafeOkHttpClient;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ViewHolder> {

    private List<Customers> mItems;
    private Context mContext;
    private CustomersItemListener mItemListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView tvCustomersName, tvCustomersEmail,tvCustomersAddress,tvCustomersCompras;
        public ImageView ivCustomersImage;
        CustomersItemListener mItemListener;

        public ViewHolder(View itemView, CustomersItemListener postItemListener) {
            super(itemView);
            tvCustomersName  = (TextView) itemView.findViewById(R.id.tvCustomersName);
            tvCustomersEmail = (TextView) itemView.findViewById(R.id.tvCustomersEmail);
            tvCustomersAddress = (TextView) itemView.findViewById(R.id.tvCustomersAddress);
            tvCustomersCompras = (TextView) itemView.findViewById(R.id.tvCustomersCompras);


            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Customers item = getItem(getAdapterPosition());
            this.mItemListener.onCustomersClick(item.getId());

            notifyDataSetChanged();
        }

        @Override
        public boolean onLongClick(View v) {
            Customers item = getItem(getAdapterPosition());
            this.mItemListener.onCustomersLongClick(item);

            //notifyDataSetChanged();
            return true;
        }
    }

    public CustomersAdapter(Context context, List<Customers> posts, CustomersItemListener itemListener) {
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public CustomersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View CustomersView = inflater.inflate(R.layout.customers_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(CustomersView, this.mItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomersAdapter.ViewHolder holder, int position) {

        Customers item = mItems.get(position);
        TextView textView1 = holder.tvCustomersName;
        TextView textView2 = holder.tvCustomersEmail;
        TextView textView3 = holder.tvCustomersAddress;
        TextView textView4 = holder.tvCustomersCompras;
        textView1.setText(item.getFirstName());
        textView2.setText(item.getEmail());
        textView3.setText("Address");
        textView4.setText(item.getOrdersCount().toString());


        //Picasso.get().load(item.getImages().get(0).getSrc().replace("https", "http")).transform(new CropCircleTransformation()).resize(150, 150).into(imageView);


        /*OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        Picasso picasso = new Picasso.Builder(mContext)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
        picasso.get().load("http://192.168.1.66/~niluxer/wordpress/wp-content/uploads/2018/05/long-sleeve-tee-2.jpg").into(holder.ivCustomersImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateCustomers(List<Customers> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private Customers getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface CustomersItemListener {
        void onCustomersClick(long id);
        void onCustomersLongClick(Customers Customers);
    }
}
