// Generated code from Butter Knife. Do not modify!
package com.ontariolegalpool.olp;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FightTicketActivity_ViewBinding implements Unbinder {
  private FightTicketActivity target;

  @UiThread
  public FightTicketActivity_ViewBinding(FightTicketActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FightTicketActivity_ViewBinding(FightTicketActivity target, View source) {
    this.target = target;

    target._fightTicket = Utils.findRequiredViewAsType(source, R.id.btn_fight_ticket, "field '_fightTicket'", ImageButton.class);
    target._payTicket = Utils.findRequiredViewAsType(source, R.id.btn_pay_ticket, "field '_payTicket'", ImageButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FightTicketActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target._fightTicket = null;
    target._payTicket = null;
  }
}
