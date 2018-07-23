package com.sty.custom.actv;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.sty.custom.actv.adapter.AutoEditTextAdapter;

import java.util.Arrays;

//代码参考：https://www.cnblogs.com/blog4wei/p/9100726.html
//         https://blog.csdn.net/lvshaorong/article/details/51878833

public class MainActivity extends AppCompatActivity {
    private AutoCompleteTextView actvOrigin;
    private AutoCompleteTextView actvCustom;

    private boolean isStopThread = false;
    private String oldKeyWord;
    private String newKeyWord;

    private String[] dataArray = new String[]{
            "aaaabbbcdddddddd",
            "abcdefg",
            "aabcd",
            "bsageaa",
            "baaaaadd",
            "xmlsalaksd",
            "aabbccdd",
            "aacceeff",
            "aaddll"
    };

    private ArrayAdapter<String> originAdapter;
    private AutoEditTextAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initOriginActv();
        initCustomActv();
    }

    private void initOriginActv(){
        actvOrigin = findViewById(R.id.actv_origin);
        originAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataArray);
        actvOrigin.setThreshold(1);  //从第一个字符就开始匹配
        actvOrigin.setDropDownWidth(800);
        actvOrigin.setAdapter(originAdapter);
    }

    private void initCustomActv(){
        actvCustom = findViewById(R.id.actv_custom);
        customAdapter = new AutoEditTextAdapter(this, Arrays.asList(dataArray));
        customAdapter.setListener(new AutoEditTextAdapter.MyOnItemClickListener() {
            @Override
            public void onItemClicked(String str) {
                Toast.makeText(MainActivity.this, str + "-----is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        actvCustom.setThreshold(1);
        actvCustom.setDropDownWidth(800);
        actvCustom.setAdapter(customAdapter);

        actvCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actvCustom.showDropDown();
            }
        });

        actvCustom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //This method is called to notify you that, within s, the count characters beginning
                // at start are about to be replaced by new text with length after.
                Log.i("sty", "beforeTextChanged--s:(" + s +")--start:(" + start + ")--count:(" + count + ")--after:(" + after + ")");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This method is called to notify you that, within s, the count characters beginning
                // at start have just replaced old text that had length before[经测试只有0和1，分别代表删除和添加和删除].
                Log.i("sty", "onTextChanged--s:(" + s +")--start:(" + start + ")--before:(" + before + ")--count:(" + count + ")");
                newKeyWord = s.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //This method is called to notify you that, somewhere within s, the text has been changed.
            }
        });

        new Thread(new SearchRunnable()).start();
    }

    class SearchRunnable implements Runnable{
        private String keyWord;
        private Handler handler = new Handler();

        public void pushKeyWord(String keyWord){
            this.keyWord = keyWord;
            handler.removeCallbacks(this);
            if(!isStopThread) {
                handler.postDelayed(this, 500);
            }
        }

        @Override
        public void run() {
            if(!TextUtils.isEmpty(keyWord) && !keyWord.equals(oldKeyWord)){
                oldKeyWord = keyWord;
                //取消网络请求
                //发起新的网络请求
                Log.i("sty", "------发起网络请求");
            }
            pushKeyWord(newKeyWord);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStopThread = true;
    }
}
