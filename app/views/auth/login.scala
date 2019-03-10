package views.html
package auth

import play.api.data.Form

import lila.api.Context
import lila.app.templating.Environment._
import lila.app.ui.ScalatagsTemplate._

import controllers.routes

object login {

  val twoFactorHelp = span(dataIcon := "")(
    "Open the two-factor authentication app on your device to view your authentication code and verify your identity."
  )

  def apply(form: Form[_], referrer: Option[String])(implicit ctx: Context) = views.html.base.layout(
    title = trans.signIn.txt(),
    moreJs = jsTag("login.js"),
    responsive = true,
    moreCss = responsiveCssTag("auth")
  ) {
      main(cls := "auth auth-login box box-pad")(
        h1(trans.signIn.frag()),
        st.form(
          cls := "form3",
          action := s"${routes.Auth.authenticate}${referrer.?? { ref => s"?referrer=${java.net.URLEncoder.encode(ref, "US-ASCII")}" }}",
          method := "post"
        )(
            div(cls := "one-factor")(
              form3.globalError(form),
              auth.formFields(form("username"), form("password"), none, register = false),
              form3.submit(trans.signIn.frag(), icon = none)
            ),
            div(cls := "two-factor none")(
              form3.group(form("token"), raw("Authentication code"), help = Some(twoFactorHelp))(form3.input(_)(autocomplete := "off", pattern := "[0-9]{6}")),
              p(cls := "error none")("Invalid code."),
              form3.submit(trans.signIn.frag(), icon = none)
            )
          ),
        div(cls := "alternative")(
          a(href := routes.Auth.signup())(trans.signUp.frag()),
          a(href := routes.Auth.passwordReset())(trans.passwordReset.frag())
        )
      )
    }
}