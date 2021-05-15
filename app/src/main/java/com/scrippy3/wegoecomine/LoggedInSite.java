package com.scrippy3.wegoecomine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LoggedInSite extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationItemView btn_weekly, btn_mountly, btn_yestoday;
    private TextView text_top_score_value, text_top_rank, text_mid_energi_use, text_mid_energi_use_score, text_mid_kwh_pr_km, text_mid_kwh_pr_km_score, text_mid_trips, text_mid_persona, text_mid_score_value, text_mid_header;
    private ImageView persona_image, btn_info, btn_menu;
    private String[] dataArray;
    private ArrayList<Trip> lastWeekTrip, lastMounthTrip, tripsInTotal;
    private Button btn_new_trip;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private Trip tripToAdd;
    private Trip currentTrip;
    private int kmDrove, energiScore, kwhprkmScore;
    private double kmPrKwh, kwhUse;
    private String persona_text, kmPrKwh_text, energiUse_text;
    private DecimalFormat numberFormat;
    private LoadingDialog loadingDialog;

    private SharedPreferences sharedPref;
    SharedPreferences.Editor sharedPrefEditor;

    //ARRAY VALUES:
    //0: top score
    //1: top persona.
    //2: mid score.
    //3: mid persona.
    //4: mid kwh use.
    //5: mid energi score.
    //6: mid kwh pr km.
    //7:  kwh pr km score.
    //8: acceleration score.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in_site);
        getSupportActionBar().hide();
        dataArray = new String[10];

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        sharedPrefEditor = sharedPref.edit();

        //Assigning variables
        AssignVariabels();

        numberFormat = new DecimalFormat("0.0000");

        //MISSING A LOADING WHILE GETTING DATA!

        //MISSING GETTING THE DATA FUNCTION

        //MISSING REMOVING LOADING AFTER DATA HAVE GOTTEN

        //Setting up on click listeners
        btn_yestoday.setOnClickListener(this);
        btn_menu.setOnClickListener(this);
        btn_info.setOnClickListener(this);
        btn_mountly.setOnClickListener(this);
        btn_weekly.setOnClickListener(this);
        btn_new_trip.setOnClickListener(this);

        //Setting the startup to be the yestoday
