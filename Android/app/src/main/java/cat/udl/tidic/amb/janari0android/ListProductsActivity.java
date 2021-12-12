package cat.udl.tidic.amb.janari0android;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import cat.udl.tidic.amb.janari0android.adapters.ListProductAdapter;
import cat.udl.tidic.amb.janari0android.adapters.SearchStockAdapter;

public class ListProductsActivity extends AppCompatActivity {

    private static final String TAG = "bakedbeans";
    ArrayList<Product> products = new ArrayList<>() ;
    RecyclerView recyclerView;
    ListProductAdapter listProductAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    //
    ImageButton goBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);
        Intent myIntent = getIntent(); // gets the previously created intent
        int page =0;
        page = myIntent.getIntExtra("Page", 0);
        getData(page);
        goBack = findViewById(R.id.goBackButton);
        goBack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListProductsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void buildRecyclerView() {
        recyclerView = findViewById(R.id.list_productRecycler);
        listProductAdapter = new ListProductAdapter(products,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listProductAdapter);
    }
    private void getData(int page) {
        products = new ArrayList<>();
        if(page!=4) {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 7);
            db.collection("users").document(user.getUid()).collection("products")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    //products.add(document.toObject(Product.class));

                                    Product p = document.toObject(Product.class);
                                    Calendar cprod = Calendar.getInstance();
                                    cprod.setTime(p.expirationDate);
                                    if (page == 1) {//to expire
                                        if (cprod.compareTo(c) <= 0 && cprod.compareTo(Calendar.getInstance()) > 0) {
                                            products.add(p);
                                        }
                                    } else if (page == 2) {//all
                                        if (cprod.compareTo(Calendar.getInstance()) > 0) {
                                            products.add(p);
                                        }
                                    } else if (page == 3) {//expired
                                        if (cprod.compareTo(Calendar.getInstance()) <= 0) {
                                            products.add(p);
                                        }
                                    }
                                }
                                buildRecyclerView();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }else{

            db.collection("users").document(user.getUid()).collection("productsSale")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    ProductSale p = document.toObject(ProductSale.class);
                                    products.add(p.product);
                                }
                                buildRecyclerView();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }
    }
}
