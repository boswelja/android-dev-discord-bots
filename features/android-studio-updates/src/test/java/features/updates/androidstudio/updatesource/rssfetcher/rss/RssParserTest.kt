/*
 * Copyright 2024 AndroidDev Discord Dev Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package features.updates.androidstudio.updatesource.rssfetcher.rss

import features.updates.androidstudio.updatesource.rssfetcher.Author
import features.updates.androidstudio.updatesource.rssfetcher.Link
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.time.OffsetDateTime
import java.time.ZoneOffset

class RssParserTest {

    private lateinit var parser: RssParser

    @BeforeEach
    fun setUp() {
        parser = RssParserFactory.create()
    }

    @Test
    fun parseFeed() = runTest {
        val entryHtmlA = """
            &lt;p&gt;Android Studio - Flamingo | 2022.2.1 Canary 9 is now available in the Canary
            channel.&lt;/p&gt; &lt;p&gt;If you already have an Android Studio build on the&amp;nbsp;&lt;a href=&quot;https://developer.android.com/studio/intro/update.html#channels&quot;&gt;Canary
            or Dev channel&lt;/a&gt;, you can get the update by clicking&amp;nbsp;&lt;b&gt;Help&lt;/b&gt;&amp;nbsp;&amp;gt;&amp;nbsp;&lt;b&gt;Check
            for Update&lt;/b&gt;&amp;nbsp;(or&amp;nbsp;&lt;b&gt;Android Studio&lt;/b&gt;&amp;nbsp;&amp;gt;&amp;nbsp;&lt;b&gt;Check
            for Updates&lt;/b&gt;&amp;nbsp;on macOS). Otherwise, you can&amp;nbsp;&lt;a href=&quot;https://developer.android.com/studio/preview/index.html&quot;&gt;download
            it here&lt;/a&gt;.&lt;/p&gt;.
        """.trimHtmlIndenting()
        val entryHtmlB = """
            &lt;p&gt;Android Studio - Electric Eel | 2022.1.1 Beta 5 is now available in the Beta
            channels.&lt;/p&gt; &lt;p&gt;If you already have an Android Studio build on the&amp;nbsp;&lt;a href=&quot;https://developer.android.com/studio/intro/update.html#channels&quot;&gt;Beta
            channel&lt;/a&gt;, you can get the update by clicking&amp;nbsp;&lt;b&gt;Help&lt;/b&gt;&amp;nbsp;&amp;gt;&amp;nbsp;&lt;b&gt;Check
            for Update&lt;/b&gt;&amp;nbsp;(or&amp;nbsp;&lt;b&gt;Android Studio&lt;/b&gt;&amp;nbsp;&amp;gt;&amp;nbsp;&lt;b&gt;Check
            for Updates&lt;/b&gt;&amp;nbsp;on macOS). Otherwise, you can&amp;nbsp;&lt;a href=&quot;https://developer.android.com/studio/preview/index.html&quot;&gt;download
            it here&lt;/a&gt;.&lt;/p&gt;
        """.trimHtmlIndenting()

        val xml = """
            <?xml version='1.0' encoding='UTF-8'?><?xml-stylesheet href="http://www.blogger.com/styles/atom.css" type="text/css"?>
            <feed xmlns='http://www.w3.org/2005/Atom' xmlns:openSearch='http://a9.com/-/spec/opensearchrss/1.0/' xmlns:blogger='http://schemas.google.com/blogger/2008' xmlns:gd="http://schemas.google.com/g/2005" xmlns:thr='http://purl.org/syndication/thread/1.0'>
                <id>tag:blogger.com,1999:blog-3325683420543787015</id>
                <updated>2022-11-30T19:27:01.610-08:00</updated>
                <title type='text'>Android Studio Release Updates</title>
                <subtitle type='html'>Provides official announcements for new versions of Android Studio and other Android developer tools.</subtitle>
                <link rel='http://schemas.google.com/g/2005#feed' type='application/atom+xml' href='https://androidstudio.googleblog.com/feeds/posts/default'/>
                <link rel='self' type='application/atom+xml' href='https://www.blogger.com/feeds/3325683420543787015/posts/default'/>
                <link rel='alternate' type='text/html' href='https://androidstudio.googleblog.com/'/>
                <link rel='hub' href='http://pubsubhubbub.appspot.com/'/>
                <link rel='next' type='application/atom+xml' href='https://www.blogger.com/feeds/3325683420543787015/posts/default?start-index=26&amp;max-results=25'/>
                <author>
                    <name>Jamal Eason</name>
                    <uri>http://www.blogger.com/profile/11425468413618881872</uri>
                    <email>noreply@blogger.com</email>
                    <gd:image rel='http://schemas.google.com/g/2005#thumbnail' width='16' height='16' src='https://img1.blogblog.com/img/b16-rounded.gif'/>
                </author>
                <generator version='7.00' uri='http://www.blogger.com'>Blogger</generator>
                <openSearch:totalResults>543</openSearch:totalResults>
                <openSearch:startIndex>1</openSearch:startIndex>
                <openSearch:itemsPerPage>25</openSearch:itemsPerPage>
                <entry>
                    <id>tag:blogger.com,1999:blog-3325683420543787015.post-926632259736729368</id>
                    <published>2022-11-30T19:26:00.000-08:00</published>
                    <updated>2022-11-30T19:26:08.782-08:00</updated>
                    <title type='text'>Android Studio Flamingo Canary 9 now available</title>
                    <content type='html'>$entryHtmlA</content>
                    <link rel='edit' type='application/atom+xml' href='https://www.blogger.com/feeds/3325683420543787015/posts/default/926632259736729368'/>
                    <link rel='self' type='application/atom+xml' href='https://www.blogger.com/feeds/3325683420543787015/posts/default/926632259736729368'/>
                    <link rel='alternate' type='text/html' href='https://androidstudio.googleblog.com/2022/11/android-studio-flamingo-canary-9-now.html' title='Android Studio Flamingo Canary 9 now available'/>
                    <author>
                        <name>Navee</name>
                        <uri>http://www.blogger.com/profile/05292073289086474068</uri>
                        <email>noreply@blogger.com</email>
                        <gd:image rel='http://schemas.google.com/g/2005#thumbnail' width='16' height='16' src='https://img1.blogblog.com/img/b16-rounded.gif'/>
                    </author>
                </entry>
                <entry>
                    <id>tag:blogger.com,1999:blog-3325683420543787015.post-1650240193865345981</id>
                    <published>2022-11-30T09:48:00.002-08:00</published>
                    <updated>2022-11-30T09:48:57.168-08:00</updated>
                    <title type='text'>Android Studio Electric Eel RC1 now availabe</title>
                    <content type='html'>$entryHtmlB</content>
                    <link rel='edit' type='application/atom+xml' href='https://www.blogger.com/feeds/3325683420543787015/posts/default/1650240193865345981'/>
                    <link rel='self' type='application/atom+xml' href='https://www.blogger.com/feeds/3325683420543787015/posts/default/1650240193865345981'/>
                    <link rel='alternate' type='text/html' href='https://androidstudio.googleblog.com/2022/11/android-studio-electric-eel-rc1-now.html' title='Android Studio Electric Eel RC1 now availabe'/>
                    <author>
                        <name>Amy Gu</name>
                        <uri>http://www.blogger.com/profile/09764370306420891064</uri>
                        <email>noreply@blogger.com</email>
                        <gd:image rel='http://schemas.google.com/g/2005#thumbnail' width='16' height='16' src='https://img1.blogblog.com/img/b16-rounded.gif'/>
                    </author>
                </entry>
            </feed>
        """.trimXmlFormatting()
        val expectedLastUpdatedOn = OffsetDateTime.of(
            // 2022-11-30T19:27:01.610-08:00 --> UTC 2022-12-01T03:27:01.610Z
            LocalDate.of(2022, Month.DECEMBER, 1),
            LocalTime.of(3, 27, 1, 610_000_000),
            ZoneOffset.UTC,
        )
        val expectedAuthor = Author(
            name = "Jamal Eason",
        )
        val expectedLinks = listOf(
            Link(
                url = "https://androidstudio.googleblog.com/feeds/posts/default",
            ),
            Link(
                url = "https://www.blogger.com/feeds/3325683420543787015/posts/default",
            ),
            Link(
                url = "https://androidstudio.googleblog.com/",
            ),
            Link(
                url = "http://pubsubhubbub.appspot.com/",
            ),
            Link(
                url = "https://www.blogger.com/feeds/3325683420543787015/posts/default?start-index=26&max-results=25",
            ),
        )

        val feed = parser.parseFeed(xml)
        assertEquals("tag:blogger.com,1999:blog-3325683420543787015", feed.id) { "id does not match" }
        assertEquals(expectedLastUpdatedOn, feed.lastUpdatedOn) { "lastUpdatedOn does not match" }
        assertEquals("Android Studio Release Updates", feed.title) { "title does not match" }
        assertEquals(
            "Provides official announcements for new versions of Android Studio and other Android developer tools.",
            feed.subtitle,
        ) { "subtitle does not match" }
        assertEquals(expectedAuthor, feed.author) { "author does not match" }
        assertEquals(expectedLinks, feed.links) { "link does not match" }
        assertEquals(2, feed.entries.size) { "entry size does not match" }
        assertEquals(
            "tag:blogger.com,1999:blog-3325683420543787015.post-926632259736729368",
            feed.entries[0].id,
        ) { "feed.entry[0].id does not match" }
        assertEquals(
            "tag:blogger.com,1999:blog-3325683420543787015.post-1650240193865345981",
            feed.entries[1].id,
        ) { "feed.entry[1].id does not match" }
        assertEquals(
            "Android Studio Flamingo Canary 9 now available",
            feed.entries[0].title,
        ) { "feed.entry[0].title does not match" }
        assertEquals(3, feed.entries[0].links.size) { "feed.entry[0].link.size does not match" }
    }

    private fun String.trimHtmlIndenting(): String {
        return trimIndent()
            .lines()
            .filter { it.isNotBlank() }
            .joinToString(separator = " ")
    }

    private fun String.trimXmlFormatting(): String {
        return lines()
            .filter { it.isNotBlank() }
            .joinToString(separator = "") { it.trim() }
    }
}
