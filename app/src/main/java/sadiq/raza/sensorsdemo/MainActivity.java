package sadiq.raza.sensorsdemo;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    SensorManager sensorManager;
    TextView tv;
    Button button;
    StringBuilder sb;
    Sensor lightSensor,proxSensor,temSensor,accel,magSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager=(SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proxSensor=sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        temSensor=sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        accel=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        tv=findViewById(R.id.textView);
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAvailableSensors();
                tv.setText(sb.toString());
            }
        });

    }
   public void checkAvailableSensors()
   {
       List<Sensor> sensorList= sensorManager.getSensorList(Sensor.TYPE_ALL);
        sb = new StringBuilder("");
       for(Sensor sensor: sensorList)
       {
           String s=sensor.getName()+" Vendor :"+sensor.getVendor()+" Version :"+sensor.getVersion();
           sb.append(s+"\n");
       }
   }

    @Override
    protected void onResume() {
        super.onResume();
        if(lightSensor!=null)
            sensorManager.registerListener(this,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        if(proxSensor!=null)
            sensorManager.registerListener(this,proxSensor,SensorManager.SENSOR_DELAY_NORMAL);
        if(temSensor!=null)
            sensorManager.registerListener(this,temSensor,SensorManager.SENSOR_DELAY_NORMAL);
        if(accel!=null)
            sensorManager.registerListener(this,accel,SensorManager.SENSOR_DELAY_NORMAL);
        if(magSensor!=null)
            sensorManager.registerListener(this,magSensor,SensorManager.SENSOR_DELAY_NORMAL);
        else
            Log.e("Unavailable ","Sensor not available");
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this); //unregister all the sensors
    }
    float[] value2=new float[3];
    float[] magArr=new float[3];

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensoType=sensorEvent.sensor.getType();
        switch (sensoType)
        {
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                break;
            case Sensor.TYPE_LIGHT:
                float[] value=sensorEvent.values;
                Toast.makeText(this, "Light  "+value[0], Toast.LENGTH_SHORT).show();
                break;
            case Sensor.TYPE_PROXIMITY:
                float[] value1=sensorEvent.values;
                Toast.makeText(this, "Distance is "+value1[0], Toast.LENGTH_SHORT).show();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                 value2=sensorEvent.values.clone();
                float x=value2[0];
                float y=value2[1];
                float z=value2[2];
                Log.e("Accelerometer  ",x+" "+y+" "+z);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                 magArr=new float[3];
                magArr=sensorEvent.values.clone();
                float i=magArr[0];
                float j=magArr[1];
                float k=magArr[2];
                Log.e("Magnetic field  ",i+" "+j+" "+k);
                break;
        }
        float[] rotationMatrix=new float[9];
        boolean rotMatOk=SensorManager.getRotationMatrix(rotationMatrix,null,value2,magArr);
        if(rotMatOk)
        {
            float orientation[] =new float[3];
            SensorManager.getOrientation(rotationMatrix,orientation);
            float azimuth=orientation[0];
            float pitch=orientation[1];
            float roll=orientation[2];
            Log.e("Rotation Matrix :"," Azimuth "+azimuth*57.2958+" Pitch "+pitch*57.2958+" Roll "+roll*57.3958);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
