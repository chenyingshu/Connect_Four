package connect4.susan.connect_four;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Edited by Susan on 2016/10/31
 */

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txt_PlayerName1, txt_PlayerName2, txt_UID1, txt_UID2;
    Button btn_StartGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //set two players' information
        txt_PlayerName1 = (EditText) findViewById(R.id.editText_player1);
        txt_PlayerName2 = (EditText) findViewById(R.id.editText_player2);
        txt_UID1 = (EditText) findViewById(R.id.editText_UID1);
        txt_UID2 = (EditText) findViewById(R.id.editText_UID2);

        //Set start_game button listener
        btn_StartGame = (Button) findViewById(R.id.btn_startgame);
        btn_StartGame.setOnClickListener(this);

        //Instruction information notice
        ImageButton btn_help = (ImageButton)findViewById(R.id.icon_help);
        btn_help.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                ViewGroup root = (ViewGroup)findViewById(R.id.toast_layout_root);
                View layout = LayoutInflater.from(context).inflate(R.layout.toast_style,root);
                ImageView mImageView = (ImageView) layout.findViewById(R.id.iv);
                mImageView.setBackgroundResource(R.drawable.help);
                Toast toast = new Toast(context);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_startgame) {
            String p1 = txt_PlayerName1.getText().toString();
            String p2 = txt_PlayerName2.getText().toString();
            String uid1 = txt_UID1.getText().toString();
            String uid2 = txt_UID2.getText().toString();
            /*System.out.print("@@@@@@@@@@@@@@@@\n"+
                    "Player1: "+ p1 +"("+ uid1 +")\n"+
                    "Player2: "+ p2 +"("+ uid2 +")\n"+
                    "@@@@@@@@@@@@@@@@@\n");
             */
            //New activity
            Intent intent = new Intent(getBaseContext(), GameActivity.class);
            intent.putExtra("p1", p1);
            intent.putExtra("p2", p2);
            intent.putExtra("uid1", uid1);
            intent.putExtra("uid2", uid2);
            startActivityForResult(intent, 0);
        }
    }

}
