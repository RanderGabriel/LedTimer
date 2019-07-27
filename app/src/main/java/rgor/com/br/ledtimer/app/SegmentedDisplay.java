package rgor.com.br.ledtimer.app;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

/**
 *
 * Esse componente é usado para exibir os displays na tela. Ele possui alguns métodos muito
 * importantes para o funcionamento do programa. São eles:
 *  setCanHide - recebe um boolean indicando se esse display deve ser ocultado ao chegar em 0
 *  setOnEndListener - recebe uma instancia que implemente a interface OnEndListener, que terá o
 *  método onEnd chamado assim que esse display chegar em 0
 *  setTime - recebe um inteiro (entre 0 e 9) e exibe no display
 *  setColor - recebe uma cor (int) e colore o display com a cor selecionada.
 *
 */
public class SegmentedDisplay extends LinearLayout {

    private int mColor;
    private List<ImageView> mLeds;
    private boolean mCanHide = true;
    private OnEndListener mOnEndListener;
    private int[] mState;

    public SegmentedDisplay(Context context) {
        super(context);
        init(null, context);
    }

    public void setCanHide(boolean mCanHide) {
        this.mCanHide = mCanHide;
    }

    public SegmentedDisplay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, context);
    }

    public SegmentedDisplay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, context);
    }

    private void init(AttributeSet attrs, Context context) {
        inflate(context, R.layout.segmented_display, this);
        mLeds = new ArrayList<>();
        mState = new int[] {1, 1, 1, 1, 1, 1, 0};
        mColor = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);
        mLeds.add((ImageView) findViewById(R.id.led_a));
        mLeds.add((ImageView) findViewById(R.id.led_b));
        mLeds.add((ImageView) findViewById(R.id.led_c));
        mLeds.add((ImageView) findViewById(R.id.led_d));
        mLeds.add((ImageView) findViewById(R.id.led_e));
        mLeds.add((ImageView) findViewById(R.id.led_f));
        mLeds.add((ImageView) findViewById(R.id.led_g));
    }

    public void setState()
    {
        int j = 0;
        for(int i : mState)
        {
            if(i == 0)
            {
                ImageViewCompat.setImageTintList(mLeds.get(j), ColorStateList.valueOf(ContextCompat.getColor(getContext(), android.R.color.darker_gray)));
            }
            else
            {
                ImageViewCompat.setImageTintList(mLeds.get(j), ColorStateList.valueOf(mColor));
            }
            j++;
        }
    }

    public void setTime(int time)
    {
        switch (time)
        {
            case 0:
                if(mCanHide)
                {
                    this.setVisibility(View.GONE);
                    return;
                }
                else
                {
                    mState = new int[] {1, 1, 1, 1, 1, 1, 0};
                }
                if(mOnEndListener != null)
                {
                    mOnEndListener.onEnd();
                }
                break;
            case 1:
                mState = new int[] {0, 1, 1, 0, 0, 0, 0};
                break;
            case 2:
                mState = new int[] {1, 1, 0, 1, 1, 0, 1};
                break;
            case 3:
                mState = new int[] {1, 1, 1, 1, 0, 0, 1};
                break;
            case 4:
                mState = new int[] {0, 1, 1, 0, 0, 1, 1};
                break;
            case 5:
                mState = new int[] {1, 0, 1, 1, 0, 1, 1};
                break;
            case 6:
                mState = new int[] {1, 0, 1, 1, 1, 1, 1};
                break;
            case 7:
                mState = new int[] {1, 1, 1, 0, 0, 0, 0};
                break;
            case 8:
                mState = new int[] {1, 1, 1, 1, 1, 1, 1};
                break;
            case 9:
                mState = new int[] {1, 1, 1, 1, 0, 1, 1};
                break;
        }
        this.setVisibility(View.VISIBLE);
        setState();
    }

    public void setColor (int color)
    {
        mColor = color;
        setState();
    }

    public void setOnEndListener(OnEndListener onEnd) {
        mOnEndListener = onEnd;
    }
}

