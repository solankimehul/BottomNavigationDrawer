package com.example.bottomappbarbehaviour


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
/*import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pcs.cipriani_couture.R*/
import java.util.*
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IntegerRes
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                //EspressoIdlingResource.increment()
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                //EspressoIdlingResource.decrement()
            }
        })
        show()
    }
}


inline fun View.snackBar(message: String, length: Int = Snackbar.LENGTH_LONG/*, f: Snackbar.() -> Unit*/) {
    val snack = Snackbar.make(this, message, length)
    //snack.f()
    snack.show()
}

@SuppressLint("ResourceType")
inline fun View.snack(@IntegerRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    snack(resources.getString(messageRes), length, f)
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

@SuppressLint("ResourceType")
fun Snackbar.action(@IntegerRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

/*fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}*/

/*fun Context.loadImageInGlide(img: ImageView, url: String) {
    Glide.with(this)
            .asBitmap()
            .load(url)
            .apply(RequestOptions().placeholder(R.drawable.ic_image_icon))
            .into(img)
}*/

fun Activity.loadFragment(fragment: Fragment) {
    val fragmentTransaction = (this as AppCompatActivity).supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.container, fragment).addToBackStack(fragment::class.java.simpleName).commit()
}

fun Activity.loadFragmentWithoutBackStack(fragment: Fragment) {
    val fragmentTransaction = (this as AppCompatActivity).supportFragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.container, fragment).commit()
}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}

fun String.random(): String {
    return UUID.randomUUID().toString()
}


fun String.getRandomString(length: Int): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..length)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
}

/************************  Views  ****************************/

/**
 * will show the view If Condition is true else make if INVISIBLE or GONE Based on the [makeInvisible] flag
 */
fun View.showIf(boolean: Boolean, makeInvisible: Boolean = false) {
    visibility = if (boolean) View.VISIBLE else if (makeInvisible) View.INVISIBLE else View.GONE
}

/**
 * will hide the view If Condition is true else make if INVISIBLE or GONE Based on the [makeInvisible] flag
 */
fun View.hideIf(boolean: Boolean, makeInvisible: Boolean = false) {
    showIf(boolean.not(), makeInvisible)
}

/**
 * will enable the view If Condition is true else enables It
 */
fun View.enableIf(boolean: Boolean) { isEnabled = boolean }

/**
 * will disable the view If Condition is true else enables It
 */
fun View.disableIf(boolean: Boolean) = { isEnabled = boolean.not() }

/**
 * show KeyBoard
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

/**
 * hide Keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

/**
 * set View Padding From Left
 */
fun View.setPaddingLeft(value: Int) = setPadding(value, paddingTop, paddingRight, paddingBottom)

/**
 * set View Padding From Right
 */
fun View.setPaddingRight(value: Int) = setPadding(paddingLeft, paddingTop, value, paddingBottom)

/**
 * set View Padding From Top
 */
fun View.setPaddingTop(value: Int) = setPaddingRelative(paddingStart, value, paddingEnd, paddingBottom)

/**
 * set View Padding From Bottom
 */
fun View.setPaddingBottom(value: Int) = setPaddingRelative(paddingStart, paddingTop, paddingEnd, value)

/**
 * set View Padding From Start
 */
fun View.setPaddingStart(value: Int) = setPaddingRelative(value, paddingTop, paddingEnd, paddingBottom)

/**
 * set View Padding From End
 */
fun View.setPaddingEnd(value: Int) = setPaddingRelative(paddingStart, paddingTop, value, paddingBottom)

/**
 * set View Padding On Horizontal Edges
 */
fun View.setPaddingHorizontal(value: Int) = setPaddingRelative(value, paddingTop, value, paddingBottom)

/**
 * set View Padding From Vertical Edges
 */
fun View.setPaddingVertical(value: Int) = setPaddingRelative(paddingStart, value, paddingEnd, value)

/**
 * set height
 */
fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

/**
 * set Width
 */
fun View.setWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}

/**
 * resize the View Width Height
 */
fun View.resize(width: Int, height: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = width
        lp.height = height
        layoutParams = lp
    }
}

/**
 * check if View is Visible to User Or Not
 */
