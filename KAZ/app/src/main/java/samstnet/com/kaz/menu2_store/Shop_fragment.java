package samstnet.com.kaz.menu2_store;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;
import samstnet.com.kaz.eventbus.Customer;
import samstnet.com.kaz.eventbus.Item_type;

public class Shop_fragment extends Fragment {
    MainActivity activity;
    Adapter adapter;
    EditText editText1;
    EditText editText2;
    String name;
    String mobile;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    class Adapter extends BaseAdapter {

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
            Item_View2 view = null;
            //데이터를 많이 쓸수록 재사용 할 수 있게 하기위해서 사용한다.

            if(convertView==null){
                view = new Item_View2(getActivity().getApplicationContext());
            }
            else
            {
                view=(Item_View2) convertView;
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
        //2019-07-03 전역 변수 데이터 가져옴
        Item_type[] storeitem = Customer.item;

        ListView listview =  rootView.findViewById(R.id.ListView);
        adapter = new Adapter();
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item_type item = (Item_type) adapter.getItem(position);//index 할당
                Toast.makeText(getActivity(),"선택 "+item.getName(),Toast.LENGTH_SHORT).show();//toast 메세지 출력

            }
        });
        return rootView;
    }
}
