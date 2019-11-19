package com.qsir.input.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qsir.input.R;
import com.qsir.input.utils.SharedPreferencesUtils;


public class InputView extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private View mPanelView;
    private EditText mInput;
    private ImageView mVoice;
    private ImageView mEmoji;
    private ImageView mMore;
    private Button mSend;
    private boolean mIsKeyboardActive;

    public InputView(Context context) {
        super(context);
        initView(context);
    }


    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeValue(context, attrs);
        initView(context);
    }

    public InputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeValue(context, attrs);
        initView(context);
    }

    public InputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initTypeValue(context, attrs);
        initView(context);
    }

    private void initTypeValue(Context context, AttributeSet attrs) {
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.input_view, this, true);
        mPanelView = findViewById(R.id.panel_view);
        mInput = findViewById(R.id.input_et);
        mVoice = findViewById(R.id.voice_btn);
        mEmoji = findViewById(R.id.emoji_btn);
        mMore = findViewById(R.id.more_btn);
        mSend = findViewById(R.id.send_btn);
        mInput.setOnClickListener(this);
        mVoice.setOnClickListener(this);
        mEmoji.setOnClickListener(this);
        mMore.setOnClickListener(this);
        mSend.setOnClickListener(this);
    }

    private void setPanelHeight() {
        if (SharedPreferencesUtils.getInstance().getPanelHeight() != 0) {
            ViewGroup.LayoutParams params = mPanelView.getLayoutParams();
            params.height = SharedPreferencesUtils.getInstance().getPanelHeight();
            mPanelView.setLayoutParams(params);
        }

    }

    private boolean panelHided = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emoji_btn:
                if (SharedPreferencesUtils.getInstance().getPanelHeight() != 0) {
                    ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                }
                showOrHidePanel();
                break;
            case R.id.input_et:
                mEmoji.setImageResource(R.mipmap.icon_emoji);
                panelHided = true;
                break;
        }

    }

    private void showOrHidePanel() {
        if (mIsKeyboardActive) {
            if (panelHided) {
                mPanelView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPanelView.setVisibility(VISIBLE);
                    }
                }, 200);
                mEmoji.setImageResource(R.mipmap.icon_input);
                panelHided = false;
                hideSoftInputFromWindowNothing();
            } else {
                mEmoji.setImageResource(R.mipmap.icon_emoji);
                panelHided = true;
                toggleSoftInputNothing();
            }

        } else {
            if (panelHided) {
                toggleSoftInputNothing();
                mPanelView.setVisibility(VISIBLE);
                mEmoji.setImageResource(R.mipmap.icon_input);
                panelHided = false;
            } else {
                mEmoji.setImageResource(R.mipmap.icon_emoji);
                panelHided = true;
                toggleSoftInputNothing();
            }
        }

    }


    public void setRootView(final View rootView) {
        if (SharedPreferencesUtils.getInstance().getPanelHeight() != 0) {
            setPanelHeight();
        }
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            int mScreenHeight = 0;
            Rect mRect = new Rect();

            private int getScreenHeight() {
                if (mScreenHeight > 0) {
                    return mScreenHeight;
                }
                mScreenHeight = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                        .getDefaultDisplay().getHeight();
                return mScreenHeight;
            }

            @Override
            public void onGlobalLayout() {
                rootView.getWindowVisibleDisplayFrame(mRect);
                int screenHeight = getScreenHeight(); //屏幕高度
                int keyboardHeight = screenHeight - mRect.bottom; // 输入法的高度
                boolean isActive = false;
                if (Math.abs(keyboardHeight) > screenHeight / 5) {

                    isActive = true;
                    if (SharedPreferencesUtils.getInstance().getPanelHeight() == 0) {
                        SharedPreferencesUtils.getInstance().putPanelHeight(keyboardHeight);
                        setPanelHeight();
                    }

                }
                mIsKeyboardActive = isActive;


            }
        });
    }

    public void setBackView(View backView) {
        backView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPanelView.setVisibility(GONE);
                mEmoji.setImageResource(R.mipmap.icon_emoji);
                panelHided = true;
                setSoftInputModeResize();
                return false;
            }
        });
    }

    public void toggleSoftInputNothing() {
        if (mPanelView.getVisibility() == VISIBLE) {
            if (SharedPreferencesUtils.getInstance().getPanelHeight() == 0) {
                mPanelView.setVisibility(GONE);
            }
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void hideSoftInputFromWindowNothing() {

        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInput.getApplicationWindowToken(), 0);
    }

    public void setSoftInputModeResize() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
        ((Activity) mContext).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}
