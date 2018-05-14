// Generated code from Butter Knife. Do not modify!
package com.ontariolegalpool.olp;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomeFragment_ViewBinding implements Unbinder {
  private HomeFragment target;

  @UiThread
  public HomeFragment_ViewBinding(HomeFragment target, View source) {
    this.target = target;

    target._fightticketText = Utils.findRequiredViewAsType(source, R.id.txt_fight_ticket, "field '_fightticketText'", TextView.class);
    target._fightticketButton = Utils.findRequiredViewAsType(source, R.id.btn_fight_ticket, "field '_fightticketButton'", ImageButton.class);
    target._payticketText = Utils.findRequiredViewAsType(source, R.id.txt_pay_ticket, "field '_payticketText'", TextView.class);
    target._payticketButton = Utils.findRequiredViewAsType(source, R.id.btn_pay_ticket, "field '_payticketButton'", ImageButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HomeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._fightticketText = null;
    target._fightticketButton = null;
    target._payticketText = null;
    target._payticketButton = null;
  }
}
