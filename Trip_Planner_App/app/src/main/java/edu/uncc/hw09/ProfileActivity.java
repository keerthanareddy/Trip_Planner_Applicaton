package edu.uncc.hw09;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import edu.uncc.DataObj.UserProfile;

public class ProfileActivity extends AppCompatActivity {
    UserProfile up;
    UserProfile logUp;
    DatabaseReference userRef;
    SharedPreferences shpr;
    String log_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Your Profile");
        shpr = getSharedPreferences("login",MODE_PRIVATE);
        log_email = shpr.getString("user_email","");

        final String email = getIntent().getStringExtra("email");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserProfile u = snapshot.getValue(UserProfile.class);
                    String uEmail = u.getEmail();
                    if(email.equals(uEmail)){
                        up = u;
                    }
                    if(log_email.equals(uEmail)){
                        logUp = u;
                    }
                }
                setData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setData(){
        Picasso.with(this).load(up.getDisplayPic()).into((ImageView) findViewById(R.id.profile_pic));
        ((TextView)findViewById(R.id.profile_name)).setText(up.getFirstName()+" "+up.getLastName());
        ((TextView)findViewById(R.id.profile_email)).setText(up.getEmail());
        if(up.getGender()!=null){
            ((TextView)findViewById(R.id.profile_gender)).setText(up.getGender());
        }else{
            ((TextView)findViewById(R.id.profile_gender)).setText("Not set");
        }

        if(up.getEmail().equals(log_email)){
            ((Button)findViewById(R.id.add_friend)).setVisibility(View.INVISIBLE);
            ((Button)findViewById(R.id.edit_profile)).setVisibility(View.VISIBLE);
        }else{
            ((Button)findViewById(R.id.add_friend)).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.edit_profile)).setVisibility(View.INVISIBLE);
            String frnds = logUp.getFriends();
            if(frnds!=null){
                if(frnds.contains(up.getUser_id())){
                    ((Button)findViewById(R.id.add_friend)).setText("Friends");
                    ((Button)findViewById(R.id.add_friend)).setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
                    ((Button)findViewById(R.id.add_friend)).setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_focused));
                    ((Button)findViewById(R.id.add_friend)).setClickable(false);
                }
            }

        }


    }

    public void addFriend(View v){
        logUp.addFriend(up.getUser_id());
        up.addFriend(logUp.getUser_id());
        userRef.child(logUp.getUser_id()).setValue(logUp);
        userRef.child(up.getUser_id()).setValue(up);
        Log.d("demo","added successfully!");
        Toast.makeText(this, "Added Successfully!", Toast.LENGTH_SHORT).show();
        ((Button)v).setText("Friends");
        ((Button)v).setBackgroundColor(getResources().getColor(R.color.cardview_shadow_start_color));
        ((Button)v).setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_focused));
        ((Button)v).setClickable(false);
    }

}
