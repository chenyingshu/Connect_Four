package connect4.susan.connect_four;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Edited by Susan on 2016/11/2
 */

public class GameActivity extends AppCompatActivity {

    String PlayerName1, PlayerName2, UID1, UID2;
    public int p1win, p2win, playerTurn, winFlag;

    final Integer ROW_NUM = 6;
    final Integer COLUMN_NUM = 7;
    float screen_height = 0, screen_width = 0;
    public TableLayout chessBoard = null;
    public ImageButton retractButton = null;
    float board_width = 0, board_height = 0;
    int image_size = 0;
    public Point[][] chessPos = new Point[ROW_NUM][COLUMN_NUM];
    public int[][] chessColor = new int[ROW_NUM][COLUMN_NUM];
    final int RED_COLOR = 1;
    final int GREEN_COLOR = 2;
    final int EMPTY_COLOR = 0;

    private Long startTime;
    private Handler timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

        init();
        emptyInit();
        gameStart();
    }

    /**/
    public void calBoardPosition(){
        //Chess Board Table Layout
        chessBoard = (TableLayout) findViewById(R.id.chessBoard);
        board_width = screen_width - 10;
        image_size = (int) (board_width / 7);
        board_height = board_width / 7 * 6;
        int[] origin = new int[2];
        chessBoard.getLocationOnScreen(origin);
        origin[0] += 5;

        //Calculate the image position
        for (int i = 0; i < ROW_NUM; i++){
            for (int j = 0; j < COLUMN_NUM; j++){
                int x = (int)(i * image_size + 0.5 + origin[0]);
                int y = (int)(j * image_size + 0.5 + origin[1]);
                Point pos = new Point();
                pos.set(x,y);
                chessPos[i][j] = pos;
            }
        }
    }

    /**/
    public void init(){

        Intent intent = this.getIntent();
        PlayerName1 = intent.getStringExtra("p1");
        PlayerName2 = intent.getStringExtra("p2");
        UID1 = intent.getStringExtra("uid1");
        UID2 = intent.getStringExtra("uid2");

        /* Data Initialization */
        // Screen Size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        screen_width = size.x;
        screen_height = size.y;

        //calculate the every hole position
        calBoardPosition();
        //Initialize the chess board
        for (int i = 0; i <= ROW_NUM; i++){
            TableRow tableRow = new TableRow(this);
            tableRow.setId(i);
            for (int j = 0; j < COLUMN_NUM; j++){
                ImageView imageView = new ImageView(this);
                imageView.setId((i+1)*10+j);
                imageView.setAdjustViewBounds(true);
                imageView.setMaxWidth(image_size);
                imageView.setMaxHeight(image_size);
                if (i < ROW_NUM) {
                    imageView.setImageResource(R.drawable.empty);
                } else {
                    tableRow.setMinimumHeight(image_size/2);
                    imageView.setMaxHeight(image_size/2);
                }
                tableRow.addView(imageView);
            }
            chessBoard.addView(tableRow);
        }
        p1win = 0;
        p2win = 0;
        retractButton = (ImageButton)findViewById(R.id.btn_retract);
    }

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            final TextView time = (TextView)findViewById(R.id.timer);
            Long spentTime = System.currentTimeMillis() - startTime;
            Long minutes = (spentTime / 1000) / 60;
            Long seconds = (spentTime / 1000) % 60;
            time.setText("Timer: "+minutes+" : "+seconds);
            timerHandler.postDelayed(this, 1000);
        }
    };
    /*
       Empty board &
       Set/restart timer
     */
    public void emptyInit() {
        //Empty the chess board & record
        for (int i = 0; i < ROW_NUM; i++){
            TableRow tableRow = (TableRow)findViewById(i);
            for (int j = 0; j < COLUMN_NUM; j++){
                ImageView imageView = (ImageView)findViewById((i+1)*10+j);
                imageView.setImageResource(R.drawable.empty);
                chessColor[i][j] = EMPTY_COLOR;
            }
        }

        TextView playerInfo = (TextView)findViewById(R.id.playerInfo1);
        playerInfo.setText("Player 1: "+p1win + "\n" +
                        PlayerName1 + "("+UID1+")\n");
        playerInfo = (TextView)findViewById(R.id.playerInfo2);
        playerInfo.setText("Player 2: "+ p2win + "\n" +
                        PlayerName2 + "("+UID2+")\n");

        //Timer starts
        startTime = System.currentTimeMillis();
        timerHandler.removeCallbacks(updateTimer);
        timerHandler.postDelayed(updateTimer, 1000);
    }


    public void showArrow(int columnId){
        ImageView imageView;
        for (int j = 0; j < COLUMN_NUM; j++) {
            imageView = (ImageView)findViewById((ROW_NUM+1)*10+j);
            imageView.setVisibility(View.INVISIBLE);
        }
        if (columnId >= 0){
            imageView = (ImageView)findViewById((ROW_NUM+1)*10+columnId);
            if (place[columnId] > 0) {
                imageView.setImageResource(R.drawable.arrow);
            } else {
                //Vibration
                imageView.setImageResource(R.drawable.arrow_error);
                Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(300);
            }
            imageView.setVisibility(View.VISIBLE);
        }
    }

    public void checkLineForOnePoint(int color, int i, int j, int x, int y) {
        ImageView winImage;
        if ((i+x*3)<ROW_NUM && (j+y*3)<COLUMN_NUM && (i+x*3)>=0 && (j+y*3)>=0){
            if (chessColor[i+x][j+y] == color && chessColor[i+x*2][j+y*2] == color && chessColor[i+x*3][j+y*3] == color) {
                //change image to win color
                winFlag = color;
                winImage = (ImageView)findViewById((i+1)*10+j);
                if (color == RED_COLOR){
                    winImage.setImageResource(R.drawable.red_light);
                } else winImage.setImageResource(R.drawable.green_light);
                winImage = (ImageView)findViewById((i+x+1)*10+j+y);
                if (color == RED_COLOR){
                    winImage.setImageResource(R.drawable.red_light);
                } else winImage.setImageResource(R.drawable.green_light);
                winImage = (ImageView)findViewById((i+x*2+1)*10+j+y*2);
                if (color == RED_COLOR){
                    winImage.setImageResource(R.drawable.red_light);
                } else winImage.setImageResource(R.drawable.green_light);
                winImage = (ImageView)findViewById((i+x*3+1)*10+j+y*3);
                if (color == RED_COLOR){
                    winImage.setImageResource(R.drawable.red_light);
                } else winImage.setImageResource(R.drawable.green_light);

                int k = 4;
                while ((i+x*k)<ROW_NUM && (j+y*k)<COLUMN_NUM && chessColor[i+x*k][j+y*k] == color) {
                    //change image to win color
                    winImage = (ImageView)findViewById((i+x*k+1)*10+j+y*k);
                    if (color == RED_COLOR){
                        winImage.setImageResource(R.drawable.red_light);
                    } else winImage.setImageResource(R.drawable.green_light);
                    k++;
                }
            }
        }
    }

    /* Check all possible 4 consecutive chesses */
    public boolean checkLine() {
        for (int i = 0; i < ROW_NUM; i++) {
            for (int j = 0; j < COLUMN_NUM; j++) {
                if ((winFlag >= 0 && chessColor[i][j] == winFlag) ||
                 (winFlag < 0 && chessColor[i][j] != EMPTY_COLOR)) {
                    //Right Upward
                    checkLineForOnePoint(chessColor[i][j],i,j,-1,1);
                    //Right
                    checkLineForOnePoint(chessColor[i][j],i,j,0,1);
                    //Right Downward
                    checkLineForOnePoint(chessColor[i][j],i,j,1,1);
                    //Downward
                    checkLineForOnePoint(chessColor[i][j],i,j,1,0);
                }
            }
        }
        if (winFlag <0) {
            return false;
        } else return true;
    }

    public void showPlayerImage(int turn) {
        ImageView img1 = (ImageView)findViewById(R.id.playerImage1);
        ImageView img2 = (ImageView)findViewById(R.id.playerImage2);
        if (turn == 0) {
            img1.setVisibility(View.VISIBLE);
            img2.setVisibility(View.INVISIBLE);
        } else {
            img1.setVisibility(View.INVISIBLE);
            img2.setVisibility(View.VISIBLE);
        }
    }


    int[] place = new int[COLUMN_NUM];
    int[] moves = new int[ROW_NUM*COLUMN_NUM];

    public void gameStart() {
        winFlag = -1;
        for (int j = 0; j < COLUMN_NUM; j++){
            place[j] = ROW_NUM;
        }
        playerTurn = 0;
        showPlayerImage(playerTurn);

        /** Chess board listener **/
        chessBoard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x_cord = (int) event.getRawX();
                int placeId = x_cord / image_size;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_MOVE:
                        showArrow(placeId);
                        break;
                    case MotionEvent.ACTION_UP:
                        showArrow(-1); //clear all arrows
                        if (place[placeId] > 0){
                            ImageView chess = (ImageView)findViewById(place[placeId]*10+placeId);
                            if (playerTurn%2 == 0) {
                                chess.setImageResource(R.drawable.red);
                                chessColor[place[placeId]-1][placeId] = RED_COLOR;
                            } else {
                                chess.setImageResource(R.drawable.green);
                                chessColor[place[placeId]-1][placeId] = GREEN_COLOR;
                            }
                            moves[playerTurn] = placeId;
                            playerTurn++;
                            if (playerTurn == ROW_NUM*COLUMN_NUM){
                                winFlag = EMPTY_COLOR;
                                chessBoard.setOnTouchListener(null);
                                System.out.print(winFlag+"***WINNER***\n");
                                showWinner(winFlag);
                            }
                            showPlayerImage(playerTurn%2);
                            place[placeId]--;
                            if (checkLine()) {
                                chessBoard.setOnTouchListener(null);
                                System.out.print(winFlag+"***WINNER***\n");
                                showWinner(winFlag);
                            }
                        }
                        break;
                    default:
                        showArrow(-1);
                        break;
                }
                return true;
            }
        });

        /** Retract button listener **/
        retractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerTurn > 0) {
                    playerTurn--;
                    int j = moves[playerTurn];
                    place[j]++;
                    ImageView img = (ImageView)findViewById(place[j]*10+j);
                    img.setImageResource(R.drawable.empty);
                    showPlayerImage(playerTurn%2);
                }
            }
        });
    }

    /*Show the winner*/
    public void showWinner(int winner) {
        String winMsg;
        if (winner == RED_COLOR) {
            winMsg = "Winner: Player 1!";
            p1win++;
        } else if (winner == GREEN_COLOR){
            winMsg = "Winner: Player 2!";
            p2win++;
        } else {
            winMsg = "DRAW !";
        }
        new AlertDialog.Builder(this)
                .setTitle("GAME OVER")
                .setMessage(winMsg)
                .setIcon(R.drawable.logo)
                .setCancelable(false)
                .setPositiveButton("Restart",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        emptyInit();
                        gameStart();
                    }
                })
                .setNegativeButton("Menu",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();

    }
}