package xyz.fycz.myreader.greendao.service;

import org.jetbrains.annotations.NotNull;

import xyz.fycz.myreader.R;
import xyz.fycz.myreader.application.App;
import xyz.fycz.myreader.greendao.DbManager;
import xyz.fycz.myreader.greendao.entity.BookGroup;
import xyz.fycz.myreader.greendao.gen.BookDao;
import xyz.fycz.myreader.greendao.gen.BookGroupDao;
import xyz.fycz.myreader.util.SharedPreUtils;
import xyz.fycz.myreader.util.help.StringHelper;

import java.util.List;

/**
 * @author fengyue
 * @date 2020/9/26 12:14
 */
public class BookGroupService extends BaseService{
    private static volatile BookGroupService sInstance;

    public static BookGroupService getInstance() {
        if (sInstance == null){
            synchronized (BookGroupService.class){
                if (sInstance == null){
                    sInstance = new BookGroupService();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取所有书籍分组
     * @return
     */
    public List<BookGroup> getAllGroups(){
        return DbManager.getInstance().getSession().getBookGroupDao()
                .queryBuilder()
                .orderAsc(BookGroupDao.Properties.Num)
                .list();
    }

    /**
     * 通过I的获取书籍分组
     * @param groupId
     * @return
     */
    public BookGroup getGroupById(String groupId){
        return DbManager.getInstance().getSession().getBookGroupDao()
                .queryBuilder()
                .where(BookGroupDao.Properties.Id.eq(groupId))
                .unique();
    }

    /**
     * 添加书籍分组
     * @param bookGroup
     */
    public void addBookGroup(BookGroup bookGroup){
        bookGroup.setNum(countBookGroup());
        bookGroup.setId(StringHelper.getStringRandom(25));
        addEntity(bookGroup);
    }

    /**
     * 删除书籍分组
     * @param bookGroup
     */
    public void deleteBookGroup(BookGroup bookGroup){
        deleteEntity(bookGroup);
    }

    public void deleteGroupById(String id){
        DbManager.getInstance().getSession().getBookGroupDao().deleteByKey(id);
    }

    public void createPrivateGroup(){
        BookGroup bookGroup = new BookGroup();
        bookGroup.setName("私密书架");
        addBookGroup(bookGroup);
        SharedPreUtils.getInstance().putString("privateGroupId", bookGroup.getId());
    }

    public void deletePrivateGroup(){
        String privateGroupId = SharedPreUtils.getInstance().getString("privateGroupId");
        deleteGroupById(privateGroupId);
        BookService.getInstance().deleteBooksByGroupId(privateGroupId);
    }

    /**
     * 当前是否为私密书架
     * @return
     */
    public boolean curGroupIsPrivate(){
        String curBookGroupId = SharedPreUtils.getInstance().getString(App.getmContext().getString(R.string.curBookGroupId), "");
        String privateGroupId = SharedPreUtils.getInstance().getString("privateGroupId");
        return !StringHelper.isEmpty(curBookGroupId) && curBookGroupId.equals(privateGroupId);
    }

    public int countBookGroup(){
        return (int) DbManager.getInstance().getSession().getBookGroupDao()
                .queryBuilder()
                .count();
    }

    public void updateGroups(@NotNull List<BookGroup> groups) {
        BookGroupDao bookDao = DbManager.getInstance().getSession().getBookGroupDao();
        bookDao.insertOrReplaceInTx(groups);
    }
}
