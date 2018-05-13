package mil.android.babysitter.touch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import static android.support.v7.widget.helper.ItemTouchHelper.Callback.makeMovementFlags;

/**
 * Created by madina on 5/13/18.
 */

public class ProfileTouchHelperCallback extends ItemTouchHelper.Callback{

    private ProfileTouchHelperAdapter profileTouchHelperAdapter;

    public ProfileTouchHelperCallback(ProfileTouchHelperAdapter profileTouchHelperAdapter) {
        this.profileTouchHelperAdapter = profileTouchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        profileTouchHelperAdapter.onItemMove(
                viewHolder.getAdapterPosition(),
                target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        profileTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
