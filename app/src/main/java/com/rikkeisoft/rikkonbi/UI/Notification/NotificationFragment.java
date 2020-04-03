package com.rikkeisoft.rikkonbi.UI.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rikkeisoft.rikkonbi.Adapter.NotificationAdapter.NotificationAdapter;
import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Extension.RecycleViewDisabler;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.NotificationModel.Notification;
import com.rikkeisoft.rikkonbi.NetworkReceiver.Application.MyApplication;
import com.rikkeisoft.rikkonbi.NetworkReceiver.ConnectivityReceiver;
import com.rikkeisoft.rikkonbi.PreferencesProvider.PreferencesProvider;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;
import com.rikkeisoft.rikkonbi.Presenter.PresenterCheckWiFi.PresenterCheckWiFi;
import com.rikkeisoft.rikkonbi.Presenter.PresenterNotification.PresenterNotification;
import com.rikkeisoft.rikkonbi.R;
import com.rikkeisoft.rikkonbi.SQLite.myDBHelper;
import com.rikkeisoft.rikkonbi.UI.CheckInternet.CheckInternetFragment;
import com.rikkeisoft.rikkonbi.UI.MainPage.MainPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment implements MainActivityPresenter.View.MessageNotificationView
        ,MainActivityPresenter.View.ConnectedWifi,View.OnClickListener
{
    private static final String TAG = "NotificationFragment";
    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private APIInterface mApiInterface;
    private static PresenterNotification mPresenterNotification;
    private String mUserId;
    private BottomSheetListener mListener;
    private String mTokenId;
    private NotificationAdapter mNotificationAdapter;
    private List<String> mNotiList;
    private PresenterCheckWiFi mCheckWiFi;
    private TextView mNoItem;
    private String mAccount;
    private static List<String> mListToken;
    private boolean isLoading = false;
    private int pageIndex = 1;
    public ProgressBar mProgressBar;
    private List<Notification> mNotification;
    private myDBHelper helper;
    private int mCheck = 0;
    private Button mBtnWatch;
    private Button mBtnRemove;
    private Button mBtnCancel;
    public boolean aBoolean = false;
    private BlurView blurView;
    private LinearLayout mLinear;
    private RelativeLayout mRela;
    private boolean mIsCheckInternet;
    private String mID;
    private RecyclerView.OnItemTouchListener disabler = new RecycleViewDisabler();

    public interface BottomSheetListener {
        void onButtonClicked(int id, String command);
    }

    public static NotificationFragment getInstance()
    {
        return new NotificationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new myDBHelper(getContext());


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notification, container, false);

        mBtnWatch = (Button) root.findViewById(R.id.btn_watch);
        mBtnWatch.setOnClickListener(this);
        mBtnRemove = (Button) root.findViewById(R.id.btn_remove);
        mBtnRemove.setOnClickListener(this);
        mBtnCancel = (Button) root.findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);

        mLinear = (LinearLayout) root.findViewById(R.id.Linear_setting);
        blurView = (BlurView) root.findViewById(R.id.blurView);
        mRela = (RelativeLayout) root.findViewById(R.id.relation_blur);
        mLinear.setVisibility(View.GONE);
        mRela.setVisibility(View.GONE);


        mApiInterface = ApiUtils.getTokenAccess();

        mAccount = PreferencesProvider.getInstance(getContext()).getCommitLogin(getContext());


        mPresenterNotification = new PresenterNotification(this);

        mProgressBar = (ProgressBar) root.findViewById(R.id.main_progress);

        mCheckWiFi = new PresenterCheckWiFi(this);
        mCheckWiFi.CheckWiFiBackground();




        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycle_message);
        mRecyclerView.removeOnItemTouchListener(disabler);


        mNoItem = (TextView) root.findViewById(R.id.text_no_item);
        mNoItem.setVisibility(View.INVISIBLE);




        mListToken = new ArrayList<>();

        mNotiList = new ArrayList<>();


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = mLinearLayoutManager.getChildCount();
                int totalItemCount = mLinearLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading)
                {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0)
                    {
                        isLoading = true;

                        pageIndex++;

                        getNotificationInfo(mTokenId,pageIndex);

                    }
                }
            }
        });

        updateUI();
        setVisibelView(aBoolean);
        BlurBackground();


        mNotificationAdapter = new NotificationAdapter(this,mNotiList);
        mLinearLayoutManager =new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mNotificationAdapter);



        //interface Mainpage
        ((MainPage) getActivity()).cPage("Notification");
        ((MainPage) getActivity()).Progress("Update");
        ((MainPage) getActivity()).RefreshUI(new MainPage.ListenerMethod() {
            @Override
            public void RefreshNotification(String token) {
                mCheck = 1;
                updateUI();
                // Log.d(TAG,String.valueOf(pageIndex));

            }
        });


        return root;

    }

    public void mID(String mID){
        this.mID = mID;
    }
    public void setVisibelView(boolean aBoolean){
        if(aBoolean){
            mRela.setVisibility(View.VISIBLE);
            mLinear.setVisibility(View.VISIBLE);
            mRecyclerView.addOnItemTouchListener(disabler);
        }
        else{
            mLinear.setVisibility(View.GONE);
            mRela.setVisibility(View.GONE);
            mRecyclerView.removeOnItemTouchListener(disabler);
        }

    }

    public void BlurBackground(){
        float radius = 1f;

        View decorView = getActivity().getWindow().getDecorView();
        //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //Set drawable to draw in the beginning of each blurred frame (Optional).
        //Can be used in case your layout has a lot of transparent space and your content
        //gets kinda lost after after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(getContext()))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);
    }



    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pageIndex = 1;
            mCheck = 0;
            updateUI();
        }
    };



    @Override
    public void onStart() {
        super.onStart();
        pageIndex = 1;
        mCheck = 0;
        helper.deleteAll();
        setVisibelView(aBoolean);
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            MyApplication.getInstance().setConnectivityListener(mCheckWiFi);
            getActivity().registerReceiver(broadcastReceiver,new IntentFilter("NEW_MASSAGE"));


        }catch (Exception e){
            //already registered
        }

    }

    @Override
    public void setDataNotification(List<String> notification) {
        mNotiList.clear();
        mNotiList.addAll(notification);

        if (notification.size() >= 1) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoItem.setVisibility(View.INVISIBLE);
            isLoading = false;
            mNotificationAdapter.notifyDataSetChanged();
        }else{
            mNotificationAdapter.notifyDataSetChanged();
            mNoItem.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);

        }
    }



    @Override
    public void showProgress(String message) {

    }


    @Override
    public void showMessage(String message) {

    }

    public void updateUI(){
        mListToken.clear();
        String user_info = String.valueOf(PreferencesProvider.getsInstanceToken(getContext()).getTokenBearer(getContext()));
        final JSONObject jobject;
        try {
            jobject = new JSONObject(user_info);
            String token = jobject.getString("token");
            String id = jobject.getString("id");

            this.mUserId = id;
            this.mTokenId = getResources().getString(R.string.bearer)+token;

            mListToken.add(mTokenId);


            if(mCheck == 0) {
                getNotificationInfo(mTokenId, pageIndex);
            }else {
                if (helper.countRows() >= 1) {
                    mPresenterNotification.OnShowNotification(helper.getData(), pageIndex);
                }
                else{
                    mNoItem.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                }
            }

            mCheckWiFi.CheckWiFiBackground();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void OnClickItem(String position) {
    }
    @Override
    public void RefreshRecycle(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    @Override
    public void showConnectedWifi(boolean isConnected) {
        if (!isConnected) {
            Bundle bundle = new Bundle();
            bundle.putString("TAG",TAG);
            CheckInternetFragment checkInternetFragment = new CheckInternetFragment();
            checkInternetFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, checkInternetFragment, null)
                    .addToBackStack(null)
                    .commit();

            helper.deleteAll();
            ((MainPage) getActivity()).setTabBottom("false");
            mIsCheckInternet = false;

        }
        else{
            /*getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, this, null)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();*/
            ((MainPage) getActivity()).setTabBottom("true");
            mIsCheckInternet = true;


        }
    }

    public void NoInternet(){
        Bundle bundle = new Bundle();
        bundle.putString("TAG",TAG);
        CheckInternetFragment checkInternetFragment = new CheckInternetFragment();
        checkInternetFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, checkInternetFragment, null)
                .addToBackStack(null)
                .commitAllowingStateLoss();

        helper.deleteAll();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            getActivity().unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
            //already registered
        }

    }


    public void getNotificationInfo( String tokenUser, int pageNum) {

        mNotification = new ArrayList<>();
        mNotification.clear();
        mApiInterface = ApiUtils.getTokenAccess();

        mProgressBar.setVisibility(View.VISIBLE);

        final int pageNumber = pageNum;

        Call<List<Notification>> call = mApiInterface.getNotification("application/json", tokenUser, pageNum);

        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if(response.isSuccessful()){
                    mNotification = response.body();

                    if(mNotification.size() >= 1) {

                        for (int i = 0; i < mNotification.size(); i++) {
                            int mid = mNotification.get(i).getId();
                            String title = mNotification.get(i).getTitle();
                            String content = mNotification.get(i).getContent();
                            String description = mNotification.get(i).getDescription();
                            String isRead = String.valueOf(mNotification.get(i).isRead());
                            String createdOn = mNotification.get(i).getCreatedOn();
                            long id = helper.insertData(mid, title, content, description, isRead, createdOn);
                            if (id <= 0) {
                                helper.updateAll(String.valueOf(mid), title, content, description, isRead, createdOn);
                                //Log.d(TAG, "Insertion Unsuccessful");
                            } else {
                                //Log.d(TAG, "Insertion Successful");
                            }
                        }
                        mNoItem.setVisibility(View.INVISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        Log.d(TAG,helper.getData().toString());
                        mPresenterNotification.OnShowNotification(helper.getData(),pageNum);
                    }
                    else{
                        if(pageNumber != 1){
                            mNoItem.setVisibility(View.INVISIBLE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            pageIndex = pageNumber-1;
                        }else{
                            mNoItem.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.INVISIBLE);
                        }
                    }


                    if(mProgressBar != null)
                    {
                        mProgressBar.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                mProgressBar.setVisibility(View.GONE);

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_watch:WatchNotification();break;
            case R.id.btn_remove:RemoveNotification();break;
            case R.id.btn_cancel:close();
                ;break;
        }
    }

    public void checkConnect() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showConnectedWifi(isConnected);

    }

    public void close(){
        setVisibelView(aBoolean);
    }

    public void WatchNotification(){
        checkConnect();
        if(mIsCheckInternet) {
            mListener.onButtonClicked(Integer.parseInt(mID), "1");
        }
        else{
            Toast.makeText(getActivity(),getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }
        setVisibelView(aBoolean);

        // dismiss();

        //((MainPage)  getActivity()).setTabBottom("true");
    }

    public void RemoveNotification(){
        checkConnect();
        if(mIsCheckInternet) {
            mListener.onButtonClicked(Integer.parseInt(mID),"2");
        }
        else{
            Toast.makeText(getActivity(),getResources().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }
        setVisibelView(aBoolean);
        // dismiss();
        //((MainPage)).setTabBottom("true");
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}

