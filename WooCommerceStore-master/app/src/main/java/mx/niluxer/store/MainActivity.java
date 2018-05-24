package mx.niluxer.store;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import mx.niluxer.store.adapters.CustomersAdapter;
import mx.niluxer.store.data.model.Category;
import mx.niluxer.store.data.model.Image;
import mx.niluxer.store.data.model.Customers;
import mx.niluxer.store.data.remote.ApiUtils;
import mx.niluxer.store.data.remote.WooCommerceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView rvCustomers;
    private WooCommerceService mService;
    private CustomersAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = ApiUtils.getWooCommerceService();
        rvCustomers = (RecyclerView) findViewById(R.id.rvCustomers);
        mAdapter = new CustomersAdapter(this, new ArrayList<Customers>(0), new CustomersAdapter.CustomersItemListener() {

            @Override
            public void onCustomersClick(long id) {
                Toast.makeText(MainActivity.this, "Customers id is " + id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCustomersLongClick(Customers Customers) {
                showContextualMenu(Customers);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvCustomers.setLayoutManager(layoutManager);
        rvCustomers.setAdapter(mAdapter);
        rvCustomers.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvCustomers.addItemDecoration(itemDecoration);

        loadCustomers();

    }

    public void loadCustomers() {
        mService.getCustomers().enqueue(new Callback<List<Customers>>() {
            @Override
            public void onResponse(Call<List<Customers>> call, Response<List<Customers>> response) {

                if(response.isSuccessful()) {
                    mAdapter.updateCustomers(response.body());
                    Log.d("MainActivity", "Customers loaded from API");
                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<List<Customers>> call, Throwable t) {
                //showErrorMessage();
                Log.d("MainActivity", "error loading from API");

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

    private void showContextualMenu(final Customers Customers)
    {
        final CharSequence[] items = { "Ver Informacion", "Ver ordenes" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Menu:");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                switch (item)
                {
                    case 0:
                        //Toast.makeText(MainActivity.this, Customers.getName(), Toast.LENGTH_SHORT).show();
                        NewEditCustomersDialog newEditCustomersDialog = new NewEditCustomersDialog(MainActivity.this);
                        newEditCustomersDialog.setEditMode(true);
                        newEditCustomersDialog.setCustomers(Customers);
                        newEditCustomersDialog.show();

                        break;
                    case 1:
                        //Toast.makeText(MainActivity.this, Customers.getId() + "", Toast.LENGTH_SHORT).show();
                       /* mService.deleteCustomers(Customers.getId()).enqueue(new Callback<Customers>() {
                            @Override
                            public void onResponse(Call<Customers> call, Response<Customers> response) {
                                if (response.isSuccessful())
                                {
                                    Toast.makeText(MainActivity.this, "Customers deleted successfully...", Toast.LENGTH_SHORT).show();
                                    loadCustomers();
                                } else {
                                    try {
                                        System.out.println(response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Customers> call, Throwable t) {
                                System.out.println("Error deleting Customers...");
                            }
                        });*/
                       Intent i=new Intent (MainActivity.this,MainActivity2.class);
                       int id=Customers.getId();
                       i.putExtra("id",id);

                       startActivity(i);

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

    public class NewEditCustomersDialog extends android.app.AlertDialog {

        private boolean editMode = false;
        Context context;
        private Customers customers;

        protected NewEditCustomersDialog(Context context) {
            super(context);
            this.context = context;
        }

        public void setEditMode(boolean editMode)
        {
            this.editMode = editMode;
        }

        public void setCustomers(Customers customers)
        {
            this.customers = customers;
        }


        @Override
        protected void onCreate(Bundle savedInstanceState) {




            String title = "Informacion de los Clientes";
           // if(editMode) title = "Edit Customers";
            setTitle(title);
            setMessage("Informacion de los CLientes");
            View view = LayoutInflater.from(context).inflate(R.layout.info_item, null);
            setView(view);
            final TextView tvCustomersName, tvCustomersEmail,tvCustomersAddress,tvCustomersCompras,tvCustomersFechaCreacion,tvCustomersRol,tvCustomersUserName,tvCustomersTotalGastado,tvCustomersDireccionFacturacion,tvCustomersDireccionEnvio;
            tvCustomersName  = (TextView) view.findViewById(R.id.tvCustomersName);
            tvCustomersEmail = (TextView) view.findViewById(R.id.tvCustomersEmail);
            tvCustomersAddress = (TextView) view.findViewById(R.id.tvCustomersAddress);
            tvCustomersCompras = (TextView) view.findViewById(R.id.tvCustomersCompras);
            tvCustomersFechaCreacion = (TextView) view.findViewById(R.id.tvCustomersFechaCreacion);
            tvCustomersRol = (TextView) view.findViewById(R.id.tvCustomersRol);
            tvCustomersUserName = (TextView) view.findViewById(R.id.tvCustomersUserName);
            tvCustomersTotalGastado = (TextView) view.findViewById(R.id.tvCustomersTotalGastado);
            tvCustomersDireccionFacturacion = (TextView) view.findViewById(R.id.tvCustomersDireccionFacturacion);
            tvCustomersDireccionEnvio = (TextView) view.findViewById(R.id.tvCustomersDireccionEnvio);

            tvCustomersName.setText(customers.getFirstName());
            tvCustomersEmail.setText(customers.getEmail());
            tvCustomersAddress.setText("Address");
            tvCustomersCompras.setText(customers.getOrdersCount().toString());
            tvCustomersFechaCreacion.setText(customers.getDateCreated());
            tvCustomersRol.setText(customers.getRole());
            tvCustomersUserName.setText(customers.getUsername());
            tvCustomersTotalGastado.setText(customers.getTotalSpent());
            tvCustomersDireccionFacturacion.setText(customers.getBilling().getAddress1());
            tvCustomersDireccionEnvio.setText(customers.getShipping().getAddress1());





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
