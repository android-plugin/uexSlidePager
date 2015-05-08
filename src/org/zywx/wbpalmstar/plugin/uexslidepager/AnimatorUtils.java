package org.zywx.wbpalmstar.plugin.uexslidepager;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimatorUtils {
    public static Animator animViewFadeIn(View paramView)
    {
      return animViewFadeIn(paramView, 200L, null);
    }

    public static Animator animViewFadeIn(View paramView, long paramLong, Animator.AnimatorListener paramAnimatorListener)
    {
      float[] arrayOfFloat = new float[2];
      arrayOfFloat[0] = 0.0F;
      arrayOfFloat[1] = 1.0F;
      ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView, "alpha", arrayOfFloat);
      localObjectAnimator.setDuration(paramLong);
      if (paramAnimatorListener != null)
        localObjectAnimator.addListener(paramAnimatorListener);
      localObjectAnimator.start();
      return localObjectAnimator;
    }

    public static Animator animViewFadeOut(View paramView)
    {
      return animViewFadeOut(paramView, 200L, null);
    }

    public static Animator animViewFadeOut(View paramView, long paramLong, Animator.AnimatorListener paramAnimatorListener)
    {
      float[] arrayOfFloat = new float[2];
      arrayOfFloat[0] = 1.0F;
      arrayOfFloat[1] = 0.0F;
      ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView, "alpha", arrayOfFloat);
      localObjectAnimator.setDuration(paramLong);
      if (paramAnimatorListener != null)
        localObjectAnimator.addListener(paramAnimatorListener);
      localObjectAnimator.start();
      return localObjectAnimator;
    }

    public static void animateHeartbeat(View paramView)
    {
      float[] arrayOfFloat1 = new float[2];
      arrayOfFloat1[0] = 1.0F;
      arrayOfFloat1[1] = 1.5F;
      ValueAnimator localValueAnimator1 = ValueAnimator.ofFloat(arrayOfFloat1);
      localValueAnimator1.setInterpolator(new OvershootInterpolator());
      localValueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          //do something
        }
      });
      float[] arrayOfFloat2 = new float[2];
      arrayOfFloat2[0] = 1.5F;
      arrayOfFloat2[1] = 1.0F;
      ValueAnimator localValueAnimator2 = ValueAnimator.ofFloat(arrayOfFloat2);
      localValueAnimator2.setInterpolator(new DecelerateInterpolator());
      localValueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          //do something
        }
      });
      AnimatorSet localAnimatorSet = new AnimatorSet();
      localAnimatorSet.play(localValueAnimator2).after(localValueAnimator1);
      localAnimatorSet.setDuration(200L);
      localAnimatorSet.start();
    }

    public static void animateShake(View paramView)
    {
      float[] arrayOfFloat = new float[2];
      arrayOfFloat[0] = paramView.getY();
      arrayOfFloat[1] = (paramView.getY() + paramView.getContext().getResources().getDimensionPixelSize(2131296331));
      ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView, "Y", arrayOfFloat);
      localObjectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
      localObjectAnimator.setRepeatMode(2);
      localObjectAnimator.setRepeatCount(-1);
      localObjectAnimator.setDuration(500L);
      localObjectAnimator.start();
    }

    public static void cancelAnimation(View paramView)
    {
      Animation localAnimation = paramView.getAnimation();
      if (localAnimation != null)
      {
        localAnimation.cancel();
        paramView.clearAnimation();
      }
    }

    public static ScaleAnimation getIconScaleAnimation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
    {
      ScaleAnimation localScaleAnimation = new ScaleAnimation(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6);
      localScaleAnimation.setInterpolator(new DecelerateInterpolator());
      localScaleAnimation.setDuration(200L);
      return localScaleAnimation;
    }

    public static TranslateAnimation getIconTranslateAnimation(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
    {
      TranslateAnimation localTranslateAnimation = new TranslateAnimation(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
      localTranslateAnimation.setInterpolator(new DecelerateInterpolator());
      localTranslateAnimation.setDuration(200L);
      return localTranslateAnimation;
    }

    public static Animator moveScrollViewToX(View paramView, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
    {
      int[] arrayOfInt = new int[1];
      arrayOfInt[0] = paramInt1;
      ObjectAnimator localObjectAnimator = ObjectAnimator.ofInt(paramView, "scrollX", arrayOfInt);
      localObjectAnimator.setDuration(paramInt2);
      localObjectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
      localObjectAnimator.setStartDelay(paramInt3);
      if (paramBoolean)
        localObjectAnimator.start();
      return localObjectAnimator;
    }

    public static void showBackgroundColorAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3)
    {
      int[] arrayOfInt = new int[2];
      arrayOfInt[0] = paramInt1;
      arrayOfInt[1] = paramInt2;
      ObjectAnimator localObjectAnimator = ObjectAnimator.ofInt(paramView, "backgroundColor", arrayOfInt);
      localObjectAnimator.setDuration(paramInt3);
      localObjectAnimator.setEvaluator(new ArgbEvaluator());
      localObjectAnimator.start();
    }

    public static Animator showUpAndDownBounce(View paramView, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
    {
      float[] arrayOfFloat = new float[1];
      arrayOfFloat[0] = paramInt1;
      ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView, "translationY", arrayOfFloat);
      if (paramBoolean2)
        localObjectAnimator.setInterpolator(new OvershootInterpolator());
      localObjectAnimator.setDuration(paramInt2);
      if (paramBoolean1)
        localObjectAnimator.start();
      return localObjectAnimator;
    }
}
