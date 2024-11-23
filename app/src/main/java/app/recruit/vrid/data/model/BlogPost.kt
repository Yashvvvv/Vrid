package app.recruit.vrid.data.model

import android.os.Parcelable
import android.text.Html
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "blog_posts")
@JsonClass(generateAdapter = true)
data class BlogPost(
    @PrimaryKey
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: RenderedField,
    @Json(name = "content") val content: RenderedField,
    @Json(name = "excerpt") val excerpt: RenderedField,
    @Json(name = "date") val date: String,
    @Json(name = "modified") val modified: String,
    @Json(name = "author") val authorId: Int,
    @Json(name = "featured_media") val featuredMediaId: Int = 0
) : Parcelable {
    val titleText: String get() = title.rendered
    val contentText: String get() = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <style>
                body {
                    font-family: system-ui;
                    padding: 16px;
                    margin: 0;
                    line-height: 1.6;
                }
                img {
                    max-width: 100%;
                    height: auto;
                }
                pre {
                    overflow-x: auto;
                    background: #f5f5f5;
                    padding: 16px;
                    border-radius: 4px;
                }
            </style>
        </head>
        <body>
            ${content.rendered}
        </body>
        </html>
    """.trimIndent()
    
    val excerptText: String get() = Html.fromHtml(excerpt.rendered, Html.FROM_HTML_MODE_COMPACT).toString().trim()
}

@JsonClass(generateAdapter = true)
@Parcelize
data class RenderedField(
    @Json(name = "rendered") val rendered: String,
    @Json(name = "protected") val isProtected: Boolean = false
) : Parcelable 