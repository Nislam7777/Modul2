package android.me.b1_si_prak1b_13120220021;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Button btnBukaGallery, btnBukaKamera;
    private final int REQ_CODE_PERM_STORAGE = 1;
    private final int REQ_CODE_BUKA_GALLERY = 1;
    private final int REQ_CODE_BUKA_KAMERA = 2;
    private DataFileGambar dataFileGambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.imageView);
        btnBukaGallery = findViewById(R.id.btnBukaGallery);
        btnBukaGallery.setOnClickListener(view -> bukaGalleryClick());
        btnBukaKamera = findViewById(R.id.btnBukaKamera);
        btnBukaKamera.setOnClickListener(view -> bukaKameraClick());
    }

    @SuppressWarnings("deprecation")
    private void bukaGallery(){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        startActivityForResult(intent, REQ_CODE_BUKA_GALLERY);
    }

    private void bukaGalleryClick(){
        int permissionReadStorage = 1;
        if(Build.VERSION.SDK_INT > 30){
            permissionReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES);
        }else{
            permissionReadStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(permissionReadStorage != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT > 30){
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.READ_MEDIA_IMAGES}, REQ_CODE_PERM_STORAGE);
            }else{
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_CODE_PERM_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_CODE_PERM_STORAGE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                bukaGallery();
            }
        }
    }

    public DataFileGambar getDataFileGambar() throws Exception{
        DataFileGambar dataFileGambar = null;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile("hasil", ".jpg", storageDir);
        dataFileGambar = new DataFileGambar(image.getAbsolutePath(), image);
        return dataFileGambar;
    }

    @SuppressWarnings("deprecation")
    private void bukaKameraClick(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            dataFileGambar = getDataFileGambar();
            Uri uri = FileProvider.getUriForFile(this, getPackageName(), dataFileGambar.getFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQ_CODE_BUKA_KAMERA);
        }catch(Exception e){
            System.out.println(e.getMessage().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_BUKA_GALLERY){
            Cursor cursor;
            String col = "_data";
            String projection[] = {col};
            cursor = getApplicationContext().getContentResolver().query(data.getData(), projection, null, null, null);
            cursor.moveToFirst();
            int index = cursor.getColumnIndexOrThrow(col);
            String pathGambar = cursor.getString(index);
            Bitmap gambar = BitmapFactory.decodeFile(pathGambar);
            imageView.setImageBitmap(gambar);
        }
        else if(requestCode == REQ_CODE_BUKA_KAMERA){
            Bitmap bitmap = BitmapFactory.decodeFile(dataFileGambar.getPathfile());
            imageView.setImageBitmap(bitmap);
        }
    }
}