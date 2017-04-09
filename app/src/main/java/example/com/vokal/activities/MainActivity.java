package example.com.vokal.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import example.com.vokal.R;
import example.com.vokal.adapter.CustomAdapter;

public class MainActivity extends AppCompatActivity {
    private Button clickButton;
    private ListView listView;
    List<Map.Entry<String, Integer>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clickButton = (Button) findViewById(R.id.btn_click);
        listView = (ListView) findViewById(R.id.listview);
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/plain");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                String chooserName = "Browse";
                Intent chooser = Intent.createChooser(intent, chooserName);

                startActivityForResult(chooser, 1);
            }
        });

    }
    private void findOccurrenceOfWords(String words){
        String[] word = words.split("\\s+");
        Map<String, Integer> map = new HashMap<>();
        for (String w : word) {
            Integer n = map.get(w);
            n = (n == null) ? 1 : ++n;
            map.put(w, n);
        }
        Set<Map.Entry<String, Integer>> set = map.entrySet();
        list = new ArrayList<>(set);
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );
        if(list.size()!=0){
            displayAdapter(list);
        }

    }

    private void displayAdapter(List<Map.Entry<String, Integer>> list) {
        CustomAdapter mAdapter = new CustomAdapter(this);
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getValue()>count) {
                mAdapter.addItem(list.get(i));
                mAdapter.addSectionHeaderItem(i,count+" - "+(count+10),list.get(i));
                count+=10;
            }else {
                mAdapter.addItem(list.get(i));
            }
        }
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
            case 1:

                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String fileName = null;
                    if (uri != null) {
                        if (uri.toString().startsWith("file:")) {
                            fileName = uri.getPath();
                        } else { // uri.startsWith("content:")

                            Cursor c = getContentResolver().query(uri, null, null, null, null);

                            if (c != null && c.moveToFirst()) {

                                int id = c.getColumnIndex(MediaStore.Images.Media.DATA);
                                if (id != -1) {
                                    fileName = c.getString(id);
                                }

                            }
                        }
                    }
                    openFile(fileName);
                }
        }
    }

    private void openFile(String path){

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
        }
        findOccurrenceOfWords(text.toString());
    }

}
