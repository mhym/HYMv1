/*Copyright (c) <2014>, Miles
--
-- Company:				---
--
-- Engineers: 			Ömer Naci Soydemir
--
-- Create Date:			27/11/2014
--
-- Project Name: 		HYM
--
-- Project Manager		Suhap SAHIN, Adnan Kavak
--
-- Target Devices:  	Samsung Galaxy Nexus 5
--
-- Android Versions: 	Jelly Bean (API level 16)
--
-- Description: Uygulama bir Activtiy, bir Service ve bir tane BroadcastReceiver uygulama bilesenini(aplication component) icermektedir.
--				Bu uygulama bilesenlerinden Activity Sistemi baslatip olmektedir.
--				Dolayisiyla telefon uyusa bile bu bilesenler islemciyi uyandirip calismaktadir.
--				Service bileseni, telefon uykuda olsa bile uynadirma kilidi alarak kayit oldugu Broadcast Receiver bilesenini
--				Alarm Manager API'si vasitasi ile 5 dakika araliklarla calistirmaktadir.
--
 */

package hym.com.hymv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


public class HYMStart extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "MyActivity";
    private static final String GTAG = "HYM271114";
    private BluetoothAdapter bluetoothAdapter;
    private Intent serviceIntent, enableBluetooth, deviceList;
    private static final int requestBluetoothUp = 1;
    private static final int requestDeviceList = 2;
    private String macAddress = "00:00:00:00:00:00";
    private Button setupButton, startButton, stopButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");
        Log.i(GTAG, "onCreate");

        Typeface font = Typeface.createFromAsset(getAssets(), "verdana.ttf"); // Belirtilen yazı verilerinden yeni bir yazı türü oluşturur.

        int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_titled", "id", "android"); // Veri kaynak adı için bir kaynak kimliği döndürür.

        if (actionBarTitleId > 0){

            TextView title = (TextView)findViewById(actionBarTitleId);

            if (title != null){

                title.setTypeface(font);

            }
        }

        setContentView(R.layout.activity_hymstart);

        //serviceIntent = new Intent(getApplicationContext(), WakeLockService.class);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Log.w(TAG, "Ana ekran yüklendi.");
        Log.w(TAG, "ActionBar has been intalled");
        Log.w(TAG, "BluetoothAdapter yüklenmistir.");

        initialize();

        setupButton.setTypeface(font);
        startButton.setTypeface(font);
        stopButton.setTypeface(font);
    }

    private void initialize() {

        setupButton = (Button) findViewById(R.id.setupButton);
        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        setupButton.setOnClickListener(this);
        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.setupButton){

            Log.v(TAG,"setupButton click i");

            StartBlueTooth();

        }

        if (v.getId() == R.id.startButton){

            Log.v(TAG, "startButton click i");

            finish();

            //startService(serviceIntent);

        }

        if (v.getId() == R.id.stopButton){

            Log.v(TAG, "stopButton click i");

            AlertDialog.Builder alert = new AlertDialog.Builder(HYMStart.this);

            alert
                    .setTitle("Uyarı")
                    .setMessage("Uygulamayı sonlandırmak istiyormusunuz ? ")
                    .setCancelable(false)

                    .setPositiveButton("Evet", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            finish();

                        }
                    })
                    .setNegativeButton("Hayir",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            });

            AlertDialog dialog = alert.create();
            dialog.show();

        } // stopButton sonu

    }

    private void StartBlueTooth() {

        Log.v(TAG, "StartBlueTooth metodu");
        Log.v(GTAG, "StartBlueTooth metodu");

        /*if (!bluetoothAdapter.isEnabled()) {
            Log.w(TAG, "bluetooth aktif degil");
            enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, requestBluetoothUp);
            Log.w(TAG, "enableBluetooth Activity has been started");
        } else {
            Log.w(TAG, "bluetooth aktif");
            if (bluetoothAdapter != null) {
                deviceList = new Intent(getApplicationContext(),
                        DeviceListActivity.class);
                startActivityForResult(deviceList, requestDeviceList);
                Log.w(TAG, "BluetoothAdapter has been gotten");
                Log.w(TAG, "devicelist has been started");
            }

        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "onActivityResult");
        Log.i(GTAG, "onActivityResult");

        switch (requestCode){

            case requestBluetoothUp :

                if (resultCode == RESULT_OK){

                    Log.w(TAG, "Bluetooth aktif");
                    StartBlueTooth();

                } else{

                    Log.w(TAG, "Bluetooth aktif degil");
                    finish();

                }

                break;

            case requestDeviceList :

                if (resultCode == Activity.RESULT_OK){

                    //macAddress = data.getExtras().getString(DeviceListActivity.DEVICE_MAC);
                    writeFile();
                }

                break;
        }


    }

    private void writeFile() {

        Log.i(TAG, "writeFile");
        Log.i(GTAG, "writeFile");

        /*try {
            String filePath = getApplicationContext().getFilesDir().getPath()
                    .toString()
                    + "/macfile.txt";
            File myFile = new File(filePath);
            if (myFile.exists()) {
                Toast.makeText(getApplicationContext(),
                        "Bu cihazin kurulumu mevcuttur.", Toast.LENGTH_LONG)
                        .show();
                myFile.delete();
            }
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(macAddress);
            myOutWriter.close();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.hymstart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
