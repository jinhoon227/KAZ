
package samstnet.com.kaz.menu2_store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;

import java.util.ArrayList;

import samstnet.com.kaz.MainActivity;
import samstnet.com.kaz.R;
import samstnet.com.kaz.eventbus.BusProvider;
import samstnet.com.kaz.eventbus.Customer;
import samstnet.com.kaz.eventbus.Item_type;

import static android.app.Activity.RESULT_OK;

public class Menu2FragStore extends Fragment {
    MainActivity activity;
    StoreAdapter StAdapter;
    ListView listview;
    EditText moneyview;
    ImageView treeview;
    //listview 에니메이션 기능 구현
    AlphaInAnimationAdapter animationAdapter;
    GlideDrawableImageViewTarget gifImage;
    Customer cus;




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
        cus =(Customer)getActivity().getApplication();
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
            if(item.isBuy()){
                if(item.isWear())
                {
                    view.setwear("장착중");
                }
                else
                {
                    view.setwear(" ");
                }
            }
            else
            {
                view.setwear(item.getPrice()+" 씨앗");
            }

            return view;
        }

    }

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu2_frag_store, container, false);
        Item_type[] storeitem = Customer.item;
        int money=cus.getMoney();
        //남은 돈 출력
        moneyview=(EditText)rootView.findViewById(R.id.textView_money);
        moneyview.addTextChangedListener(textWatcher);
        moneyview.setText(money+" 씨앗");
        Log.d("","씨앗"+cus.getMoney());
        //인덱스 참고 함수

        //에니메이션을 위해 dynamiclistview 사용
        listview =  rootView.findViewById(R.id.storeListView);
        StAdapter = new StoreAdapter();

        //에니메이션 클레스 wrapping
        animationAdapter = new AlphaInAnimationAdapter(StAdapter);

        animationAdapter.setAbsListView(listview);
        //wrapping
        listview.setAdapter(animationAdapter);

        //treeview
        treeview = rootView.findViewById(R.id.treeImage);

        gifImage=new GlideDrawableImageViewTarget(treeview);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Item_type item = (Item_type) StAdapter.getItem(position);//index 할당
                Toast.makeText(getActivity(),"선택 "+item.getName(),Toast.LENGTH_SHORT).show();//toast 메세지 출력
                //팝업 메세지 출력
                Intent intent = new Intent(getActivity(), PopupActivity.class);
                if (item.isBuy()) {
                    if (item.isWear()) {
                        intent.putExtra("data", item.getName() + " 장착 해제 합니다");
                        item.setWear(false);
                    } else {
                        intent.putExtra("data", item.getName() + " 장착 합니다");
                        item.setWear(true);
                        switch (position) {
                            case 0:
                                Glide.with(getActivity()).load(R.drawable.sprinkler).into(gifImage);
                                break;
                            case 1:
                                Glide.with(getActivity()).load(R.drawable.happy).into(gifImage);
                                break;
                            case 2:
                                Glide.with(getActivity()).load(R.drawable.happy).into(gifImage);
                                break;
                            case 3:
                                Glide.with(getActivity()).load(R.drawable.sunglasses).into(gifImage);
                                break;
                            case 4:
                                Glide.with(getActivity()).load(R.drawable.scarf).into(gifImage);
                                break;
                            default:
                                Glide.with(getActivity()).load(R.drawable.sprinkler).into(gifImage);
                        }

                    }
                } else {
                    intent.putExtra("data", "아이탬 : " + item.getName() + "를 " + item.getPrice() + "씨앗에 구매하시겠습니까?");
                }
                cus.setPosition(position);
                startActivityForResult(intent, 1);
                BusProvider.getInstance().post(item);

            }
        });
        for(int i=0;i<storeitem.length;i++) {
            if(storeitem[i]!=null) {
                StAdapter.addItem(storeitem[i]);
            }
        }
        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int position;
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                Log.d("result ","result 값:"+result);
                position=cus.getPosition();
                if(result.equals("confirm"))
                {
                    Log.d("","포지션"+position);
                    if(!Customer.item[position].isBuy())
                    {
                        Customer.item[position].setBuy(true);
                        cus.setMoney(cus.getMoney()-cus.item[position].getPrice());
                        moneyview.setText(cus.getMoney()+" 씨앗");
                        Log.d("isbuy","들어감 : 돈"+cus.getMoney());
                    }
                }
            }
        }
    }
    //2019-07-03 데이터 갱신을 위해 osnResume 추가
    @Override
    public void onResume(){
        super.onResume();
        StAdapter.notifyDataSetChanged();
    }
    //2019 07-19 데이터 변화시 글자 바뀌는 함수
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable edit) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }
    };

}