fun View.isVisible() = visibility == View.VISIBLE

/**
 * Check if View Visiblity == GONE
 */
fun View.isGone() = visibility == View.GONE

/**
 * Check if View is Invisible to User
 */
fun View.isInvisible() = visibility == View.INVISIBLE

/**
 * get Activity On Which View is inflated to
 */
fun View.getActivity(): Activity? {
    if (context is Activity)
        return context as Activity
    return null
}


/**
 * Set On Single Click Listener,
 * It will allow user to click on the button after the specified tolerance. So no multiClick Or FastClick From Now
 *
 * @property tolerance the Millis to Skip the User Click
 */
/*fun View.setOnSingleClickListener(tolerance: Long = 500, onClick: (v: View) -> Unit) {
    var lastClicked = 0L
    setOnClickListener {
        if (currentTimeMillis - lastClicked > tolerance) {
            onClick(it)
            lastClicked = currentTimeMillis
        }
    }
}*/


/**********************   Context  **************************/

/**
 * shows the toast with a Single Call, Just Provide your [msg] and [length] (Optionally)
 */
/*
fun Context.showToast(msg: String, length: Int = Toast.LENGTH_LONG) = Toast.makeText(this, msg, length).show()
*/

/**
 * There is No Such Thing name Hard Toast, Its just an AlertDialog which will the [msg] you passed until user cancels it.
 */
fun Context.showToastHard(msg: String) = AlertDialog.Builder(this).setMessage(msg).show()

/**
 * Want to Confirm the User Action? Just call showConfirmationDialog with required + optional params to do so.
 *  call :  requireContext().showConfirmationDialog("Your message here", {
 *                    if (it) requireContext().showToast("Yes clicked")
 *                     else requireContext().showToast("No clicked")
 *            })
 */
fun Context.showConfirmationDialog(
        msg: String,
        onResponse: (result: Boolean) -> Unit,
        positiveText: String = "Yes",
        negetiveText: String = "No",
        cancelable: Boolean = false
) =
        AlertDialog.Builder(this).setMessage(msg).setPositiveButton(positiveText) { _, _ -> onResponse(true) }.setNegativeButton(
                negetiveText
        ) { _, _ -> onResponse(false) }.setCancelable(cancelable).show()


/**
 * Want your user to choose Single thing from a bunch? call showSinglePicker and provide your options to choose from
 */
fun Context.showSinglePicker(choices: Array<String>, onResponse: (index: Int) -> Unit, checkedItemIndex: Int = -1) =
        AlertDialog.Builder(this).setSingleChoiceItems(choices, checkedItemIndex) { dialog, which ->
            onResponse(which)
            dialog.dismiss()
        }.show()

/**
 * Want your user to choose Multiple things from a bunch? call showMultiPicker and provide your options to choose from
 */
fun Context.showMultiPicker(
        choices: Array<String>,
        onResponse: (index: Int, isChecked: Boolean) -> Unit,
        checkedItems: BooleanArray? = null
) =
        AlertDialog.Builder(this).setMultiChoiceItems(choices, checkedItems) { _, which, isChecked ->
            onResponse(
                    which,
                    isChecked
            )
        }.setPositiveButton("Done", null).show()


/**
 * Try Catch within a single line
 */
fun tryAndCatch(runnable: () -> Unit, onCatch: ((e: Throwable?) -> Unit)? = null, onFinally: (() -> Unit)? = null) =
        try {
            runnable.invoke()
        } catch (e: Throwable) {
            onCatch?.invoke(e)
        } finally {
            onFinally?.invoke()
        }

/**
 * get Screen Width Easily
 */
inline val Context.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

/**
 * get Screen Height Easily
 */
inline val Context.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

/**
 * get Android ID
 */
@SuppressLint("HardwareIds")
fun Context.getAndroidID() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

/**
 * get Device ID a.k.a Android ID
 */
fun Context.getDeviceID() = getAndroidID()

/**
 * get Telephony Manager
 */
fun Context.getTelephonyManager() = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

/**
 * get Device IMEI
 *
 * Requires READ_PHONE_STATE Permission
 */
@SuppressLint("HardwareIds")
@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
fun Context.getIMEI() = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> this.getTelephonyManager().imei
    else -> getTelephonyManager().deviceId
}


