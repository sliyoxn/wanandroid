package com.example.sorena.wanandroidapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sorena.wanandroidapp.R;
import com.example.sorena.wanandroidapp.adapter.SystemItemBaseAdapter;
import com.example.sorena.wanandroidapp.bean.Chapter;
import com.example.sorena.wanandroidapp.bean.FlowItem;
import com.example.sorena.wanandroidapp.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.sorena.wanandroidapp.util.JSONUtil.getMapInArray;
import static com.example.sorena.wanandroidapp.util.JSONUtil.getValue;

public class SystemFragment extends BaseFragment {

    private ListView mSystemListViewShowItem;
    private SystemItemBaseAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView(){

        mSystemListViewShowItem = getActivity().findViewById(R.id.system_listView_showCharacterItem);
        mSystemListViewShowItem.setVisibility(View.GONE);
        getListData();
    }

    private void getListData(){

        HttpUtil.sendHttpRequest("https://www.wanandroid.com/tree/json", new HttpUtil.HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                String data = getValue("data",response,new String[]{});
                Map<String,List<String>> childMap = getMapInArray(data,new String[]{"name","children"});
                List<String> childrenFirst =  childMap.get("children");
                List<String> name = childMap.get("name");
                Map<String,List<String>> dataMap;
                List<Chapter> chapters = new ArrayList<>();
                for (int i = 0; i < name.size(); i++) {
                    dataMap = getMapInArray(childrenFirst.get(i),new String[]{"id","name"});
                    List<String> idList = dataMap.get("id");
                    List<String> nameList = dataMap.get("name");
                    Chapter chapter = new Chapter();
                    chapter.setChapterName(name.get(i));
                    List<FlowItem> itemList = new ArrayList<>();
                    for (int j = 0; j < idList.size(); j++){
                        FlowItem flowItem = new FlowItem();
                        flowItem.setId(Integer.parseInt(idList.get(j)));
                        flowItem.setName(nameList.get(j));
                        flowItem.setParentsName(name.get(i));
                        itemList.add(flowItem);
                    }
                    chapter.setFlowItems(itemList);
                    chapters.add(chapter);
                }
                initListViewData(chapters);

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }


    private void initListViewData(final List<Chapter> chapters)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter = new SystemItemBaseAdapter(getActivity(),R.layout.system_item, chapters);
                mSystemListViewShowItem.setAdapter(mAdapter);
                mSystemListViewShowItem.setVisibility(View.VISIBLE);
            }
        });
    }

}

