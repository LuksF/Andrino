package com.example.lucas.andrino;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    //final Context context = this;
    public String ipAddress = "192.168.0.26";
    Button setting;
    Switch switchVerde;
    Switch switchRojo;
    int verde = 0;
    int rojo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setting = (Button) findViewById(R.id.setting);
        switchRojo = (Switch) findViewById(R.id.switch1);
        switchVerde = (Switch) findViewById(R.id.switch2);
        switchRojo.setOnCheckedChangeListener(this);
        switchVerde.setOnCheckedChangeListener(this);
    }

    /** When the button clicks this method executes**/
    public void settingClick (View view){
        Intent intent = new Intent(this,setting.class);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        String ledStatus;
        if(switchVerde.isChecked() && verde ==0) {
            verde = 1;
            if (ipAddress.equals("0.0.0.0"))
                Toast.makeText(MainActivity.this, "Please enter the ip address...", Toast.LENGTH_SHORT).show();
            else{
                ledStatus = "1";
                ipAddress = "192.168.0.26";
                String serverAdress = ipAddress + ":" + "80";
                HttpRequestTask requestTask = new HttpRequestTask(serverAdress);
                requestTask.execute(ledStatus);
            }
        }
        if(!switchVerde.isChecked() && verde ==1){
            verde = 0;
            ledStatus = "0";
            ipAddress = "192.168.0.26";
            String serverAdress = ipAddress + ":" + "80";
            HttpRequestTask requestTask = new HttpRequestTask(serverAdress);
            requestTask.execute(ledStatus);
        }

        if (switchRojo.isChecked() && rojo ==0) {
            rojo = 1;
            if (ipAddress.equals("0.0.0.0"))
                Toast.makeText(MainActivity.this, "Please enter the ip address...", Toast.LENGTH_SHORT).show();
            else{
                ledStatus = "1";
                ipAddress = "192.168.0.27";
                String serverAdress = ipAddress + ":" + "80";
                HttpRequestTask requestTask = new HttpRequestTask(serverAdress);
                requestTask.execute(ledStatus);
                //Toast.makeText(MainActivity.this, "Prender led Rojo", Toast.LENGTH_SHORT).show();
            }
        }

        if(!switchRojo.isChecked() && rojo ==1){
            rojo = 0;
            ledStatus = "0";
            ipAddress = "192.168.0.27";
            String serverAdress = ipAddress + ":" + "80";
            HttpRequestTask requestTask = new HttpRequestTask(serverAdress);
            requestTask.execute(ledStatus);
            //Toast.makeText(MainActivity.this, "Apagar led Rojo", Toast.LENGTH_SHORT).show();
        }
    }
}

class HttpRequestTask extends AsyncTask<String, Void, String> {

    private String serverAdress;
    private String serverResponse = "";
    private AlertDialog dialog;

    public HttpRequestTask(String serverAdress) {

        this.serverAdress = serverAdress;
           /* dialog = new AlertDialog.Builder(context)
                    .setTitle("HTTP Response from Ip Address:")
                    .setCancelable(true)
                    .create();*/
    }

    @Override
    protected String doInBackground(String... params) {
        // dialog.setMessage("Data sent , waiting response from server...");
        //if (!dialog.isShowing())
        //    dialog.show();
        String val = params[0];
        final String url = "http://" + serverAdress + "/led/" + val;

        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet();
            getRequest.setURI(new URI(url));
            HttpResponse response = client.execute(getRequest);
            InputStream inputStream = null;
            inputStream = response.getEntity().getContent();
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(inputStream));
            serverResponse = bufferedReader.readLine();
            inputStream.close();

        } catch (URISyntaxException e) {
            e.printStackTrace();
            serverResponse = e.getMessage();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            serverResponse = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            serverResponse = e.getMessage();
        }

        return serverResponse;

    }

    @Override
    protected void onPostExecute(String s) {
        //dialog.setMessage(serverResponse);
        //Toast.makeText(this,serverResponse, Toast.LENGTH_SHORT).show();
        //if (!dialog.isShowing())
        //    dialog.show();
    }

    @Override
    protected void onPreExecute() {
        //dialog.setMessage("Sending data to server, please wait...");
        //if (!dialog.isShowing())
        //    dialog.show();
    }
}
