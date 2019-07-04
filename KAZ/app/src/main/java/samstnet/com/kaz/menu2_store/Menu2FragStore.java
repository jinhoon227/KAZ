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
import android.widget.ListView;
import android.widget.Toast;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;

import java.util.ArrayList;

import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;
import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.Item_type;

public class Menu2FragStore extends Fragment {
    MainActivity activity;
    StoreAdapter StAdapter;
    ListView listview;

    //listview 에니메이션 기능 구현
    AlphaInAnimationAdapter animationAdapter;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;

    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);

    }

    class StoreAdapter extends BaseAdapter {

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
        public boolean hasStableIds() {
            return true;
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu2_frag_store, container, false);

        //인덱스 참고 함수

        //에니메이션을 위해 dynamiclistview 사용
        listview =  rootView.findViewById(R.id.storeListView);
        StAdapter = new StoreAdapter();

        //에니메이션 클레스 wrapping
        animationAdapter = new AlphaInAnimationAdapter(StAdapter);

        animationAdapter.setAbsListView(listview);
        //wrapping
        listview.setAdapter(animationAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item_type item = (Item_type) StAdapter.getItem(position);//index 할당
                Toast.makeText(getActivity(),"선택 "+item.getName(),Toast.LENGTH_SHORT).show();//toast 메세지 출력
                BusProvider.getInstance().post(item);

            }
        });
        StAdapter.addItem(new Item_type("1","01035925006",R.drawable.img1));
        StAdapter.addItem(new Item_type("2","01035925006",R.drawable.img2));
        StAdapter.addItem(new Item_type("3","01035925006",R.drawable.img3));
        StAdapter.addItem(new Item_type("4","01035925006",R.drawable.img4));
        StAdapter.addItem(new Item_type("4","01035925006",R.drawable.img4));
        StAdapter.addItem(new Item_type("4","01035925006",R.drawable.img4));
        StAdapter.addItem(new Item_type("4","01035925006",R.drawable.img4));
        StAdapter.addItem(new Item_type("4","01035925006",R.drawable.img4));
        StAdapter.addItem(new Item_type("4","01035925006",R.drawable.img4));
        StAdapter.addItem(new Item_type("4","01035925006",R.drawable.img4));
        return rootView;
    }
}
