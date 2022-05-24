package com.example.launchdinner;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter2 extends BaseAdapter {

    Context mContext;
    long id;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Board> listViewItemList = new ArrayList<Board>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter2() {

    }

    public ListViewAdapter2(Context context) {
        mContext = context;

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item2, parent, false);
        }

        LinearLayout cmdArea = (LinearLayout) convertView.findViewById(R.id.cmdArea2);
        cmdArea.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                id = listViewItemList.get(pos).getId();
                Log.d("onClick", String.valueOf(id)+"!!!!!!!!!!!!!!!!!");

                Intent intent = new Intent(v.getContext() , BoardDetailActivity.class);
                intent.putExtra("id", id);

                context.startActivity(intent);
                //거래 번호 - 2
//                String substr = listViewItemList.get(pos).getSeq().substring(8);
//                Toast.makeText(v.getContext(), substr, Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(v.getContext(), ApplyViewActivity.class);
//                intent.putExtra("seq", substr);

//                context.startActivity(intent);
            }
        });


        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        TextView descTextView1 = (TextView) convertView.findViewById(R.id.textView2) ;
        TextView descTextView2 = (TextView) convertView.findViewById(R.id.textView3) ;
        TextView descTextView3 = (TextView) convertView.findViewById(R.id.textView4) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        Board listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getTitle());
        descTextView1.setText(listViewItem.getComusermVO());
        descTextView2.setText(listViewItem.getLocalDateTime());
        descTextView3.setText(String.valueOf(listViewItem.getId()));

        return convertView;
    }



    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String desc1, String desc2, long id) {
        Board item = new Board();

//
        item.setTitle(title);
//        item.setContent(desc1);
        item.setLocalDateTime(desc1);
        item.setComusermVO(desc2);
        item.setId(id);
//
//
//        // 진행상태
//        if(desc6.equals(Character.toString('N'))){
//            item.setDelivery_yn("배달원 모집중");
//        } else if(desc6.equals(Character.toString('Y'))){
//            item.setDelivery_yn("배달완료");
//        } else if(desc6.equals(Character.toString('P'))){
//            item.setDelivery_yn("배달중");
//        }


        listViewItemList.add(item);
    }
}

//class NetworkTask extends AsyncTask<Void, Void, String> {
//
//    String url;
//    ContentValues values;
//
//    private String mTitle = null;
//
//    NetworkTask(String url){
//        this.url = url;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//        //progress bar를 보여주는 등등의 행위
//    }
//
//    @Override
//    protected String doInBackground(Void... params) {
//        String result;
//        RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
//        result = requestHttpURLConnection.request(url, values);
//        return result; // 결과가 여기에 담깁니다. 아래 onPostExecute()의 파라미터로 전달됩니다.
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        // 통신이 완료되면 호출됩니다.
//        // 결과에 따른 UI 수정 등은 여기서 합니다.
//    }
//}
