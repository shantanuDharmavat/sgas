package shantanu.testmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import Util.GlobalVariable;
import Util.NetworkCallHandler;

/**
 * Created by Shantanu on 03-02-2017.
 */

public class IncidentPhotoActivity extends AppCompatActivity {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    static String incidentId;
    ImageView ivImage;
    Bitmap bitmap;
    Button buttonSubmit,buttonRetake;
    byte[] encode;
    String imageName="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_photo);
        ivImage = (ImageView) findViewById(R.id.imageViewIncidentImage);
        buttonRetake = (Button) findViewById(R.id.buttonRetakeImg);
        buttonSubmit = (Button) findViewById(R.id.buttonImageSubmit);

        nameGenerator();
        incidentId = getIntent().getStringExtra("id");
        dialogeBuilder();

        buttonRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogeBuilder();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String convertedImage = Base64.encodeToString(encode,Base64.NO_WRAP);
                new sendImageTask().execute(convertedImage,incidentId);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        encode = bytes.toByteArray();

        File destination = new File(GlobalVariable.getImageFile(), imageName);

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(bitmap);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                encode = bytes.toByteArray();
                Log.e("BYTE array",""+encode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bitmap);
    }

    private void nameGenerator(){
        imageName = incidentId + "_" + (System.currentTimeMillis() % 100000) + ".jpg";
    }

    private void dialogeBuilder(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setTitle("Media");
        alertBuilder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
            }
        });

        alertBuilder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });
        alertBuilder.show();
    }

    private class sendImageTask extends AsyncTask<String ,Void,String >{
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(IncidentPhotoActivity.this);
            dialog.setMessage("Sending...");
            dialog.setTitle("Please Wait");
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing())
                dialog.cancel();
            AlertDialog.Builder dialog = new AlertDialog.Builder(IncidentPhotoActivity.this);
            dialog.setTitle("Success");
            dialog.setIcon(android.R.drawable.stat_sys_upload_done);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
            String responseString = new NetworkCallHandler().imageSender(params[0],params[1],IncidentPhotoActivity.this);
            Log.e("image response",""+responseString);
            return responseString;
        }
    }

}
