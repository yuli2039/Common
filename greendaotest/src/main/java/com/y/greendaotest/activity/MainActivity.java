package com.y.greendaotest.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.y.greendaotest.App;
import com.y.greendaotest.R;
import com.y.greendaotest.entity.User;
import com.y.greendaotest.gen.UserDao;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listview;
    private MyAdapter adapter;

    private EditText etInput;
    private UserDao userDao;
    private ArrayList<User> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.list);
        etInput = (EditText) findViewById(R.id.etInput);

        userDao = App.daoSession.getUserDao();

        data = new ArrayList<>();
        adapter = new MyAdapter(data, this);
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deletebyKey(data.get(i).getId());
                searchAndRefresh();
                return false;
            }
        });
        listview.setAdapter(adapter);

        searchAndRefresh();
    }

    /**
     * 重新从数据库查询所有数据并显示
     */
    private void searchAndRefresh() {
        data.clear();

        List<User> users = userDao.loadAll();
        data.addAll(users);
        adapter.notifyDataSetChanged();
    }

    /**
     * 增
     */
    public void btnAdd(View v) {
        String trim = etInput.getText().toString().trim();
        if (trim.isEmpty()) {
            Toast.makeText(this, "输入信息为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!trim.contains("#")) {
            Toast.makeText(this, "格式为：name#age#sex", Toast.LENGTH_SHORT).show();
            return;
        }
        // name#18#0
        String[] split = trim.split("#");

        User us = new User();
        us.setName(split[0]);
        us.setAge(Integer.valueOf(split[1]));
        us.setSex(split[2].equals("0"));
        userDao.insert(us);
//        userDao.update(us); // 改

        searchAndRefresh();
    }

    /**
     * 删
     */
    private void deletebyKey(long key) {
        userDao.deleteByKey(key);
    }

    /**
     * 查
     */
    public void btnSearch(View v) {
        String trim = etInput.getText().toString().trim();
        if (trim.isEmpty()) {
            Toast.makeText(this, "输入信息为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!trim.contains("#")) {
            Toast.makeText(this, "不支持的查询条件", Toast.LENGTH_SHORT).show();
            return;
        }

        // name#yu
        // age#10
        String[] split = trim.split("#");

        Query<User> query;
        if (split[0].equals("name")) {
            query = userDao.queryBuilder()
                    .where(UserDao.Properties.Name.eq(split[1]))// name = ?
                    .orderAsc(UserDao.Properties.Age)
                    .build();
        } else if (split[0].equals("age")) {
            query = userDao.queryBuilder()
                    .where(UserDao.Properties.Age.lt(split[1]))// age < ?
                    .orderAsc(UserDao.Properties.Age)
                    .build();
        } else {
            Toast.makeText(this, "不支持的查询条件", Toast.LENGTH_SHORT).show();
            return;
        }

        List<User> list = query.list();
        data.clear();
        data.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private static class MyAdapter extends BaseAdapter {

        List<User> data;
        Context context;

        public MyAdapter(List<User> data, Context context) {
            super();
            this.data = data;
            this.context = context;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Viewholder holder;
            if (view == null) {
                view = View.inflate(context, R.layout.list_item, null);
                holder = new Viewholder();
                holder.tvId = (TextView) view.findViewById(R.id.tvId);
                holder.tvName = (TextView) view.findViewById(R.id.tvName);
                holder.tvAge = (TextView) view.findViewById(R.id.tvAge);
                holder.tvSex = (TextView) view.findViewById(R.id.tvSex);
                view.setTag(holder);
            } else {
                holder = (Viewholder) view.getTag();
            }
            User user = data.get(i);
            holder.tvId.setText(String.valueOf(user.getId()));
            holder.tvName.setText(user.getName());
            holder.tvAge.setText(user.getAge() + "岁");
            holder.tvSex.setText(user.getSex() ? "男" : "女");
            return view;
        }
    }

    private static class Viewholder {
        public TextView tvId, tvName, tvAge, tvSex;
    }

}