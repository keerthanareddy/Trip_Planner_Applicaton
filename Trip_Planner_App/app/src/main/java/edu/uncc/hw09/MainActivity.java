package edu.uncc.hw09;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.uncc.DataObj.Trip;
import edu.uncc.DataObj.UserProfile;

public class MainActivity extends AppCompatActivity {

    static String email;

    SharedPreferences shpr;
    DatabaseReference mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = getIntent().getStringExtra("email");
        shpr = getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = shpr.edit();
        editor.putString("user_email",email);
        editor.commit();
        mDb = FirebaseDatabase.getInstance().getReference();

        mDb.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<UserProfile> people = new ArrayList<UserProfile>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()  ) {
                    UserProfile u = snapshot.getValue(UserProfile.class);
                    if(!email.equals(u.getEmail())){
                        people.add(u);
                    }
                }
                setPeople(people);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDb.child("trips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Trip> trips = new ArrayList<Trip>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()  ) {
                    Trip u = snapshot.getValue(Trip.class);
                    trips.add(u);
                }
                setTrips(trips);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void gotoTrip(Trip t){
        Intent i2 = new Intent(MainActivity.this,TripActivity.class);
        i2.putExtra("trip",t);
        startActivity(i2);
    }

    public void setTrips(ArrayList<Trip> trips){
        RecyclerView mRecView = (RecyclerView)findViewById(R.id.trip_recycle);
        mRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        mRecView.setAdapter(new TripAdapter(trips,this));
    }

    public void setPeople(ArrayList<UserProfile> people){
        RecyclerView mRecView = (RecyclerView)findViewById(R.id.friend_recycle);
        mRecView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
        mRecView.setAdapter(new FriendsAdapter(people,this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.user_prof){
            goToProfile(email);
        }
        return true;
    }

    public void goToProfile(String emailId){
        Intent i2 = new Intent(MainActivity.this,ProfileActivity.class);
        i2.putExtra("email",emailId);
        startActivity(i2);
    }

    public void createTrip(View v){
        Intent intent = new Intent(MainActivity.this, CreateTripActivity.class);
        startActivity(intent);
    }

}
