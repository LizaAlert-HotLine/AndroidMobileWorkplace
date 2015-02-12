/*
    Copyright (c) 2014 Igor Tseglevskiy <igor.tseglevskiy@gmail.com>
    Copyright (c) 2014 Other contributors as noted in the AUTHORS file.

    Этот файл является частью приложения "Мобильное рабочее место оператора
    Горячей линии по пропавшим детям".

    Данная лицензия разрешает лицам, получившим копию "Мобильного рабочего
    места оператора Горячей линии по пропавшим детям" и сопутствующей
    документации (в дальнейшем именуемыми «Программное Обеспечение»),
    безвозмездно использовать Программное Обеспечение без ограничений, включая
    неограниченное право на использование, копирование, изменение, добавление,
    публикацию, распространение, сублицензирование и/или продажу копий
    Программного Обеспечения, также как и лицам, которым предоставляется данное
    Программное Обеспечение, при соблюдении следующих условий:

    Указанное выше уведомление об авторском праве и данные условия должны быть
    включены во все копии или значимые части данного Программного Обеспечения.

    ДАННОЕ ПРОГРАММНОЕ ОБЕСПЕЧЕНИЕ ПРЕДОСТАВЛЯЕТСЯ «КАК ЕСТЬ», БЕЗ КАКИХ-ЛИБО
    ГАРАНТИЙ, ЯВНО ВЫРАЖЕННЫХ ИЛИ ПОДРАЗУМЕВАЕМЫХ, ВКЛЮЧАЯ, НО НЕ
    ОГРАНИЧИВАЯСЬ ГАРАНТИЯМИ ТОВАРНОЙ ПРИГОДНОСТИ, СООТВЕТСТВИЯ ПО ЕГО
    КОНКРЕТНОМУ НАЗНАЧЕНИЮ И ОТСУТСТВИЯ НАРУШЕНИЙ ПРАВ. НИ В КАКОМ СЛУЧАЕ
    АВТОРЫ ИЛИ ПРАВООБЛАДАТЕЛИ НЕ НЕСУТ ОТВЕТСТВЕННОСТИ ПО ИСКАМ О ВОЗМЕЩЕНИИ
    УЩЕРБА, УБЫТКОВ ИЛИ ДРУГИХ ТРЕБОВАНИЙ ПО ДЕЙСТВУЮЩИМ КОНТРАКТАМ, ДЕЛИКТАМ
    ИЛИ ИНОМУ, ВОЗНИКШИМ ИЗ, ИМЕЮЩИМ ПРИЧИНОЙ ИЛИ СВЯЗАННЫМ С ПРОГРАММНЫМ
    ОБЕСПЕЧЕНИЕМ ИЛИ ИСПОЛЬЗОВАНИЕМ ПРОГРАММНОГО ОБЕСПЕЧЕНИЯ ИЛИ ИНЫМИ
    ДЕЙСТВИЯМИ С ПРОГРАММНЫМ ОБЕСПЕЧЕНИЕМ.

    Кроме содержимого в этом уведомлении, ни название "Горячей линии по
    пропавшим детям", ни название "Добровольного поискового отряда "Лиза
    Алерт", ни имена вышеуказанных держателей авторских прав не должно быть
    использовано в рекламе или иным способом, чтобы увеличивать продажу,
    использование или другие работы в этом Программном обеспечении без
    предшествующего письменного разрешения.

    Permission is hereby granted, free of charge, to any person obtaining a
    copy of this software and associated documentation files (the "Software"),
    to deal in the Software without restriction, including without limitation
    the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the
    Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
    THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
    FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
    DEALINGS IN THE SOFTWARE.

    Except as contained in this notice, the name of Liza Alert or the name of
    Liza Alerts's hotline department or the name(s) the above copyright holders
    shall not be used in advertising or otherwise to promote the sale, use or
    other dealings in this Software without prior written authorization.
 */

package ru.lizaalert.hotline.auth

import java.io.IOException

import android.accounts.AccountManager
import android.app.{LoaderManager, Activity}
import android.content.{DialogInterface, Intent, Loader}
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.gms.auth.{UserRecoverableAuthException, GooglePlayServicesAvailabilityException}
import com.google.android.gms.common.{GooglePlayServicesUtil, AccountPicker}
import ru.lizaalert.hotline.R
import ru.lizaalert.hotline.lib.settings.Settings

class Auth extends Activity {

  object AuthState extends Enumeration {
    type AuthState = Value
    val Unknown, GetMail, GetToken, GooglePlayShitHappens, GetPermissions = Value
  }

  import AuthState._

  object RequestConstants {
    val REQUEST_CODE_PICK_ACCOUNT = 1001
    val REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1002
    val LOADER_GET_TOKEN = 2001
    val StateTag = "state"
    val MailTag = "mail"
    val StatusTag = "status"
  }
  import RequestConstants._

  val scope = "oauth2:https://spreadsheets.google.com/feeds https://docs.google.com/feeds"

