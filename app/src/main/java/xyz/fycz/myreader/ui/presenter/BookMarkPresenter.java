package xyz.fycz.myreader.ui.presenter;

import android.app.Activity;
import android.content.Intent;
import xyz.fycz.myreader.R;
import xyz.fycz.myreader.application.SysManager;
import xyz.fycz.myreader.base.BasePresenter;
import xyz.fycz.myreader.common.APPCONST;
import xyz.fycz.myreader.greendao.entity.Book;
import xyz.fycz.myreader.greendao.entity.BookMark;
import xyz.fycz.myreader.greendao.service.BookMarkService;
import xyz.fycz.myreader.ui.activity.CatalogActivity;
import xyz.fycz.myreader.ui.adapter.BookMarkAdapter;
import xyz.fycz.myreader.ui.fragment.BookMarkFragment;

import java.util.ArrayList;

/**
 * @author fengyue
 * @date 2020/7/22 11:11
 */
public class BookMarkPresenter implements BasePresenter {
    private BookMarkFragment mBookMarkFragment;
    private BookMarkService mBookMarkService;
    private ArrayList<BookMark> mBookMarks = new ArrayList<>();
    private BookMarkAdapter mBookMarkAdapter;
    private Book mBook;

    public BookMarkPresenter(BookMarkFragment mBookMarkFragment) {
        this.mBookMarkFragment = mBookMarkFragment;
        mBookMarkService = new BookMarkService();
    }

    @Override
    public void start() {
        mBook = ((CatalogActivity) mBookMarkFragment.getActivity()).getmBook();;
        initBookMarkList();
        mBookMarkFragment.getLvBookmarkList().setOnItemClickListener((parent, view, position, id) -> {
            BookMark bookMark = mBookMarks.get(position);
            int chapterPos = bookMark.getBookMarkChapterNum();
            int pagePos = bookMark.getBookMarkReadPosition();
            Intent intent = new Intent();
            intent.putExtra(APPCONST.CHAPTER_PAGE, new int[]{chapterPos, pagePos});
            mBookMarkFragment.getActivity().setResult(Activity.RESULT_OK, intent);
            mBookMarkFragment.getActivity().finish();
        });

        mBookMarkFragment.getLvBookmarkList().setOnItemLongClickListener((parent, view, position, id) -> {
            if (mBookMarks.get(position) != null) {
                mBookMarkService.deleteBookMark(mBookMarks.get(position));
                initBookMarkList();
            }
            return true;
        });
    }

    private void initBookMarkList() {
        mBookMarks = (ArrayList<BookMark>) mBookMarkService.findBookAllBookMarkByBookId(mBook.getId());
        mBookMarkAdapter = new BookMarkAdapter(mBookMarkFragment.getActivity(), R.layout.listview_chapter_title_item, mBookMarks);
        mBookMarkFragment.getLvBookmarkList().setAdapter(mBookMarkAdapter);
    }

    /**
     * 搜索过滤
     * @param query
     */
    public void startSearch(String query) {
        mBookMarkAdapter.getFilter().filter(query);
        mBookMarkFragment.getLvBookmarkList().setSelection(0);
    }
}
