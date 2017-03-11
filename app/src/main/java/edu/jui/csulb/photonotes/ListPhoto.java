package edu.jui.csulb.photonotes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListPhoto extends AppCompatActivity {

    ListView list;
    SQLliteHelper sqLliteHelper;
    ArrayList<Note> dataGetSet;
    private static final int PER_REQUEST_CODE1 = 100;
    private static final int PER_REQUEST_CODE2 = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try{
            if(ActivityCompat.checkSelfPermission(ListPhoto.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ListPhoto.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PER_REQUEST_CODE1);
            }
            else if(ActivityCompat.checkSelfPermission(ListPhoto.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ListPhoto.this, new String[]{Manifest.permission.CAMERA}, PER_REQUEST_CODE2);
            }
        }
        catch (Exception e)
        {
            System.out.print(e.toString());
        }

        list=(ListView) findViewById(R.id.listview);
        sqLliteHelper = new SQLliteHelper(getApplicationContext());
        showcaptionlist();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListPhoto.this, ViewPhoto.class);
                i.putExtra("id", dataGetSet.get(position).id);
                startActivity(i);
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent= new Intent(ListPhoto.this,AddPhoto.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode,Intent data){

        super.onActivityResult(requestcode, resultcode, data);
        showcaptionlist();
    }

    @Override
    protected void onResume() {
        showcaptionlist();
        super.onResume();
    }

    private void showcaptionlist(){
        dataGetSet = sqLliteHelper.getCaptionList();

        if(dataGetSet.size()==0)
        {
            list.setAdapter(null);
            Toast.makeText(getApplicationContext(), "No List Item has been added yet. Click on plus button to add a new note.", Toast.LENGTH_LONG);
        }
        else
        {
            ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, dataGetSet);
            list.setAdapter(adp);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_photo, menu);
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PER_REQUEST_CODE1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Your permission
                ActivityCompat.requestPermissions(ListPhoto.this, new String[]{Manifest.permission.CAMERA}, PER_REQUEST_CODE2);
            }
            else
            {
                Toast.makeText(this, "Provide appropriate permissions to avoid any unwanted behavior.", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == PER_REQUEST_CODE2){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Done
            }
            else {
                Toast.makeText(this, "Provide appropriate permissions to avoid any unwanted behavior.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:edu.jui.csulb.photonotes"));
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