  var state = Unknown
  var mail: String = null
  var statusCode:Int = 0

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_auth)
    getActionBar.setHomeButtonEnabled(true)
    getActionBar.setDisplayHomeAsUpEnabled(true)

    if (savedInstanceState != null) {
      if (savedInstanceState.containsKey(StateTag)) {
        setState(AuthState(savedInstanceState.getInt(StateTag)))
      }
      if (savedInstanceState.containsKey(MailTag)) {
        mail = savedInstanceState.getString(MailTag)
      }
      if (savedInstanceState.containsKey(StatusTag)) {
        statusCode = savedInstanceState.getInt(StatusTag)
      }
    }

    if (state == Unknown) {
      setState(GetMail)
    }

  }

  val getTokenCallback = new LoaderManager.LoaderCallbacks[GetTokenResult] {
    def onCreateLoader(id: Int, args: Bundle): Loader[GetTokenResult] = {
      Log.d("8800", "onCreateLoader " + id)
      new GetTokenLoader(getApplicationContext, mail, scope)
    }

    def onLoadFinished(loader: Loader[GetTokenResult], data: GetTokenResult) {
      l("onLoadFinished " + data)
      getLoaderManager.destroyLoader(LOADER_GET_TOKEN)

      if (data.token != null) {
        l("save token '" + data.token + "'")
        Settings.instance(getApplicationContext).setAccountMail(mail)
        Settings.instance(getApplicationContext).setAccountToken(data.token)
        setState(Unknown)
        setResult(Activity.RESULT_OK)
        finish()
      }

      else data.e match {
        case exception: GooglePlayServicesAvailabilityException =>
          l("GooglePlayServicesAvailabilityException")
          statusCode = exception.getConnectionStatusCode
          setState(GooglePlayShitHappens)
          stepGoglePlayShitHappens()

        case exception: UserRecoverableAuthException =>
          l("UserRecoverableAuthException")
          val intent: Intent = exception.getIntent
          startActivityForResult(intent, REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR)
          setState(GetPermissions)

        case _ => if (data.e.isInstanceOf[IOException]) {
          l("IOException")
          data.e.printStackTrace()
          setState(Unknown)
          setResult(Activity.RESULT_CANCELED)
          finish()
        }
      }


    }

    def onLoaderReset(loader: Loader[GetTokenResult]) {
      l("onLoaderReset")
    }
  }

  def stepGoglePlayShitHappens() = {
    val dialog = GooglePlayServicesUtil
      .getErrorDialog(
        statusCode,
        Auth.this,
        REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR,
        new DialogInterface.OnCancelListener {
          def onCancel(dialog: DialogInterface): Unit = {
            l("cancel")
            setState(Unknown)
            setResult(Activity.RESULT_CANCELED)
            finish()
          }
        })
    dialog.show()
  }

  def stepPickAccount() = {
    val accountTypes = Array("com.google")
    val intent = AccountPicker.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null)
    startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT)
  }

  def stepGetToken() = {
    getLoaderManager.initLoader(LOADER_GET_TOKEN, null, getTokenCallback)
  }

  override def onResume(): Unit = {
    l("onResume " + state)
    super.onResume()

    state match {
      case GetMail =>
        stepPickAccount()

      case GetToken =>
        stepGetToken()

      case GooglePlayShitHappens =>
        stepGoglePlayShitHappens()

      case GetPermissions =>
        l("GetPermissions state. what to do?")
    }
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    l("request " + requestCode + " result " + resultCode)

    (requestCode, resultCode) match {
      case (REQUEST_CODE_PICK_ACCOUNT, Activity.RESULT_OK) =>
        mail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
        l("got " + mail)
        setState(GetToken)

      case (REQUEST_CODE_PICK_ACCOUNT, _) =>
        l("REQUEST_CODE_PICK_ACCOUNT cancelled")
        setResult(Activity.RESULT_CANCELED)
        finish()

      case (REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR, Activity.RESULT_OK) =>
        l("REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR ok")
        setState(GetToken)

      case (REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR, _) =>
        l("REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR error")
        setResult(Activity.RESULT_CANCELED)
        finish()

      case _ =>
        l("wtf?")
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
  }

  override def onSaveInstanceState(outState: Bundle): Unit = {
    super.onSaveInstanceState(outState)
    outState.putInt(StateTag, state.id)
    outState.putInt(StatusTag, statusCode)
    outState.putString(MailTag, mail)
  }

  override def onOptionsItemSelected(menuItem: MenuItem): Boolean = {
    menuItem.getItemId match {
      case android.R.id.home =>
        onBackPressed()
        return true
    }
    super.onOptionsItemSelected(menuItem)
  }

  def setState (newState:AuthState): Unit = {
    l("go " + state + " -> " + newState)
    state = newState
  }

  def l(s:String) = {
    Log.d("8800", s)
  }
}