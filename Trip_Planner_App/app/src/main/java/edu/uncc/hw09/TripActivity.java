package edu.uncc.hw09;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import edu.uncc.DataObj.Trip;
import edu.uncc.DataObj.UserProfile;

public class TripActivity extends AppCompatActivity {
    Trip actTrip;
    DatabaseReference mDb;
    String friends = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        actTrip = (Trip)getIntent().getSerializableExtra("trip");

        mDb = FirebaseDatabase.getInstance().getReference();
        mDb.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] fnId = actTrip.getPeople().split(",");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()  ) {
                    UserProfile u = snapshot.getValue(UserProfile.class);
                    if(Arrays.asList(fnId).contains(u.getUser_id())){
                        friends += u.getFirstName()+" "+u.getLastName()+", ";
                    }
                }
                setTripData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setTripData(){
        ((TextView)findViewById(R.id.trip_v_title)).setText(actTrip.getTitle());
        ((TextView)findViewById(R.id.trip_v_loc)).setText(actTrip.getLocation());
        ((TextView)findViewById(R.id.trip_inv_ppl)).setText(friends);
        Picasso.with(this).load(actTrip.getPhotoURL()).into((ImageView)findViewById(R.id.trip_v_pic));
    }

    public void gotoChat(View v){
        Intent i2 = new Intent(TripActivity.this,ChatActivity.class);
        i2.putExtra("trip",actTrip);
        startActivity(i2);
    }
}
