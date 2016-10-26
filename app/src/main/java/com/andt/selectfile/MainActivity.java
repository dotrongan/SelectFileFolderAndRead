package com.andt.selectfile;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;

import com.andt.selectfile.selectfile.SelectActivity;
import com.andt.selectfile.selectfile.SelectMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.tvFilePath) TextView tvFilePath;
    @BindView(R.id.btnOpenFile) Button btnOpenFile;
    @BindView(R.id.btnOpenFolder) Button btnOpenFolder;
    @BindView(R.id.btnSaveFile) Button btnSaveFile;

    protected static final int PATH_RESULT = 123;
    Intent i = null;
    private static final String TAG = "Andt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btnOpenFile.setOnClickListener(this);
        btnOpenFolder.setOnClickListener(this);
        btnSaveFile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOpenFile:
                i = new Intent(MainActivity.this, SelectActivity.class);
                i.putExtra(SelectActivity.EX_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
                i.putExtra(SelectActivity.EX_STYLE, SelectMode.SELECT_FILE);
                startActivityForResult(i, PATH_RESULT);
                break;
            case R.id.btnOpenFolder:
                i = new Intent(MainActivity.this, SelectActivity.class);
                i.putExtra(SelectActivity.EX_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
                i.putExtra(SelectActivity.EX_STYLE, SelectMode.SELECT_FOLDER);
                startActivityForResult(i, PATH_RESULT);
                break;
            case R.id.btnSaveFile:
                i = new Intent(MainActivity.this, SelectActivity.class);
                i.putExtra(SelectActivity.EX_PATH, Environment.getExternalStorageDirectory().getAbsolutePath());
                i.putExtra(SelectActivity.EX_STYLE, SelectMode.SAVE_FILE);
                startActivityForResult(i, PATH_RESULT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PATH_RESULT && resultCode == RESULT_OK) {
            tvFilePath.setText(data.getStringExtra(SelectActivity.EX_PATH_PARENT));
            String fileName = data.getStringExtra(SelectActivity.EX_FILE_NAME);
            String path = data.getStringExtra(SelectActivity.EX_PATH_PARENT);
            Log.d("Andt","path "+path+" fileName "+fileName);
            File sdcard = new File(path);
            File file = new File(sdcard,fileName);
            Log.d(TAG, readTextFile(file));
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public String readTextFile(File filePath) {
        StringBuilder text = new StringBuilder();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                String url = line.toString();
                if(url.length() > 0 && URLUtil.isValidUrl(url)) {
                    text.append(line);
                    text.append('\n');
                }
//                text.append(line);
//                text.append('\n');
            }
            br.close() ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

}
