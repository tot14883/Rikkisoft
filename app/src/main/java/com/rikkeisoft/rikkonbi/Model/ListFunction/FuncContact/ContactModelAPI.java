package com.rikkeisoft.rikkonbi.Model.ListFunction.FuncContact;

import android.os.AsyncTask;
import android.util.Log;

import com.rikkeisoft.rikkonbi.Api.APIInterface;
import com.rikkeisoft.rikkonbi.Api.ApiUtils;
import com.rikkeisoft.rikkonbi.Model.ModelAPI.ContactModel.Contact;
import com.rikkeisoft.rikkonbi.Presenter.MainActivityPresenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactModelAPI implements MainActivityPresenter.Model.GetContact{

    private APIInterface mApiInterface;
    private final String TAG = "ContactModelAPI";


    @Override
    public void getContact(OnSuccessGetContact onSuccessGetContact, String token) {
        mApiInterface = ApiUtils.getTokenAccess();
        new PostContact(onSuccessGetContact,token).execute();
    }

    public class PostContact extends AsyncTask<String,String ,String > {
        private String token;
        private OnSuccessGetContact onSuccessGetContact;
        public PostContact(OnSuccessGetContact onSuccessGetContact, String token){
            this.onSuccessGetContact = onSuccessGetContact;
            this.token = token;
        }
        @Override
        protected String doInBackground(String... strings) {
            Call<Contact> call = mApiInterface.getContact("application/json",token);
            call.enqueue(new Callback<Contact>() {
                @Override
                public void onResponse(Call<Contact> call, Response<Contact> response) {
                    if(response.isSuccessful()){
                        Contact contact = response.body();

                        onSuccessGetContact.OnSuccessContact(contact.getGeneralInfo(),contact.getHotline());
                    }
                }

                @Override
                public void onFailure(Call<Contact> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                }
            });
            return call.toString();
        }
    }
}
