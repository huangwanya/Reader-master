package xyz.fycz.myreader.ui.fragment;

import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import xyz.fycz.myreader.databinding.FragmentLocalBookBinding;
import xyz.fycz.myreader.greendao.service.BookService;
import xyz.fycz.myreader.ui.adapter.FileSystemAdapter;
import xyz.fycz.myreader.util.media.MediaStoreHelper;
import xyz.fycz.myreader.widget.DividerItemDecoration;


/**
 * @author fengyue
 * @date 2020/8/12 20:02
 * 本地书籍
 */

public class LocalBookFragment extends BaseFileFragment {
    private FragmentLocalBookBinding binding;

    @Override
    protected View bindView(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentLocalBookBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        mAdapter = new FileSystemAdapter();
        binding.localBookRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.localBookRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        binding.localBookRvContent.setAdapter(mAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mAdapter.setOnItemClickListener(
                (view, pos) -> {
                    //如果是已加载的文件，则点击事件无效。
                    String path = mAdapter.getItem(pos).getAbsolutePath();
                    if (BookService.getInstance().findBookByPath(path) != null) {
                        return;
                    }

                    //点击选中
                    mAdapter.setCheckedItem(pos);

                    //反馈
                    if (mListener != null) {
                        mListener.onItemCheckedChange(mAdapter.getItemIsChecked(pos));
                    }
                }
        );
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        //更新媒体库
        try {
            MediaScannerConnection.scanFile(getContext(), new String[]{Environment
                    .getExternalStorageDirectory().getAbsolutePath()}, new String[]{"text/plain", "application/epub+zip"}, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        MediaStoreHelper.getAllBookFile(getActivity(),
                (files) -> {
                    if (files.isEmpty()) {
                        binding.refreshLayout.showEmpty();
                    } else {
                        mAdapter.refreshItems(files);
                        binding.refreshLayout.showFinish();
                        //反馈
                        if (mListener != null) {
                            mListener.onCategoryChanged();
                        }
                    }
                });
    }
}
