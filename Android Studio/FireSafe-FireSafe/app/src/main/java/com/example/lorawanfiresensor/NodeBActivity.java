package com.example.lorawanfiresensor;

import android.app.Activity;
import android.os.Bundle;
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

public class NodeBActivity extends Activity {

    // message comes in the form {"temperature":XX,"humidity":XX}
    //with algo future msgs will be {"temperature":XX,"humidity":XX, "direction":XX}


    static String MQTTHOST= "tcp://hairdresser.cloudmqtt.com:15955";
    static String USERNAME= "pslgynex";
    static String PASSWORD= "MOCo261u5hxW";
    String topicStr="Sensor/Data";

    MqttAndroidClient client;

    TextView mtvTemperatureB;
    TextView mtvHumidityB;

    ImageButton mibtnTemperatureLogB;
    ImageButton mibtnHumidityLogB;

    ImageView mivTemperature;
    ImageView mivHumidity;


    MqttConnectOptions options;

    IMqttToken token ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_b); //first XML


        mibtnTemperatureLogB =(ImageButton)findViewById(R.id.ibtnTemperatureLogB);
        mibtnHumidityLogB =(ImageButton)findViewById(R.id.ibtnTemperatureLogB);

        mivTemperature =(ImageView)findViewById(R.id.ivTemperature);
        mivHumidity =(ImageView)findViewById(R.id.ivHumidity);

        mtvTemperatureB= (TextView)findViewById(R.id.tvTemperatureB);
        mtvHumidityB=(TextView)findViewById(R.id.tvHumidityB);

        String clientId = MqttClient.generateClientId();
        client =
                new MqttAndroidClient(this.getApplicationContext(), MQTTHOST,
                        clientId);

        options = new MqttConnectOptions();

        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        deployMqtt();


    }

    public void deployMqtt(){

        try {
            token = client.connect(options);

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(NodeBActivity.this,"successfully connected", Toast.LENGTH_LONG).show();

                    try{
                        client.subscribe(topicStr,1); //subscribing
                    }
                    catch (MqttException e){
                        e.printStackTrace();
                    }                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(NodeBActivity.this,"connection failed", Toast.LENGTH_LONG).show();

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

                // message comes in the form {"temperature":XX,"humidity":XX}
                //with algo future msgs will be {"temperature":XX,"humidity":XX, "direction":XX}
                String msg=new String(message.getPayload());
                String [] data=  msg.split(",");
                String []temperatureData=data[0].split(":");
                String temperature=temperatureData[1];

                mtvTemperatureB.setText(temperature);

                String []humidityData=data[1].split(":");
                String humidity=humidityData[1];
                humidity = humidity.substring(0, humidity.length() - 1); //delete this when directions is implemented
                mtvHumidityB.setText(humidity);


                /*
                direction equiv:
                String []directionData=data[2].split(":");
                String direction=directionData[1];
                direction = direction.substring(0, direction.length() - 1);
                mtvTemperature.setText(direction);
                 */
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


    }
}
