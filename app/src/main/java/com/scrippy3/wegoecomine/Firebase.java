package com.scrippy3.wegoecomine;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Firebase {

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    //private Frame frame;

    public void download(final String odo){
        ValueEventListener odolistener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                frame = snapshot.child("message").getValue(Frame.class);
//                System.out.println(frame.getFrame());
                //odo = frame.getFrame();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        myRef.addListenerForSingleValueEvent(odolistener);
    }


    public void upload(Trip trip){
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference().child("Users").child(mAuth.getUid()).child("Ture");
            myRef.push().setValue(trip);
    }
}
