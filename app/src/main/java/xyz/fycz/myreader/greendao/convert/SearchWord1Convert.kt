package xyz.fycz.myreader.greendao.convert

import org.greenrobot.greendao.converter.PropertyConverter
import xyz.fycz.myreader.greendao.entity.search.SearchWord1
import xyz.fycz.myreader.util.utils.GSON
import xyz.fycz.myreader.util.utils.fromJsonArray

/**
 * @author fengyue
 * @date 2021/12/7 8:14
 */
class SearchWord1Convert: PropertyConverter<List<SearchWord1>, String> {
    override fun convertToEntityProperty(databaseValue: String?): List<SearchWord1>? {
        return GSON.fromJsonArray(databaseValue)
    }

    override fun convertToDatabaseValue(entityProperty: List<SearchWord1>?): String {
        return GSON.toJson(entityProperty)
    }
}