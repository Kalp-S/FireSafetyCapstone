package com.example.lorawanfiresensor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class SafetyActivity extends Activity {

    // message comes in the form {"temperature":XX,"humidity":XX}
    //with algo future msgs will be {"temperature":XX,"humidity":XX, "direction":XX}

    static String MQTTHOST= "tcp://hairdresser.cloudmqtt.com:15955";
    static String USERNAME= "pslgynex";
    static String PASSWORD= "MOCo261u5hxW";
    String topicStr="Algorithm/Data"; //change topic to Algorithm/Data

    MqttAndroidClient client;

    TextView mtvDirection;
    ImageButton mibtnPubLocation;
    ImageButton mibtnPubSafety;

    //ImageView ivBlueprint= (ImageView)findViewById(R.id.ivBlueprint);
    ImageView i00 ;
    ImageView i01 ;
    ImageView i02 ;
    ImageView i03 ;
    ImageView i04 ;
    ImageView i10 ;
    ImageView i14 ;
    ImageView i20 ;
    ImageView i24 ;
    ImageView i30 ;
    ImageView i31 ;
    ImageView i32 ;
    ImageView i33 ;
    ImageView i34 ;
    ImageView i40 ;
    ImageView i44 ;
    ImageView i50 ;
    ImageView i51 ;
    ImageView i52 ;
    ImageView i53 ;
    ImageView i54 ;
    ImageView i60 ;
    ImageView i64 ;
    ImageView i70 ;
    ImageView i71 ;
    ImageView i72 ;
    ImageView i73 ;
    ImageView i74 ;
    ImageView f00;
    ImageView f02;
    ImageView f04;
    ImageView f30;
    ImageView f32;
    ImageView f34;
    ImageView f50;
    ImageView f54;
    ImageView f70;
    ImageView f71;
    ImageView f74;

    MqttConnectOptions options;

    IMqttToken token ;


    public void pub(String payload) {
        String topic = "User/Status";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety); //first XML


   //    mibtnPubLocation =(ImageButton)findViewById(R.id.ibtnPubLocation);
   //     mibtnPubSafety =(ImageButton)findViewById(R.id.ibtnPubSafety);
//        mtvDirection= (TextView)findViewById(R.id.tvDirection);

        //ivBlueprint.setVisibility(View.VISIBLE);

        i00= (ImageView)findViewById(R.id.i00);
        i01= (ImageView)findViewById(R.id.i01);
        i02= (ImageView)findViewById(R.id.i02);
        i03= (ImageView)findViewById(R.id.i03);
        i04= (ImageView)findViewById(R.id.i04);
        i10= (ImageView)findViewById(R.id.i10);
        i14= (ImageView)findViewById(R.id.i14);
        i20= (ImageView)findViewById(R.id.i20);
        i24= (ImageView)findViewById(R.id.i24);
        i30= (ImageView)findViewById(R.id.i30);
        i31= (ImageView)findViewById(R.id.i31);
        i32= (ImageView)findViewById(R.id.i32);
        i33= (ImageView)findViewById(R.id.i33);
        i34= (ImageView)findViewById(R.id.i34);
        i40= (ImageView)findViewById(R.id.i40);
        i44= (ImageView)findViewById(R.id.i44);
        i50= (ImageView)findViewById(R.id.i50);
        i51= (ImageView)findViewById(R.id.i51);
        i52= (ImageView)findViewById(R.id.i52);
        i53= (ImageView)findViewById(R.id.i53);
        i54= (ImageView)findViewById(R.id.i54);
        i60= (ImageView)findViewById(R.id.i60);
        i64= (ImageView)findViewById(R.id.i64);
        i70= (ImageView)findViewById(R.id.i70);
        i71= (ImageView)findViewById(R.id.i71);
        i72= (ImageView)findViewById(R.id.i72);
        i73= (ImageView)findViewById(R.id.i73);
        i74= (ImageView)findViewById(R.id.i74);
        f00= (ImageView)findViewById(R.id.f00);
        f02= (ImageView)findViewById(R.id.f02);
        f04= (ImageView)findViewById(R.id.f04);
        f30= (ImageView)findViewById(R.id.f30);
        f32= (ImageView)findViewById(R.id.f32);
        f34= (ImageView)findViewById(R.id.f34);
        f50= (ImageView)findViewById(R.id.f50);
        f54= (ImageView)findViewById(R.id.f54);
        f70= (ImageView)findViewById(R.id.f70);
        f71= (ImageView)findViewById(R.id.f71);
        f74= (ImageView)findViewById(R.id.f74);

        i00.setVisibility(View.INVISIBLE);
        i01.setVisibility(View.INVISIBLE);
        i02.setVisibility(View.INVISIBLE);
        i03.setVisibility(View.INVISIBLE);
        i04.setVisibility(View.INVISIBLE);
        i10.setVisibility(View.INVISIBLE);
        i14.setVisibility(View.INVISIBLE);
        i20.setVisibility(View.INVISIBLE);
        i24.setVisibility(View.INVISIBLE);
        i30.setVisibility(View.INVISIBLE);
        i31.setVisibility(View.INVISIBLE);
        i32.setVisibility(View.INVISIBLE);
        i33.setVisibility(View.INVISIBLE);
        i34.setVisibility(View.INVISIBLE);
        i40.setVisibility(View.INVISIBLE);
        i44.setVisibility(View.INVISIBLE);
        i50.setVisibility(View.INVISIBLE);
        i51.setVisibility(View.INVISIBLE);
        i52.setVisibility(View.INVISIBLE);
        i53.setVisibility(View.INVISIBLE);
        i54.setVisibility(View.INVISIBLE);
        i60.setVisibility(View.INVISIBLE);
        i64.setVisibility(View.INVISIBLE);
        i70.setVisibility(View.INVISIBLE);
        i71.setVisibility(View.INVISIBLE);
        i72.setVisibility(View.INVISIBLE);
        i73.setVisibility(View.INVISIBLE);
        i74.setVisibility(View.INVISIBLE);
        f00.setVisibility(View.INVISIBLE);
        f02.setVisibility(View.INVISIBLE);
        f04.setVisibility(View.INVISIBLE);
        f30.setVisibility(View.INVISIBLE);
        f32.setVisibility(View.INVISIBLE);
        f34.setVisibility(View.INVISIBLE);
        f50.setVisibility(View.INVISIBLE);
        f54.setVisibility(View.INVISIBLE);
        f70.setVisibility(View.INVISIBLE);
        f71.setVisibility(View.INVISIBLE);
        f74.setVisibility(View.INVISIBLE);

        String clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(this.getApplicationContext(), MQTTHOST,
                        clientId);

        options = new MqttConnectOptions();

        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        deployMqtt();

 //       mibtnPubLocation.setOnClickListener(new View.OnClickListener() {
    //        /**
    //         * This method is done when the user clicks the Node A button
    //         *
    //         * @param v
     //        */
   //         @Override
   //         public void onClick(View v) {
    //            pub("helpMe,location");
    //            Toast.makeText(SafetyActivity.this,"Help is on the way! Your location has been sent", Toast.LENGTH_LONG).show();

    //        }
     //   });


    //    mibtnPubSafety.setOnClickListener(new View.OnClickListener() {
    //        /**
    //         * This method is done when the user clicks the Node A button
    //         *
     //        * @param v
     //        */
     //       @Override
     //       public void onClick(View v) {
      //          pub("imSafe,name");
       //         Toast.makeText(SafetyActivity.this,"You have been marked safe", Toast.LENGTH_LONG).show();

      //      }
      //  });



    }

    public void deployMqtt(){

        try {
            token = client.connect(options);

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(SafetyActivity.this,"successfully connected", Toast.LENGTH_LONG).show();

                    try{
                        client.subscribe(topicStr,1); //subscribing
                    }
                    catch (MqttException e){
                        e.printStackTrace();
                    }                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(SafetyActivity.this,"connection failed", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String msg=new String(message.getPayload());
                msg = msg.replaceAll("[^a-zA-Z0-9 ]", "");
                String [] data=  msg.split(" ");
                //String []data= new String[] {"A","B","C","D","E"}; //for testing

                i00.setVisibility(View.INVISIBLE);
                i01.setVisibility(View.INVISIBLE);
                i02.setVisibility(View.INVISIBLE);
                i03.setVisibility(View.INVISIBLE);
                i04.setVisibility(View.INVISIBLE);
                i10.setVisibility(View.INVISIBLE);
                i14.setVisibility(View.INVISIBLE);
                i20.setVisibility(View.INVISIBLE);
                i24.setVisibility(View.INVISIBLE);
                i30.setVisibility(View.INVISIBLE);
                i31.setVisibility(View.INVISIBLE);
                i32.setVisibility(View.INVISIBLE);
                i33.setVisibility(View.INVISIBLE);
                i34.setVisibility(View.INVISIBLE);
                i40.setVisibility(View.INVISIBLE);
                i44.setVisibility(View.INVISIBLE);
                i50.setVisibility(View.INVISIBLE);
                i51.setVisibility(View.INVISIBLE);
                i52.setVisibility(View.INVISIBLE);
                i53.setVisibility(View.INVISIBLE);
                i54.setVisibility(View.INVISIBLE);
                i60.setVisibility(View.INVISIBLE);
                i64.setVisibility(View.INVISIBLE);
                i70.setVisibility(View.INVISIBLE);
                i71.setVisibility(View.INVISIBLE);
                i72.setVisibility(View.INVISIBLE);
                i73.setVisibility(View.INVISIBLE);
                i74.setVisibility(View.INVISIBLE);
                f00.setVisibility(View.INVISIBLE);
                f02.setVisibility(View.INVISIBLE);
                f04.setVisibility(View.INVISIBLE);
                f30.setVisibility(View.INVISIBLE);
                f32.setVisibility(View.INVISIBLE);
                f34.setVisibility(View.INVISIBLE);
                f50.setVisibility(View.INVISIBLE);
                f54.setVisibility(View.INVISIBLE);
                f70.setVisibility(View.INVISIBLE);
                f71.setVisibility(View.INVISIBLE);
                f74.setVisibility(View.INVISIBLE);

                if(data[0].equals("draginotest")) {
                    f00.setVisibility(View.VISIBLE);
                }

                if(data[0].equals("dragino2")){
                    f02.setVisibility(View.VISIBLE);
                }

                if(data[0].equals("dragino3")){
                    f04.setVisibility(View.VISIBLE);
                }

                if(data[0].equals("dragino4")){
                    f30.setVisibility(View.VISIBLE);
                }

                for(int i=1;i<data.length;i+=2){
                    if(data[i].equals('0') && data[i+1].equals('0')){
                        i00.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('0') && data[i+1].equals('1')){
                        i01.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('0') && data[i+1].equals('2')){
                        i02.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('0') && data[i+1].equals('3')){
                        i03.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('0') && data[i+1].equals('4')){
                        i04.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('1') && data[i+1].equals('0')){
                        i10.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('1') && data[i+1].equals('4')){
                        i14.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('2') && data[i+1].equals('0')){
                        i20.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('2') && data[i+1].equals('4')){
                        i24.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('3') && data[i+1].equals('0')){
                        i30.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('3') && data[i+1].equals('1')){
                        i31.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('3') && data[i+1].equals('2')){
                        i32.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('3') && data[i+1].equals('3')){
                        i33.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('3') && data[i+1].equals('4')){
                        i34.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('4') && data[i+1].equals('0')){
                        i40.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('4') && data[i+1].equals('4')){
                        i44.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('5') && data[i+1].equals('0')){
                        i50.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('5') && data[i+1].equals('1')){
                        i51.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('5') && data[i+1].equals('2')){
                        i52.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('5') && data[i+1].equals('3')){
                        i53.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('5') && data[i+1].equals('4')){
                        i54.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('6') && data[i+1].equals('0')){
                        i60.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('6') && data[i+1].equals('4')){
                        i64.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('7') && data[i+1].equals('0')){
                        i70.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('7') && data[i+1].equals('1')){
                        i71.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('7') && data[i+1].equals('2')){
                        i72.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('7') && data[i+1].equals('3')){
                        i73.setVisibility(View.VISIBLE);
                    }

                    if(data[i].equals('7') && data[i+1].equals('4')){
                        i74.setVisibility(View.VISIBLE);
                    }


                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


    }
}

//change iv visibility:
//ivBlueprint.setVisibility(View.VISIBLE);
