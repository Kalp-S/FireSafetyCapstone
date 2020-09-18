package com.example.lorawanfiresensor;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.util.Log;
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

//very first activity
public class MainActivity extends AppCompatActivity {

    private String msg;



    static String MQTTHOST= "tcp://hairdresser.cloudmqtt.com:15955";
    static String USERNAME= "pslgynex";
    static String PASSWORD= "MOCo261u5hxW";
    String topicStr="Sensor/Data";

    MqttAndroidClient client;

    TextView mtvTest;

    MqttConnectOptions options;

    ImageButton mibtnDisconnect;
    ImageButton mibtnConnect;



    public void openSafetyActivity() {

        ImageButton mibtnSafety = (ImageButton) findViewById(R.id.ibtnSafety);

        mibtnSafety.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is done when the user clicks the Node A button
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                String msgIntent;
                Intent SafetyActivityIntent = new Intent(MainActivity.this, SafetyActivity.class); //intent object opens the next activity

                startActivity(SafetyActivityIntent); //                                                                 and this

            }
        });
    }



    public void openNodeAActivity() {

        ImageButton mibtnNodeA = (ImageButton) findViewById(R.id.ibtnNodeA);

        mibtnNodeA.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is done when the user clicks the Node A button
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                String msgIntent;
                Intent NodeAIntent = new Intent(MainActivity.this, NodeAActivity.class); //intent object opens the next activity
                NodeAIntent.putExtra("extra",msg);

                startActivity(NodeAIntent); //                                                                 and this

            }
        });
    }
    public void openNodeBActivity() {

        ImageButton mibtnNodeB = (ImageButton) findViewById(R.id.ibtnNodeB);

        mibtnNodeB.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is done when the user clicks the Node A button
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                Intent NodeBIntent = new Intent(MainActivity.this, NodeBActivity.class); //intent object opens the next activity
                startActivity(NodeBIntent); //                                                                 and this

            }
        });
    }

    public void pub(View v) {
        String topic = "foo/bar";
        String payload = "the payload";
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void sub(){
        try{
            client.subscribe(topicStr,1);

        }
        catch (MqttException e){
            e.printStackTrace();
        }
    }

    public void conn(View v){
        mibtnDisconnect = (ImageButton) findViewById(R.id.ibtnDisconnect);
        mibtnConnect = (ImageButton) findViewById(R.id.ibtnConnect);

        mibtnDisconnect.setAlpha((float)1); //test 1 or 0
        mibtnConnect.setEnabled(false);
        mibtnDisconnect.setEnabled(true);
        mibtnDisconnect.clearColorFilter();
        mibtnConnect.setAlpha((float)0.5);


        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(MainActivity.this,"successfully connected", Toast.LENGTH_LONG).show();
                    sub(); //added after sucuess of connection to mqtt broker
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MainActivity.this,"connection failed", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconn(View v){

        mibtnConnect.setAlpha((float)1); //test 1 or 0
        mibtnDisconnect = (ImageButton) findViewById(R.id.ibtnDisconnect);
        mibtnConnect = (ImageButton) findViewById(R.id.ibtnConnect);
        mibtnConnect.setEnabled(true);
        mibtnDisconnect.setEnabled(false);
        mibtnConnect.clearColorFilter();
        mibtnDisconnect.setAlpha((float)0.5);


        try {
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(MainActivity.this,"disconnected", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MainActivity.this,"connection failed", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //first XML



        //mtvTest= (TextView)findViewById(R.id.tvTest);

        String clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(this.getApplicationContext(), MQTTHOST,
                        clientId);

        options = new MqttConnectOptions();

        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());


        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(MainActivity.this,"successfully connected", Toast.LENGTH_LONG).show();
                    sub(); //added after sucuess of connection to mqtt broker
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MainActivity.this,"connection failed", Toast.LENGTH_LONG).show();

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
                msg=new String(message.getPayload());
                mtvTest.setText(msg);
            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


        openNodeAActivity();
        openNodeBActivity();
        openSafetyActivity();


    }

}