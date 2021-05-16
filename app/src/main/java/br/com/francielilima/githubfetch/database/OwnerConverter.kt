package br.com.francielilima.githubfetch.database

import androidx.room.TypeConverter
import br.com.francielilima.githubfetch.entities.Owner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class OwnerConverter {
    var gson = Gson()

    @TypeConverter
    fun stringToOwner(data: String?): Owner? {
        if (data == null) {
            return null
        }
        val type: Type = object : TypeToken<Owner?>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun ownerToString(owner: Owner?): String? {
        return gson.toJson(owner)
    }
}