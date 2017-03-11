package edu.jui.csulb.photonotes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

import static android.R.attr.button;
import static edu.jui.csulb.photonotes.R.id.editText;

public class AddPhoto extends AppCompatActivity {
    EditText caption;
    Button list, cam, save;
    ImageView imageView;
    private static final int CAMERA_REQUEST=1888;
    int flag=0;
    String path="";
    Bitmap bitImage;
    public  static SQLliteHelper sqlLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        sqlLiteHelper=new SQLliteHelper(getApplicationContext());
//        sqlLiteHelper.op

        cam = (Button)findViewById(R.id.camera);
        save = (Button)findViewById(R.id.save);
        caption = (EditText)findViewById(R.id.editText);

        imageView = (ImageView)findViewById(R.id.imageView);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraintent,CAMERA_REQUEST);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                String caption_name = caption.getText().toString();
                if (caption.length()!=0){
                    storeImage();
                    Note dataGetterSetter = new Note();
                    dataGetterSetter.image=path;
                    dataGetterSetter.caption = caption.getText().toString();
                    sqlLiteHelper.addtoList(dataGetterSetter);
                    Toast.makeText(getApplicationContext(),"Added Successfully..!",Toast.LENGTH_SHORT).show();
                    caption.setText("");
                    imageView.setImageResource(R.mipmap.cam);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Please add both image and caption",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitImage = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitImage);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void storeImage(){
        String root = Environment.getExternalStorageDirectory().toString();
        File mydir = new File(root+ "/PhotoNotes");
        mydir.mkdirs();
        Random generator = new Random();
        int rnum = 2017;
        rnum=generator.nextInt(rnum);
        String filename =  "Image."+ rnum +".jpg";
        File file=new File(mydir, filename);
        if (file.exists()) file.delete();

        path = file.getAbsolutePath();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try{
            super.onSaveInstanceState(outState);
            if(bitImage!=null)
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                outState.putByteArray("image",byteArray);
                outState.putInt("flag",flag);
            }
        }
        catch (Exception e)
        {
            System.out.print(e.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        byte[] byteArray = savedInstanceState.getByteArray("image");
        flag = savedInstanceState.getInt("flag");
        if(byteArray!=null)
        {
            bitImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageView.setImageBitmap(bitImage);
        }
    }

}
