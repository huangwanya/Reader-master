package xyz.fycz.myreader.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.RecyclerView;

import xyz.fycz.myreader.R;

/**
 * @author fengyue
 * @date 2020/8/12 20:02
 */

public abstract class ViewHolderImpl<T> implements IViewHolder<T> {
    private View view;
    private Context context;
    /****************************************************/
    protected abstract int getItemLayoutId();

    @Override
    public View createItemView(ViewGroup parent) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(getItemLayoutId(), parent, false);
        context = parent.getContext();
        return view;
    }

    protected <V extends View> V findById(int id){
        return (V) view.findViewById(id);
    }

    protected Context getContext(){
        return context;
    }

    protected View getItemView(){
        return view;
    }

    @Override
    public void onClick() {
    }
}
