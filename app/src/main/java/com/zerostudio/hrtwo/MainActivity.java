package com.zerostudio.hrtwo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zerostudio.hrtwo.db.dao.bean.QuesstionOption;
import com.zerostudio.hrtwo.db.dao.bean.QuesstionOptionDao;
import com.zerostudio.hrtwo.db.dao.bean.Section;
import com.zerostudio.hrtwo.db.dao.bean.SectionDao;
import com.zerostudio.hrtwo.utils.AssetsUtil;
import com.zerostudio.hrtwo.utils.LogUtil;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView viewById = (TextView) findViewById(R.id.tv_main);
        setSupportActionBar(toolbar);
        try {
            AssetsUtil.copyDatabase(this,"humanresource_third_tiku.db");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<QuesstionOption> users = new QuesstionOptionDao(this, QuesstionOption.class).queryForAll();
        Section users2 = new SectionDao(this, Section.class).queryById(2);
        if (users != null && users2 != null){
            viewById.setText(users.get(1).toString()+users2.toString());
        }else{
            LogUtil.i("null database ");
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
