package mx.niluxer.store;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import mx.niluxer.store.adapters.OrdersAdapter;
import mx.niluxer.store.data.model.Orders;
import mx.niluxer.store.data.model.Orders;
import mx.niluxer.store.data.remote.ApiUtils;
import mx.niluxer.store.data.remote.WooCommerceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity2 extends AppCompatActivity {

    private RecyclerView rvOrders;
    private WooCommerceService mService;
    private OrdersAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = ApiUtils.getWooCommerceService();
        rvOrders = (RecyclerView) findViewById(R.id.rvCustomers);
        mAdapter = new OrdersAdapter(this, new ArrayList<Orders>(0), new OrdersAdapter.OrdersItemListener() {

            @Override
            public void onOrdersClick(long id) {
//                Toast.makeText(MainActivity2.this, "Orders id is " + id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOrdersLongClick(Orders Orders) {
                showContextualMenu(Orders);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvOrders.setLayoutManager(layoutManager);
        rvOrders.setAdapter(mAdapter);
        rvOrders.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvOrders.addItemDecoration(itemDecoration);

        int customer_id = getIntent().getExtras().getInt("id");
        System.out.println("Customer id 1: " + customer_id);

        loadOrders(customer_id);

    }

    public void loadOrders(int customer_id) {
        System.out.println("Customer id 2: " + customer_id);
        mService.getOrders(customer_id).enqueue(new Callback<List<Orders>>() {
            @Override
            public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {

                if(response.isSuccessful()) {
                    mAdapter.updateOrders(response.body());
                    System.out.println(response.body().toString());
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


    private void showContextualMenu(final Orders Orders)
    {
        final CharSequence[] items = { "Ver Informacion", "Ver ordenes" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Menu:");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                switch (item)
                {
                    case 0:
                        //Toast.makeText(MainActivity.this, Orders.getName(), Toast.LENGTH_SHORT).show();
                        MainActivity2.NewEditOrdersDialog newEditOrdersDialog = new MainActivity2.NewEditOrdersDialog(MainActivity2.this);
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
//                        Intent i=new Intent (MainActivity.this,MainActivity2.class);
                        //int id=Orders.getId();
                        //i.putExtra("idOrders",id);

  //                      startActivity(i);

                        break;
                }

            }

        });

        AlertDialog alert = builder.create();

        alert.show();
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


/*

            String title = "Informacion de los Clientes";
           // if(editMode) title = "Edit Orders";
            setTitle(title);
            setMessage("Informacion de los CLientes");
            View view = LayoutInflater.from(context).inflate(R.layout.info_item, null);
            setView(view);
            final TextView tvOrdersName, tvOrdersEmail,tvOrdersAddress,tvOrdersCompras,tvOrdersFechaCreacion,tvOrdersRol,tvOrdersUserName,tvOrdersTotalGastado,tvOrdersDireccionFacturacion,tvOrdersDireccionEnvio;
            tvOrdersName  = (TextView) view.findViewById(R.id.tvOrdersName);
            tvOrdersEmail = (TextView) view.findViewById(R.id.tvOrdersEmail);
            tvOrdersAddress = (TextView) view.findViewById(R.id.tvOrdersAddress);
            tvOrdersCompras = (TextView) view.findViewById(R.id.tvOrdersCompras);
            tvOrdersFechaCreacion = (TextView) view.findViewById(R.id.tvOrdersFechaCreacion);
            tvOrdersRol = (TextView) view.findViewById(R.id.tvOrdersRol);
            tvOrdersUserName = (TextView) view.findViewById(R.id.tvOrdersUserName);
            tvOrdersTotalGastado = (TextView) view.findViewById(R.id.tvOrdersTotalGastado);
            tvOrdersDireccionFacturacion = (TextView) view.findViewById(R.id.tvOrdersDireccionFacturacion);
            tvOrdersDireccionEnvio = (TextView) view.findViewById(R.id.tvOrdersDireccionEnvio);

            tvOrdersName.setText(Orders.getFirstName());
            tvOrdersEmail.setText(Orders.getEmail());
            tvOrdersAddress.setText("Address");
            tvOrdersCompras.setText(Orders.getOrdersCount().toString());
            tvOrdersFechaCreacion.setText(Orders.getDateCreated());
            tvOrdersRol.setText(Orders.getRole());
            tvOrdersUserName.setText(Orders.getUsername());
            tvOrdersTotalGastado.setText(Orders.getTotalSpent());
            tvOrdersDireccionFacturacion.setText(Orders.getBilling().getAddress1());
            tvOrdersDireccionEnvio.setText(Orders.getShipping().getAddress1());

*/



            /*setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   // Intent siguiente=new Intent(MainActivity.this,MainActivity2.class);
                    //startActivity(siguiente);
                }
            });*/

            super.onCreate(savedInstanceState);

        }
    }

}
