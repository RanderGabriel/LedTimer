package rgor.com.br.ledtimer.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/*
*
* Essa classe modela a View do App. Ela gerencia 3 Segmented displays, passando sempre para eles
* o tempo que cada um deve mostrar. Ele gerencia também quando ocultar ou não cada display.
* Ele configura um Timer quando o usuário clica no botão de iniciar a contagem. A cada segundo o
* número é decrementado, então, o método renderTime é executado (esse método é responsável por
* mostrar um tempo nos displays).
* Ela também mostra um dialog para seleção de cores quando o icone da paleta de cores é clicado.
* Ao selecionar uma cor na paleta, a função setColor do segmented display é chamada com a cor
* selecionada no dialog, colorindo o display.
*
 */

public class MainActivity extends AppCompatActivity implements OnEndListener {

    ImageView mPalleteIcon;
    EditText mInputField;
    Button mStartTimerButton;
    Button mOkButton;
    List<SegmentedDisplay> mDisplays;
    Timer mTimer;
    int mNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }
        mInputField = findViewById(R.id.input_text);
        mOkButton = findViewById(R.id.submit_button);
        mPalleteIcon = findViewById(R.id.pallete_icon);
        mPalleteIcon.setOnClickListener(v -> openChangeColorDialog());
        mStartTimerButton = findViewById(R.id.start_timer_button);
        mStartTimerButton.setOnClickListener(v -> startTimer());
        mOkButton.setOnClickListener(v -> onButtonClick());
        mDisplays = new ArrayList<>();
        mDisplays.add((SegmentedDisplay) findViewById(R.id.display_1));
        mDisplays.add((SegmentedDisplay) findViewById(R.id.display_2));
        mDisplays.add((SegmentedDisplay) findViewById(R.id.display_3));
        mTimer = new Timer();
        mDisplays.get(0).setCanHide(false);
        mNumber = 0;
        renderTime();
        mDisplays.get(0).setOnEndListener(this);
    }

    private void openChangeColorDialog() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                changeColor(selectedColor);
                            }
                        });
                    }
                })
                .build()
                .show();
    }

    private void changeColor(int selectedColor) {
        for (SegmentedDisplay disp: mDisplays) {
            disp.setColor(selectedColor);
        }
    }

    public void onButtonClick()
    {
        mNumber = Integer.parseInt(mInputField.getText().toString());
        renderTime();
    }

    public void startTimer()
    {
        if(mNumber == 0)
        {
            return;
        }
        mStartTimerButton.setEnabled(false);
        mStartTimerButton.setText("Timer in progress");
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNumber--;
                        renderTime();
                    }
                });
            }
        }, 0, 1000);
    }

    private void renderTime() {
        if(mNumber >= 10)
        {
            mDisplays.get(1).setCanHide(false);
        }
        else
        {
            mDisplays.get(1).setCanHide(true);
        }
        if(mNumber >= 100)
        {
            mDisplays.get(2).setCanHide(false);
        }
        else
        {
            mDisplays.get(2).setCanHide(true);
        }
        mDisplays.get(0).setTime(mNumber % 10);
        mDisplays.get(1).setTime((mNumber % 100) / 10);
        mDisplays.get(2).setTime((mNumber % 1000) / 100);
    }

    @Override
    public void onEnd()
    {
        if(mNumber == 0)
        {
            mTimer.cancel();
            mStartTimerButton.setEnabled(true);
            mStartTimerButton.setText("Start timer");
        }
    }
}