//        ChangeDataArray(1);
//        PushTestData();
        GetDataFromDatabase();

        loadingDialog = new LoadingDialog(LoggedInSite.this);

        loadingDialog.startLoadingDialog();



    }



    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth == null)
            finish();
    }

    private void AssignVariabels() {

        //Buttons assigned
        btn_yestoday = findViewById(R.id.btn_yestoday);
        btn_weekly = findViewById(R.id.btn_weekly);
        btn_mountly = findViewById(R.id.btn_mounthly);
        btn_new_trip = findViewById(R.id.btn_new_trip);

        //Textview assigned
        text_top_rank = findViewById(R.id.top_text_rank);
        text_top_score_value = findViewById(R.id.top_text_score_value);
        text_mid_persona = findViewById(R.id.mid_text_persona_value);
        text_mid_energi_use_score = findViewById(R.id.mid_text_energiuse_value_score);
        text_mid_energi_use = findViewById(R.id.mid_text_energiuse_value);
        text_mid_kwh_pr_km = findViewById(R.id.mid_text_kmprkwh_value);
        text_mid_kwh_pr_km_score = findViewById(R.id.mid_text_kmprkwh_value_score);
        text_mid_score_value = findViewById(R.id.mid_text_score_value);
        text_mid_header = findViewById(R.id.mid_text_weekormounthortotal);
        text_mid_trips = findViewById(R.id.mid_text_trips_value);

        //Imageviews
        persona_image = findViewById(R.id.top_image);
        btn_info = findViewById(R.id.btn_info);
        btn_menu = findViewById(R.id.btn_menu);

        //Arraylist
        lastWeekTrip = new ArrayList<>();
        lastMounthTrip = new ArrayList<>();
        tripsInTotal = new ArrayList<>();

        //Google
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        System.out.println(mAuth.getUid());

    }


    @Override
    public void onClick(View v) {
        //In this function its posible to make new function that change image and text views to the correct state.
        switch (v.getId()){
            case R.id.btn_mounthly:
                System.out.println("btn mounthly");
                ChangeDataArray(3);
                break;
            case R.id.btn_weekly:
                System.out.println("btn weekly");
                ChangeDataArray(2);
                break;
            case R.id.btn_yestoday:
                System.out.println("btn yestoday");
                ChangeDataArray(1);
                break;
            case R.id.btn_info:
                System.out.println("btn info");
                new AlertDialog.Builder(LoggedInSite.this)
                        .setTitle("Info")
                        .setMessage("You score is calculated based on a combined score of energi use and Km each Kwh." +
                                " \n Energi use score weight is 30%, while Km each Kwh score is weight is 70%")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, null)
                        // A null listener allows the button to dismiss the dialog and take no further actioin
                        .show();
                break;
            case R.id.btn_menu:
                System.out.println("menu");

                Intent intent = new Intent(LoggedInSite.this, BigMenu.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case R.id.btn_new_trip:
                System.out.println("New trip");
                Intent intent2 = new Intent(LoggedInSite.this, BluetoothActivity2.class);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            default:
                System.out.println("Error");
        }
    }

    private void ChangeDataArray(int i) {
        //i = 1: yestoday
        //i = 2: weekly
        //i = 3: mounthly

        //ARRAY VALUES:
        //0: top score
        //1: top persona.
        //2: mid score.
        //3: mid persona.
        //4: mid kwh use.
        //5: mid energi score.
        //6: mid kwh pr km.
        //7:  kwh pr km score.
        //8: acceleration score.

//        private int kmDrove, kwhUse, energiScore, kmPrKwh, kwhprkmScore;
        //WEEKLY
        if (i == 1){
            kwhUse = 0;
            kmDrove = 0;
            kmPrKwh = 0;
            for (int u = 0; u < lastWeekTrip.size(); u++){
                kwhUse += lastWeekTrip.get(u).getKwhForTrip();
                kmDrove += lastWeekTrip.get(u).getKmDif();
            }

            System.out.println(kmDrove);
            System.out.println(kwhUse);
            kmPrKwh = (double) kmDrove/kwhUse;
            System.out.println(kmPrKwh);

            System.out.println(numberFormat.format(kwhUse));

            dataArray[2] = ScoreInAverage(ScoreFromKwhUse(kwhUse), ScoreFromKmPrKwh(kmPrKwh));
            dataArray[3] = PersonaFromScore(ScoreInAverage(ScoreFromKwhUse(kwhUse), ScoreFromKmPrKwh(kmPrKwh)));
            dataArray[4] = numberFormat.format(kwhUse);
            dataArray[5] = ScoreFromKwhUse(kwhUse);
            dataArray[6] = String.valueOf(numberFormat.format(kmPrKwh));
            dataArray[7] = ScoreFromKmPrKwh(kmPrKwh);
            dataArray[8] = "Weekly";
            dataArray[9] = String.valueOf(lastWeekTrip.size());


        }
        //MOUNTHLY
        else if (i == 2){

            kwhUse = 0;
            kmDrove = 0;
            for (int u = 0; u < lastMounthTrip.size(); u++){
                kwhUse += lastMounthTrip.get(u).getKwhForTrip();
                kmDrove += lastMounthTrip.get(u).getKmDif();
            }

            kmPrKwh = (double) kmDrove/kwhUse;

            System.out.println(numberFormat.format(kwhUse));
            dataArray[2] = ScoreInAverage(ScoreFromKwhUse(kwhUse), ScoreFromKmPrKwh(kmPrKwh));
            dataArray[3] = PersonaFromScore(ScoreInAverage(ScoreFromKwhUse(kwhUse), ScoreFromKmPrKwh(kmPrKwh)));
            dataArray[4] = String.valueOf(numberFormat.format(kwhUse));
            dataArray[5] = ScoreFromKwhUse(kwhUse);
            dataArray[6] = String.valueOf(numberFormat.format(kmPrKwh));
            dataArray[7] = ScoreFromKmPrKwh(kmPrKwh);
            dataArray[8] = "Mounthly";
            dataArray[9] = String.valueOf(lastMounthTrip.size());
        }
        //TOTAL
        else if (i == 3){
            kwhUse = 0;
            kmDrove = 0;
            for (int u = 0; u < tripsInTotal.size(); u++){
                kwhUse += tripsInTotal.get(u).getKwhForTrip();
                kmDrove += tripsInTotal.get(u).getKmDif();
            }

            kmPrKwh = (double) kmDrove/kwhUse;

            dataArray[2] = ScoreInAverage(ScoreFromKwhUse(kwhUse), ScoreFromKmPrKwh(kmPrKwh));
            dataArray[3] = PersonaFromScore(ScoreInAverage(ScoreFromKwhUse(kwhUse), ScoreFromKmPrKwh(kmPrKwh)));
            dataArray[4] = String.valueOf(numberFormat.format(kwhUse));
            dataArray[5] = ScoreFromKwhUse(kwhUse);
            dataArray[6] = String.valueOf(numberFormat.format(kmPrKwh));
            dataArray[7] = ScoreFromKmPrKwh(kmPrKwh);
            dataArray[8] = "Total";
            dataArray[9] = String.valueOf(tripsInTotal.size());
        }


        UpdateTextviewWithData();
    }

    private String PersonaFromScore(String scoreInAverage) {

        if (Double.parseDouble(scoreInAverage) > 4.4){
            persona_text = "Greta Thunberg";
        } else if (Double.parseDouble(scoreInAverage) > 3.6){
            persona_text = "Uffe Elbæk";
        } else if (Double.parseDouble(scoreInAverage) > 2.8){
            persona_text = "Lars Løkke";
        } else if (Double.parseDouble(scoreInAverage) > 1.9){
            persona_text = "Trump";
        } else if (Double.parseDouble(scoreInAverage) > 0.1){
            persona_text = "Xi Jinpooh";
        } else {
            persona_text = "No persona yet";
        }


        return persona_text;
    }

    private String ScoreInAverage(String kwhUserScore, String kmPrKwh) {
        if (kwhUserScore.contains("NaN") || kmPrKwh.contains("NaN"))
            return "NaN";
        return String.valueOf((Integer.parseInt(kwhUserScore)*0.3)+(Integer.parseInt(kmPrKwh)*0.7));
    }

    private String ScoreFromKmPrKwh(double kmPrKwh) {

            if (kmPrKwh > 6.5) {
                kmPrKwh_text = "5";
            } else if (kmPrKwh > 5.5) {
                kmPrKwh_text = "4";
            } else if (kmPrKwh > 4.5) {
                kmPrKwh_text = "3";
            } else if (kmPrKwh > 3.5) {
                kmPrKwh_text = "2";
            } else if (kmPrKwh > 0.001){
                kmPrKwh_text = "1";
            } else {
                kmPrKwh_text = "5";
            }
        return kmPrKwh_text;
    }

    private String ScoreFromKwhUse(double kwhUse) {
        if (kwhUse == 0) {
            energiUse_text = "5";
        }
        else {
            if (kwhUse < 150) {
                energiUse_text = "5";
            } else if (kwhUse < 215) {
                energiUse_text = "4";
            } else if (kwhUse < 275) {
                energiUse_text = "3";
            } else if (kwhUse < 312) {
                energiUse_text = "2";
            } else {
                energiUse_text = "1";
            }
        }

        return energiUse_text;
    }

    private void UpdateTextviewWithData() {

        text_top_score_value.setText(dataArray[0]);
        text_top_rank.setText(dataArray[1]);
        text_mid_score_value.setText(dataArray[2]);
        text_mid_persona.setText(dataArray[3]);
        text_mid_energi_use.setText(dataArray[4]);
        text_mid_energi_use_score.setText(dataArray[5]);
        text_mid_kwh_pr_km.setText(dataArray[6]);
        text_mid_kwh_pr_km_score.setText(dataArray[7]);
        text_mid_header.setText(dataArray[8]);
        text_mid_trips.setText(dataArray[9]);
    }

    private void GetDataFromDatabase() {
        myRef = database.getReference().child("Users").child(mAuth.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                lastWeekTrip.clear();
                lastMounthTrip.clear();
                tripsInTotal.clear();

                for (DataSnapshot snap : snapshot.getChildren()){
                    //Checking if the snap is trips
                    if (snap.getKey().contains("Ture")){
                        //In trips we enter each trips uniq ID and create the trip from that.
                        for (DataSnapshot snap2 : snap.getChildren()){
                            tripToAdd = new Trip();
                            //Getting all the data from each trip
                            for (DataSnapshot snapTripInfo : snap2.getChildren()){
                                if (snapTripInfo.getKey().contains("startTime"))
                                    tripToAdd.setStartTime(Integer.parseInt(snapTripInfo.getValue().toString()));
                                else if (snapTripInfo.getKey().contains("endTime"))
                                    tripToAdd.setEndTime(Integer.parseInt(snapTripInfo.getValue().toString()));
                                else if (snapTripInfo.getKey().contains("startSOC"))
                                    tripToAdd.setStartSOC(Integer.parseInt(snapTripInfo.getValue().toString()));
                                else if (snapTripInfo.getKey().contains("endSOC"))
                                    tripToAdd.setEndSOC(Integer.parseInt(snapTripInfo.getValue().toString()));
                                else if (snapTripInfo.getKey().contains("startODO"))
                                    tripToAdd.setStartODO(Integer.parseInt(snapTripInfo.getValue().toString()));
                                else if (snapTripInfo.getKey().contains("endODO"))
                                    tripToAdd.setEndODO(Integer.parseInt(snapTripInfo.getValue().toString()));
                                else if (snapTripInfo.getKey().contains("Score"))
                                    tripToAdd.setScore(Double.parseDouble(snapTripInfo.getValue().toString()));
                                else if (snapTripInfo.getKey().contains("Kmperkw"))
                                    tripToAdd.setKmperkw(Double.parseDouble(snapTripInfo.getValue().toString()));
                                else if (snapTripInfo.getKey().contains("kwhForTrip"))
                                    tripToAdd.setKwhForTrip(Double.parseDouble(snapTripInfo.getValue().toString()));
                                else if (snapTripInfo.getKey().contains("kmDif"))
                                    tripToAdd.setKmDif(Integer.parseInt(snapTripInfo.getValue().toString()));
                            }
                            //Weekly
                            if (((System.currentTimeMillis()/1000l) - (60*60*24*7)) < tripToAdd.getStartTime()){
                                lastWeekTrip.add(tripToAdd);
                            }
                            //Mounthly
                            if (((System.currentTimeMillis()/1000l) - (60*60*24*30)) < tripToAdd.getStartTime()){
                                lastMounthTrip.add(tripToAdd);
                            }
                            //All time
                            tripsInTotal.add(tripToAdd);
                        }


//                        TESTING STUFF
                        System.out.println("Weekly: " + lastWeekTrip.size());
                        System.out.println("Mouthly: " + lastMounthTrip.size());
                        System.out.println("Total: " + tripsInTotal.size());

                        System.out.println("Weekly accept time: " + ((System.currentTimeMillis()/1000l) - (60*60*24*7)));
                        System.out.println("Mounthly accept time: " + ((System.currentTimeMillis()/1000l) - (60*60*24*30)));

                        ShowStaticInfo();
                        ChangeDataArray(1);


                    }
                    //Checking if its personal information
                    else if (snap.getKey().contains("PersonligData")){

                    }

                }

                loadingDialog.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ShowStaticInfo() {
        kwhUse = 0;
        kmDrove = 0;
        for (int u = 0; u < tripsInTotal.size(); u++){
            kwhUse += tripsInTotal.get(u).getKwhForTrip();
            kmDrove += tripsInTotal.get(u).getKmDif();
        }

        kmPrKwh = (double) kmDrove/kwhUse;


        dataArray[1] = ScoreInAverage(ScoreFromKwhUse(kwhUse), ScoreFromKmPrKwh(kmPrKwh));
        dataArray[0] = PersonaFromScore(ScoreInAverage(ScoreFromKwhUse(kwhUse), ScoreFromKmPrKwh(kmPrKwh)));
        Drawable myDrawable;
        if (Double.parseDouble(dataArray[1]) > 4.4){
            myDrawable = getResources().getDrawable(R.drawable.rank1);
        } else if (Double.parseDouble(dataArray[1]) < 3.6){
            myDrawable = getResources().getDrawable(R.drawable.rank2);
        } else if (Integer.parseInt(dataArray[1]) < 2.8){
            myDrawable = getResources().getDrawable(R.drawable.rank3);
        } else if (Integer.parseInt(dataArray[1]) < 1.9){
            myDrawable = getResources().getDrawable(R.drawable.rank4);
        } else {
            myDrawable = getResources().getDrawable(R.drawable.rank5);
        }

        persona_image.setImageDrawable(myDrawable);


    }




}