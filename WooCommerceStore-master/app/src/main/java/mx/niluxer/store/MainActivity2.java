package mx.niluxer.store;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import mx.niluxer.store.adapters.InfoAdapter;
import mx.niluxer.store.data.model.Orders;
import mx.niluxer.store.data.model.Orders;
import mx.niluxer.store.data.remote.ApiUtils;
import mx.niluxer.store.data.remote.WooCommerceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity2 extends AppCompatActivity {

    private RecyclerView rvCustomers;
    private WooCommerceService mService;
    private InfoAdapter iAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_item);

        mService = ApiUtils.getWooCommerceService();
        rvCustomers = (RecyclerView) findViewById(R.id.rvCustomers);
        iAdapter = new InfoAdapter(this, new ArrayList<Orders>(0), new InfoAdapter.OrdersItemListener() {

            @Override
            public void onOrdersClick(long id) {
                Toast.makeText(MainActivity2.this, "Orders id is " + id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOrdersLongClick(Orders Orders) {
                showContextualMenu(Orders);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvCustomers.setLayoutManager(layoutManager);
        rvCustomers.setAdapter(iAdapter);
        rvCustomers.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvCustomers.addItemDecoration(itemDecoration);

        loadOrders();

    }

    public void loadOrders() {
        mService.getOrders().enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {

                if(response.isSuccessful()) {
                    iAdapter.updateOrders(response.body());
                    Log.d("MainActivity2", "Orders loaded from API");
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<List<Orders>> call, Throwable t) {
                //showErrorMessage();
                Log.d("MainActivity2", "error loading from API");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {

            case R.id.mnuExit:
                confirmExit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showContextualMenu(final Orders Orders)
    {
        final CharSequence[] items = { "Ver Informacion", "Ver ordenes" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Action:");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                switch (item)
                {
                    case 0:
                        //Toast.makeText(MainActivity.this, Orders.getName(), Toast.LENGTH_SHORT).show();
                        NewEditOrdersDialog newEditOrdersDialog = new NewEditOrdersDialog(MainActivity2.this);
                        newEditOrdersDialog.setEditMode(true);
                        newEditOrdersDialog.setOrders(Orders);
                        newEditOrdersDialog.show();

                        break;
                    case 1:
                        //Toast.makeText(MainActivity.this, Orders.getId() + "", Toast.LENGTH_SHORT).show();
                       /* mService.deleteOrders(Orders.getId()).enqueue(new Callback<Orders>() {
                            @Override
                            public void onResponse(Call<Orders> call, Response<Orders> response) {
                                if (response.isSuccessful())
                                {
                                    Toast.makeText(MainActivity.this, "Orders deleted successfully...", Toast.LENGTH_SHORT).show();
                                    loadOrders();
                                } else {
                                    try {
                                        System.out.println(response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Orders> call, Throwable t) {
                                System.out.println("Error deleting Orders...");
                            }
                        });*/
                        break;
                }

            }

        });

        AlertDialog alert = builder.create();

        alert.show();
    }

    private void runThread()
    {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    //Your code goes here
                    InetAddress address = null;
                    try {
                        address = InetAddress.getByName("https://store");
                        System.out.println(address.getHostAddress());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void confirmExit()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Exit");
        builder.setMessage("Please confirm exit.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    public class NewEditOrdersDialog extends android.app.AlertDialog {

        private boolean editMode = false;
        Context context;
        private Orders Orders;

        protected NewEditOrdersDialog(Context context) {
            super(context);
            this.context = context;
        }

        public void setEditMode(boolean editMode)
        {
            this.editMode = editMode;
        }

        public void setOrders(Orders Orders)
        {
            this.Orders = Orders;
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {


           /* String title = "New Orders";
            if(editMode) title = "Edit Orders";
            setTitle(title);
            setMessage("Orders Details");
            View view = LayoutInflater.from(context).inflate(R.layout.new_edit_Orders, null);
            setView(view);

            final EditText txtOrdersName = view.findViewById(R.id.txtOrdersName);
            final EditText txtOrdersType = view.findViewById(R.id.txtOrdersType);
            final EditText txtOrdersCategory = view.findViewById(R.id.txtOrdersCategory);
            final EditText txtOrdersRegularPrice = view.findViewById(R.id.txtOrdersRegularPrice);
            final EditText txtOrdersDescription = view.findViewById(R.id.txtOrdersDescription);*/

            /*String btnText = "Send";
            if(editMode)
            {
                btnText = "Save";
                txtOrdersName.setText(Orders.getName());
                txtOrdersType.setText(Orders.getType());
                Category category = Orders.getCategories().get(0);
                txtOrdersCategory.setText(category.getId()+"");
                txtOrdersRegularPrice.setText(Orders.getRegularPrice());
                txtOrdersDescription.setText(Orders.getDescription());
            }*/




            setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });

            super.onCreate(savedInstanceState);

        }
    }

}
