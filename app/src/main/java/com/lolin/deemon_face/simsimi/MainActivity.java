package com.lolin.deemon_face.simsimi;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lolin.deemon_face.simsimi.Adapter.CustomAdapter;
import com.lolin.deemon_face.simsimi.Helper.HttpDataHandler;
import com.lolin.deemon_face.simsimi.model.ChatModel;
import com.lolin.deemon_face.simsimi.model.SimsimiModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    EditText editText;
    List<ChatModel> list_chat = new ArrayList<> ();
    FloatingActionButton btn_send_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);


        listView= findViewById (R.id.list_of_message);
        editText = findViewById (R.id.user_message);
        btn_send_message = findViewById (R.id.fab);

        btn_send_message.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String text = editText.getText ().toString ();
                ChatModel model = new ChatModel (text,true);
                list_chat.add (model);
                new SimsimiApi().execute(list_chat);
                editText.setText ("");

            }
        });

    }

    private class SimsimiApi extends AsyncTask<List<ChatModel>,Void,String> {

        String stream = null;
        List<ChatModel> models;
        String text = editText.getText ().toString ();

        @Override
        protected String doInBackground(List<ChatModel>... params) {
            String url = String.format ("http://sandbox.api.simsimi.com/request.p?key=%s&lc=en&ft=1.0&text=%s",getString (R.string.simsimi_api),text);
            models = params[0];
            HttpDataHandler httpDataHandler = new HttpDataHandler ();
            stream= httpDataHandler.GetHttpData (url);
            return stream;

        }

        @Override
        protected void onPostExecute(String s) {

            Gson gson = new Gson ();
            SimsimiModel response = gson.fromJson (s,SimsimiModel.class);
            ChatModel chatModel = new ChatModel (response.getResponse (),false);
            models.add (chatModel);
            CustomAdapter adapter = new CustomAdapter (models,getApplicationContext ());
            listView.setAdapter (adapter);

        }
    }
}
