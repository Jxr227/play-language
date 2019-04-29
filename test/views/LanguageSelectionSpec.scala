/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package views

import java.util.Locale

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Lang, Messages, MessagesApi, MessagesImpl}
import play.api.mvc.{Call, PathBindable}
import play.api.test.Helpers._
import uk.gov.hmrc.play.language.LanguageUtils.{English, Welsh}

class LanguageSelectionSpec extends PlaySpec with GuiceOneAppPerSuite {

  import play.api.i18n.Messages.Implicits._
  val messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]

  def languageMap: Map[String, Lang] = Map("english" -> English,
    "cymraeg" -> Welsh)

  def langToUrl(lang: String): Call = Call("GET", "/language/" + implicitly[PathBindable[String]].unbind("lang", lang))

  "Language selection template view" should {

    "give a link to switch to Welsh when current language is English" in {
      implicit val lang = Lang("en")
      val messages = implicitly[Messages]
      val html = views.html.language_selection.render(languageMap, langToUrl, None, None, messages)
      contentType(html) must be("text/html")
      contentAsString(html) must include(messages("id=\"cymraeg-switch\""))
      contentAsString(html) must include("/language/cymraeg")
    }

    "show correct current language message when current language is English" in {
      implicit val lang = Lang("en")
      val messages = implicitly[Messages]
      val html = views.html.language_selection.render(languageMap, langToUrl, None, None, messages)
      contentType(html) must be("text/html")
      contentAsString(html) must include("English")
      contentAsString(html) must not include ">English<"
    }

    "give a link to switch to English when current language is Welsh" in {
      implicit val lang = Lang("cy")
      val messages = implicitly[Messages]
      val html = views.html.language_selection.render(languageMap, langToUrl, None, None, messages)
      contentType(html) must be("text/html")
      contentAsString(html) must include(messages("id=\"english-switch\""))
      contentAsString(html) must include("/language/english")
    }

    "show correct current language message when current language is Welsh" in {
      implicit val lang = Lang("cy")
      val messages = implicitly[Messages]
      val html = views.html.language_selection.render(languageMap, langToUrl, None, None, messages)
      contentType(html) must be("text/html")
      contentAsString(html) must include("Cymraeg")
      contentAsString(html) must not include ">Cymraeg<"
    }

    "show a custom class if it is set" in {
      implicit val lang = Lang("cy")
      val messages = implicitly[Messages]
      val html = views.html.language_selection.render(languageMap, langToUrl, Some("float--right"), None, messages)
      contentType(html) must be("text/html")
      contentAsString(html) must include("class=\"float--right\"")
    }

    "show a data-journey-click attribute for GA if it is set and language is Welsh" in {
      implicit val lang = Lang("cy")
      val messages = implicitly[Messages]
      val html = views.html.language_selection.render(languageMap, langToUrl, None, Some("appName"), messages)
      contentType(html) must be("text/html")
      contentAsString(html) must include("data-journey-click=\"appName:language: en\"")
    }

    "show a data-journey-click attribute for GA if it is set and language is English" in {
      implicit val lang = Lang("en")
      val messages = implicitly[Messages]
      val html = views.html.language_selection.render(languageMap, langToUrl, None, Some("appName"), messages)
      contentType(html) must be("text/html")
      contentAsString(html) must include("data-journey-click=\"appName:language: cy\"")
    }

    "show correct current language message when current language is Spanish" in {
      implicit val Spanish = Lang("es")
      val messages = implicitly[Messages]

      val mockLanguageMap = Map("english" -> English,
        "cymraeg" -> Welsh,
        "español" -> Spanish)

      val html = views.html.language_selection.render(mockLanguageMap, langToUrl, None, None, messages)
      contentType(html) must be("text/html")
      contentAsString(html) must include("Español")
      contentAsString(html) must not include ">Español<"
    }
  }
}
