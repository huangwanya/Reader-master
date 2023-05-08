package xyz.fycz.myreader.ui.fragment;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import xyz.fycz.myreader.databinding.FragmentBookListBinding;
import xyz.fycz.myreader.ui.presenter.BookcasePresenter;
import xyz.fycz.myreader.widget.custom.DragSortGridView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookcaseFragment extends Fragment {

    private FragmentBookListBinding binding;

    private BookcasePresenter mBookcasePresenter;

    public BookcaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookListBinding.inflate(inflater, container, false);
        mBookcasePresenter = new BookcasePresenter(this);
        mBookcasePresenter.start();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBookcasePresenter.destroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        mBookcasePresenter.init();
    }

    public void init(){
        mBookcasePresenter.init();
    }

    public LinearLayout getLlNoDataTips() {
        return binding.llNoDataTips;
    }

    public RecyclerView getRvBook() {
        return binding.rvBookList;
    }

    public SmartRefreshLayout getSrlContent() {
        return binding.srlBookList;
    }

    public BookcasePresenter getmBookcasePresenter() {
        return mBookcasePresenter;
    }

    public boolean isRecreate() {
        return binding == null;
    }

    public RelativeLayout getRlBookEdit() {
        return binding.rlBookEdit;
    }

    public CheckBox getmCbSelectAll() {
        return binding.bookSelectedAll;
    }

    public Button getmBtnDelete() {
        return binding.bookBtnDelete;
    }

    public Button getmBtnAddGroup() {
        return binding.bookAddGroup;
    }
}
