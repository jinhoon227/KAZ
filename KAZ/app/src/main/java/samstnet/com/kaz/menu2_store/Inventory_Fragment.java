package samstnet.com.kaz.menu2_store;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;
import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.Item_type;
import samstnet.com.kaz.menu1_growth_inventory.Item_View1;

public class Inventory_Fragment extends Fragment {
    MainActivity activity;
    MyAdapter adapter;
    EditText editText1;
    EditText editText2;
    String name;
    String mobile;
    int redit;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();

    }
    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);

    }
    class MyAdapter extends BaseAdapter{

        ArrayList<Item_type> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Item_type item)
        {
            items.add(item);
        }
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Item_View1 view = null;
            //데이터를 많이 쓸수록 재사용 할 수 있게 하기위해서 사용한다.

            if(convertView==null){
                view = new Item_View1(getActivity().getApplicationContext());
            }
            else
            {
                view=(Item_View1) convertView;
            }

            Item_type item = items.get(position);
            view.setName(item.getName());
            view.setMobile(item.getMobile());
            view.setImage(item.getResId());
            return view;
        }
    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu1_frag_grow_inventory, container, false);
        //인덱스 참고 함수

        ListView listview =  rootView.findViewById(R.id.ListView);
        editText1 = (EditText)rootView.findViewById(R.id.editText1);
        editText2 = (EditText)rootView.findViewById(R.id.editText2);
        adapter = new MyAdapter();
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item_type item = (Item_type) adapter.getItem(position);//index 할당
                Toast.makeText(getActivity(),"선택 "+item.getName(),Toast.LENGTH_SHORT).show();//toast 메세지 출력

            }
        });
            Button button = rootView.findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=editText1.getText().toString();
                mobile=editText2.getText().toString();
                adapter.addItem(new Item_type(name, mobile, R.drawable.img1));
                adapter.notifyDataSetChanged();
            }
        });
        return rootView;
    }

    @Subscribe
    public void getStoreItem(Item_type itt){
        Log.d("일단","해봐");
        String name=itt.getName();
        String mobile=itt.getMobile();
        int redit = itt.getResId();
        adapter.addItem(new Item_type(name,mobile,redit));

    }
}
