package com.pocketvietnam.org;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class LifeValueActivity extends AppCompatActivity
{
    private ListView lvLife;
    private EditText edSearch;
    private ArrayList<Item> lifeList = new ArrayList<>();

    private View footer;
    private TextView tvPlus;

    //Admob
    private AdView mBannerAd;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_value);


        Intent i = getIntent();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(i.getStringExtra("title"));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            actionBar.setElevation(0);
        }

        initAdmob();

        edSearch = findViewById(R.id.ed_search);
        lvLife = findViewById(R.id.lv_life_list);

        ListCheck(i.getIntExtra("key", 1));

        // set adapter
        final LifeValueAdapter adapter = new LifeValueAdapter(this, lifeList);
        lvLife.setAdapter(adapter);
        lvLife.setTextFilterEnabled(true);

        // filter on text change
        edSearch.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (adapter != null)
                {
                    adapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
    }

    /**
     * row item
     */
    public interface Item
    {
        boolean isSection();

        String getTitle();

        String getItem2();

        String getItem3();

        String getItem4();
    }

    /**
     * Section Item
     */
    public class SectionItem implements Item
    {
        private final String title;

        public SectionItem(String title)
        {
            this.title = title;
        }

        public String getTitle()
        {
            return title;
        }

        public String getItem2()
        {
            return title;
        }

        public String getItem3()
        {
            return title;
        }

        public String getItem4()
        {
            return title;
        }

        @Override
        public boolean isSection()
        {
            return true;
        }
    }

    /**
     * Entry Item
     */
    public class EntryItem implements Item
    {
        public final String item1, item2, item3, item4;

        public EntryItem(String item1, String item2, String item3, String item4)
        {
            this.item1 = item1;
            this.item2 = item2;
            this.item3 = item3;
            this.item4 = item4;
        }

        public String getTitle()
        {
            return item1;
        }

        public String getItem2()
        {
            return item2;
        }

        public String getItem3()
        {
            return item3;
        }

        public String getItem4()
        {
            return item4;
        }

        @Override
        public boolean isSection()
        {
            return false;
        }
    }

    /**
     * Adapter
     */
    public class LifeValueAdapter extends BaseAdapter
    {
        private Context context;
        private ArrayList<Item> item;
        private ArrayList<Item> originalItem;

        public LifeValueAdapter()
        {
            super();
        }

        public LifeValueAdapter(Context context, ArrayList<Item> item)
        {
            this.context = context;
            this.item = item;
            //this.originalItem = item;
        }

        @Override
        public int getCount()
        {
            return item.size();
        }

        @Override
        public Object getItem(int position)
        {
            return item.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (item.get(position).isSection())
            {
                // if section header
                convertView = inflater.inflate(R.layout.header_life_value, parent, false);
                TextView tvSectionTitle = convertView.findViewById(R.id.tv_flag);
                tvSectionTitle.setText((item.get(position)).getTitle());
            }
            else
            {
                // if item
                convertView = inflater.inflate(R.layout.listview_item_life, parent, false);
                TextView tvItem1 = convertView.findViewById(R.id.tv_item1);
                TextView tvItem2 = convertView.findViewById(R.id.tv_item2);
                TextView tvItem3 = convertView.findViewById(R.id.tv_item3);
                TextView tvItem4 = convertView.findViewById(R.id.tv_item4);
                tvItem1.setText((item.get(position)).getTitle());
                tvItem2.setText((item.get(position)).getItem2());
                tvItem3.setText((item.get(position)).getItem3());
                tvItem4.setText((item.get(position)).getItem4());
            }

            return convertView;
        }

        /**
         * Filter
         */
        public Filter getFilter()
        {
            Filter filter = new Filter()
            {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results)
                {

                    item = (ArrayList<Item>) results.values;
                    notifyDataSetChanged();
                }

                @SuppressWarnings("null")
                @Override
                protected FilterResults performFiltering(CharSequence constraint)
                {

                    FilterResults results = new FilterResults();
                    ArrayList<Item> filteredArrayList = new ArrayList<>();

                    if (originalItem == null || originalItem.size() == 0)
                    {
                        originalItem = new ArrayList<>(item);
                    }

                    /*
                     * if constraint is null then return original value
                     * else return filtered value
                     */
                    if (constraint == null && constraint.length() == 0)
                    {
                        results.count = originalItem.size();
                        results.values = originalItem;
                    }
                    else
                    {
                        for (int i = 0; i < originalItem.size(); i++)
                        {
                            if (originalItem.get(i).getItem2().startsWith(constraint.toString()) ||
                                    originalItem.get(i).getItem3().startsWith(constraint.toString()) ||
                                    originalItem.get(i).getItem4().startsWith(constraint.toString()))
                            {
                                filteredArrayList.add(originalItem.get(i));
                            }
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }

                    return results;
                }
            };

            return filter;
        }
    }

    public void ListCheck(int check)
    {
        switch (check)
        {
            case 1:
                lifeList.add(new EntryItem("상대", "베트남어", "발음", "한국어"));
                lifeList.add(new SectionItem("주문시"));
                lifeList.add(new EntryItem("A", "xin chào", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("B", "xin chào", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("A", "em ơi, cho anh thực đơn", "엠 어이 조 아잉 특던", "매뉴판 주세요"));
                lifeList.add(new EntryItem("B", "đây ạ ", "더이 아", "여기있습니다"));
                lifeList.add(new EntryItem("A", "chúng tôi muốn gọi  된장찌게 1 cái và XXX 2 cái và soju 1 chai", "중또이 무언 고이 된장 못, XXX 하이, 쏘주 1 짜이", "된장1, XXX 2, 소주 1병요"));
                lifeList.add(new EntryItem("B", "vâng ạ", "벙 아", "네 알겠습니다"));
                lifeList.add(new SectionItem("추가 주문시"));
                lifeList.add(new EntryItem("A", "em ơi, ở đây thêm soju 1 chai nữa ", "엠어이 어 더이 템 짜이 쏘주 느어", "소주 한병 추가요"));
                lifeList.add(new EntryItem("A", "em ơi, ở đây thêm nước canh ", "엠어이 어 더이 템 느억 까잉", "국물 좀 더주세요"));
                lifeList.add(new EntryItem("A", "em ơi, ở đây thay đổi cái vỉ", "엠어이 어 더이 타이도이 까이비", "불판좀 갈아주세요"));
                lifeList.add(new EntryItem("A", "em ơi, ở đây thêm than", "엠어이 어 더이 템 탄", "숯좀 더 넣어주세요"));
                lifeList.add(new EntryItem("B", "vâng ạ", "벙 아", "네 알겠습니다"));
                lifeList.add(new SectionItem("계산시"));
                lifeList.add(new EntryItem("A", "em ơi tính tiền, Thanh toán ( Em ơi hóa đơn)", "엠어이 띵 띠엔", "계산요, 계산요 ( 영수증 주세요)"));
                lifeList.add(new EntryItem("A", "bao nhiêu tiền? ", "바오 니에유 띠엔", "얼마예요"));
                lifeList.add(new EntryItem("B", "350,000 VND", "바짬 남므응인", "350,000 동 입니다"));
                lifeList.add(new EntryItem("A", "đây. Anh cần hóa đơn đỏ", "더이, 아잉 껀 화던 도오", "여기요, 나는 세금 영수증이 필요합니다"));
                lifeList.add(new EntryItem("A", "ok, cảm ơn ", "깜언", "감사합니다"));
                break;
            case 2:
                lifeList.add(new EntryItem("상대", "베트남어", "발음", "의미"));
                lifeList.add(new SectionItem("가게에 옷을 살때"));
                lifeList.add(new EntryItem("B", "xin chào", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("A", "xin chào", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("B", "Anh tìm gì ạ?", "아잉 띰 지", "무엇을 찾으세요?"));
                lifeList.add(new EntryItem("A", "anh tìm áo ngắn tay (반팔 옷)", "아잉 띰 아오 응안 따이", "반팔옷을 찾고 있어요"));
                lifeList.add(new EntryItem("B", "đây có áo ngắn tay ạ", "더이 꼬 아오 응안 따이 아", "여기 반팔옷이 있습니다. "));
                lifeList.add(new EntryItem("A", "Ở đây có áo màu đỏ không?", "어 더이 꼬 아오 도 ", "이거 빨간색 있나요?"));
                lifeList.add(new EntryItem("B", "tôi sẽ tìm đã", "또이 쎄 띰 다", "찾아보겠습니다"));
                lifeList.add(new EntryItem("B", "cỡ anh như thế nào ?", "꺼어 아잉 느으 테 나오", "사이즈가 어떻게 되세요?"));
                lifeList.add(new EntryItem("A", "cỡ anh là 100 ", "꺼어 아잉 라 못짬", "100이면 됩니다"));
                lifeList.add(new EntryItem("B", "đây ạ", "더이 아", "여기 있습니다"));
                lifeList.add(new EntryItem("A", "Anh muốn mặc thử được không?", "아잉 무온 막트 드억 콩", "입어 봐도 될까요?"));
                lifeList.add(new EntryItem("A", "Có cỡ to hơn không?", "꼬 꺼 또 헌 콩", "좀 더 큰 사이즈 있나요?"));
                lifeList.add(new EntryItem("A", "cái này bao nhiêu tiền? ", "까이 나이 바오 니에유 띠엔", "얼마입니까"));
                lifeList.add(new EntryItem("B", "đây là 500,000VND", "더이 라 남짬 므응인 ", "50만동 입니다"));
                lifeList.add(new EntryItem("A", "OK, nhưng không cho giảm giá a ?", "OK 느응 콩 초 자암 자 아?", "깍아 주실수 있나요?"));
                lifeList.add(new EntryItem("A", "nếu giảm giá thì chúng tôi đến đây lại", "네우 자암 자 티 중또이 덴 더이 라이", "깍아 주면 다음에 또 오겠습니다"));
                lifeList.add(new EntryItem("B", "ok. Hay trả 450000 vnd nhé", "하이 자 본짬 남믕인 녜", "OK, 45만동 입니다"));
                lifeList.add(new EntryItem("A", "OK đây, cảm ơn", "OK 더이야, 깜언", "여기있습니다. 감사합니다"));
                footer = getLayoutInflater().inflate(R.layout.footer_life_list, null, false);
                tvPlus = footer.findViewById(R.id.tv_plus);
                tvPlus.setText("☞ 응용\n\n" +
                        "ống quần(바지), quần cụt(반바지), váy(치마),\n" +
                        "xống(스커트), áo khoác(잠바), màu xanh(파란색),\n" +
                        "màu đen(검은색), màu xám(회색), màu vàng(노란색)\n");
                lvLife.addFooterView(footer);
                break;
            case 3:
                lifeList.add(new EntryItem("상대", "베트남어", "발음", "의미"));
                lifeList.add(new SectionItem("밖에서 외식하러 갈때"));
                lifeList.add(new EntryItem("B", "Tối nay anh ấy muốn ăn gì ? ", "또이 나이 아잉어이 무온 안지", "오늘 저녁에 무엇을 드실래요"));
                lifeList.add(new EntryItem("A", "anh muốn đi nhà hàng ăn tối", "아잉 무온 디 냐 항 안 또이", "나는 식당에서 저녁 먹고 싶어요"));
                lifeList.add(new EntryItem("B", "ok. Tôi sẽ chuẩn bị đi ra", "오케이, 또이 세 주운비 디 자", "오케이 나갈준비 할께요"));
                lifeList.add(new SectionItem("집에서 요리 할때"));
                lifeList.add(new EntryItem("B", "anh muốn ăn tối gì ?", "아잉 무언 안 또이 지?", "오빠 뭘먹고 싶어요?"));
                lifeList.add(new EntryItem("A", "anh muốn ăn Tangsuyok", "아잉 무언 안 탕수육", "나는 탕수육을 먹고 싶어요"));
                lifeList.add(new EntryItem("B", "hôm qua tối đã mua sách về món ăn hàn quốc", "홈꾸어 또이 다 무어 싸익 베 몬안 한꾹", "어제 한국 음식 책을 샀어요"));
                lifeList.add(new EntryItem("B", "tôi biết phương pháp món ăn này", "또이 비엣 프엉팝 몬안 나이", "저는 이 음식의 요리법을 알고 있어요"));
                lifeList.add(new EntryItem("A", "Ok. Anh sẽ chờ em, anh đói quá", "ok 또이세 쳐 엠, 아잉 도이 꽈 ", "어. 기다릴께. 배고프다"));
                lifeList.add(new EntryItem("A", "mấy phút nữa em nấu xong ?", "머이 풋 느어 엠 노우  쏭?", "언제 요리 다되니?"));
                lifeList.add(new EntryItem("B", "10 phút nữa", "므어이 풋 아", "10분 이후에 "));
                lifeList.add(new EntryItem("A", "ok.", "오케이", "네"));

                lifeList.add(new SectionItem("음식을 시킬때"));
                lifeList.add(new EntryItem("A", "Alo", "알로", "여보세요"));
                lifeList.add(new EntryItem("B", "dạ, đây là nhà hàng hàn quốc CHOI", "자, 더이라 냐항 초이", "네 여기는 최식당입니다."));
                lifeList.add(new EntryItem("A", "anh muốn gọi món ăn, nhà hàng CHOI có vận chuyển món ăn được không?", "아잉무언 고이 몬안, 초이 꼬 츄엔 몬안 드억 콩?", "최식당에 음식좀 주문하려고 합니다. 배달되나요?"));
                lifeList.add(new EntryItem("B", "dạ được ạ, thì anh muốn gọi món ăn gì?", "자, 드억아, 티 아잉 무언 몬안 지?", "네 됩니다. 무엇을 주문하시겠습니까?"));
                lifeList.add(new EntryItem("A", "anh muốn 된장찌개 một và 김치찌개 1 một, 김밥 2, 떡복기 1", "아잉 무언 된장찌개 못, 김밥 하이 떡뽁기 못…", "저는 된장찌개1, 김치찌개 1, 김밥2, 떡복기 1…."));
                lifeList.add(new EntryItem("B", "tôi xin lỗi, tôi khó nghe rõ. Anh ơi nói lại ", "또이 씬로이, 또이 코 녜 조, 아잉어이 노이 라이", "죄송합니다. 잘못들었습니다. 다시 말씀해주세요"));
                lifeList.add(new EntryItem("A", "anh muốn 된장찌개 một và 김치찌개 1 một, 김밥 2, 떡뽁기 1", "아잉무언 된장찌개 못, 김밥 못…", "저는 된장찌개1, 김치찌개 1, 김밥…."));
                lifeList.add(new EntryItem("B", "OK. Tôi biết rồi. Cho tôi địa chỉ của anh", "오케이, 또이 비엣 조이, 초 또이 디야 치이 꾸어 아잉", "네 알겠습니다. 주소를 알려 주세요"));
                lifeList.add(new EntryItem("B", "số 1, Nguyen gia thieu(주소)khi em đến. Em sẽ gọi cho anh lại", "쏘못,응우엔 자티여우(주소), 키 엠덴, 엠 쎄 고이 초 아잉 라이", "응우엔 자티여우 쏘 못 (주소), 여기온후 전화주세요"));
                lifeList.add(new EntryItem("B", "số điện thoại của anh là 01X-0000-0000", "쏘 덴토아이 꾸어 아잉라 콩못0-xxxx-xxxx", "제 연락처는 01X-xxxx-xxxx"));
                lifeList.add(new EntryItem("B", " Tôi biết rồi", " 또이 비엣조이", "네 잘알겠습니다"));
                lifeList.add(new EntryItem("A", "mấy phút nữa em đến nhà anh?", "머이풋 느어, 엠  덴 냐 아잉?", "몇분후에 오나요?"));
                lifeList.add(new EntryItem("B", "30 phút a.", "바므어이 풋 아", "30분요"));
                lifeList.add(new EntryItem("A", "OK, cảm on, anh sẽ đợi.", "오케이 깜언, 아잉 세 더이", "네 기다릴께요"));

                lifeList.add(new SectionItem("배달와서"));
                lifeList.add(new EntryItem("B", "alo, anh ơi, tôi đến tới nơi rồi. ", "알로, 아잉어이 또이 덴 떠이 너이 조이", "여보세요.  여기 도착했습니다"));
                lifeList.add(new EntryItem("A", "Ok, anh sẽ đi ra bây giờ", "오케이 아잉 세 디 자 버이져", "네 지금 나갈께요"));
                lifeList.add(new EntryItem("A", "bao nhiêu tiền? ", "바오 니에우 띠엔", "얼마예요"));
                lifeList.add(new EntryItem("B", "250000 VND", "아이짬 남믕인", "250000 VND"));
                lifeList.add(new EntryItem("A", "đây", "더이", "여기요"));
                lifeList.add(new EntryItem("B", "đây, tiền thối lại 50000 VND", "더이, 띠엔 토이 라이 남믕인동", "네, 여기 잔돈요"));
                lifeList.add(new EntryItem("A", "OK. Cảm ơn", "오케이 깜언", "네 감사요"));
                footer = getLayoutInflater().inflate(R.layout.footer_life_list, null, false);
                tvPlus = footer.findViewById(R.id.tv_plus);
                tvPlus.setText("☞ 응용\n\n" +
                        "Một(못:1), Hai(하이:2), Ba(바:3), bốn(본:4),\n" +
                        "Năm(남:5), Sáu(사우:6), Bảy(버이:7), Tám(땀:8),\n" +
                        "Chín(찐:9), Mười(므어이:10)");
                lvLife.addFooterView(footer);
                break;
            case 4:
                lifeList.add(new EntryItem("상대", "베트남어", "발음", "의미"));
                lifeList.add(new SectionItem("아침 출근후"));
                lifeList.add(new EntryItem("A", "Em ơi, cho anh 1  cà phê nhé", "엠어이, 초 아잉 못 까이 까페 네", "커피 한잔 주세요"));
                lifeList.add(new EntryItem("B", "vâng a, đây ạ", "벙아, 더이야", "네엡"));
                lifeList.add(new SectionItem("미팅할때"));
                lifeList.add(new EntryItem("A", "Sếp sẽ họp với các bộ phận lúc 14:00", "셉 세 헙 버이 깍 보판 룩 므어이 본져", "2시에 전 부서 미팅할거다"));
                lifeList.add(new EntryItem("A", "Em thông báo cho các bộ phận", "엠 통바오 저 깍 보펀", "각 부서에 전달해라"));
                lifeList.add(new EntryItem("B", "vâng ạ, Em sẽ thông báo a", "벙아, 엠세 통바오 아", "네, 통보하겠습니다"));
                lifeList.add(new SectionItem("잔업을 해야할때"));
                lifeList.add(new EntryItem("A", "hôm nay mọi người làm tăng ca. ", "홈나이 모이 응어이 람 땅까", "오늘 전체 잔업한다"));
                lifeList.add(new EntryItem("A", "Vì chúng ta có nhiều việc hôm nay", "비 중따 꼬 니에우 비역", "왜냐하면 오늘 일이 많기 때문이다"));
                lifeList.add(new EntryItem("B", "vâng a, tôi sẽ họp với mọi người", "벙아, 또이세 헙 버이 모이 응어이", "네 작업자들과 협의하겠습니다"));
                lifeList.add(new SectionItem("지시한것을 하지 않았을때"));
                lifeList.add(new EntryItem("A", "Em ơi, em lại đây", "엠어이, 라이더이", "엠어이 여기로 와봐라"));
                lifeList.add(new EntryItem("B", "vâng a,", "벙아", "네"));
                lifeList.add(new EntryItem("A", "Tại sao em không làm theo chỉ thị của anh?", "따이 싸오 엠 콩 람 테오 치티 꾸어 아잉", "왜 내 지시대로 하지 않았냐"));
                lifeList.add(new EntryItem("A", "lý do là gì?", "리조 라지?", "이유가 뭐냐"));
                lifeList.add(new EntryItem("A", "không có thời gian a ?", "콩꼬 터이 잔 아?", "시간이 없냐"));
                lifeList.add(new EntryItem("A", "tại sao không báo cáo  cho sếp?", "따이 싸오 콩 바오 카오 쵸 셉?", "왜 나한테 보고를 하지 않냐?"));
                lifeList.add(new EntryItem("A", "bây giờ em làm lại,  khi em xong việc, em báo cáo cho sếp lại", "버이져 엠 람라이, 키 엠 쏭 비역, 엠 바오카오 라이", "지금 다시 시작해서 끝내고 다시 보고해라"));
                lifeList.add(new EntryItem("B", "vâng ạ", "벙아", "네"));
                lifeList.add(new SectionItem("퇴근할때"));
                lifeList.add(new EntryItem("A", "Mọi người tan làm nhé", "모이응어이 딴 람 녜", "이제 그만하고 퇴근합시다"));
                lifeList.add(new EntryItem("B", "vâng a,", "벙아", "네"));
                lifeList.add(new SectionItem("회사 소개할때"));
                lifeList.add(new EntryItem("A", "xin chào", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("B", "xin chào", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("A", "tôi sẽ giới thiệu công ty mình", "또이 쎄 저이 티여우 꽁띠 밍", "우리 회사를 소개해드리겠습니다"));
                lifeList.add(new EntryItem("B", "vâng", "", "네"));
                lifeList.add(new EntryItem("A", "tôi kinh doanh lắp ráp cửa hàng bán thành phẩm", "또이 낑 조아잉 랍 짭 꾸어 항 반 타잉 펌", "저는 반제품 조립을 경영하고 있습니다"));
                lifeList.add(new EntryItem("A", "công ty mình tên là XXXX", "꽁띠 밍 뗀라 XXXX", "저희 회사 이름은 XXX  입니다"));
                lifeList.add(new EntryItem("A", "công ty mình thành lập vào năm 2015", "꽁띠 밍 타잉 럽 바오 남 2015", "저희 회사는 2015년에 설립되었습니다"));
                lifeList.add(new EntryItem("A", "chúng tôi làm việc từ 08:00~17:00", "중 또이 람 비역 뜨 땀 져 덴 남 져", "저희 회사는 08:00~17:00까지 합니다"));
                lifeList.add(new EntryItem("A", "Nhưng nếu chúng tôi có nhiều việc, chúng tôi cũng làm đêm", "능 네우 중또이 꼬 니에유 비역, 넨 중또 꿍 람 뎀", "그런데 만약 회사에 일이 많으면 야간도 합니다"));
                lifeList.add(new EntryItem("A", "chúng tôi có tổng cộng 50 nhân viên", "중 또이 꼬 똥 꽁 남므어이 연 비엔", "저희는 총 50명의 사원이 있습니다"));
                lifeList.add(new EntryItem("A", "công ty chúng tôi có 6 đối tác(khách hàng)", "꽁띠 중 또이 꼬 싸우 도이딱(카익항)", "저희는 6군데 거래처(고객사)가 있습니다."));
                lifeList.add(new EntryItem("A", "sản phẩm của chúng tôi là XXX", "산펌 꾸어 중또이라 XXXX", "저희  생산품은 XXX 입니다"));
                lifeList.add(new EntryItem("A", "sức chứa của chúng tôi là 20000ea vào tháng", "슥 츠어 꾸어 중 또이 라 하이므응인 바오 탕", "저희 케파는 월 20000ea 입니다"));
                lifeList.add(new EntryItem("B", "Trong tòa nhà này có căng tin không? ", "쩡 토아 냐 나이 꼬 깡 띤 콩", "회사내에 식당이 있나요?"));
                lifeList.add(new EntryItem("A", "có căng tin, nhưng món ăn không phù hợp cho người hàn", "꼬 깡 띤, 능 몬안 콩 푸헙 초 응어이 한", "네 있습니다만, 한국인 입에는 맞지 않습니다"));
                footer = getLayoutInflater().inflate(R.layout.footer_life_list, null, false);
                tvPlus = footer.findViewById(R.id.tv_plus);
                tvPlus.setText("☞ 응용\n\n" +
                        "Quản lý(관리), chú ý(주의), sản xuất(생산),\n" +
                        "Sản lượng(생산수량), kế hoạch(계획), chất lượng(품질),\n" +
                        "kinh doanh(영업), kỹ thuật(기술),\n" +
                        "phát triển(개발)\n");
                lvLife.addFooterView(footer);
                break;
            case 5:
                lifeList.add(new EntryItem("상대", "베트남어", "발음", "의미"));
                lifeList.add(new SectionItem("방을 예약 할때"));
                lifeList.add(new EntryItem("A", "Alo", "알로", "여보세요"));
                lifeList.add(new EntryItem("B", "tôi muốn đặt phòng bên khách sạn anh", "또이  무온 닷퐁 벤 카익산 아잉", "나는 당신네 호텔에 예약을 하고 싶습니다"));
                lifeList.add(new EntryItem("A", "anh muốn đặt phòng mấy phòng ạ ?", "아잉 무온 닷 퐁 머이 퐁 아?", "당신은 몇 개의 룸을 예약하고 싶으신가요"));
                lifeList.add(new EntryItem("B", "Tôi muốn 2 phòng ", "또이 무온 하이 퐁", "나는 방 2개를 원합니다"));
                lifeList.add(new EntryItem("A", "vâng ạ, tôi sẽ kiểm tra trước", "벙아, 또이세 끼엠차 쯔억", "네, 먼저 확인해 보겠습니다"));
                lifeList.add(new EntryItem("A", "chúng tôi hết phòng rồi", "중또이 헷 퐁 조이", "저희 호텔 방은 다 나갔습니다"));
                lifeList.add(new EntryItem("A", "chúng tôi có 2 phòng ngủ, nhưng 1 phòng là nhỏ, 1 phòng là to", "중도이 꼬 하이 퐁 무, 능 못 퐁 뇨, 못 퐁 또", "2개 방이 있는데 한방은 작습니다"));
                lifeList.add(new EntryItem("A", "anh có sao không ?", "아잉 꼬 싸오 콩", "괜찮습니까"));
                lifeList.add(new EntryItem("B", "Không sao", "콩사오", "네 괜찮습니다"));
                lifeList.add(new EntryItem("B", "1 phòng bao nhiêu tiền? ", "못 퐁라 바이뉴 띠엔", "방하나에 얼마인가요"));
                lifeList.add(new EntryItem("A", "1 phòng là năm trăm nghìn, phòng nhỏ là bốn trăm nghìn ạ", "못 퐁 라 남짬믕인, 퐁 뇨 라 본짬믕인아", "한방은 50만동, 작은방은 40만동입니다"));
                lifeList.add(new EntryItem("A", "anh có đặt phòng không ?", "아잉 꼬 닷 퐁 콩", "예약 하시겠습니까"));
                lifeList.add(new EntryItem("B", "giảm giá được không ?", "잠 자아 드억 콩", "좀  가격을  깍아주실수 있나요"));
                lifeList.add(new EntryItem("A", "không được giảm giá. ", "콩 드억 잠 자", "가격을 깍아 줄수 없습니다"));
                lifeList.add(new EntryItem("A", "Nếu muốn nói chuyện với giám đốc  thì tôi sẽ báo giám đốc", "네우 무온 노이쭈엔 버이 짬독 티 또이세 바오 짬독", "만약 사장님하고 연락하고 싶다면 말하겠습니다"));
                lifeList.add(new EntryItem("B", "OK cảm ơn", "오케이 깜언", "네 감사합니다"));
                lifeList.add(new EntryItem("A", "vâng ạ, không có gì ạ", "벙아, 꽁꼬지아", "네 괜찮습니다"));
                break;
            case 6:
                lifeList.add(new EntryItem("상대", "베트남어", "발음", "의미"));
                lifeList.add(new SectionItem("택시 탈때"));
                lifeList.add(new EntryItem("B", "xin chào", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("A", "xin chào", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("B", "anh muốn đi đâu? ", "아잉 무언 디 더우?", "어디로 가시겠습니까?"));
                lifeList.add(new EntryItem("A", "tôi muốn đi Nhà hàng CHOI", "또이 무언 디 냐항 초이", "저는 최식당으로 가고 싶습니다"));
                lifeList.add(new EntryItem("A", "tôi có địa chỉ. Anh ơi xem nay", "또이 꼬 디아치. 아잉 쌤 나이", "주소가 있습니다. 여기보세요"));
                lifeList.add(new EntryItem("B", "tôi biết rồi. ", "또이 비엣조이", "제가 압니다"));
                lifeList.add(new EntryItem("A", "tôi không biết", "또이 콩 비엣", "저는 모릅니다. "));
                lifeList.add(new EntryItem("A", "Anh mở máy tìm đường", "아잉 머 마이 띰 드엉", "네비게이션을 켜주세요"));
                lifeList.add(new EntryItem("A", "Anh biết rồi thì khởi hành", "아잉 비엣 티 커이 하잉", "당신이 알고 있으면 출발하세요"));
                lifeList.add(new EntryItem("A", "tôi biết đường. Đi đi", "또이 비엣 드엉", "제가 길을 아니 갑시다"));
                lifeList.add(new EntryItem("B", "ok.", "오케이", "오케이"));
                lifeList.add(new EntryItem("A", "rẻ trái", "제차이", "왼쪽으로 돌아주세요"));
                lifeList.add(new EntryItem("A", "rẻ phải", "제파이", "오른쪽으로 돌아주세요"));
                lifeList.add(new EntryItem("A", "đi thẳng", "디 탕", "직진해주세요"));
                lifeList.add(new EntryItem("A", "đây, dừng lại", "더이, 증라이", "여기입니다. 세워주세요"));
                lifeList.add(new EntryItem("B", "25,000 VND", "하이남응인 동", "25,000 동입니다"));
                lifeList.add(new EntryItem("A", "đây 30,000 VND, không cần trả lại tiền lẻ", "더이 바믕인 동, 콩껀 짜 라이 띠엔 레", "30,000동 요. 거스름돈은 필요없습니다"));
                lifeList.add(new EntryItem("B", "cảm ơn", "깜언", "감사합니다"));
                lifeList.add(new EntryItem("A", "không có gì", "꽁꼬지", "별말씀을요"));
                break;
            case 7:
                lifeList.add(new EntryItem("상대", "베트남어", "발음", "의미"));
                lifeList.add(new SectionItem("아플때"));
                lifeList.add(new EntryItem("A", "Alo, tôi đau bụng lắm rồi", "알로, 또이 다우붕 람 조이", "여보세요. 저는 배가 심하게 아파요"));
                lifeList.add(new EntryItem("A", "xin hãy gọi xe cấp cứu", "씬 하이 고이 쎄 껍 끄우", "구급차를 좀 불러주세요"));
                lifeList.add(new EntryItem("B", "ah OK. Tôi sẽ gọi cho", "아 오케이 또이 쎄 고이", "네 감사합니다"));
                lifeList.add(new SectionItem("사고가 났을때"));
                lifeList.add(new EntryItem("A", "Alo, tôi bị tai nạn giao thông rồi", "알로, 또이 비 따이난 자오통 조이", "여보세요. 저는 교통사고가 났습니다"));
                lifeList.add(new EntryItem("A", "xin hãy gọi người này hay gọi công ty bảo hiểm ", "씬 하이 고이 응어이 나이 하이 고이 꽁띠 바오휘염", "이사람에게(또는 보험회사) 연락좀 해주시겠어요,"));
                lifeList.add(new EntryItem("B", "ah OK. Tôi sẽ gọi cho", "아 오케이 또이 쎄 고이", "네 불러 드릴께요"));
                lifeList.add(new EntryItem("A", "vâng, cám ơn", "", "네 감사합니다"));
                lifeList.add(new SectionItem("도난, 분실 사고가 났을때"));
                lifeList.add(new EntryItem("A", "Alo, Ví của tôi bị trộm, tôi phải làm thế nào ?", "알로, 비 꾸어 또이 비 쫌, 또이 파이 람 테 나오", "지갑을 도난 당했어요. 어떻하죠"));
                lifeList.add(new EntryItem("B", "hay gọi cảnh sát hoặc công an", "하이 고이 까잉삿 호악 꽁안", "경찰이나 공안에 연락주세요"));
                lifeList.add(new EntryItem("A", "vâng, cám ơn", "벙 깜언", "네 감사합니다"));
                footer = getLayoutInflater().inflate(R.layout.footer_life_list, null, false);
                tvPlus = footer.findViewById(R.id.tv_plus);
                tvPlus.setText("☞  해외 여행 주의 사항\n\n" +
                        "1. 외국에 거주 또는 여행시에는 반드시 현지말이\n" +
                        "   되는 사람을 알고 있어야 하며\n" +
                        "   비상시 연락할수 있는 사람이\n" +
                        "   있어야 한다.\n" +
                        "2. 회사경우에는 통역 담당자,\n" +
                        "   여행자 경우 현지말을 할수 있는 사람을\n" +
                        "   알고 있어야 한다.\n" +
                        "3. 만약 현지에 아는 사람이 없는경우\n" +
                        "   한국에 가족들이라도 바로 연락되도록\n" +
                        "   사전에 지정을 해둔다.\n" +
                        "4. số điện thoại khẩn cấp\n" +
                        "   công an việt nam (베트남 경찰) : 113\n" +
                        "   số điện thoại báo cháy (화재) : 114\n" +
                        "   số điện thoại cấp cứu khẩn cấp (병원) : 115");
                lvLife.addFooterView(footer);
                break;
            case 8:
                lifeList.add(new EntryItem("상대", "베트남어", "발음", "의미"));
                lifeList.add(new SectionItem("티켓팅"));
                lifeList.add(new EntryItem("A", "xin chào", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("B", "xin chào", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("A", "đây là hộ chiếu của tôi và vé máy bay", "더이 라 호치여우 꾸어 또이 바 베 마이바이", "여기 여권하고 비행기표 입니다"));
                lifeList.add(new EntryItem("B", "nâng lên túi xách trên băng tải ", "낭렌 뚜이 싹 쩬 방 따이", "컨베이어에 가방을 올려주세요"));
                lifeList.add(new EntryItem("A", "đây", "더이", "여기요"));
                lifeList.add(new EntryItem("B", "anh có muốn gửi túi xách cho hàn quốc ?", "아잉 꼬 무언 그이 뚜이 싹 초 한꾹?", "가방을 한국으로 보내면 되나요?"));
                lifeList.add(new EntryItem("B", "hay anh muốn nhận túi xách ở bangkok?", "하이 하잉 무언 연 뚜이 싹 어 방콕?", "아니면 가방을 방콕으로 보내면 되나요?"));
                lifeList.add(new EntryItem("A", "anh muốn gửi túi xách cho hàn quốc", "아잉 무언 그이 뚜이 쵸 한꾹", "저는 한국으로 가방을 보내고 싶습니다"));
                lifeList.add(new EntryItem("B", "anh có thể gửi 1 túi xách", "하잉 꼬테 그이 못 뚜이 싹", "당신은 1개 가방만 보낼수가 있습니다"));
                lifeList.add(new EntryItem(" A", "tôi biết rồi", "또이 비엣조이", "저도 압니다."));
                lifeList.add(new EntryItem("B", "đây là vé máy bay của anh.", "더이라 베 마이 바이 꾸어 아잉", "여기  당신의 비행기표입니다"));
                lifeList.add(new EntryItem("A", "anh quên, Hãy tích điểm chuyến bay cho anh", "아잉 꾸엔, 하이 틱 디엠 츄엔 바이 초 아잉", "아 깜빡했습니다. 마일리지 입력해주세요"));
                lifeList.add(new EntryItem("A", "ok, cảm ơn ", "오케이 깜언", "네 감사합니다"));
                lifeList.add(new SectionItem("가방을 분실했을때"));
                lifeList.add(new EntryItem("A", "xin chào", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("B", "xin chào", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("A", "Tôi suy nghĩ mất túi xách.", "또이 쉬이니 멋 뚜이 싹", "가방을 분실한것 같아요"));
                lifeList.add(new EntryItem("A", "tôi không tìm thấy túi xách", "또이 콩 띰 터이 뚜이 싹", "가방을 찾을수가 없습니다"));
                lifeList.add(new EntryItem("B", "số máy bay là bao nhiêu? ", "쏘 마이 바이 라 바우 니에우", "비행기 편명이 몇번인가요?"));
                lifeList.add(new EntryItem("A", "tôi đã lên máy bay số VN417", "또이 다 렌 마이 바이 쏘  베너 본못바이", "VN417을 탔습니다."));
                lifeList.add(new EntryItem("B", "rồi. Chờ 1 chút. Tôi xác nhận đã", "조이. 처 못주웃, 또이 싹 년 다", "알겠습니다. 확인할테니 잠시만기다려주세요"));
                lifeList.add(new EntryItem("A", "ok", "오케이", "오케이"));
                lifeList.add(new EntryItem("B", "đi băng tải số 4. chờ đến 10 phút..", "디 방 따이 쏘 본, 쳐 므어이풋, ", "4번 짐 컨베이어로 가서 10분 정도 기다려 보세요"));
                lifeList.add(new EntryItem("B", " Nếu không thấy thì anh đến đây lại", "네우 콩 터이 티 아잉 덴 더이 라이", "만약 가방을 못 찾을 경우 여기로 다시오세요"));
                lifeList.add(new EntryItem("B", "Chúng tôi sẽ tìm và bảo quản ở đây.", "중 또이 쎄 띰 바 바오 관 어 더이", "우리가 찾아서 여기에 보관하고 있습니다"));
                lifeList.add(new EntryItem("A", "OK cảm ơn", "오케이 깜언", "네 감사합니다"));
                lifeList.add(new SectionItem("비행기를 놓쳤을때"));
                lifeList.add(new EntryItem("A", "xin chào. Tôi đến sân bay muộn rồi. Bây giờ  Tôi có thể đi vào nơi xuất cảnh được không?", "씬자오, 또이 덴 썬바이 무온 조이, 버이져 또이 꼬테 바오 너이 쏫 까잉 드억 콩", "저는 공항에 좀 늦었는데 지금 출국장으로들어갈수 있나요"));
                lifeList.add(new EntryItem("B", "ah không được a", "콩 드억", "안됩니다"));
                lifeList.add(new EntryItem("A", "thì mấy giờ có máy báy đi hàn quốc ?", "티 머이저 꼬 마이바이 디 한꾹", "그럼 몇시에 한국 가는 비행기가 있나요"));
                lifeList.add(new EntryItem("B", "tôi sẽ xem trước đã", "또이 쎄 샘 쯔억 다", "먼저 확인해보겠습니다"));
                lifeList.add(new EntryItem("B", "khoảng 2 tiếng sau có máy bay đi hàn quốc", "쾅 하이 띠잉 싸우 꼬 마이바이 디 한국", "2시간후 한국가는 비행기가 있습니다"));
                lifeList.add(new EntryItem("A", "OK, nếu anh muốn thay đổi vé thì ở đâu thay đổi được ạ?", "OK. 네우 아잉 무언 타이 도이 베 티 어더우 타이 도이 드억 아", "네 표를 바꿀려면 어디로 가야 할까요"));
                lifeList.add(new EntryItem("B", "bên kia (hãy đi văn phòng máy bay )", "벤끼어( 하이 디 반퐁 마이바이)", "저기요 ( 공항 사무실로 가보세요)"));
                lifeList.add(new SectionItem("티켓을 변경 할때"));
                lifeList.add(new EntryItem("A", "xin chào, đây là văn phòng máy bay đúng không?", "씬짜오 더이 라 반퐁 마이 바이 둥 콩", "안녕하세요. 여기가 공항 사무실이 맞나요?"));
                lifeList.add(new EntryItem("A", "tôi muộn giờ máy bay rồi. tôi muốn đổi thời gian vé máy bay", "또이무언 져마이 바이 조이. 또이 무언 도이 터이 지안 베 마이바이", "저는 비행기를 놓쳤습니다. 비행기 표를 바꿀수 있나요?"));
                lifeList.add(new EntryItem("B", " vâng, cho tôi vé cũ, tôi sẽ tìm thời gian máy bay đi hàn quốc ( 도착지 명)", "벙, 조 또이 베 꾸, 또이 세  띰 터이 지안 마이 바이 디 한꾹", "네 기존 비행기표를 주세요. 한국행 비행기를 찾아보겠습니다"));
                lifeList.add(new EntryItem("B", " tôi tìm được rồi. Lúc 22:00 có máy bay đi hàn quốc", "또이 띰드억 조이. 룩 하이하이 져 꼬 마이 바이 디 한꾹", "네  찾았습니다. 22시에 한국행 비행기가 있네요."));
                lifeList.add(new EntryItem("B", "Phải nộp thêm phí phụ thu. Phụ phí là 1 triệu ", "파이 놉 템 피 푸 투. 푸 피 라 못 찌에우", "추가 요금 100만동 을 내셔야 합니다"));
                lifeList.add(new EntryItem("A", "Vâng, đây 1 triệu ạ", "벙 더이 못 찌에우 아", "100만동 여기요"));
                lifeList.add(new EntryItem("A", "Vâng, cảm ơn", "벙 깜언", "네 감사합니다"));
                break;
            case 9:
                lifeList.add(new EntryItem("상대", "베트남어", "발음", "의미"));
                lifeList.add(new SectionItem("베트남 여성을 만났을때 기본 대화 방법"));
                lifeList.add(new EntryItem("B", "xin chào ", "씬짜오", "안녕하세요"));
                lifeList.add(new EntryItem("A", "rất vui được gặp em", "젓부이 드억 갑 엠", "만나서 반가워요"));
                lifeList.add(new EntryItem("B", "em cũng vậy", "엠 꿍 버이", "나도 그래요"));
                lifeList.add(new EntryItem("A", "em tên là gì ?", "엠 땐 라지", "이름이 뭐예요?"));
                lifeList.add(new EntryItem("B", "em tên là XXXX", "엠 땐 라 XXX", "제이름은 XXX 입니다"));
                lifeList.add(new EntryItem("A", "ah. Tên anh là XXXX", "땐 아잉 라 XXX", "오빠 이름은 XXX 입니다"));
                lifeList.add(new EntryItem("A", "em bao nhiêu tuổi ?", "엠 바오 니에유 뚜어이?", "너는 몇살이예요?"));
                lifeList.add(new EntryItem("B", "em 24 tuổi ( sinh năm 1991)", "엠 하이뜨 뚜어이", "전 몇살입니다( 19XX 년생 입니다)"));
                lifeList.add(new EntryItem("A", "em sống ở đâu?", "엠 쏭 어더우", "너는 어디 사니?"));
                lifeList.add(new EntryItem("B", "em sống ở thành phố bac ninh", "엠 쏭 어 타잉포 박닌", "저는 박닌에 살아요"));
                lifeList.add(new EntryItem("B", "con anh?", "꼰 아잉", "그러는 당신은요?"));
                lifeList.add(new EntryItem("A", "anh cũng sống ở bac ninh.", "아잉 꿍 쏭 어 박닌", "나도 박닌 살아요"));
                lifeList.add(new EntryItem("A", "em chỗ nào ở  bac ninh a ?", "엠 초나오 어 박닌 ?", "박닌 어디사세요?"));
                lifeList.add(new EntryItem("B", "em ở Yna bac ninh", "엠 어 이나", "박닌 인아 삽니다"));
                lifeList.add(new EntryItem("A", "em sống với ai? ", "엠 쏭 버이 아이", "누구랑 사세요?"));
                lifeList.add(new EntryItem("B", "em sống với bạn ( or gia đình)", "엠 쏭 버이 반", "친구랑 삽니다( 가족과 삽니다)"));
                lifeList.add(new EntryItem("A", "em cao bao nhiêu? ", "엠 까오 바오니에유", "키는 몇이예요?"));
                lifeList.add(new EntryItem("B", "em cao 1 mét 60", "엠 까오 못멧 사우므어이", "저는 165 입니다"));
                lifeList.add(new EntryItem("A", "anh là 1 mét 75", "아잉 라 못멧 버이남", "저는 175 입니다"));
                lifeList.add(new EntryItem("A", "cân nặng em bao nhiêu?", "껀 낭 엠 바오니에우", "너는 몸무게가 몇 kg 이니?"));
                lifeList.add(new EntryItem("B", "48 kg ạ", "본 땀 키로그람", "48kg 입니다"));
                lifeList.add(new EntryItem("A", "em làm nghề gì ?", "엠 람얘 지?", "무슨일해요?"));
                lifeList.add(new EntryItem("B", "em làm ở công ty ( or karaoke )", "엠 람 어 꽁띠", "저는 공장 다녀요"));
                lifeList.add(new EntryItem("A", "em ở bộ phận nào a ?", "엠 어 보판 나오", "무슨부서예요?"));
                lifeList.add(new EntryItem("B", "Em làm ở bộ phận sản xuất ( or người kiểm tra)", "엠 람 어 보판 산솟", "생산부서 입니다"));
                break;
        }
    }

    public void initAdmob()
    {
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        mBannerAd = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mBannerAd.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3286891742591316/2671282606");
        //Using : ca-app-pub-3286891742591316/2671282606
        //Test : ca-app-pub-3940256099942544/1033173712
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mBannerAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                // Code to be executed when an ad finishes loading.
                // 광고가 문제 없이 로드시 출력됩니다.
                Log.d("@@@", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode)
            {
                // Code to be executed when an ad request fails.
                // 광고 로드에 문제가 있을시 출력됩니다.
                Log.d("@@@", "onAdFailedToLoad " + errorCode);
            }

            @Override
            public void onAdOpened()
            {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked()
            {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication()
            {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed()
            {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                // Code to be executed when an ad finishes loading.
                // 광고가 문제 없이 로드시 출력됩니다.
                Log.d("@@@", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode)
            {
                // Code to be executed when an ad request fails.
                // 광고 로드에 문제가 있을시 출력됩니다.
                Log.d("@@@", "onAdFailedToLoad " + errorCode);
            }

            @Override
            public void onAdOpened()
            {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked()
            {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication()
            {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed()
            {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        MainActivity.BackKeyCnt++;

        if (MainActivity.BackKeyCnt == 5)
        {
            MainActivity.BackKeyCnt = 0;

            if (mInterstitialAd.isLoaded())
            {
                mInterstitialAd.show();
            }
            else
            {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }

        else
            finish();
    }
}
