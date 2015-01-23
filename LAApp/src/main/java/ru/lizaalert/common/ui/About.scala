package ru.lizaalert.common.ui

import java.io.{IOException, InputStreamReader, BufferedReader}

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.{MenuItem, View}
import android.widget.{LinearLayout, ImageView, TextView}
import com.yandex.metrica.YandexMetrica
import ru.lizaalert.common.{BuildConfig, R}
import java.lang.StringBuilder

class About extends Activity {
  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_about)
    getActionBar.setHomeButtonEnabled(true)
    getActionBar.setDisplayHomeAsUpEnabled(true)

    val pInfo = getPackageManager().getPackageInfo(getPackageName(), 0)

    val am = getAssets



    val sb = (new StringBuilder)
      .append(getString(R.string.msg_about))
      .append("\n\n")
      .append(getString(R.string.msg_version))
      .append(" ")
      .append(pInfo.versionName)
      .append(" (")
      .append(BuildConfig.VERSION_CODE)
      .append(")")

    findViewById(R.id.about_version)
      .asInstanceOf[TextView]
      .setText(sb.toString)

    val authors = findViewById(R.id.about_authors).asInstanceOf[TextView]
    val authorsLayout = findViewById(R.id.authors_layout).asInstanceOf[LinearLayout]
    val iAuthorsOpen = findViewById(R.id.ico_authors_open).asInstanceOf[ImageView]
    val iAuthorsClose = findViewById(R.id.ico_authors_close).asInstanceOf[ImageView]
    val authorsText = readFileFromAssets("AUTHORS")
    if (authorsText != null) {
      authorsLayout.setOnClickListener(new View.OnClickListener() {
        def onClick(v: View): Unit = {
          toggleTV(authors, iAuthorsOpen, iAuthorsClose)
        }
      })
      authors.setText(authorsText)
    } else {
      authors.setVisibility(View.GONE)
    }

    val license = findViewById(R.id.about_license).asInstanceOf[TextView]
    val licenseLayout = findViewById(R.id.license_layout).asInstanceOf[LinearLayout]
    val iLicenseOpen = findViewById(R.id.ico_license_open).asInstanceOf[ImageView]
    val iLicenseClose = findViewById(R.id.ico_license_close).asInstanceOf[ImageView]
    val licenseText = readFileFromAssets("LICENSE")
    if (licenseText != null) {
      license.setText(licenseText)
      licenseLayout.setOnClickListener(new View.OnClickListener() {
        def onClick(v: View): Unit = {
          toggleTV(license, iLicenseOpen, iLicenseClose)
        }
      })
    } else {
      licenseLayout.setVisibility(View.GONE)
    }

    //    val scalaTextView = findViewById(R.id.scala_text_view).asInstanceOf[TextView]
//    scalaTextView.setText(new HelloJava().say())

    Log.d("debug", "HelloActivity onCreate")
  }

  override def onOptionsItemSelected(menuItem: MenuItem): Boolean = {
    menuItem.getItemId match {
      case android.R.id.home =>
        onBackPressed
        return true
    }
    return (super.onOptionsItemSelected(menuItem))
  }

  def toggleTV (view:TextView, iOpen:ImageView, iClose:ImageView) = {
    if (view.getVisibility == View.VISIBLE) {
      view.setVisibility(View.GONE)
      iOpen.setVisibility(View.GONE)
      iClose.setVisibility(View.VISIBLE)
    } else {
      view.setVisibility(View.VISIBLE)
      iOpen.setVisibility(View.VISIBLE)
      iClose.setVisibility(View.GONE)
    }
  }

  private def readFileFromAssets(fileName: String): String = {
    try {
      val reader = new BufferedReader(new InputStreamReader(getAssets.open(fileName), "UTF-8"))
      val sb = new java.lang.StringBuilder

      var s = reader.readLine
      while (s != null) {
        sb.append(s)
        sb.append(System.getProperty("line.separator"))
        s = reader.readLine
      }

//      Iterator
//        .continually(reader.readLine)
//        .takeWhile(null !=)
//        .foreach(sb.append)

      reader.close
      return sb.toString
    }
    catch {
      case e: IOException => {
        Log.e("8800", "couldn't open $fileName")
        e.printStackTrace
      }
    }

    return null
  }

  override def onResume() = {
    super.onResume()
    YandexMetrica.onResumeActivity(this)
  }

  override def onPause() = {
    super.onPause()
    YandexMetrica.onResumeActivity(this)
  }
}
