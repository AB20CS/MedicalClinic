package com.example.medicalclinic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.Date;

public class PatientMainActivity extends AppCompatActivity {

    public static final String USERNAME_INTENT = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        Intent intent = getIntent();
        String patient_username = intent.getStringExtra(PatientLoginActivity.USERNAME_INTENT);

        TextView patient_display = findViewById(R.id.textView6);
        patient_display.setText(patient_username); //Change this so it's patient_username as the argument

        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference("patients/" + patient_username + "/upcoming_appointments"); //Replace with patient_username
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Patient patient_update = new Patient();
                try {
                    patient_update.update_appointments(patient_username); //Replace with patient_username
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                TextView appointment_display = findViewById(R.id.textView8);
                String appointment_list = "";
                for (DataSnapshot child:dataSnapshot.getChildren()) {
                    AppointmentSlot post = child.getValue(AppointmentSlot.class);

                    appointment_list += "Date: " + post.getDate() + " Doctor: " + post.doctor.getUsername() + "\n";
                }

                appointment_display.setText(appointment_list);
                appointment_display.setMovementMethod(new ScrollingMovementMethod());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("warning", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mPostReference.addValueEventListener(postListener);

    }

    public void patientBookOnClick(View view) {

        Intent intent = getIntent();
        String patient_username = intent.getStringExtra(PatientLoginActivity.USERNAME_INTENT);

        Intent sendPatient = new Intent(this, MainActivity.class); //Replace with actual class activity name
        sendPatient.putExtra(USERNAME_INTENT, patient_username); //Replace with patient_username;
        startActivity(sendPatient);
    }

    public void patientLogOutOnClick(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}