/************************* Edit text ***************************/

/**
 * Add TextChangedListener Instead Of Full TextWatcher to get Callback OnTextChange
 */
fun EditText.myOnTextChangedListener(listener: (String) -> (Unit)) {

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener(s.toString())
        }
    })
}

/**
 * Add AfterTextChangedListener Instead Of Full TextWatcher to get Callback After OnTextChange
 */
fun EditText.setAfterTextChangedListener(listener: (String) -> (Unit)) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            listener(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

/**
 * Add BeforeTextChangedListener Instead Of Full TextWatcher to get Callback Before OnTextChange
 */
fun EditText.setBeforeTextChangedListener(listener: (String) -> (Unit)) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            listener(s.toString())
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}


/************************   ViewPager  *******************************/

/**
 * Use setOnPageSelectedListener instead of Using the Full OnPageChangeListener
 */
fun androidx.viewpager.widget.ViewPager.setOnPageSelectedListener(listener: (Int) -> (Unit)) {
    addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(p0: Int) {
        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        }

        override fun onPageSelected(page: Int) {
            listener(page)
        }
    })
}

/**
 * Use setOnPageScrollStateChangedListener instead of Using the Full OnPageChangeListener
 */
fun androidx.viewpager.widget.ViewPager.setOnPageScrollStateChangedListener(listener: (Int) -> (Unit)) {
    addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(p0: Int) {
            listener(p0)
        }

        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
        }

        override fun onPageSelected(page: Int) {

        }
    })
}


/*********************   Bitmap ****************************/


/**
 * Converts Bitmap to Base64 Easily.
 */
fun Bitmap.toBase64(compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): String {
    val result: String
    val baos = ByteArrayOutputStream()
    compress(compressFormat, 100, baos)
    baos.flush()
    baos.close()
    val bitmapBytes = baos.toByteArray()
    result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
    baos.flush()
    baos.close()
    return result
}

/**
 * rotate Bitmap With a ease. Just call [rotateTo] with the [angle] and you will get new Resized Bitmap
 */
fun Bitmap.rotateTo(angle: Float, recycle: Boolean = true): Bitmap {
    val matrix = Matrix()
    matrix.setRotate(angle)
    val newBitmap = Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    if (recycle && isRecycled.not() && newBitmap != this)
        recycle()
    return newBitmap
}

/**
 * Converts Bitmap to ByteArray Easily.
 */
fun Bitmap.toByteArray(compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(compressFormat, 100, stream)
    return stream.toByteArray()
}

/**
 * Compress Bitmap by Quality
 */
fun Bitmap.compressByQuality(quality: Int, recycle: Boolean = true): Bitmap? {
    val baos = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, quality, baos)
    val bytes = baos.toByteArray()
    if (recycle && !isRecycled) recycle()
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

/**
 * Compress Bitmap by Sample Size
 */
fun Bitmap.compressBySampleSize(maxWidth: Int, maxHeight: Int, recycle: Boolean = true): Bitmap? {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val bytes = baos.toByteArray()
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
    options.inJustDecodeBounds = false
    if (recycle && !isRecycled) recycle()
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
}

//***********Private Methods Are below**********************
private fun calculateInSampleSize(options: BitmapFactory.Options, maxWidth: Int, maxHeight: Int): Int {
    var height = options.outHeight
    var width = options.outWidth
    var inSampleSize = 1
    do {
        width = width shr 1
        height = height shr 1
        val bool = width >= maxWidth && height >= maxHeight
        if (bool.not())
            break
        else
            inSampleSize = inSampleSize shl 1
    } while (true)
    return inSampleSize
}


/**
 * Want the Image to GreyScale? Just call [toGrayScale] and get the grey Image.
 */
fun Bitmap.toGrayScale(recycle: Boolean): Bitmap? {
    val ret = Bitmap.createBitmap(width, height, config)
    val canvas = Canvas(ret)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f)
    val colorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)
    paint.colorFilter = colorMatrixColorFilter
    canvas.drawBitmap(this, 0f, 0f, paint)
    if (recycle && !isRecycled && ret != this) recycle()
    return ret
}
