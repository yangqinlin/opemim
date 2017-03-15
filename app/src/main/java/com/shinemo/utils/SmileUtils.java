package com.shinemo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.widget.EditText;

import com.shinemo.imdemo.R;
import com.shinemo.openim.helper.ApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmileUtils {

    public static final int MAX_TEXT = 1000;

    public static final Map<String, Integer> mFaceMap1 = new HashMap<>();
    public static final Map<Integer, String> mFaceMap1_2 = new HashMap<>();

    public static String[] md5Array, md5Array2, md5Array5;

    private static void initFaceMap1() {
        mFaceMap1.put("[微笑]", R.drawable.m01);
        mFaceMap1.put("[色]", R.drawable.m02);
        mFaceMap1.put("[呆]", R.drawable.m03);
        mFaceMap1.put("[大哭]", R.drawable.m04);
        mFaceMap1.put("[害羞]", R.drawable.m05);
        mFaceMap1.put("[闭嘴]", R.drawable.m06);
        mFaceMap1.put("[睡觉]", R.drawable.m07);
        mFaceMap1.put("[委屈]", R.drawable.m08);
        mFaceMap1.put("[无语]", R.drawable.m09);
        mFaceMap1.put("[气愤]", R.drawable.m10);
        mFaceMap1.put("[可爱]", R.drawable.m11);
        mFaceMap1.put("[龇牙笑]", R.drawable.m12);
        mFaceMap1.put("[亲亲]", R.drawable.m13);
        mFaceMap1.put("[难过]", R.drawable.m14);
        mFaceMap1.put("[坏笑]", R.drawable.m15);
        mFaceMap1.put("[酷]", R.drawable.m16);
        mFaceMap1.put("[害怕]", R.drawable.m17);
        mFaceMap1.put("[抓狂]", R.drawable.m18);
        mFaceMap1.put("[偷笑]", R.drawable.m19);
        mFaceMap1.put("[哼]", R.drawable.m20);
        mFaceMap1.put("[大笑]", R.drawable.m21);
        mFaceMap1.put("[奋斗]", R.drawable.m22);
        mFaceMap1.put("[发火]", R.drawable.m23);
        mFaceMap1.put("[疑问]", R.drawable.m24);
        mFaceMap1.put("[嘘]", R.drawable.m25);
        mFaceMap1.put("[晕]", R.drawable.m26);
        mFaceMap1.put("[衰]", R.drawable.m27);
        mFaceMap1.put("[打]", R.drawable.m28);
        mFaceMap1.put("[哭笑不得]", R.drawable.m29);
        mFaceMap1.put("[抠鼻屎]", R.drawable.m30);
        mFaceMap1.put("[鼓掌]", R.drawable.m31);
        mFaceMap1.put("[尴尬]", R.drawable.m32);
        mFaceMap1.put("[无聊]", R.drawable.m33);
        mFaceMap1.put("[打哈欠]", R.drawable.m34);
        mFaceMap1.put("[阴险]", R.drawable.m35);
        mFaceMap1.put("[生病]", R.drawable.m36);
        mFaceMap1.put("[犯困]", R.drawable.m37);
        mFaceMap1.put("[深思]", R.drawable.m38);
        mFaceMap1.put("[拜拜]", R.drawable.m39);
        mFaceMap1.put("[真棒]", R.drawable.m40);
        mFaceMap1.put("[拳头]", R.drawable.m41);
        mFaceMap1.put("[勾引]", R.drawable.m42);
        mFaceMap1.put("[小拇指]", R.drawable.m43);
        mFaceMap1.put("[否定]", R.drawable.m44);
        mFaceMap1.put("[我爱你]", R.drawable.m45);
        mFaceMap1.put("[OK]", R.drawable.m46);
        mFaceMap1.put("[胜利]", R.drawable.m47);
        mFaceMap1.put("[合作]", R.drawable.m48);
        mFaceMap1.put("[拍手]", R.drawable.m49);
        mFaceMap1.put("[鄙视]", R.drawable.m50);
        mFaceMap1.put("[赞]", R.drawable.m51);
        mFaceMap1.put("[抱拳]", R.drawable.m52);
        mFaceMap1.put("[爱心]", R.drawable.m53);
        mFaceMap1.put("[嘴唇]", R.drawable.m54);
        mFaceMap1.put("[奖杯]", R.drawable.m55);
        mFaceMap1.put("[奖牌]", R.drawable.m56);
        mFaceMap1.put("[玫瑰]", R.drawable.m57);
        mFaceMap1.put("[凋谢]", R.drawable.m58);
        mFaceMap1.put("[红旗]", R.drawable.m59);
        mFaceMap1.put("[鞭炮]", R.drawable.m60);
        mFaceMap1.put("[大刀]", R.drawable.m61);
        mFaceMap1.put("[咖啡]", R.drawable.m62);
        mFaceMap1.put("[米饭]", R.drawable.m63);
        mFaceMap1.put("[蛋糕]", R.drawable.m64);
        mFaceMap1.put("[音乐]", R.drawable.m65);
        mFaceMap1.put("[西瓜]", R.drawable.m66);
        mFaceMap1.put("[钱]", R.drawable.m67);
        mFaceMap1.put("[礼物]", R.drawable.m68);
        mFaceMap1.put("[麦克风]", R.drawable.m69);
        mFaceMap1.put("[篮球]", R.drawable.m70);
        mFaceMap1.put("[足球]", R.drawable.m71);
        mFaceMap1.put("[钉钉子]", R.drawable.m72);
        mFaceMap1.put("[白眼]", R.drawable.m73);
        mFaceMap1.put("[傲慢]", R.drawable.m74);
        mFaceMap1.put("[抱抱]", R.drawable.m75);
        mFaceMap1.put("[大便]", R.drawable.m76);
        mFaceMap1.put("[干杯]", R.drawable.m77);
        mFaceMap1.put("[饥饿]", R.drawable.m78);
        mFaceMap1.put("[惊讶]", R.drawable.m79);
        mFaceMap1.put("[可怜]", R.drawable.m80);
        mFaceMap1.put("[骷髅]", R.drawable.m81);
        mFaceMap1.put("[撇嘴]", R.drawable.m82);
        mFaceMap1.put("[糗大了]", R.drawable.m83);
        mFaceMap1.put("[闪电]", R.drawable.m84);
        mFaceMap1.put("[伤心]", R.drawable.m85);
        mFaceMap1.put("[太阳]", R.drawable.m86);
        mFaceMap1.put("[吐]", R.drawable.m87);
        mFaceMap1.put("[月亮]", R.drawable.m88);

        mFaceMap1_2.put(R.drawable.m01, "[微笑]");
        mFaceMap1_2.put(R.drawable.m02, "[色]");
        mFaceMap1_2.put(R.drawable.m03, "[呆]");
        mFaceMap1_2.put(R.drawable.m04, "[大哭]");
        mFaceMap1_2.put(R.drawable.m05, "[害羞]");
        mFaceMap1_2.put(R.drawable.m06, "[闭嘴]");
        mFaceMap1_2.put(R.drawable.m07, "[睡觉]");
        mFaceMap1_2.put(R.drawable.m08, "[委屈]");
        mFaceMap1_2.put(R.drawable.m09, "[无语]");
        mFaceMap1_2.put(R.drawable.m10, "[气愤]");
        mFaceMap1_2.put(R.drawable.m11, "[可爱]");
        mFaceMap1_2.put(R.drawable.m12, "[龇牙笑]");
        mFaceMap1_2.put(R.drawable.m13, "[亲亲]");
        mFaceMap1_2.put(R.drawable.m14, "[难过]");
        mFaceMap1_2.put(R.drawable.m15, "[坏笑]");
        mFaceMap1_2.put(R.drawable.m16, "[酷]");
        mFaceMap1_2.put(R.drawable.m17, "[害怕]");
        mFaceMap1_2.put(R.drawable.m18, "[抓狂]");
        mFaceMap1_2.put(R.drawable.m19, "[偷笑]");
        mFaceMap1_2.put(R.drawable.m20, "[哼]");
        mFaceMap1_2.put(R.drawable.m21, "[大笑]");
        mFaceMap1_2.put(R.drawable.m22, "[奋斗]");
        mFaceMap1_2.put(R.drawable.m23, "[发火]");
        mFaceMap1_2.put(R.drawable.m24, "[疑问]");
        mFaceMap1_2.put(R.drawable.m25, "[嘘]");
        mFaceMap1_2.put(R.drawable.m26, "[晕]");
        mFaceMap1_2.put(R.drawable.m27, "[衰]");
        mFaceMap1_2.put(R.drawable.m28, "[打]");
        mFaceMap1_2.put(R.drawable.m29, "[哭笑不得]");
        mFaceMap1_2.put(R.drawable.m30, "[抠鼻屎]");
        mFaceMap1_2.put(R.drawable.m31, "[鼓掌]");
        mFaceMap1_2.put(R.drawable.m32, "[尴尬]");
        mFaceMap1_2.put(R.drawable.m33, "[无聊]");
        mFaceMap1_2.put(R.drawable.m34, "[打哈欠]");
        mFaceMap1_2.put(R.drawable.m35, "[阴险]");
        mFaceMap1_2.put(R.drawable.m36, "[生病]");
        mFaceMap1_2.put(R.drawable.m37, "[犯困]");
        mFaceMap1_2.put(R.drawable.m38, "[深思]");
        mFaceMap1_2.put(R.drawable.m39, "[拜拜]");
        mFaceMap1_2.put(R.drawable.m40, "[真棒]");
        mFaceMap1_2.put(R.drawable.m41, "[拳头]");
        mFaceMap1_2.put(R.drawable.m42, "[勾引]");
        mFaceMap1_2.put(R.drawable.m43, "[小拇指]");
        mFaceMap1_2.put(R.drawable.m44, "[否定]");
        mFaceMap1_2.put(R.drawable.m45, "[我爱你]");
        mFaceMap1_2.put(R.drawable.m46, "[OK]");
        mFaceMap1_2.put(R.drawable.m47, "[胜利]");
        mFaceMap1_2.put(R.drawable.m48, "[合作]");
        mFaceMap1_2.put(R.drawable.m49, "[拍手]");
        mFaceMap1_2.put(R.drawable.m50, "[鄙视]");
        mFaceMap1_2.put(R.drawable.m51, "[赞]");
        mFaceMap1_2.put(R.drawable.m52, "[抱拳]");
        mFaceMap1_2.put(R.drawable.m53, "[爱心]");
        mFaceMap1_2.put(R.drawable.m54, "[嘴唇]");
        mFaceMap1_2.put(R.drawable.m55, "[奖杯]");
        mFaceMap1_2.put(R.drawable.m56, "[奖牌]");
        mFaceMap1_2.put(R.drawable.m57, "[玫瑰]");
        mFaceMap1_2.put(R.drawable.m58, "[凋谢]");
        mFaceMap1_2.put(R.drawable.m59, "[红旗]");
        mFaceMap1_2.put(R.drawable.m60, "[鞭炮]");
        mFaceMap1_2.put(R.drawable.m61, "[大刀]");
        mFaceMap1_2.put(R.drawable.m62, "[咖啡]");
        mFaceMap1_2.put(R.drawable.m63, "[米饭]");
        mFaceMap1_2.put(R.drawable.m64, "[蛋糕]");
        mFaceMap1_2.put(R.drawable.m65, "[音乐]");
        mFaceMap1_2.put(R.drawable.m66, "[西瓜]");
        mFaceMap1_2.put(R.drawable.m67, "[钱]");
        mFaceMap1_2.put(R.drawable.m68, "[礼物]");
        mFaceMap1_2.put(R.drawable.m69, "[麦克风]");
        mFaceMap1_2.put(R.drawable.m70, "[篮球]");
        mFaceMap1_2.put(R.drawable.m71, "[足球]");
        mFaceMap1_2.put(R.drawable.m72, "[钉钉子]");
        mFaceMap1_2.put(R.drawable.m73, "[白眼]");
        mFaceMap1_2.put(R.drawable.m74, "[傲慢]");
        mFaceMap1_2.put(R.drawable.m75, "[抱抱]");
        mFaceMap1_2.put(R.drawable.m76, "[大便]");
        mFaceMap1_2.put(R.drawable.m77, "[干杯]");
        mFaceMap1_2.put(R.drawable.m78, "[饥饿]");
        mFaceMap1_2.put(R.drawable.m79, "[惊讶]");
        mFaceMap1_2.put(R.drawable.m80, "[可怜]");
        mFaceMap1_2.put(R.drawable.m81, "[骷髅]");
        mFaceMap1_2.put(R.drawable.m82, "[撇嘴]");
        mFaceMap1_2.put(R.drawable.m83, "[糗大了]");
        mFaceMap1_2.put(R.drawable.m84, "[闪电]");
        mFaceMap1_2.put(R.drawable.m85, "[伤心]");
        mFaceMap1_2.put(R.drawable.m86, "[太阳]");
        mFaceMap1_2.put(R.drawable.m87, "[吐]");
        mFaceMap1_2.put(R.drawable.m88, "[月亮]");
    }

    public static int[] smile = new int[]{R.drawable.m01, R.drawable.m82, R.drawable.m02, R.drawable.m03, R.drawable.m04, R.drawable.m05, R.drawable.m06,
            R.drawable.m07, R.drawable.m08, R.drawable.m09, R.drawable.m10, R.drawable.m11, R.drawable.m73, R.drawable.m74, R.drawable.m78, R.drawable.m12, R.drawable.m79, R.drawable.m13, R.drawable.m80,
            R.drawable.m14, R.drawable.m15, R.drawable.m16, R.drawable.m17, R.drawable.m18, R.drawable.m87,
            R.drawable.m19, R.drawable.m20, R.drawable.m21, R.drawable.m22, R.drawable.m23,
            R.drawable.m24, R.drawable.m25, R.drawable.m26, R.drawable.m27, R.drawable.m81, R.drawable.m28, R.drawable.m29,
            R.drawable.m30, R.drawable.m83, R.drawable.m31, R.drawable.m32, R.drawable.m33, R.drawable.m34, R.drawable.m35, R.drawable.m36,
            R.drawable.m37, R.drawable.m38, R.drawable.m39, R.drawable.m40, R.drawable.m41,
            R.drawable.m42, R.drawable.m43, R.drawable.m44, R.drawable.m45, R.drawable.m46,
            R.drawable.m47, R.drawable.m48, R.drawable.m49, R.drawable.m50, R.drawable.m51,
            R.drawable.m52, R.drawable.m53, R.drawable.m85, R.drawable.m54, R.drawable.m77, R.drawable.m55, R.drawable.m56,
            R.drawable.m57, R.drawable.m58, R.drawable.m59, R.drawable.m60, R.drawable.m61,
            R.drawable.m62, R.drawable.m63, R.drawable.m64, R.drawable.m84, R.drawable.m65, R.drawable.m66,
            R.drawable.m67, R.drawable.m68, R.drawable.m75, R.drawable.m69, R.drawable.m70, R.drawable.m71, R.drawable.m76, R.drawable.m88, R.drawable.m86,
            R.drawable.m72,};






    private static final Factory spannableFactory = Spannable.Factory
            .getInstance();

    private static final Map<Pattern, Integer> emoticons = new HashMap<>();

    static {
        initFaceMap1();
        for (Entry<String, Integer> entry : mFaceMap1.entrySet()) {
            addPattern(emoticons, entry.getKey(), entry.getValue());
        }
    }

    private static void addPattern(Map<Pattern, Integer> map, String smile,
                                   int resource) {
        map.put(Pattern.compile(Pattern.quote(smile)), resource);
    }

    /**
     * replace existing spannable with smiles
     *
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable, int bound) {
        boolean hasChanges = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Drawable d = context.getResources().getDrawable(entry.getValue());
                    d.setBounds(0, 0, bound, bound);

                    ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(span,
                            matcher.start(), matcher.end(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return hasChanges;
    }

    public static void appendTextToInputText(String text, EditText mInputText) {

        if (mInputText != null && !TextUtils.isEmpty(text)) {
            String inputText = mInputText.getText().toString();
            int len = inputText.length() + text.length();
            if (len > MAX_TEXT) {
                return;
            }

            int before = mInputText.getSelectionStart();
            int end = mInputText.getSelectionEnd();
            int length = text.length();

            String content = inputText.substring(0, before) + text
                    + inputText.substring(end);

            Spannable span = getSmiledText(ApplicationContext.getInstance(), content);
            if (span != null) {
                mInputText.setText(span);
            } else {
                mInputText.setText(content);
            }
            mInputText.setSelection(before + length);

        }
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return spannableFactory.newSpannable("");
        }
        Spannable spannable = spannableFactory.newSpannable(text);
        int bound = context.getResources().getDimensionPixelSize(R.dimen.smily_column_width);
        addSmiles(context, spannable, bound);
        return spannable;
    }

    public static Spannable getSmiledText(Context context, CharSequence text, int bound) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable, bound);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }
        return b;
    }

}
