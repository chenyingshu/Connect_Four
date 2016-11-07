package connect4.susan.connect_four;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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

import java.util.HashMap;

/**
 * Edited by Susan on 2016/10/31
 * Final edition on 2016/11/7
 * Run on AS 2.2.2
 */

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txt_PlayerName1, txt_PlayerName2, txt_UID1, txt_UID2;
    Button btn_StartGame;
    private SoundPool clickSound;
    private HashMap<Integer, Integer> clickSoundMap;
    private int soundID = 1;
    private AudioManager audioManager;

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
        //MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.keypress);
        clickSound = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);;
        clickSoundMap = new HashMap<Integer, Integer>();
        clickSoundMap.put(soundID, clickSound.load(this, R.raw.click_enter, 1));
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

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

            //Sound Effect
            float curVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolumn = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float leftVolumn = curVolumn / maxVolumn;
            float rightVolumn = curVolumn / maxVolumn;
            int priority = 1;
            int no_loop = 0;
            float normal_playback_rate = 1f;
            clickSound.play(clickSoundMap.get(soundID), leftVolumn, rightVolumn, priority, no_loop, normal_playback_rate);

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
