package app.recruit.vrid.data.local

import androidx.room.TypeConverter
import app.recruit.vrid.data.model.RenderedField
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val renderedFieldAdapter = moshi.adapter(RenderedField::class.java)

    @TypeConverter
    fun fromRenderedField(value: RenderedField): String {
        return renderedFieldAdapter.toJson(value)
    }

    @TypeConverter
    fun toRenderedField(value: String): RenderedField {
        return renderedFieldAdapter.fromJson(value) ?: RenderedField(rendered = "", isProtected = false)
    }
} 