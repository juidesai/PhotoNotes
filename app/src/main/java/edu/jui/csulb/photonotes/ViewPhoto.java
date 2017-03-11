package edu.jui.csulb.photonotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewPhoto extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);

        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView)findViewById(R.id.textView);

        Intent i = getIntent();
        String id = i.getStringExtra("id");
        SQLliteHelper sqLiteHelper = new SQLliteHelper(getApplicationContext());
        Note dataGetSet = new Note();

        dataGetSet = sqLiteHelper.getImage(id);

        Bitmap myBitmap = BitmapFactory.decodeFile(dataGetSet.image);

        imageView.setImageBitmap(myBitmap);

        textView.setText(dataGetSet.caption);

        dataGetSet = sqLiteHelper.getImage(id);

    }
}
